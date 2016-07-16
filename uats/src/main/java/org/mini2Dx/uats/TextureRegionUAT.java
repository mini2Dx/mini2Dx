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
package org.mini2Dx.uats;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * User acceptance testing of {@link TextureRegion} APIs
 */
public class TextureRegionUAT extends BasicGameScreen {
	private static final float MARGIN = 4f;
	public static final String TEXTURE_PACK = "texture-region-uat";

	private Texture texture;
	private TextureRegion fullSizeFromTexture, regionFromTexture;
	private TextureRegion fullSizeFromTextureRegion, regionFromTextureRegion;

	private TextureAtlas textureAtlas;
	private TextureRegion regionFromTextureAtlas;

	private NinePatch ninePatchFromTextureRegion;
	private NinePatch ninePatchFromTextureAtlas;

	@Override
	public void initialise(GameContainer gc) {
		texture = new Texture(Gdx.files.internal(TEXTURE_PACK + ".png"));
		fullSizeFromTexture = new TextureRegion(texture);
		debug("fullSizeFromTexture", fullSizeFromTexture);

		regionFromTexture = new TextureRegion(texture, 2, 2, 34, 48);
		debug("regionFromTexture", regionFromTexture);

		fullSizeFromTextureRegion = new TextureRegion(fullSizeFromTexture);
		debug("fullSizeFromTextureRegion", fullSizeFromTextureRegion);

		regionFromTextureRegion = new TextureRegion(regionFromTexture, 2, 2, 30, 44);
		debug("regionFromTextureRegion", regionFromTextureRegion);

		textureAtlas = new TextureAtlas(Gdx.files.internal(TEXTURE_PACK + ".atlas"));

		regionFromTextureAtlas = new TextureRegion(textureAtlas.findRegion("cardDiamondsJ"));
		debug("regionFromTextureAtlas", regionFromTextureAtlas);

		ninePatchFromTextureRegion = new NinePatch(regionFromTexture, 8, 8, 8, 8);
		ninePatchFromTextureAtlas = new NinePatch(regionFromTextureAtlas, 8, 8, 8, 8);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Gdx.input.justTouched()) {
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
		g.drawTextureRegion(regionFromTexture, xPosition, yPosition);
		yPosition += regionFromTexture.getRegionHeight() * g.getScaleY();
		
		g.drawString("(10) Scaled render of TextureAtlas region", xPosition, yPosition);
		yPosition += g.getFont().getLineHeight() + MARGIN;
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
