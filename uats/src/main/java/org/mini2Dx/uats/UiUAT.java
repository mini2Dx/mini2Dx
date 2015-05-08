/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author Thomas Cashman
 */
public class UiUAT implements GameScreen {
	public static final int SCREEN_ID = 7;

	@Override
	public void initialise(GameContainer gc) {
	}

	@Override
	public void update(GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
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
		return SCREEN_ID;
	}

}
