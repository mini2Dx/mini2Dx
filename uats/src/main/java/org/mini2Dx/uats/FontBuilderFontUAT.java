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
import org.mini2Dx.gdx.utils.IntIntMap;
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
		fontParameters.xmlPath = "ch_bold_12.xml";
		fontParameters.texturePath = "ch_bold_12.png";
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
			fontCache.addText("Font cache example", 0f, 0f);
			fontCache.addText("Font cache wrapping example", 0f, font.getLineHeight(), 32f, Align.LEFT, true);
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

		g.drawString("炼金术耗尽了你的能量。你会在一定时间内重获能量，好好休息一下或者吃顿好的也会加速恢复能量。", 4f, renderY);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。\n紅國他那上上裡開型實", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.LEFT, true);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。紅國他那上上裡開型實", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.LEFT, true);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。紅國他那上上裡開型實", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.RIGHT, true);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。\n紅國他那上上裡開型實", 4f, renderY, -1f, Align.RIGHT, true);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。紅國他那上上裡開型實", 4f, renderY, (CHARACTER_WIDTH * 10) + 9, Align.CENTER, true);
		renderY += font.getLineHeight() * 4f;

		font.draw(g, "作轉望時能，一環時慢兒獨力行存人可生試什已西推開基。\n紅國他那上上裡開型實", 4f, renderY, -1f, Align.CENTER, true);
		renderY += font.getLineHeight() * 4f;

		fontCache.setPosition(128f, 4f);
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