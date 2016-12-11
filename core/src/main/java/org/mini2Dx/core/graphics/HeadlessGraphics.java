/**
 * Copyright (c) 2016 See AUTHORS file
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * LibGDX headless implementation of {@link Graphics}
 */
public class HeadlessGraphics implements Graphics {
	
	private Color color, backgroundColor, tint;
	private OrthographicCamera camera;
	
	private float translationX, translationY;
	private float scaleX, scaleY;
	private float rotation, rotationX, rotationY;
	private int windowWidth, windowHeight;

	private int lineHeight;
	private Rectangle clip;
	
	public HeadlessGraphics() {

		this.windowWidth = 0;
		this.windowHeight = 0;

		lineHeight = 1;
		color = Color.WHITE;
		backgroundColor = Color.BLACK;
		tint=null;
		
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

	@Override
	public void preRender(int gameWidth, int gameHeight) {
		this.windowWidth = gameWidth;
		this.windowHeight = gameHeight;
	}

	@Override
	public void postRender() {
		resetTransformations();
		clearShaderProgram();
		clearBlendFunction();
	}

	@Override
	public void drawLineSegment(float x1, float y1, float x2, float y2) {
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
	}

	@Override
	public void fillRect(float x, float y, float width, float height) {
	}

	@Override
	public void drawCircle(float centerX, float centerY, int radius) {
	}
	
	@Override
	public void drawCircle(float centerX, float centerY, float radius) {
	}

	@Override
	public void fillCircle(float centerX, float centerY, int radius) {
	}

	@Override
	public void fillCircle(float centerX, float centerY, float radius) {
	}
	
	@Override
	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
	}
	
	@Override
	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
	}
	
	@Override
	public void drawPolygon(float[] vertices) {
	}
	
	@Override
	public void fillPolygon(float [] vertices, short [] triangles) {
	}

	@Override
	public void drawString(String text, float x, float y) {
	}

	@Override
	public void drawString(String text, float x, float y, float targetWidth) {
	}

	@Override
	public void drawString(String text, float x, float y, float targetWidth, int horizontalAlign) {
	}

	@Override
	public void drawTexture(Texture texture, float x, float y) {
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, boolean flipY) {
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, float width, float height) {
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, float width, float height, boolean flipY) {
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y) {
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height) {
	}

	@Override
	public void drawShape(Shape shape) {
	}

	@Override
	public void fillShape(Shape shape) {
	}

	@Override
	public void drawSprite(Sprite sprite) {
	}

	@Override
	public void drawSprite(Sprite sprite, float x, float y) {
	}

	@Override
	public void drawSpriteCache(SpriteCache spriteCache, int cacheId) {
	}

	@Override
	public void drawStage(Stage stage) {
	}

	@Override
	public void drawParticleEffect(ParticleEffect effect) {
	}

	@Override
	public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height) {
	}

	@Override
	public void drawNinePatch(NinePatchDrawable ninePatchDrawable, float x, float y, float width, float height) {
	}
	
	@Override
	public void drawBitmapFontCache(BitmapFontCache bitmapFontCache) {
	}

	@Override
	public void rotate(float degrees, float x, float y) {
		this.rotation += degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}
	
	@Override
	public void setRotation(float degrees, float x, float y) {
		this.rotation = degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		this.scaleX *= scaleX;
		this.scaleY *= scaleY;
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public void clearScaling() {
		scaleX = 1f;
		scaleY = 1f;
	}

	@Override
	public void translate(float translateX, float translateY) {
		this.translationX += translateX;
		this.translationY += translateY;
	}
	
	@Override
	public void setTranslation(float translateX, float translateY) {
		this.translationX = translateX;
		this.translationY = translateY;
	}

	@Override
	public void setClip(float x, float y, float width, float height) {
		clip = new Rectangle(x, y, width, height);
	}

	@Override
	public void setClip(Rectangle clip) {
		this.clip = clip;
	}

	@Override
	public Rectangle removeClip() {
		Rectangle result = clip;
		clip = null;
		return result;
	}

	@Override
	public void setTint(Color tint) {
		this.tint = tint;
	}

	@Override
	public void setFont(BitmapFont font) {
	}

	@Override
	public void removeTint() {
	}

	@Override
	public void enableBlending() {
	}

	@Override
	public void disableBlending() {
	}

	@Override
	public void setShaderProgram(ShaderProgram shaderProgram) {
	}

	@Override
	public ShaderProgram getShaderProgram() {
		return null;
	}

	@Override
	public void clearShaderProgram() {
	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc) {
	}

	@Override
	public void clearBlendFunction() {
	}

	@Override
	public void flush() {
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
	
	@Override
	public int getLineHeight() {
		return lineHeight;
	}

	@Override
	public void setLineHeight(int lineHeight) {
		if (lineHeight > 0)
			this.lineHeight = lineHeight;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		if (color == null) {
			return;
		}
		this.color = color;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null)
			this.backgroundColor = backgroundColor;
	}

	@Override
	public BitmapFont getFont() {
		return null;
	}

	@Override
	public Color getTint() {
		return tint;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public float getTranslationX() {
		return translationX;
	}

	@Override
	public float getTranslationY() {
		return translationY;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public float getRotationX() {
		return rotationX;
	}

	@Override
	public float getRotationY() {
		return rotationY;
	}

	@Override
	public Matrix4 getProjectionMatrix() {
		return camera.combined.cpy();
	}

	@Override
	public int getWindowWidth() {
		return windowWidth;
	}

	@Override
	public int getWindowHeight() {
		return windowHeight;
	}
	
	@Override
	public float getViewportWidth() {
		return camera.viewportWidth;
	}

	@Override
	public float getViewportHeight() {
		return camera.viewportHeight;
	}

	@Override
	public String toString() {
		return "HeadlessLibGdxGraphics [color=" + color + ", backgroundColor=" + backgroundColor + ", tint=" + tint
				+ ", translationX=" + translationX + ", translationY=" + translationY + ", scaleX=" + scaleX
				+ ", scaleY=" + scaleY + ", rotation=" + rotation + ", rotationX=" + rotationX + ", rotationY="
				+ rotationY + ", windowWidth=" + windowWidth + ", windowHeight=" + windowHeight + ", lineHeight="
				+ lineHeight + "]";
	}
}
