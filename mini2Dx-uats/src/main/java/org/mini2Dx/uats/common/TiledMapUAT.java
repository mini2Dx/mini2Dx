/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.uats.common;

import java.io.IOException;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

/**
 * A {@link GameContainer} that allows visual user acceptance testing of
 * {@link TiledMap} rendering
 *
 * @author Thomas Cashman
 */
public class TiledMapUAT extends GameContainer {
	private TiledMap tiledMap;

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void initialise() {
		try {
			tiledMap = new TiledMap(Gdx.files.classpath("simple.tmx"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		g.setForegroundColor(Color.RED);
		
		tiledMap.draw(g, 0, 0);
		
		tiledMap.getTilesets().get(0).drawTileset(g, tiledMap.getWidth() * tiledMap.getTileWidth() + 32, 0);
	}

	@Override
	public void handleInput(Input input) {
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mini2Dx - Graphics Verification Test";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		cfg.useCPUSynch = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new Mini2DxGame(new TiledMapUAT()), cfg);
	}
}
