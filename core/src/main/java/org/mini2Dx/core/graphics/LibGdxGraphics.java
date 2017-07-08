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

import org.mini2Dx.core.game.GameWrapper;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * LibGDX implementation of {@link Graphics}
 */
public class LibGdxGraphics implements Graphics {
	private final GameWrapper gameWrapper;
	private final SpriteBatch spriteBatch;
	private final ShapeTextureCache colorTextureCache;
	private final ShapeRenderer shapeRenderer;
	private final PolygonSpriteBatch polygonSpriteBatch;
	private final EarClippingTriangulator triangulator = new EarClippingTriangulator();
	
	private Color color, backgroundColor, tint, defaultTint;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShaderProgram defaultShader;

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

	public LibGdxGraphics(GameWrapper gameWrapper, SpriteBatch spriteBatch, PolygonSpriteBatch polygonSpriteBatch, ShapeRenderer shapeRenderer) {
		super();
		this.gameWrapper = gameWrapper;
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;
		this.polygonSpriteBatch = polygonSpriteBatch;
		
		this.windowWidth = Gdx.graphics.getWidth();
		this.windowHeight = Gdx.graphics.getHeight();

		defaultTint = spriteBatch.getColor();
		if (defaultTint != null) {
			font = new BitmapFont(true);
		}
		tint = defaultTint;

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

	@Override
	public void preRender(int gameWidth, int gameHeight) {
		this.windowWidth = gameWidth;
		this.windowHeight = gameHeight;

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

		rendering = false;
		renderingStage = false;

		if (defaultShader == null) {
			defaultShader = SpriteBatch.createDefaultShader();
		}
	}

	@Override
	public void postRender() {
		endRendering();
		resetTransformations();
		clearShaderProgram();
		clearBlendFunction();
	}

	@Override
	public Stage createStage(Viewport viewport) {
		return new Stage(viewport, spriteBatch);
	}
	
	@Override
	public void drawLineSegment(float x1, float y1, float x2, float y2) {
		beginRendering();
		endRendering();

		/* TODO: Move all shape rendering over to using ShapeRenderer */
		renderingShapes = true;
		shapeRenderer.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color);
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
		shapeRenderer.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color);
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
		shapeRenderer.begin(ShapeType.Line);
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color);
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
		shapeRenderer.begin(ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color);
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

		/* TODO: Move all shape rendering over to using ShapeRenderer */
		renderingShapes = true;
		shapeRenderer.begin(ShapeType.Line);
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setColor(color);
		shapeRenderer.polygon(vertices);
		shapeRenderer.end();

		beginRendering();
	}
	
	@Override
	public void fillPolygon(float [] vertices, short [] triangles) {
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
			polygonRenderData[renderIndex + 2] = color.toFloatBits();
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
		if (font == null) {
			return;
		}
		beginRendering();
		font.setColor(color);
		font.draw(spriteBatch, text, x, y);
	}

	@Override
	public void drawString(String text, float x, float y, float targetWidth) {
		drawString(text, x, y, targetWidth, Align.left);
	}

	@Override
	public void drawString(String text, float x, float y, float targetWidth, int horizontalAlign) {
		if (font == null) {
			return;
		}
		beginRendering();
		font.setColor(color);
		font.draw(spriteBatch, text, x, y, targetWidth, horizontalAlign, true);
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
		spriteBatch.draw(texture, x, y, 0, 0, width, height, 1f, 1f, 0, 0, 0, texture.getWidth(), texture.getHeight(),
				false, flipY);
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y) {
		drawTextureRegion(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}

	@Override
	public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height) {
		beginRendering();
		spriteBatch.draw(textureRegion, x, y, 0f, 0f, width, height, 1f, 1f, 0f);
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
		beginRendering();
		sprite.draw(spriteBatch);
	}

	@Override
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

	@Override
	public void drawSpriteCache(SpriteCache spriteCache, int cacheId) {
		beginRendering();
		spriteCache.getProjectionMatrix().set(spriteBatch.getProjectionMatrix().cpy());
		spriteCache.getTransformMatrix().set(spriteBatch.getTransformMatrix().cpy());
		Gdx.gl.glEnable(GL20.GL_BLEND);

		spriteCache.begin();
		spriteCache.draw(cacheId);
		spriteCache.end();
	}

	@Override
	public void drawStage(Stage stage) {
		endRendering();
		
		Camera stageCamera = stage.getViewport().getCamera();
		stageCamera.up.set(0, -1, 0);
		stageCamera.direction.set(0, 0, 1);
		stageCamera.update();

		if (!stage.getRoot().isVisible()) {
			return;
		}

		renderingStage = true;
		beginRendering();
		
		spriteBatch.setProjectionMatrix(stageCamera.combined);
		polygonSpriteBatch.setProjectionMatrix(stageCamera.combined);
		shapeRenderer.setProjectionMatrix(stageCamera.combined);
		
		spriteBatch.begin();
		stage.getRoot().draw(spriteBatch, 1);
		spriteBatch.end();
		
		endRendering();
		renderingStage = false;
	}

	@Override
	public void drawParticleEffect(ParticleEffect effect) {
		beginRendering();
		effect.render(spriteBatch);
	}

	@Override
	public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height) {
		beginRendering();
		ninePatch.draw(spriteBatch, x, y, width, height);
	}

	@Override
	public void drawNinePatch(NinePatchDrawable ninePatchDrawable, float x, float y, float width, float height) {
		beginRendering();
		ninePatchDrawable.draw(spriteBatch, x, y, width, height);
	}
	
	@Override
	public void drawTiledDrawable(TiledDrawable tiledDrawable, float x, float y, float width, float height) {
		beginRendering();
		tiledDrawable.draw(spriteBatch, x, y, width, height);
	}
	
	@Override
	public void drawBitmapFontCache(BitmapFontCache bitmapFontCache) {
		beginRendering();
		bitmapFontCache.draw(spriteBatch);
	}

	@Override
	public void rotate(float degrees, float x, float y) {
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
		if (rendering) {
			endRendering();
		}

		this.rotation = degrees;
		this.rotation = this.rotation % 360f;
		this.rotationX = x;
		this.rotationY = y;
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		if (rendering) {
			endRendering();
		}

		this.scaleX *= scaleX;
		this.scaleY *= scaleY;
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		if (rendering) {
			endRendering();
		}

		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public void clearScaling() {
		if (rendering) {
			endRendering();
		}

		scaleX = 1f;
		scaleY = 1f;
	}

	@Override
	public void translate(float translateX, float translateY) {
		if (rendering) {
			endRendering();
		}

		this.translationX += translateX;
		this.translationY += translateY;
	}
	
	@Override
	public void setTranslation(float translateX, float translateY) {
		if (rendering) {
			endRendering();
		}

		this.translationX = translateX;
		this.translationY = translateY;
	}

	@Override
	public void setClip(float x, float y, float width, float height) {
		if (rendering) {
			endRendering();
		}

		clip = new Rectangle(x, y, width, height);
	}

	@Override
	public void setClip(Rectangle clip) {
		if (rendering) {
			endRendering();
		}

		this.clip = clip;
	}

	@Override
	public Rectangle removeClip() {
		if (rendering) {
			endRendering();
		}

		Rectangle result = clip;
		clip = null;
		return result;
	}

	@Override
	public void setTint(Color tint) {
		if (rendering) {
			endRendering();
		}

		this.tint = tint;
		spriteBatch.setColor(tint);
	}

	@Override
	public void setFont(BitmapFont font) {
		if (font != null) {
			if (rendering) {
				endRendering();
			}

			this.font = font;
		}
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
	public void setShaderProgram(ShaderProgram shaderProgram) {
		spriteBatch.setShader(shaderProgram);
	}

	@Override
	public ShaderProgram getShaderProgram() {
		return spriteBatch.getShader();
	}

	@Override
	public void clearShaderProgram() {
		spriteBatch.setShader(defaultShader);
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
					
					shapeRenderer.begin(ShapeType.Filled);
					
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
		return font;
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
	public String toString() {
		return "LibGdxGraphics [color=" + color + ", backgroundColor=" + backgroundColor + ", tint=" + tint
				+ ", translationX=" + translationX + ", translationY=" + translationY + ", scaleX=" + scaleX
				+ ", scaleY=" + scaleY + ", rotation=" + rotation + ", rotationX=" + rotationX + ", rotationY="
				+ rotationY + ", windowWidth=" + windowWidth + ", windowHeight=" + windowHeight + ", lineHeight="
				+ lineHeight + "]";
	}
}
