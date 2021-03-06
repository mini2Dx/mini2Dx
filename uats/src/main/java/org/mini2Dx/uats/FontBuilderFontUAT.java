/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileType;
import org.mini2Dx.core.font.FontBuilderGameFont;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

public class FontBuilderFontUAT extends BasicGameScreen implements MonospaceGameFont.FontRenderListener {
	private final int CHARACTER_WIDTH = 16;

	private final AssetManager assetManager;
	private final FontBuilderGameFont font;
	private final GameFontCache fontCache;

	private boolean cacheSetUp = false;

	public FontBuilderFontUAT(AssetManager assetManager) {
		super();
		this.assetManager = assetManager;

		final FontBuilderGameFont.FontParameters fontParameters = new FontBuilderGameFont.FontParameters();
		fontParameters.xmlPath = "ipix_regular_12.xml";
		fontParameters.texturePath = "ipix_regular_12.png";
		fontParameters.xmlFileHandleType = FileType.INTERNAL;

		font = new FontBuilderGameFont(fontParameters);
		fontCache = font.newCache();
	}

	@Override
	public void initialise(GameContainer gc) {
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
		assetManager.update();
		if (!font.load(assetManager)) {
			return;
		}
		if (!cacheSetUp) {
			fontCache.addText("我很好！如果拿到珍宝的话会更好！我们一起搞定它！", 0f, 0f);
			fontCache.addText("别担心，亲爱的朋友，伊维特会照顾好你的。", 0f, font.getLineHeight(), 32f, Align.LEFT, true);
			cacheSetUp = true;
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {

	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if (!font.load(assetManager)) {
			return;
		}

		final GameFont defaultFont = g.getFont();

		g.setFont(font);
		g.setColor(Colors.RED());

		float renderY = 4f;

		g.drawString("看到了吗?没那么难是不是?接下来就随意探索这座山吧.", 4f, renderY);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?\n接下来就随意探索这座山吧.", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.LEFT, true);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?接下来就随意探索这座山吧.。", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.LEFT, true);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?接下来就随意探索这座山吧.。", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.RIGHT, true);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?\n接下来就随意探索这座山吧.", 4f, renderY, -1f, Align.RIGHT, true);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?接下来就随意探索这座山吧.", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.CENTER, true);
		renderY += font.getLineHeight() * 5f;

		font.draw(g, "看到了吗?没那么难是不是?\n接下来就随意探索这座山吧.", 4f, renderY, -1f, Align.CENTER, true);
		renderY += font.getLineHeight() * 5f;

		fontCache.setPosition(128f, 128f);
		fontCache.draw(g);

		g.setFont(defaultFont);
		g.drawString("Change to previous font", 4f, renderY);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(FontBuilderFontUAT.class);
	}

	@Override
	public boolean preRenderChar(Graphics g, char c, float charRenderX, float charRenderY, float charRenderWidth, float charRenderHeight) {
		return true;
	}

	@Override
	public void postRenderChar(Graphics g, char c, float charRenderX, float charRenderY, float charRenderWidth, float charRenderHeight) {
	}
}