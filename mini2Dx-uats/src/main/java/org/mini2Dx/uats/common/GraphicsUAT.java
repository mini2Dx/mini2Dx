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

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A {@link GameContainer} that allows visual user acceptance testing of
 * {@link Graphics} functionality
 * 
 * @author Thomas Cashman
 */
public class GraphicsUAT extends BasicGame {
	private int playerX, playerY;
	private float scaleX, scaleY;
	private int rotation;
	
	private Sprite sprite;
	private Animation<Sprite> animation;

	@Override
	public void initialise() {
		playerX = 0;
		playerY = 0;
		scaleX = 2f;
		scaleY = 2f;
		rotation = 0;
		
		sprite = new Sprite(new Texture(Gdx.files.internal("tank.png")));
		sprite.flip(false, true);
		sprite.setPosition(512, 0);
		
		animation = new Animation<Sprite>();
		animation.addFrame(sprite, 1000);
		animation.setOriginX(sprite.getWidth() / 2f);
		animation.setOriginY(0f);
	}

	@Override
	public void update(float delta) {
		rotation += 180f * delta;
		
		animation.setRotation(rotation);
	}
	
	@Override
	public void interpolate(float alpha) {
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		
		g.setColor(Color.GREEN);
		for(int x = 0; x < 800 / 32; x++) {
			for(int y = 0; y < 600 / 32; y++) {
				g.drawRect(x * 32, y * 32, 32, 32);
			}
		}
		
		g.drawRect(32, 32, 64, 64);
		g.scale(scaleX, scaleY);
		g.translate(-(playerX * 32), -(playerY * 32));
		
		g.setColor(Color.BLUE);
		g.rotate(rotation, 32 + 16, 32 + 16);
		g.fillRect(32, 32, 32, 32);
		g.rotate(-rotation, 32 + 16, 32 + 16);
		
		g.setColor(Color.RED);


		g.rotate(rotation, 0, 0);
		g.fillRect(128, 32, 64, 64);
		g.rotate(-rotation, 0, 0);
		
		g.drawCircle(32, 160, 32);
		g.fillCircle(128, 160, 32);
		
		g.drawString("Hello, world!", 0, 256);
		
		g.scale(0.5f, 0.5f);
		g.drawSprite(sprite);
		g.drawSprite(sprite, 512, 64);
		
		animation.draw(g, 512, 128);
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
		}
		return true;
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
