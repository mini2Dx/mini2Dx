/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

/**
 * Implements graphics rendering functionality
 */
public class Graphics {
	private Color color, backgroundColor, tint, defaultTint;
	private SpriteBatch spriteBatch;
	private ShapeTextureCache colorTextureCache;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShaderProgram defaultShader;

	private float translationX, translationY;
	private float scaleX, scaleY;
	private float rotation, rotationX, rotationY;
	private float currentWidth, currentHeight;

	private int defaultBlendSrcFunc = GL20.GL_SRC_ALPHA,
			defaultBlendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
	private int lineHeight;
	private boolean rendering, renderingShapes;
	private Rectangle clip;

	public Graphics(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;

		defaultTint = spriteBatch.getColor();
		if (defaultTint != null) {
			font = new BitmapFont(true);
		}

		lineHeight = 1;
		color = Color.WHITE;
		backgroundColor = Color.BLACK;
		colorTextureCache = new ShapeTextureCache();

		translationX = 0;
		translationY = 0;
		scaleX = 1f;
		scaleY = 1f;
		rotation = 0f;
		rotationX = 0f;
		rotationY = 0f;

		/* Create Ortho camera so that 0,0 is in top-left */
		camera = new OrthographicCamera();
	}

	/**
	 * Called by mini2Dx before rendering begins
	 * 
	 * @param gameWidth
	 *            The current game window width
	 * @param gameHeight
	 *            The current game window height
	 */
	public void preRender(int gameWidth, int gameHeight) {
		this.currentWidth = gameWidth;
		this.currentHeight = gameHeight;

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

		rendering = false;

		if (defaultShader == null) {
			defaultShader = SpriteBatch.createDefaultShader();
		}
	}

	/**
	 * Called by mini2Dx after rendering
	 */
	public void postRender() {
		endRendering();
		resetTransformations();
		clearShaderProgram();
		clearBlendFunction();
	}

	/**
	 * Renders a line segment to the window in the current {@link Color} with
	 * the set line height
	 * 
	 * @param x1
	 *            X coordinate of point A
	 * @param y1
	 *            Y coordinate of point A
	 * @param x2
	 *            X coordinate of point B
	 * @param y2
	 *            Y coordinate of point B
	 */
	public void drawLineSegment(float x1, float y1, float x2, float y2) {
		beginRendering();
		endRendering();

		/* TODO: Move all shape rendering over to using ShapeRenderer */
		renderingShapes = true;
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color);
		shapeRenderer.line(x1, y1, x2, y2);

