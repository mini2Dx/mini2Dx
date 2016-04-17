/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of orthogonal
 * {@link TiledMap} rendering with layer caching disabled
 */
public class OrthogonalTiledMapNoCachingUAT extends BasicGameScreen {
	private TiledMap tiledMap;

	@Override
	public void initialise(GameContainer gc) {
		try {
			tiledMap = new TiledMap(Gdx.files.classpath("orthogonal.tmx"), true, false);
		} catch (TiledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Gdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		g.setColor(Color.RED);

		renderFullMapInTopLeftCorner(g);
		renderPartOfMapUnderTopLeftMap(g);
		renderFirstTilesetInTopRightCorner(g);
		renderTranslatedFullMapInBottomLeftCorner(g);
		renderScaledDownFullMap(g);
		renderScaledAndRotatedMapInBottomRightCorner(g);
	}

	private void renderFullMapInTopLeftCorner(Graphics g) {
		tiledMap.draw(g, 0, 0);
	}

	private void renderFirstTilesetInTopRightCorner(Graphics g) {
		tiledMap.getTilesets().get(0).drawTileset(g, tiledMap.getWidth() * tiledMap.getTileWidth() + 32, 0);
	}

	private void renderScaledAndRotatedMapInBottomRightCorner(Graphics g) {
		g.scale(1.25f, 1.25f);
		g.rotate(5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);

		// Render rotated map in bottom right corner
		tiledMap.draw(g, tiledMap.getWidth() * tiledMap.getTileWidth(),
				MathUtils.round((tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f), 1, 1, 4, 8);

		g.rotate(-5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);
		g.scale(0.8f, 0.8f);
	}

	private void renderPartOfMapUnderTopLeftMap(Graphics g) {
		tiledMap.draw(g, 32, tiledMap.getHeight() * tiledMap.getTileHeight(), 1, 1, 4, 8);
	}

	private void renderTranslatedFullMapInBottomLeftCorner(Graphics g) {
		int mapWidthInPixels = tiledMap.getWidth() * tiledMap.getTileWidth();
		g.translate(mapWidthInPixels, 0);
		tiledMap.draw(g, mapWidthInPixels,
				(tiledMap.getHeight() * tiledMap.getTileHeight()) + (8 * tiledMap.getTileHeight()));
		g.translate(-mapWidthInPixels, 0);
	}

	private void renderScaledDownFullMap(Graphics g) {
		g.setScale(0.55f, 0.55f);
		tiledMap.draw(g, MathUtils.round(g.getWindowWidth() - ((tiledMap.getTileWidth() / 2) * tiledMap.getWidth())),
				MathUtils.round(g.getWindowHeight() - ((tiledMap.getTileHeight() / 2) * tiledMap.getHeight())));
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
