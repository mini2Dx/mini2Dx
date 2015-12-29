/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class LoadingScreen extends BasicGameScreen {
	public static final int SCREEN_ID = 0;
	
	private final AssetManager assetManager;
	
	public LoadingScreen(AssetManager assetManager) {
		this.assetManager = assetManager;
		
		assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);
	}

	@Override
	public void initialise(GameContainer gc) {
		
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if(assetManager.update()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.drawString("Loading required resources... " + (assetManager.getProgress() * 100f) + "%", 32f, 32f);
	}

	@Override
	public int getId() {
		return SCREEN_ID;
	}

}
