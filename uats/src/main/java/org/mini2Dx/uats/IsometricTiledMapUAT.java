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
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of isometric
 * {@link TiledMap} rendering with layer caching disabled
 */
public class IsometricTiledMapUAT extends BasicGameScreen {
	private final AssetManager assetManager;
	
    private TiledMap tiledMap;
    
    public IsometricTiledMapUAT(AssetManager assetManager) {
    	super();
		this.assetManager = assetManager;
		assetManager.load("isometric.tmx", TiledMap.class);
	}

    @Override
    public void initialise(GameContainer gc) {
    	assetManager.finishLoading();
        try {
            tiledMap = assetManager.get("isometric.tmx", TiledMap.class);
        } catch (TiledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
    	if(Mdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
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

        renderFullMapInTopMiddle(g);
        renderPartOfMapUnderTopMiddleMap(g);
        renderScaledAndRotatedMapOnLeftSide(g);
        renderTranslatedFullMapInBottomLeftCorner(g);
    }
    
    private void renderFullMapInTopMiddle(Graphics g) {
        tiledMap.draw(g, (int) g.getWindowWidth() / 2, 0);
    }
    
    private void renderPartOfMapUnderTopMiddleMap(Graphics g) {
        tiledMap.draw(g, ((int) g.getWindowWidth() / 2) + tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight(), 1, 1, 4, 8);
    }
    
    private void renderScaledAndRotatedMapOnLeftSide(Graphics g) {
        g.scale(1.25f, 1.25f);
        g.rotate(5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);

        //Render rotated map in bottom right corner
        tiledMap.draw(g, (int) g.getWindowWidth() / 8, (tiledMap.getHeight() * tiledMap.getTileHeight()) / 2, 1, 1, 4, 8);

        g.rotate(-5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);
        g.scale(0.8f, 0.8f);
    }
    
    private void renderTranslatedFullMapInBottomLeftCorner(Graphics g) {
        int mapWidthInPixels = (tiledMap.getWidth() * tiledMap.getTileWidth()) / 2;
        float halfScreenWidth = (g.getWindowWidth() / 2);
        float halfScreenHeight = (g.getWindowHeight() / 2);
        g.translate(-(halfScreenWidth + mapWidthInPixels), -halfScreenHeight);
        tiledMap.draw(g, 0, 0);
        g.translate(halfScreenWidth + mapWidthInPixels, halfScreenHeight);
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
        return ScreenIds.getScreenId(IsometricTiledMapUAT.class);
    }
}