		beginRendering();
	}

	/**
	 * Renders a rectangle to the window in the current {@link Color} with the
	 * set line height
	 * 
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 */
	public void drawRect(float x, float y, float width, float height) {
		beginRendering();

		int roundWidth = MathUtils.round(width);
		int roundHeight = MathUtils.round(height);

		spriteBatch.draw(colorTextureCache.getRectangleTexture(color,
				roundWidth, roundHeight, getLineHeight()), x, y, 0, 0,
				roundWidth, roundHeight, 1f, 1f, 0, 0, 0, roundWidth,
				roundHeight, false, false);
	}

	/**
	 * Fills a rectangle to the window in the current {@link Color}
	 * 
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 */
	public void fillRect(float x, float y, float width, float height) {
		beginRendering();

		spriteBatch.draw(colorTextureCache.getFilledRectangleTexture(color), x,
				y, 0, 0, width, height, 1f, 1f, 0, 0, 0, 1, 1, false, false);
	}

	/**
	 * Draws a circle to the window in the current {@link Color} with the set
	 * line height
	 * 
	 * @param centerX
	 *            The x coordinate of the center of the circle
	 * @param centerY
	 *            The y coordinate of the center of the circle
	 * @param radius
	 *            The radius of the circle
	 */
	public void drawCircle(float centerX, float centerY, int radius) {
		beginRendering();

		float renderX = (centerX - radius);
		float renderY = (centerY - radius);

		Texture texture = colorTextureCache.getCircleTexture(color, radius,
				getLineHeight());
		spriteBatch.draw(texture, renderX, renderY, 0, 0, texture.getWidth(),
				texture.getHeight(), 1f, 1f, 0, 0, 0, texture.getWidth(),
				texture.getHeight(), false, false);
	}

	/**
	 * Fills a circle to the window in the current {@link Color}
	 * 
	 * @param centerX
	 *            The x coordinate of the center of the circle
	 * @param centerY
	 *            The y coordinate of the center of the circle
	 * @param radius
	 *            The radius of the circle
	 */
	public void fillCircle(float centerX, float centerY, int radius) {
		Texture texture = colorTextureCache.getFilledCircleTexture(color,
				radius);

		float renderX = (centerX - radius);
		float renderY = (centerY - radius);

		beginRendering();
		spriteBatch.draw(texture, renderX, renderY, 0, 0, texture.getWidth(),
				texture.getHeight(), 1f, 1f, 0, 0, 0, texture.getWidth(),
				texture.getHeight(), false, false);
	}

	/**
	 * Draws a string to the window
	 * 
	 * @param text
	 *            The {@link String} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 */
	public void drawString(String text, float x, float y) {
		if (font == null) {
			return;
		}
		beginRendering();
		font.setColor(color);
		font.draw(spriteBatch, text, x, y);
	}

	/**
	 * Draws a string to the window, automatically wrapping it within a specified
	 * width
	 * 
	 * @param text
	 *            The {@link String} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 * @param targetWidth
	 *            The width to render the {@link String} at. Note: The string will automatically wrapped if it is longer.
	 */
	public void drawString(String text, float x, float y, float targetWidth) {
		drawString(text, x, y, targetWidth, Align.left);
	}
	
	/**
	 * Draws a string to the window, automatically wrapping it within a specified
	 * width and aligning it to the left, center or right of the width
	 * 
	 * @param text
	 *            The {@link String} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 * @param targetWidth
	 *            The width to render the {@link String} at. Note: The string will automatically wrapped if it is longer.
	 * @param horizontalAlign
	 * 			  The horizontal alignment. Note: Use {@link Align} to retrieve the appropriate value.
	 */
	public void drawString(String text, float x, float y, float targetWidth, int horizontalAlign) {
		if (font == null) {
			return;
		}
		beginRendering();
		font.setColor(color);
		font.draw(spriteBatch, text, x, y, targetWidth, horizontalAlign, true);
	}

	/**
	 * Draws a texture to this graphics context
	 * 
	 * @param texture
	 *            The {@link Texture} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 */
	public void drawTexture(Texture texture, float x, float y) {
		drawTexture(texture, x, y, texture.getWidth(), texture.getHeight());
	}

	/**
	 * 
	 * @param texture
	 *            The {@link Texture} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 * @param width
	 *            The width to draw the texture (this can stretch/shrink the
	 *            texture if not matching the texture's width)
	 * @param height
	 *            The height to draw the texture (this can stretch/shrink the
	 *            texture if not matching the texture's height)
	 */
	public void drawTexture(Texture texture, float x, float y, float width,
			float height) {
		beginRendering();
		spriteBatch.draw(texture, x, y, 0, 0, width, height, 1f, 1f, 0, 0, 0,
				texture.getWidth(), texture.getHeight(), false, true);
	}

	/**
	 * Draws a texture region to this graphics context
	 * 
	 * @param textureRegion
	 *            The {@link TextureRegion} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 */
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y) {
		drawTextureRegion(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}
	
	/**
	 * Draws a texture region to this graphics context
	 * 
	 * @param textureRegion
	 *            The {@link TextureRegion} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 * @param width
	 *            The width to draw the region (this can stretch/shrink the
	 *            texture if not matching the region's width)
	 * @param height
	 *            The height to draw the region (this can stretch/shrink the
	 *            texture if not matching the region's height)
	 */
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height) {
		beginRendering();
		spriteBatch.draw(textureRegion, x, y, 0f, 0f, width, height, 1f, 1f, 0f);
	}

	/**
	 * Draws an instance of {@link Shape}
	 * 
	 * @param shape
	 *            The implementation of {@link Shape} to draw
	 */
	public void drawShape(Shape shape) {
		shape.draw(this);
	}

	/**
	 * Draws a {@link Sprite} with all transformations applied to this graphics
	 * context
	 * 
	 * @param sprite
	 *            The {@link Sprite} to draw
	 */
	public void drawSprite(Sprite sprite) {
		beginRendering();
		sprite.draw(spriteBatch);
	}

	/**
	 * Draws a {@link Sprite} at the given coordinates with all transformations
	 * applied to this graphics context
	 * 
	 * @param sprite
	 *            The {@link Sprite} to draw
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 */
	public void drawSprite(Sprite sprite, float x, float y) {
		beginRendering();
		float oldX = sprite.getX();
		float oldY = sprite.getY();
		Color oldTint = sprite.getColor();

		if (tint != null)
			sprite.setColor(tint);
		sprite.setPosition(x, y);
		sprite.draw(spriteBatch);
		sprite.setPosition(oldX, oldY);
		sprite.setColor(oldTint);
	}

	public void drawSpriteCache(SpriteCache spriteCache, int cacheId) {
		beginRendering();
		spriteCache.getProjectionMatrix().set(
				spriteBatch.getProjectionMatrix().cpy());
		spriteCache.getTransformMatrix().set(
				spriteBatch.getTransformMatrix().cpy());
		Gdx.gl.glEnable(GL20.GL_BLEND);

		spriteCache.begin();
		spriteCache.draw(cacheId);
		spriteCache.end();
	}

	/**
	 * Rotates the canvas by the provided degrees around the provided point
	 * 
	 * @param degrees
	 *            The degree value in a clockwise direction
	 * @param x
	 *            The x coordinate to rotate around
	 * @param y
	 *            The y coordinate to rotate around
	 */
	public void rotate(float degrees, float x, float y) {
		if (rendering) {
			endRendering();
		}

		this.rotation += degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}

	/**
	 * Scales the canvas
	 * 
	 * @param scaleX
	 *            Scaling along the X axis
	 * @param scaleY
	 *            Scaling along the Y axos
	 */
	public void scale(float scaleX, float scaleY) {
		if (rendering) {
			endRendering();
		}

		this.scaleX *= scaleX;
		this.scaleY *= scaleY;
	}

	/**
	 * Moves the graphics context by a certain amount of the X and Y axis
	 * 
	 * @param translateX
	 *            The x axis translation
	 * @param translateY
	 *            The y axis translation
	 */
	public void translate(float translateX, float translateY) {
		if (rendering) {
			endRendering();
		}

		this.translationX += translateX;
		this.translationY += translateY;
	}

	/**
	 * Sets the graphics context clip. Only pixels within this area will be
	 * rendered
	 * 
	 * @param x
	 *            The x coordinate the clip begins at
	 * @param y
	 *            The y coordinate the clip begins at
	 * @param width
	 *            The width of the clip
	 * @param height
	 *            The height of the clip
	 */
	public void setClip(float x, float y, float width, float height) {
		if (rendering) {
			endRendering();
		}

		clip = new Rectangle(x, y, width, height);
	}

	/**
	 * Sets the graphics context clip. Only pixels within this area will be
	 * rendered
	 * 
	 * @param clip
	 *            The clip area
	 */
	public void setClip(Rectangle clip) {
		if (rendering) {
			endRendering();
		}

		this.clip = clip;
	}

	/**
	 * Removes the applied clip
	 */
	public Rectangle removeClip() {
		if (rendering) {
			endRendering();
		}

		Rectangle result = clip;
		clip = null;
		return result;
	}

	/**
	 * Sets the {@link Color} to apply to draw operations
	 * 
	 * @param tint
	 *            The {@link Color} to tint with
	 */
	public void setTint(Color tint) {
		if (rendering) {
			endRendering();
		}

		this.tint = tint;
		spriteBatch.setColor(tint);
	}

	/**
	 * Sets the {@link BitmapFont} to draw {@link String}s with
	 * 
	 * @param font
	 *            A non-null instance of {@link BitmapFont}
	 */
	public void setFont(BitmapFont font) {
		if (font != null) {
			if (rendering) {
				endRendering();
			}

			this.font = font;
		}
	}

	/**
	 * Removes the tinting {@link Color}
	 */
	public void removeTint() {
		setTint(defaultTint);
	}

	/**
	 * Enables blending during rendering
	 */
	public void enabledBlending() {
		spriteBatch.enableBlending();
	}

	/**
	 * Disables blending during rendering
	 */
	public void disableBlending() {
		spriteBatch.disableBlending();
	}

	/**
	 * Applies a {@link ShaderProgram} to this instance
	 * 
	 * @param shaderProgram
	 *            The {@link ShaderProgram} to apply
	 */
	public void setShaderProgram(ShaderProgram shaderProgram) {
		spriteBatch.setShader(shaderProgram);
	}

	/**
	 * Clears the {@link ShaderProgram} applied to this instance
	 */
	public void clearShaderProgram() {
		spriteBatch.setShader(defaultShader);
	}

	/**
	 * Sets the blend function to be applied
	 * 
	 * <a href=
	 * "http://lessie2d.tumblr.com/post/28673280483/opengl-blend-function-cheat-sheet-well-this-is"
	 * >OpenGL Blend Function Cheatsheet</a>
	 * 
	 * @param srcFunc
	 *            Source GL function
	 * @param dstFunc
	 *            Destination GL function
	 */
	public void setBlendFunction(int srcFunc, int dstFunc) {
		spriteBatch.setBlendFunction(srcFunc, dstFunc);
	}

	/**
	 * Resets the blend function to its default
	 */
	public void clearBlendFunction() {
		spriteBatch.setBlendFunction(defaultBlendSrcFunc, defaultBlendDstFunc);
	}

	/**
	 * Immediately flushes everything rendered rather than waiting until the end
	 * of rendering
	 */
	public void flush() {
		spriteBatch.flush();
	}

	/**
	 * This method allows for translation, scaling, etc. to be set before the
	 * {@link SpriteBatch} begins
	 */
	private void beginRendering() {
		if (!rendering) {
			applyTransformations();
			spriteBatch.begin();
			Gdx.gl.glClearStencil(0);
			Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
			if (clip != null) {
				Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
				Gdx.gl.glColorMask(false, false, false, false);
				Gdx.gl.glDepthMask(false);
				Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 1);
				Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE,
						GL20.GL_REPLACE);

				spriteBatch.draw(colorTextureCache
						.getFilledRectangleTexture(Color.WHITE), clip.getX(),
						clip.getY(), 0f, 0f, clip.getWidth(), clip.getHeight(),
						1f, 1f, 0, 0, 0, 1, 1, false, false);
				spriteBatch.end();

				Gdx.gl.glColorMask(true, true, true, true);
				Gdx.gl.glDepthMask(true);
				Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 1);
				Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

				spriteBatch.begin();
			}

			rendering = true;
		}
	}

	/**
	 * Ends rendering
	 */
	private void endRendering() {
		if (rendering) {
			undoTransformations();
			spriteBatch.end();
			if (renderingShapes) {
				shapeRenderer.end();
			}

			if (clip != null) {
				Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
			}
		}
		rendering = false;
		renderingShapes = false;
	}

	/**
	 * Applies all translation, scaling and rotation to the {@link SpriteBatch}
	 */
	private void applyTransformations() {
		float viewportWidth = MathUtils.round(currentWidth / scaleX);
		float viewportHeight = MathUtils.round(currentHeight / scaleY);

		camera.setToOrtho(true, viewportWidth, viewportHeight);

		if (translationX != 0f || translationY != 0f) {
			camera.translate(translationX, translationY);
		}
		camera.update();

		if (rotation != 0f) {
			camera.rotateAround(new Vector3(rotationX, rotationY, 0),
					new Vector3(0, 0, 1), -rotation);
		}
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	/**
	 * Cleans up all translations, scaling and rotation
	 */
	private void undoTransformations() {

		if (rotation != 0f) {
			camera.rotateAround(new Vector3(rotationX, rotationY, 0),
					new Vector3(0, 0, 1), rotation);
		}
		camera.update();

		if (translationX != 0f || translationY != 0f) {
			camera.translate(-translationX, -translationY);
		}
		camera.update();
	}

	/**
	 * Resets transformation values
	 */
	private void resetTransformations() {
		this.translationX = 0;
		this.translationY = 0;
		this.scaleX = 1f;
		this.scaleY = 1f;
		this.rotation = 0f;
		this.rotationX = 0f;
		this.rotationY = 0f;
	}

	/**
	 * Returns the line height used
	 * 
	 * @return A value greater than 0
	 */
	public int getLineHeight() {
		return lineHeight;
	}

	/**
	 * Sets the line height to be used
	 * 
	 * @param lineHeight
	 *            A value greater than 0
	 */
	public void setLineHeight(int lineHeight) {
		if (lineHeight > 0)
			this.lineHeight = lineHeight;
	}

	/**
	 * Returns the {@link Color} to draw shapes with
	 * 
	 * @return A non-null value
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the {@link Color} to draw shapes with
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		if (color != null)
			this.color = color;
	}

	/**
	 * Returns the background {@link Color}
	 * 
	 * @return A non-null value
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the background {@link Color} to be used
	 */
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null)
			this.backgroundColor = backgroundColor;
	}

	/**
	 * Returns the {@link BitmapFont} to draw {@link String}s with
	 * 
	 * @return 15pt Arial font by default unless setFont() is called
	 */
	public BitmapFont getFont() {
		return font;
	}

	/**
	 * Returns the {@link Color} tint being applied to all draw operations
	 * 
	 * @return
	 */
	public Color getTint() {
		return tint;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public float getTranslationX() {
		return translationX;
	}

	public float getTranslationY() {
		return translationY;
	}

	public float getRotation() {
		return rotation;
	}

	public float getRotationX() {
		return rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public Matrix4 getProjectionMatrix() {
		return camera.combined.cpy();
	}

	public float getCurrentWidth() {
		return currentWidth;
	}

	public float getCurrentHeight() {
		return currentHeight;
	}

	@Override
	public String toString() {
		return "Graphics [color=" + color + ", backgroundColor="
				+ backgroundColor + ", tint=" + tint + ", translationX="
				+ translationX + ", translationY=" + translationY + ", scaleX="
				+ scaleX + ", scaleY=" + scaleY + ", rotation=" + rotation
				+ ", rotationX=" + rotationX + ", rotationY=" + rotationY
				+ ", currentWidth=" + currentWidth + ", currentHeight="
				+ currentHeight + ", lineHeight=" + lineHeight + "]";
	}
}
