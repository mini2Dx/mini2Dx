/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.PixmapFormat;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

public class PixmapUAT extends BasicGameScreen {
	private Pixmap pixmap;
	private Texture texture;

	private float timer = 0f;
	private int color;

	@Override
	public void initialise(GameContainer gc) {
		pixmap = Mdx.graphics.newPixmap(gc.getWidth(), gc.getHeight(), PixmapFormat.RGBA8888);
		texture = Mdx.graphics.newTexture(pixmap);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		timer += delta;

		if(timer > 5f) {
			color++;
			if(color > 2) {
				color = 0;
			}
			timer = 0f;
		}

		switch (color) {
		case 0:
			pixmap.setColor(Colors.RED());
			break;
		case 1:
			pixmap.setColor(Colors.GREEN());
			break;
		case 2:
			pixmap.setColor(Colors.BLUE());
			break;
		}

		pixmap.fill();

		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		texture.draw(pixmap, 0, 0);
		g.drawTexture(texture, 0, 0);

		g.setColor(Colors.WHITE());
		switch (color) {
		case 0:
			g.drawString("RED", 4, 4);
			break;
		case 1:
			g.drawString("GREEN", 4, 4);
			break;
		case 2:
			g.drawString("BLUE", 4, 4);
			break;
		}
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(PixmapUAT.class);
	}
}
