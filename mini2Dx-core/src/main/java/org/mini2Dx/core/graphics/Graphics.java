/**
 * Copyright (c) 2013, mini2Dx Project
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

/**
 * Implements graphics rendering functionality
 * 
 * @author Thomas Cashman
 */
public class Graphics {
	private Color color, backgroundColor, tint, defaultTint;
	private SpriteBatch spriteBatch;
	private ShapeTextureCache colorTextureCache;
	private OrthographicCamera camera;
	private BitmapFont font;

	private int translationX, translationY;
	private float scaleX, scaleY;
	private float rotation, rotationX, rotationY;
	private float currentWidth, currentHeight;

	private int lineHeight;
	private boolean rendering;
	private Rectangle clip;
	private Rectangle scissors;

	public Graphics(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
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
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);

		rendering = false;
	}

	/**
	 * Called by mini2Dx after rendering
	 */
	public void postRender() {
		endRendering();
		resetTransformations();
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
		if (font != null) {
			beginRendering();
			font.setColor(color);
			font.draw(spriteBatch, text, x, y);
		}
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
		beginRendering();
		spriteBatch.draw(texture, x, y, 0, 0, texture.getWidth(),
				texture.getHeight(), 1f, 1f, 0, 0, 0, texture.getWidth(),
				texture.getHeight(), false, false);
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
		beginRendering();
		spriteBatch.draw(textureRegion, x, y, 0, 0,
				textureRegion.getRegionWidth(),
				textureRegion.getRegionHeight(), 1f, 1f, 0);
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

		sprite.setPosition(x, y);
		sprite.draw(spriteBatch);
		sprite.setPosition(oldX, oldY);
	}

	public void drawSpriteCache(SpriteCache spriteCache, int cacheId) {
		beginRendering();
		spriteCache.getProjectionMatrix().set(
				spriteBatch.getProjectionMatrix().cpy());
		spriteCache.getTransformMatrix().set(
				spriteBatch.getTransformMatrix().cpy());
		Gdx.gl.glEnable(GL10.GL_BLEND);

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
	 * Applies a translation to rendering
	 * 
	 * @param translateX
	 *            The x axis translation
	 * @param translateY
	 *            The y axis translation
	 */
	public void translate(int translateX, int translateY) {
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
	 * This method allows for translation, scaling, etc. to be set before the
	 * {@link SpriteBatch} begins
	 */
	private void beginRendering() {
		if (!rendering) {
			applyTransformations();
			spriteBatch.begin();
			Gdx.gl.glClearStencil(0);
			Gdx.gl.glClear(GL10.GL_STENCIL_BUFFER_BIT);
			if (clip != null) {
				Gdx.gl.glEnable(GL10.GL_STENCIL_TEST);
				Gdx.gl.glColorMask(false, false, false, false);
				Gdx.gl.glDepthMask(false);
				Gdx.gl.glStencilFunc(GL10.GL_ALWAYS, 1, 1);
				Gdx.gl.glStencilOp(GL10.GL_REPLACE, GL10.GL_REPLACE,
						GL10.GL_REPLACE);

				spriteBatch.draw(colorTextureCache
						.getFilledRectangleTexture(Color.WHITE), clip.getX(),
						clip.getY(), 0f, 0f, clip.getWidth(), clip.getHeight(),
						1f, 1f, 0, 0, 0, 1, 1, false, false);
				spriteBatch.end();

				Gdx.gl.glColorMask(true, true, true, true);
				Gdx.gl.glDepthMask(true);
				Gdx.gl.glStencilFunc(GL10.GL_EQUAL, 1, 1);
				Gdx.gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);

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

			if (clip != null) {
				Gdx.gl.glDisable(GL10.GL_STENCIL_TEST);
			}
		}
		rendering = false;
	}

	/**
	 * Applies all translation, scaling and rotation to the {@link SpriteBatch}
	 */
	private void applyTransformations() {	
		float viewportWidth = MathUtils.round(currentWidth / scaleX);
		float viewportHeight = MathUtils.round(currentHeight / scaleY);
		
		camera.setToOrtho(true, viewportWidth, viewportHeight);
		
		if (translationX != 0f || translationY != 0f) {
			camera.translate(-translationX,
					-translationY);
		}
		camera.update();
		
		if (rotation != 0f) {
			camera.rotateAround(new Vector3(rotationX - translationX, rotationY - translationY, 0),
					new Vector3(0, 0, 1), -rotation);
		}
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Cleans up all translations, scaling and rotation
	 */
	private void undoTransformations() {
		if (rotation != 0f) {
		camera.rotateAround(new Vector3(rotationX - translationX, rotationY - translationY, 0), new Vector3(
				0, 0, 1), rotation);
		}
		camera.update();

		if (translationX != 0f || translationY != 0f) {
			camera.translate(translationX,
					translationY);
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
	 * 
	 * @return
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
}
