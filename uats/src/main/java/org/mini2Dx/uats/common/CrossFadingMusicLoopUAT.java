/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.mini2Dx.uats.common;

import java.util.concurrent.TimeUnit;

import org.mini2Dx.core.audio.CrossFadingMusicLoop;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.desktop.DesktopMini2DxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Utility instance for running a UAT for {@link CrossFadingMusicLoop}
 * 
 * @author Thomas Cashman
 */
public class CrossFadingMusicLoopUAT extends GameContainer {
	private CrossFadingMusicLoop musicLoop;

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialise() {
		musicLoop = new CrossFadingMusicLoop(
				Gdx.files.classpath("crossfade.mp3"), 119950, 5000, TimeUnit.MILLISECONDS);
	}

	@Override
	public void update(float delta) {
		if (!musicLoop.isPlaying()) {
			musicLoop.play();
		}
	}

	@Override
	public void interpolate(float alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mini2Dx - CrossFadingMusicLoop Verification Test";
		cfg.width = 240;
		cfg.height = 240;
		cfg.stencil = 8;
		cfg.vSyncEnabled = true;
		cfg.foregroundFPS = 0;
		cfg.backgroundFPS = 0;
		new LwjglApplication(new DesktopMini2DxGame(new CrossFadingMusicLoopUAT()),
				cfg);
	}
}
