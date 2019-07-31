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
import org.mini2Dx.tiled.TiledMapLoader;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * {@link Graphics} clipping functionality
 */
public class ClippingUAT extends BasicGameScreen {
    private final AssetManager assetManager;

	private TiledMap tiledMap;

    public ClippingUAT(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;

        final TiledMapLoader.TiledAssetProperties loadMapParameter = new TiledMapLoader.TiledAssetProperties();
        loadMapParameter.loadTilesets = true;

        assetManager.load("orthogonal_no_cache.tmx", TiledMap.class, loadMapParameter);
    }

    @Override
    public void initialise(GameContainer gc) {
        while(!assetManager.update()) {}
        try {
            tiledMap = assetManager.get("orthogonal_no_cache.tmx", TiledMap.class);
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
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Colors.WHITE());
        g.setColor(Colors.RED());

        g.setClip(0f, 0f, 64f, 64f);
        //Should draw first four tiles in top left corner
        tiledMap.draw(g, 0, 0);
        g.removeClip();
        
        g.setClip(0f, 192f, 64f, 64f);
        //Should draw four tiles from bottom left corner of map
        tiledMap.draw(g, 0, 0);
        g.removeClip();
        
        //Should draw whole map
        tiledMap.draw(g, 96, 0);
        
        //Should only draw part of text
        g.setClip(0, 256, 64, 64);
        g.drawString("Hello, world!", 0, 256);
        g.removeClip();

        g.setScale(2f, 2f);
        g.setClip(0, 196f, 64, 64);
        g.drawString("Hello, world 2!", 0, 196);
        g.setClip(0, 0f, g.getViewportWidth(), g.getViewportHeight());
        g.drawString("Hello, world 3!", 0, 224);
        g.removeClip();

        g.clearScaling();
        g.drawString("Helloo world!", 300, 300);
        g.translate(-100, -100);
        g.setClip(300, 300, 64, 64);
        g.drawString("Hello, world!", 300, 300);
        g.removeClip();
        g.setTranslation(0,0);
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
    	return ScreenIds.getScreenId(ClippingUAT.class);
    }
}
