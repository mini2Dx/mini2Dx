/**
 * Copyright (c) 2015, mini2Dx Project
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

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.desktop.DesktopMini2DxGame;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

/**
 * UAT for verifying Geometry classes and their rendering
 */
public class GeometryUAT extends BasicGame {
	private int playerX, playerY, originX, originY;
	private float scaleX, scaleY;
	private int rotation;
	
	private Rectangle rect;

	@Override
	public void initialise() {
		rect = new Rectangle(0, 0, 128, 128);
		
		playerX = 0;
		playerY = 0;
		originX = 0;
		originY = 0;
		scaleX = 2f;
		scaleY = 2f;
		rotation = 0;
	}

	@Override
	public void update(float delta) {
		rotation += 180f * delta;

		rect.set(playerX * 32f, playerY * 32f, 128, 128);
		rect.rotateAround(new Point(originX * 32f, originY * 32f), 180f * delta);
	}

	@Override
	public void interpolate(float alpha) {
		
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		g.setColor(Color.RED);
		rect.draw(g);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.RIGHT:
			playerX++;
			break;
		case Keys.LEFT:
			playerX--;
			break;
		case Keys.UP:
			playerY--;
			break;
		case Keys.DOWN:
			playerY++;
			break;
		case Keys.E:
			scaleX++;
			scaleY++;
			break;
		case Keys.Q:
			scaleX--;
			scaleY--;
			break;
		case Keys.R:
			rotation++;
			break;
		case Keys.W:
			originY--;
			break;
		case Keys.S:
			originY++;
			break;
		case Keys.A:
			originX--;
			break;
		case Keys.D:
			originX++;
			break;
		}
		return true;
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mini2Dx - Geometry Verification Test";
		cfg.width = 800;
		cfg.height = 600;
		cfg.vSyncEnabled = true;
		cfg.foregroundFPS = 0;
		cfg.backgroundFPS = 0;
		new LwjglApplication(new DesktopMini2DxGame("org.mini2Dx.uats.common.GeometryUAT", new GeometryUAT()), cfg);
	}
}
