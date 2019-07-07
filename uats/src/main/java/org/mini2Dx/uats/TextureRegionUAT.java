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
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link TextureRegion} APIs
 */
public class TextureRegionUAT extends BasicGameScreen {
	private static final float MARGIN = 4f;
	public static final String TEXTURE_PACK = "texture-region-uat";

	private final FileHandleResolver fileHandleResolver;

	private Texture texture;
	private TextureRegion fullSizeFromTexture, regionFromTexture;
	private TextureRegion fullSizeFromTextureRegion, regionFromTextureRegion;

	private TextureAtlas textureAtlas;
	private TextureRegion regionFromTextureAtlas;

	private NinePatch ninePatchFromTextureRegion;
	private NinePatch ninePatchFromTextureAtlas;

	public TextureRegionUAT(FileHandleResolver fileHandleResolver) {
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		texture = Mdx.graphics.newTexture(fileHandleResolver.resolve(TEXTURE_PACK + ".png"));
		fullSizeFromTexture = Mdx.graphics.newTextureRegion(texture);
		debug("fullSizeFromTexture", fullSizeFromTexture);

		regionFromTexture = Mdx.graphics.newTextureRegion(texture, 2, 2, 34, 48);
		debug("regionFromTexture", regionFromTexture);

		fullSizeFromTextureRegion = Mdx.graphics.newTextureRegion(fullSizeFromTexture);
		debug("fullSizeFromTextureRegion", fullSizeFromTextureRegion);

		regionFromTextureRegion = Mdx.graphics.newTextureRegion(regionFromTexture, 2, 2, 30, 44);
		debug("regionFromTextureRegion", regionFromTextureRegion);

		textureAtlas = Mdx.graphics.newTextureAtlas(fileHandleResolver.resolve(TEXTURE_PACK + ".atlas"));

		regionFromTextureAtlas = Mdx.graphics.newTextureRegion(textureAtlas.findRegion("cardDiamondsJ"));
		debug("regionFromTextureAtlas", regionFromTextureAtlas);

		ninePatchFromTextureRegion = Mdx.graphics.newNinePatch(regionFromTexture, 8, 8, 8, 8);
		ninePatchFromTextureAtlas = Mdx.graphics.newNinePatch(regionFromTextureAtlas, 8, 8, 8, 8);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		float xPosition = MARGIN;
		float yPosition = MARGIN;
		g.drawString("(1) Texture", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTexture(texture, xPosition, yPosition);
		yPosition += texture.getHeight() + MARGIN;

		g.drawString("(2) Full Texture using TextureRegion", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTextureRegion(fullSizeFromTexture, xPosition, yPosition);
		yPosition += fullSizeFromTexture.getRegionHeight() + MARGIN;

		g.drawString("(3) Region of Full Texture", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTextureRegion(regionFromTexture, xPosition, yPosition);
		yPosition += regionFromTexture.getRegionHeight();

		g.drawString("(4) Full Texture created from (2)", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTextureRegion(fullSizeFromTextureRegion, xPosition, yPosition);
		yPosition += fullSizeFromTextureRegion.getRegionHeight();

		g.drawString("(5) Region of (3)", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTextureRegion(regionFromTextureRegion, xPosition, yPosition);

		xPosition += gc.getWidth() / 2;
		yPosition = MARGIN;

		g.drawString("(6) Region from TextureAtlas", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawTextureRegion(regionFromTextureAtlas, xPosition, yPosition);
		yPosition += regionFromTextureAtlas.getRegionHeight();

		g.drawString("(7) NinePatch from TextureRegion", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawNinePatch(ninePatchFromTextureRegion, xPosition, yPosition, regionFromTexture.getRegionWidth(),
				regionFromTexture.getRegionHeight());
		yPosition += regionFromTexture.getRegionHeight();
		
		g.drawString("(8) NinePatch from TextureAtlas", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.drawNinePatch(ninePatchFromTextureAtlas, xPosition, yPosition, regionFromTexture.getRegionWidth(),
				regionFromTexture.getRegionHeight());
		yPosition += regionFromTexture.getRegionHeight();
		
		g.drawString("(9) Scaled render of TextureRegion", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		
		xPosition *= 0.6;
		g.setScale(1.5f, 1.5f);
		g.setTint(Colors.RED());
		g.drawTextureRegion(regionFromTexture, xPosition, yPosition);
		yPosition += regionFromTexture.getRegionHeight() * g.getScaleY();
		
		g.drawString("(10) Scaled render of TextureAtlas region", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
		g.setTint(Colors.WHITE());
		g.drawTextureRegion(regionFromTextureAtlas, xPosition, yPosition);
		yPosition += regionFromTextureAtlas.getRegionHeight() * g.getScaleY();
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(TextureRegionUAT.class);
	}

	private void debug(String name, TextureRegion textureRegion) {
		System.out.println(name + " " + textureRegion.getRegionX() + " " + textureRegion.getRegionY() + " "
				+ textureRegion.getRegionWidth() + " " + textureRegion.getRegionHeight());
		System.out.println(name + " " + textureRegion.getU() + " " + textureRegion.getU2() + " " + textureRegion.getV()
				+ " " + textureRegion.getV2());
		System.out.println(name + " " + textureRegion.isFlipX() + " " + textureRegion.isFlipY());
	}
}
