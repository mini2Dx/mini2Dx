/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.android;

import org.mini2Dx.android.di.AndroidDependencyInjection;
import org.mini2Dx.core.M2Dx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.Mini2DxGame;

import android.content.Context;

/**
 * Android implementation of {@link Mini2DxGame}
 *
 * @author Thomas Cashman
 */
public class AndroidMini2DxGame extends Mini2DxGame {
	private Context applicationContext;
	
	public AndroidMini2DxGame(Context applicationContext, GameContainer gc) {
		super(gc);
		this.applicationContext = applicationContext;
	}

	@Override
	protected void initialiseM2Dx() {
		M2Dx.di = new AndroidDependencyInjection(applicationContext);
	}

}
