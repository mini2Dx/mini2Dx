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
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * {@link Graphics} functionality
 */
public class GraphicsUAT extends BasicGameScreen {
	private final AssetManager assetManager;
	private final FileHandleResolver fileHandleResolver;

	private int playerX, playerY;
	private float scaleX, scaleY;
	private int rotation;

	private Texture texture;
	private TextureRegion textureRegion, atlasTextureRegion;
	private Sprite spriteWithTexture, spriteWithTextureRegion;
	private Animation<Sprite> animation;
	private Polygon polygon;
	private TextureAtlas textureAtlas;
	private NinePatch ninePatch;

	public GraphicsUAT(AssetManager assetManager, FileHandleResolver fileHandleResolver) {
		this.assetManager = assetManager;
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		playerX = 0;
		playerY = 0;
		scaleX = 2f;
		scaleY = 2f;
		rotation = 0;

		texture = Mdx.graphics.newTexture(fileHandleResolver.resolve("tank.png"));
		textureRegion = Mdx.graphics.newTextureRegion(texture);

		spriteWithTexture = Mdx.graphics.newSprite(texture);
		spriteWithTexture.setPosition(512, 0);

		spriteWithTextureRegion = Mdx.graphics.newSprite(textureRegion);

		animation = new Animation<Sprite>();
		animation.addFrame(Mdx.graphics.newSprite(texture), 1000);
		animation.setOriginX(spriteWithTexture.getWidth() / 2f);
		animation.setOriginY(0f);

		polygon = new Polygon(new float[] { 25f, 25f, 50f, 37.5f, 37.5f, 75f, 12.5f, 75f, 0f, 37.5f });
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (ninePatch == null) {
			textureAtlas = assetManager.get("default-mdx-theme.atlas", TextureAtlas.class);
			atlasTextureRegion = Mdx.graphics.newTextureRegion(textureAtlas.findRegion("frame"));
			ninePatch = Mdx.graphics.newNinePatch(atlasTextureRegion, 4, 4, 4, 4);
		}

		detectKeyPress(screenManager);
		rotation += 180f * delta;

		animation.setRotation(rotation);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.WHITE());

		g.setColor(Colors.GREEN());
		for (int x = 0; x < 800 / 32; x++) {
			for (int y = 0; y < 600 / 32; y++) {
				g.drawRect(x * 32, y * 32, 32, 32);
			}
		}

		g.setColor(Colors.BLUE());
		g.fillPolygon(polygon.getVertices(), polygon.getTriangles().items);
		g.setColor(Colors.BLUE());
		g.drawPolygon(polygon.getVertices());

		g.setColor(Colors.RED());
		g.fillTriangle(200f, 200f, 225f, 225f, 175f, 225f);
		g.setColor(Colors.BLUE());
		g.drawTriangle(200f, 200f, 225f, 225f, 175f, 225f);

		g.drawRect(32, 32, 64, 64);
		g.scale(scaleX, scaleY);
		g.translate(-(playerX * 32), -(playerY * 32));

		g.rotate(rotation, 32 + 16, 32 + 16);
		g.fillRect(32, 32, 32, 32);
		g.rotate(-rotation, 32 + 16, 32 + 16);

		g.setColor(Mdx.graphics.newColor(0f, 0f, 1f, 0.5f));
		g.setLineHeight(4);
		g.drawLineSegment(0f, 0f, 128f, 128f);
		g.setLineHeight(1);

		g.setColor(Colors.RED());

		g.rotate(rotation, 0, 0);
		g.fillRect(128, 32, 64, 64);
		g.rotate(-rotation, 0, 0);

		g.drawCircle(32, 160, 32);
		g.fillCircle(128, 160, 32);

		g.drawString("Hello, world!", 0, 256);

		g.scale(0.5f, 0.5f);
		g.drawSprite(spriteWithTexture);
		g.drawSprite(spriteWithTexture, 512, 64);
		g.drawTexture(texture, 512, 128);
		g.drawTextureRegion(textureRegion, 512, 192);

		g.drawLineSegment(512, 128, 620, 160);

		animation.draw(g, 512, 256);
		g.drawSprite(spriteWithTextureRegion, 512, 320);

		g.drawLineSegment(512, 160, 620, 224);

		if (ninePatch != null) {
			g.drawTextureRegion(atlasTextureRegion, 0, 360);
			g.drawNinePatch(ninePatch, 512, 360, 128, 128);
		}
	}

	public void detectKeyPress(ScreenManager<? extends GameScreen> screenManager) {
		if (Mdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			playerX++;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			playerX--;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.UP)) {
			playerY--;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			playerY++;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.E)) {
			scaleX++;
			scaleY++;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.Q)) {
			scaleX--;
			scaleY--;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.R)) {
			rotation++;
		} else if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void preTransitionIn(Transition transitionIn) {
	}

	@Override
	public void postTransitionIn(Transition transitionIn) {
	}

	@Override
	public void preTransitionOut(Transition transitionOut) {
	}

	@Override
	public void postTransitionOut(Transition transitionOut) {
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(GraphicsUAT.class);
	}
}
