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

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

/**
 * A {@link GameContainer} that allows visual user acceptance testing of
 * {@link Graphics} functionality
 * 
 * @author Thomas Cashman
 */
public class GraphicsUAT extends GameContainer {
	private int playerX, playerY;
	private float scaleX, scaleY;

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void initialise() {
		playerX = 0;
		playerY = 0;
		scaleX = 2f;
		scaleY = 2f;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		g.setColor(Color.RED);
		g.scale(scaleX, scaleY);
		g.translate(0 - playerX, 0 - playerY);

		g.drawRect(32, 32, 64, 64);
		g.rotate(25);
		g.fillRect(128, 32, 64, 64);
		g.rotate(-25);

		g.drawCircle(32, 160, 32);
		g.fillCircle(128, 160, 32);
		
		g.drawString("Hello, world!", 0, 256);
	}

	@Override
	public void handleInput(Input input) {
		if (input.isKeyPressed(Keys.RIGHT)) {
			playerX++;
		} else if (input.isKeyPressed(Keys.LEFT)) {
			playerX--;
		} else if (input.isKeyPressed(Keys.UP)) {
			playerY--;
		} else if (input.isKeyPressed(Keys.DOWN)) {
			playerY++;
		}

		if (input.isKeyPressed(Keys.E)) {
			scaleX++;
			scaleY++;
		} else if (input.isKeyPressed(Keys.Q)) {
			scaleX--;
			scaleY--;
		}
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mini2Dx - Graphics Verification Test";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		cfg.useCPUSynch = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new Mini2DxGame(new GraphicsUAT()), cfg);
	}
}
