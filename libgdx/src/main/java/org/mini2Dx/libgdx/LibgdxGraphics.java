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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.gdx.math.EarClippingTriangulator;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Matrix4;
import org.mini2Dx.libgdx.game.GameWrapper;
import org.mini2Dx.libgdx.graphics.*;

public class LibgdxGraphics implements Graphics {
	private static final String LOGGING_TAG = LibgdxGraphics.class.getSimpleName();

	public final SpriteBatch spriteBatch;

	private final GameWrapper gameWrapper;
	private final ShapeTextureCache colorTextureCache;
	private final ShapeRenderer shapeRenderer;
	private final PolygonSpriteBatch polygonSpriteBatch;
	private final EarClippingTriangulator triangulator = new EarClippingTriangulator();

	private LibgdxColor color, backgroundColor;
	private LibgdxColor tint, defaultTint;
	private OrthographicCamera camera;
	private GameFont font;
	private LibgdxShader defaultShader, currentShader;

	private float translationX, translationY;
	private float scaleX, scaleY;
	private float rotation, rotationX, rotationY;
	private int windowWidth, windowHeight;

	private int defaultBlendSrcFunc = GL20.GL_SRC_ALPHA, defaultBlendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
	private int lineHeight;
	private boolean rendering, renderingShapes, renderingStage;
	private Rectangle clip;

	private float [] triangleVertices = new float[6];
	//3 edge polygon by default, expanded as needed during rendering
	private float [] polygonRenderData = new float[15];

