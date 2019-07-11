/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledMapLoader;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of orthogonal
 * {@link TiledMap} rendering with layer caching disabled
 */
public class OrthogonalTiledMapNoCachingUAT extends BasicGameScreen {
	private final AssetManager assetManager;
	
	private TiledMap tiledMap, tsxTiledmap;
	private TiledMap renderMap;
	
	public OrthogonalTiledMapNoCachingUAT(AssetManager assetManager) {
    	super();
		this.assetManager = assetManager;

		final TiledMapLoader.TiledAssetProperties loadMapParameter = new TiledMapLoader.TiledAssetProperties();
		loadMapParameter.loadTilesets = true;

		assetManager.load("orthogonal_no_cache.tmx", TiledMap.class, loadMapParameter);
		assetManager.load("orthogonal_tsx.tmx", TiledMap.class, loadMapParameter);
	}

	@Override
	public void initialise(GameContainer gc) {
		assetManager.finishLoading();
		try {
			tiledMap = assetManager.get("orthogonal_no_cache.tmx", TiledMap.class);
			tsxTiledmap = assetManager.get("orthogonal_tsx.tmx", TiledMap.class);
			renderMap = tiledMap;
		} catch (TiledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
		if(Mdx.input.isKeyJustPressed(Input.Keys.Q)) {
			if(renderMap.equals(tiledMap)) {
				renderMap = tsxTiledmap;
			} else {
				renderMap = tiledMap;
			}
		}
		tiledMap.update(delta);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.WHITE());
		g.setColor(Colors.RED());

		renderFullMapInTopLeftCorner(g);
		renderPartOfMapUnderTopLeftMap(g);
		renderFirstTilesetInTopRightCorner(g);
		renderTranslatedFullMapInBottomLeftCorner(g);
		renderScaledDownFullMap(g);
		renderScaledAndRotatedMapInBottomRightCorner(g);
	}

	private void renderFullMapInTopLeftCorner(Graphics g) {
		renderMap.draw(g, 0, 0);
	}

	private void renderFirstTilesetInTopRightCorner(Graphics g) {
		renderMap.getTilesets().get(0).drawTileset(g, renderMap.getWidth() * renderMap.getTileWidth() + 32, 0);
	}

	private void renderScaledAndRotatedMapInBottomRightCorner(Graphics g) {
		g.scale(1.25f, 1.25f);
		g.rotate(5f, 0f, (renderMap.getHeight() * renderMap.getTileHeight()) * 1.5f);

		// Render rotated map in bottom right corner
		renderMap.draw(g, renderMap.getWidth() * renderMap.getTileWidth(),
				MathUtils.round((renderMap.getHeight() * renderMap.getTileHeight()) * 1.5f), 1, 1, 4, 8);

		g.rotate(-5f, 0f, (renderMap.getHeight() * renderMap.getTileHeight()) * 1.5f);
		g.scale(0.8f, 0.8f);
	}

	private void renderPartOfMapUnderTopLeftMap(Graphics g) {
		renderMap.draw(g, 32, renderMap.getHeight() * renderMap.getTileHeight(), 1, 1, 4, 8);
	}

	private void renderTranslatedFullMapInBottomLeftCorner(Graphics g) {
		int mapWidthInPixels = renderMap.getWidth() * renderMap.getTileWidth();
		g.translate(mapWidthInPixels, 0);
		renderMap.draw(g, mapWidthInPixels,
				(renderMap.getHeight() * renderMap.getTileHeight()) + (8 * renderMap.getTileHeight()));
		g.translate(-mapWidthInPixels, 0);
	}

	private void renderScaledDownFullMap(Graphics g) {
		g.setScale(0.55f, 0.55f);
		renderMap.draw(g, MathUtils.round(g.getWindowWidth() - ((renderMap.getTileWidth() / 2) * renderMap.getWidth())),
				MathUtils.round(g.getWindowHeight() - ((renderMap.getTileHeight() / 2) * renderMap.getHeight())));
		g.setScale(1f, 1f);
	}

	@Override
	public void preTransitionIn(Transition transitionIn) {
	}

	@Override
	public void postTransitionIn(Transition transitionIn) {
	}

	@Override
	public void preTransitionOut(Transition transitionOut) {
	}

	@Override
	public void postTransitionOut(Transition transitionOut) {
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(OrthogonalTiledMapNoCachingUAT.class);
	}
}
