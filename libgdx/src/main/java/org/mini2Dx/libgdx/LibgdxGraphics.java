/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.libgdx;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.gdx.math.Matrix4;
import org.mini2Dx.libgdx.game.GameWrapper;

public class LibgdxGraphics implements Graphics {

	public LibgdxGraphics(GameWrapper gameWrapper, SpriteBatch spriteBatch, PolygonSpriteBatch polygonSpriteBatch, ShapeRenderer shapeRenderer) {
		super();
	}

	@Override
	public void preRender(int gameWidth, int gameHeight) {

	}

	@Override
	public void postRender() {

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
	public void fillPolygon(float[] vertices, short[] triangles) {

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
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height, float rotation) {

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
	public void drawParticleEffect(ParticleEffect effect) {

	}

	@Override
	public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height) {

	}

	@Override
	public void drawTilingDrawable(TilingDrawable tilingDrawable, float x, float y, float width, float height) {

	}

	@Override
	public void drawFontCache(GameFontCache gameFontCache) {

	}

	@Override
	public void setClip(float x, float y, float width, float height) {

	}

	@Override
	public void setClip(Rectangle clip) {

	}

	@Override
	public Rectangle removeClip() {
		return null;
	}

	@Override
	public Rectangle peekClip() {
		return null;
	}

	@Override
	public void peekClip(Rectangle rectangle) {

	}

	@Override
	public void setTint(Color tint) {

	}

	@Override
	public void setFont(GameFont font) {

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
	public void setShader(Shader shader) {

	}

	@Override
	public Shader getShader() {
		return null;
	}

	@Override
	public void clearShader() {

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

	@Override
	public int getLineHeight() {
		return 0;
	}

	@Override
	public void setLineHeight(int lineHeight) {

	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public void setColor(Color color) {

	}

	@Override
	public Color getBackgroundColor() {
		return null;
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {

	}

	@Override
	public GameFont getFont() {
		return null;
	}

	@Override
	public Color getTint() {
		return null;
	}

	@Override
	public float getScaleX() {
		return 0;
	}

	@Override
	public float getScaleY() {
		return 0;
	}

	@Override
	public void scale(float scaleX, float scaleY) {

	}

	@Override
	public void setScale(float scaleX, float scaleY) {

	}

	@Override
	public void clearScaling() {

	}

	@Override
	public float getTranslationX() {
		return 0;
	}

	@Override
	public float getTranslationY() {
		return 0;
	}

	@Override
	public void translate(float translateX, float translateY) {

	}

	@Override
	public void setTranslation(float translateX, float translateY) {

	}

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public float getRotationX() {
		return 0;
	}

	@Override
	public float getRotationY() {
		return 0;
	}

	@Override
	public void rotate(float degrees, float x, float y) {

	}

	@Override
	public void setRotation(float degrees, float x, float y) {

	}

	@Override
	public Matrix4 getProjectionMatrix() {
		return null;
	}

	@Override
	public boolean isWindowReady() {
		return false;
	}

	@Override
	public int getWindowWidth() {
		return 0;
	}

	@Override
	public int getWindowHeight() {
		return 0;
	}

	@Override
	public int getWindowSafeX() {
		return 0;
	}

	@Override
	public int getWindowSafeY() {
		return 0;
	}

	@Override
	public int getWindowSafeWidth() {
		return 0;
	}

	@Override
	public int getWindowSafeHeight() {
		return 0;
	}

	@Override
	public float getViewportWidth() {
		return 0;
	}

	@Override
	public float getViewportHeight() {
		return 0;
	}

	@Override
	public long getFrameId() {
		return 0;
	}
}