	public LibgdxGraphics(GameWrapper gameWrapper, SpriteBatch spriteBatch, PolygonSpriteBatch polygonSpriteBatch, ShapeRenderer shapeRenderer) {
		super();
		this.gameWrapper = gameWrapper;
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;
		this.polygonSpriteBatch = polygonSpriteBatch;

		this.windowWidth = Gdx.graphics.getWidth();
		this.windowHeight = Gdx.graphics.getHeight();

		defaultTint = new LibgdxColor(spriteBatch.getColor());
		if (defaultTint != null) {
			font = Mdx.fonts.defaultFont();
		}
		tint = defaultTint;

		lineHeight = 1;
		//color = Color.WHITE;
		//backgroundColor = Color.BLACK;
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

	@Override
	public void preRender(int gameWidth, int gameHeight) {
		this.windowWidth = gameWidth;
		this.windowHeight = gameHeight;

		tint = defaultTint;
		spriteBatch.setColor(tint.rf(), tint.gf(), tint.bf(), tint.af());

		Gdx.gl.glClearColor(backgroundColor.rf(), backgroundColor.gf(), backgroundColor.bf(), 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

		rendering = false;
		renderingStage = false;

		if (defaultShader == null) {
			defaultShader = new LibgdxShader(SpriteBatch.createDefaultShader());
		}
	}

	@Override
	public void postRender() {
		endRendering();
		resetTransformations();
		clearShader();
		clearBlendFunction();
	}

	/**
	 * This method allows for translation, scaling, etc. to be set before the
	 * {@link SpriteBatch} begins
	 */
	private void beginRendering() {
		if (!rendering) {
			if(!renderingStage) {
				applyTransformations();

				Gdx.gl.glClearDepthf(1f);
				Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

				if (clip != null) {
					Gdx.gl.glDepthFunc(GL20.GL_LESS);
					Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

					Gdx.gl.glDepthMask(true);
					Gdx.gl.glColorMask(false, false, false, false);

					shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

					shapeRenderer.setColor(0f, 1f, 0f, 0.5f);
					shapeRenderer.rect(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight());

					shapeRenderer.end();

					spriteBatch.begin();

					Gdx.gl.glColorMask(true, true, true, true);
					Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
					Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
				} else {
					spriteBatch.begin();
				}
			}
			rendering = true;
		}
	}

	/**
	 * Ends rendering
	 */
	private void endRendering() {
		if (rendering) {
			if(!renderingStage) {
				undoTransformations();
				spriteBatch.end();
				if (renderingShapes) {
					shapeRenderer.end();
				}

				if (clip != null) {
					Gdx.gl.glClearDepthf(1f);
					Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
					Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
				}
			}
		}
		rendering = false;
		renderingShapes = false;
	}

	/**
	 * Applies all translation, scaling and rotation to the {@link SpriteBatch}
	 */
	private void applyTransformations() {
		float viewportWidth = MathUtils.round(windowWidth / scaleX);
		float viewportHeight = MathUtils.round(windowHeight / scaleY);

		camera.setToOrtho(true, viewportWidth, viewportHeight);

		if (translationX != 0f || translationY != 0f) {
			camera.translate(translationX, translationY);
		}
		camera.update();

		if (rotation != 0f) {
			camera.rotateAround(new Vector3(rotationX, rotationY, 0), new Vector3(0, 0, 1), -rotation);
		}
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		polygonSpriteBatch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Cleans up all translations, scaling and rotation
	 */
	private void undoTransformations() {
		if (rotation != 0f) {
			camera.rotateAround(new Vector3(rotationX, rotationY, 0), new Vector3(0, 0, 1), rotation);
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

	@Override
	public void drawLineSegment(float x1, float y1, float x2, float y2) {
		beginRendering();
		endRendering();

		renderingShapes = true;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color.rf(), color.gf(), color.bf(), color.af());
		shapeRenderer.rectLine(x1, y1, x2, y2, lineHeight);
		shapeRenderer.end();

		beginRendering();
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		int roundWidth = MathUtils.round(width);
		int roundHeight = MathUtils.round(height);

		beginRendering();
		endRendering();

		renderingShapes = true;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color.rf(), color.gf(), color.bf(), color.af());
		shapeRenderer.rectLine(x, y, x + roundWidth, y, lineHeight);
		shapeRenderer.rectLine(x, y, x , y + roundHeight, lineHeight);
		shapeRenderer.rectLine(x + roundWidth, y, x + roundWidth, y + roundHeight, lineHeight);
		shapeRenderer.rectLine(x, y + roundHeight, x + roundWidth, y + roundHeight, lineHeight);
		shapeRenderer.end();

		beginRendering();
	}

	@Override
	public void fillRect(float x, float y, float width, float height) {
		beginRendering();

		spriteBatch.draw(colorTextureCache.getFilledRectangleTexture(color), x, y, 0, 0, width, height, 1f, 1f, 0, 0, 0,
				1, 1, false, false);
	}

	@Override
	public void drawCircle(float centerX, float centerY, int radius) {
		beginRendering();
		endRendering();

		renderingShapes = true;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color.rf(), color.gf(), color.bf(), color.af());
		shapeRenderer.circle(centerX, centerY, radius);
		shapeRenderer.end();
	}

	@Override
	public void drawCircle(float centerX, float centerY, float radius) {
		drawCircle(centerX, centerY, MathUtils.round(radius));
	}

	@Override
	public void fillCircle(float centerX, float centerY, int radius) {
		beginRendering();
		endRendering();

		renderingShapes = true;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color.rf(), color.gf(), color.bf(), color.af());
		shapeRenderer.circle(centerX, centerY, radius);
		shapeRenderer.end();

		beginRendering();
	}

	@Override
	public void fillCircle(float centerX, float centerY, float radius) {
		fillCircle(centerX, centerY, MathUtils.round(radius));
	}

	@Override
	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		triangleVertices[0] = x1;
		triangleVertices[1] = y1;
		triangleVertices[2] = x2;
		triangleVertices[3] = y2;
		triangleVertices[4] = x3;
		triangleVertices[5] = y3;
		drawPolygon(triangleVertices);
	}

