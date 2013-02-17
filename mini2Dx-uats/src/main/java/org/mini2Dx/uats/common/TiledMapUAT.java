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
		g.setColor(Color.RED);
		
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
