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
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.tiled.renderer.StaticTileRenderer;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link org.mini2Dx.core.graphics.Sprite} APIs
 */
public class SpriteUAT extends BasicGameScreen {
	private static final float MARGIN = 4f;
	public static final String TEXTURE_PACK = "texture-region-uat";

	private final FileHandleResolver fileHandleResolver;

	private Texture texture;
	private TextureRegion textureRegion, flipHRegion, flipVRegion, flipHVRegion;

	private Sprite spriteFullTexture;
	private Sprite spriteTextureRegion, spriteFlipHRegion, spriteFlipVRegion, spriteFlipHVRegion;

	public SpriteUAT(FileHandleResolver fileHandleResolver) {
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		texture = Mdx.graphics.newTexture(fileHandleResolver.resolve(TEXTURE_PACK + ".png"));
		textureRegion = Mdx.graphics.newTextureRegion(texture, 2, 2, 34, 48);
		flipHRegion = Mdx.graphics.newTextureRegion(texture, 2, 2, 34, 48);
		flipHRegion.setFlipX(true);
		flipVRegion = Mdx.graphics.newTextureRegion(texture, 2, 2, 34, 48);
		flipVRegion.setFlipY(true);
		flipHVRegion = Mdx.graphics.newTextureRegion(texture, 2, 2, 34, 48);
		flipHVRegion.setFlip(true, true);

		spriteFullTexture = Mdx.graphics.newSprite(texture);
		spriteTextureRegion = Mdx.graphics.newSprite(textureRegion);
		spriteFlipHRegion = Mdx.graphics.newSprite(flipHRegion);
		spriteFlipVRegion = Mdx.graphics.newSprite(flipVRegion);
		spriteFlipHVRegion = Mdx.graphics.newSprite(flipHVRegion);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		float xPosition = MARGIN;
		float yPosition = MARGIN;
		g.drawString("(1) Sprite from Texture - " +
				spriteFullTexture.isFlipX() + "-" + spriteFullTexture.isFlipY(), xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawSprite(spriteFullTexture, xPosition, yPosition);
		yPosition += spriteFullTexture.getHeight() + MARGIN;

		g.drawString("(2) Sprite from TextureRegion - " +
				spriteTextureRegion.isFlipX() + "-" + spriteTextureRegion.isFlipY(), xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawSprite(spriteTextureRegion, xPosition, yPosition);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(3) Sprite from TextureRegion (H)" +
				spriteFlipHRegion.isFlipX() + "-" + spriteFlipHRegion.isFlipY(), xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawSprite(spriteFlipHRegion, xPosition, yPosition);
		yPosition += spriteFlipHRegion.getRegionHeight();

		g.drawString("(4) Sprite from TextureRegion (V)" +
				spriteFlipVRegion.isFlipX() + "-" + spriteFlipVRegion.isFlipY(), xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawSprite(spriteFlipVRegion, xPosition, yPosition);
		yPosition += spriteFlipVRegion.getRegionHeight();

		g.drawString("(5) Sprite from TextureRegion (H + V)" +
				spriteFlipHVRegion.isFlipX() + "-" + spriteFlipHVRegion.isFlipY(), xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawSprite(spriteFlipHVRegion, xPosition, yPosition);
		yPosition += spriteFlipHVRegion.getRegionHeight() + MARGIN;

		g.drawString("(6) drawTileImage of (2) hvd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, false, false, false);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(7) drawTileImage of (3) hvd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteFlipHRegion, (int) xPosition, (int) yPosition, false, false, false);
		yPosition += spriteFlipHRegion.getRegionHeight() + MARGIN;

		g.drawString("(8) drawTileImage of (4) hvd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteFlipVRegion, (int) xPosition, (int) yPosition, false, false, false);

		xPosition += gc.getWidth() / 2;
		yPosition = MARGIN;

		g.drawString("(9) drawTileImage of (5) hvd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, false, false, false);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(10) drawTileImage of (2) Hvd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, true, false, false);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(11) drawTileImage of (2) hVd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, false, true, false);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(12) drawTileImage of (2) hvD", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, false, false, true);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(13) drawTileImage of (2) HVd", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, true, true, false);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(14) drawTileImage of (2) HvD", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, true, false, true);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(15) drawTileImage of (2) hVD", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, false, true, true);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

		g.drawString("(16) drawTileImage of (2) HVD", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		StaticTileRenderer.drawTileImage(g, spriteTextureRegion, (int) xPosition, (int) yPosition, true, true, true);
		yPosition += spriteTextureRegion.getRegionHeight() + MARGIN;

	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(SpriteUAT.class);
	}
}