	@Override
	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		triangleVertices[0] = x1;
		triangleVertices[1] = y1;
		triangleVertices[2] = x2;
		triangleVertices[3] = y2;
		triangleVertices[4] = x3;
		triangleVertices[5] = y3;
		fillPolygon(triangleVertices, triangulator.computeTriangles(triangleVertices).items);
	}

	@Override
	public void drawPolygon(float[] vertices) {
		beginRendering();
		endRendering();

		renderingShapes = true;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color.rf(), color.gf(), color.bf(), color.af());
		shapeRenderer.polygon(vertices);
		shapeRenderer.end();

		beginRendering();
	}

	@Override
	public void fillPolygon(float[] vertices, short[] triangles) {
		beginRendering();
		endRendering();

		renderingShapes = true;
		if(vertices.length * 5 > polygonRenderData.length) {
			polygonRenderData = new float[vertices.length * 5];
		}

		int totalPoints = vertices.length / 2;
		for(int i = 0; i < totalPoints; i++) {
			int verticesIndex = i * 2;
			int renderIndex = i * 5;
			polygonRenderData[renderIndex] = vertices[verticesIndex];
			polygonRenderData[renderIndex + 1] = vertices[verticesIndex + 1];
			polygonRenderData[renderIndex + 2] = color.color.toFloatBits();
			polygonRenderData[renderIndex + 3] = vertices[verticesIndex];
			polygonRenderData[renderIndex + 4] = vertices[verticesIndex + 1];
		}

		polygonSpriteBatch.begin();
		polygonSpriteBatch.draw(colorTextureCache.getFilledRectangleTexture(color), polygonRenderData, 0, vertices.length * 5, triangles, 0, triangles.length);
		polygonSpriteBatch.end();

		beginRendering();
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
		drawTexture(texture, x, y, texture.getWidth(), texture.getHeight());
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, boolean flipY) {
		drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), flipY);
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, float width, float height) {
		drawTexture(texture, x, y, width, height, true);
	}

	@Override
	public void drawTexture(Texture texture, float x, float y, float width, float height, boolean flipY) {
		beginRendering();
		spriteBatch.draw((LibgdxTexture) texture, x, y, 0, 0, width, height, 1f, 1f, 0, 0, 0, texture.getWidth(), texture.getHeight(),
				false, flipY);
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y) {
		drawTextureRegion(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height) {
		drawTextureRegion(textureRegion, x, y, width, height, 0f);
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height,
	                              float rotation) {
		beginRendering();
		final LibgdxTextureRegion gdxTextureRegion = (LibgdxTextureRegion) textureRegion;
		spriteBatch.draw(gdxTextureRegion.textureRegion, x, y, 0f, 0f, width, height, 1f, 1f, rotation);
	}

	@Override
	public void drawShape(Shape shape) {
		shape.draw(this);
	}

	@Override
	public void fillShape(Shape shape) {
		shape.fill(this);
	}

	@Override
	public void drawSprite(Sprite sprite) {
		final LibgdxSprite gdxSprite = (LibgdxSprite) sprite;

		beginRendering();
		gdxSprite.sprite.draw(spriteBatch);
	}

	@Override
	public void drawSprite(Sprite sprite, float x, float y) {
		final LibgdxSprite gdxSprite = (LibgdxSprite) sprite;

		beginRendering();
		float oldX = sprite.getX();
		float oldY = sprite.getY();
		Color oldTint = sprite.getTint();

		if (tint != null) {
			sprite.setTint(tint);
		}
		sprite.setPosition(x, y);
		gdxSprite.sprite.draw(spriteBatch);
		sprite.setPosition(oldX, oldY);
		sprite.setTint(oldTint);
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
	public void drawFontCache(GameFontCache fontCache) {
		beginRendering();
		fontCache.draw(this);
	}

	@Override
	public void setClip(float x, float y, float width, float height) {
		if (rendering) {
			endRendering();
		}

		if(MathUtils.isEqual(0f, x) && MathUtils.isEqual(0f, y) &&
				MathUtils.isEqual(getViewportWidth(), width) &&
				MathUtils.isEqual(getViewportHeight(), height)) {
			clip = null;
		} else if(clip == null) {
			clip = new Rectangle(x, y, width, height);
		} else {
			clip.set(x, y, width, height);
		}
	}

	@Override
	public void setClip(Rectangle clip) {
		setClip(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight());
	}

	@Override
	public Rectangle removeClip() {
		if (clip == null) {
			return null;
		}
		if (rendering) {
			endRendering();
		}

		Rectangle result = clip;
		clip = null;
		return result;
	}

	@Override
	public Rectangle peekClip() {
		Rectangle result = new Rectangle();
		peekClip(result);
		return result;
	}

	@Override
	public void peekClip(Rectangle rectangle) {
		if(clip == null) {
			rectangle.set(0f, 0f, getViewportWidth(), getViewportHeight());
		} else {
			rectangle.set(clip);
		}
	}

	@Override
	public void setTint(Color tint) {
		this.tint = (LibgdxColor) tint;
		spriteBatch.setColor(this.tint.color);
	}

	@Override
	public void removeTint() {
		setTint(defaultTint);
	}

	@Override
	public void enableBlending() {
		spriteBatch.enableBlending();
	}

	@Override
	public void disableBlending() {
		spriteBatch.disableBlending();
	}

	@Override
	public void setShader(Shader shader) {
		this.currentShader = (LibgdxShader) shader;
		spriteBatch.setShader(currentShader.shaderProgram);
	}

	@Override
	public Shader getShader() {
		return currentShader;
	}

	@Override
	public void clearShader() {
		setShader(defaultShader);
	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc) {
		spriteBatch.setBlendFunction(srcFunc, dstFunc);
	}

	@Override
	public void clearBlendFunction() {
		spriteBatch.setBlendFunction(defaultBlendSrcFunc, defaultBlendDstFunc);
	}

	@Override
	public void flush() {
		spriteBatch.flush();
	}


	@Override
	public void setFont(GameFont font) {
		if (font == null) {
			return;
		}
		this.font = font;
	}

	@Override
	public void setLineHeight(int lineHeight) {
		if (lineHeight > 0) {
			this.lineHeight = lineHeight;
		}
	}

	@Override
	public void setColor(Color color) {
		if (color == null) {
			return;
		}
		this.color = (LibgdxColor) color;
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null) {
			this.backgroundColor = (LibgdxColor) backgroundColor;
		}
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		if (MathUtils.isEqual(1f, scaleX) && MathUtils.isEqual(1f, scaleY)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		this.scaleX *= scaleX;
		this.scaleY *= scaleY;
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		if (MathUtils.isEqual(this.scaleX, scaleX) && MathUtils.isEqual(this.scaleY, scaleY)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public void clearScaling() {
		if (MathUtils.isEqual(this.scaleX, 1f) && MathUtils.isEqual(this.scaleY, 1f)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		scaleX = 1f;
		scaleY = 1f;
	}

	@Override
	public void translate(float translateX, float translateY) {
		if (MathUtils.isZero(translateX) && MathUtils.isZero(translateY)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		this.translationX += translateX;
		this.translationY += translateY;
	}

	@Override
	public void setTranslation(float translateX, float translateY) {
		if (MathUtils.isEqual(this.translationX, translateX) && MathUtils.isEqual(this.translationY, translateY)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		this.translationX = translateX;
		this.translationY = translateY;
	}

	@Override
	public void rotate(float degrees, float x, float y) {
		if (MathUtils.isZero(degrees)) {
			return;
		}
		if (rendering) {
			endRendering();
		}

		this.rotation += degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}

	@Override
	public void setRotation(float degrees, float x, float y) {
		if(MathUtils.isEqual(this.rotation, degrees) &&
				MathUtils.isEqual(this.rotationX, x) &&
				MathUtils.isEqual(this.rotationY, y)) {
			return;
		}

		if (rendering) {
			endRendering();
		}

		this.rotation = degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}

	@Override
	public Matrix4 getProjectionMatrix() {
		return new Matrix4(camera.combined.val);
	}

	@Override
	public boolean isWindowReady() {
		return gameWrapper.isGameWindowReady();
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
	public int getWindowSafeX() {
		return 0;
	}

	@Override
	public int getWindowSafeY() {
		return 0;
	}

	@Override
	public int getWindowSafeWidth() {
		return windowWidth;
	}

	@Override
	public int getWindowSafeHeight() {
		return windowHeight;
	}

	@Override
	public long getFrameId() {
		return Gdx.graphics.getFrameId();
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
	public float getTranslationX() {
		return translationX;
	}

	@Override
	public float getTranslationY() {
		return translationY;
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
	public Color getTint() {
		return tint;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getLineHeight() {
		return lineHeight;
	}

	@Override
	public GameFont getFont() {
		return font;
	}
}
