/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.uats.common;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 *
 * @author Thomas Cashman
 */
public class BlendingUAT extends BasicGame {
	private Sprite sprite;

	@Override
	public void initialise() {
		sprite = new Sprite(new Texture(Gdx.files.internal("unsealed.png")));
		sprite.flip(false, true);
	}

	@Override
	public void update(float delta) {
	}
	
	@Override
	public void interpolate(float alpha) {
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.BLACK);
		//g.drawTexture(texture, 0, 0);
		
		/* Render shadow over everything */
		g.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Color shadow = new Color(1, 1, 1, 0.9f);
		g.setColor(shadow);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		/* Render lights to alpha mask */
		Gdx.gl.glColorMask(false, false, false, true);
		g.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		g.setColor(new Color(1f, 1f, 1f, 1f));
		g.fillCircle(sprite.getWidth() / 2f, sprite.getHeight() / 2f, MathUtils.round(sprite.getWidth() / 6f));
		g.flush();
		
		/* Render the scene */
		Gdx.gl.glColorMask(true, true, true, true);   
		g.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_DST_ALPHA);
		g.drawSprite(sprite);
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mini2Dx - Graphics Verification Test";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		cfg.useCPUSynch = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new Mini2DxGame(new BlendingUAT()), cfg);
	}
}