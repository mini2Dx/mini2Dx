/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.desktop;

import org.mini2Dx.core.M2Dx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.desktop.di.DesktopDependencyInjection;

/**
 * Desktop implementation of {@link Mini2DxGame}
 *
 * @author Thomas Cashman
 */
public class DesktopMini2DxGame extends Mini2DxGame {

	public DesktopMini2DxGame(GameContainer gc) {
		super(gc);
	}

	@Override
	protected void initialiseM2Dx() {
		M2Dx.di = new DesktopDependencyInjection();
	}

}
