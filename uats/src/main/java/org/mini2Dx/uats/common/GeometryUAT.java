/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.uats.common;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

/**
 *
 *
 * @author Thomas Cashman
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
		new LwjglApplication(new Mini2DxGame(new GeometryUAT()), cfg);
	}
}
