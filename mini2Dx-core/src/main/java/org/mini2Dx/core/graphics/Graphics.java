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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Implements graphics rendering functionality
 * 
 * @author Thomas Cashman
 */
public class Graphics {
	private Color color, backgroundColor;
	private SpriteBatch spriteBatch;
	private ColorTextureCache colorTextureCache;
	private OrthographicCamera camera;
	private BitmapFont font;

	private int translationX, translationY;
	private float scaleX, scaleY;
	private float rotation;
	private float currentWidth, currentHeight;

	private int lineHeight;
	private boolean rendering;

	public Graphics(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;

		lineHeight = 1;
		color = Color.WHITE;
		backgroundColor = Color.BLACK;
		colorTextureCache = new ColorTextureCache();

		translationX = 0;
		translationY = 0;
		scaleX = 1f;
		scaleY = 1f;
		rotation = 0f;

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
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		rendering = false;
	}

	/**
	 * Called by mini2Dx after rendering
	 */
	public void postRender() {
		spriteBatch.end();
		undoTransformations();
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
		spriteBatch.draw(colorTextureCache.getRectangleTextureForColor(color),
				(x * scaleX), (y * scaleY), 0, 0, width, height, scaleX,
				scaleY, 0, 0, 0, 1, 1, false, false);
		spriteBatch.draw(
				colorTextureCache.getRectangleTextureForColor(backgroundColor),
				(x * scaleX) + (lineHeight * scaleX), (y * scaleY)
						+ (lineHeight * scaleY), 0, 0,
				width - (lineHeight * 2), height - (lineHeight * 2), scaleX,
				scaleY, 0, 0, 0, 1, 1, false, false);
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
		spriteBatch.draw(colorTextureCache.getRectangleTextureForColor(color),
				(x * scaleX), (y * scaleY), 0, 0, width, height, scaleX,
				scaleY, 0, 0, 0, 1, 1, false, false);
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
		Texture texture = colorTextureCache.getCircularTextureForColor(color,
				radius);
		spriteBatch.draw(texture, (centerX * scaleX) - (radius * scaleX),
				(centerY * scaleY) - (radius * scaleY), 0, 0,
				texture.getWidth(), texture.getHeight(), scaleX, scaleY, 0, 0,
				0, texture.getWidth(), texture.getHeight(), false, false);

		texture = colorTextureCache.getCircularTextureForColor(backgroundColor,
				(radius - lineHeight));

		spriteBatch.draw(texture, ((centerX * scaleX) - (radius * scaleX))
				+ (lineHeight * scaleX),
				((centerY * scaleY) - (radius * scaleY))
						+ (lineHeight * scaleY), 0, 0, texture.getWidth(),
				texture.getHeight(), scaleX, scaleY, 0, 0, 0,
				texture.getWidth(), texture.getHeight(), false, false);
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
		Texture texture = colorTextureCache.getCircularTextureForColor(color,
				radius);

		beginRendering();
		spriteBatch.draw(texture, (centerX * scaleX) - (radius * scaleX),
				(centerY * scaleY) - (radius * scaleY), 0, 0,
				texture.getWidth(), texture.getHeight(), scaleX, scaleY, 0, 0,
				0, texture.getWidth(), texture.getHeight(), false, false);
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
			font.setScale(scaleX, scaleY);
			font.draw(spriteBatch, text, x * scaleX, y * scaleY);
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
		spriteBatch.draw(texture, (x * scaleX), (y * scaleY), 0, 0,
				texture.getWidth(), texture.getHeight(), scaleX, scaleY, 0, 0,
				0, texture.getWidth(), texture.getHeight(), false, false);
	}
	
	/**
	 * Draws a texture region to this graphics context
	 * @param textureRegion The {@link TextureRegion} to draw
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordinate to draw at
	 */
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y) {
		beginRendering();
		spriteBatch.draw(textureRegion, (x * scaleX), (y * scaleY), 0, 0, textureRegion.getRegionWidth(),
				textureRegion.getRegionHeight(), scaleX, scaleY, 0);
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
		float x = sprite.getX();
		float y = sprite.getY();
		sprite.setPosition(x * scaleX, y * scaleY);
		sprite.setScale(scaleX, scaleY);
		sprite.draw(spriteBatch);

		sprite.setPosition(x, y);
		sprite.setScale(1.0f, 1.0f);
	}

	/**
	 * Rotates the canvas by the provided degrees
	 * 
	 * @param degrees
	 *            The degree value in a clockwise direction
	 */
	public void rotate(float degrees) {
		if (rendering) {
			spriteBatch.end();
			camera.rotate(-rotation);
			rendering = false;
		}

		this.rotation += -degrees;
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
			spriteBatch.end();
			undoTransformations();
			rendering = false;
		}

		this.scaleX = scaleX;
		this.scaleY = scaleY;
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
			spriteBatch.end();
			undoTransformations();
			rendering = false;
		}

		this.translationX = translateX;
		this.translationY = translateY;
	}

	/**
	 * This method allows for translation, scaling, etc. to be set before the
	 * {@link SpriteBatch} begins
	 */
	private void beginRendering() {
		if (!rendering) {
			applyTransformations();
			spriteBatch.begin();
			rendering = true;
		}
	}

	/**
	 * Applies all translation, scaling and rotation to the {@link SpriteBatch}
	 */
	private void applyTransformations() {
		camera.setToOrtho(true, currentWidth, currentHeight);
		if (rotation != 0f) {
			camera.rotate(rotation);
		}

		if (translationX != 0 || translationY != 0) {
			camera.translate(translationX * scaleX, translationY * scaleY);
		}
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Cleans up all translations, scaling and rotation
	 */
	private void undoTransformations() {
		if (translationX != 0 || translationY != 0) {
			camera.translate(-(translationX * scaleX), -(translationY * scaleY));
		}

		if (rotation != 0f) {
			camera.rotate(-rotation);
		}
		camera.update();

		if (font != null) {
			font.setScale(1f, 1f);
		}
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
	 * Returns the foreground {@link Color}
	 * 
	 * @return A non-null value
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the foreground {@link Color} to be used
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
}
