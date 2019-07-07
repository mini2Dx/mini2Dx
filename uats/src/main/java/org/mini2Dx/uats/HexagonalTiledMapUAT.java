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
 *
 */
public class HexagonalTiledMapUAT extends BasicGameScreen {
    private static final String STAGGER_X_MAP = "hexagonal_x.tmx";
    private static final String STAGGER_Y_MAP = "hexagonal_y.tmx";

	private final AssetManager assetManager;
	
    private TiledMap staggerXTiledMap, staggerYTiledMap;
    
    public HexagonalTiledMapUAT(AssetManager assetManager) {
    	super();
		this.assetManager = assetManager;
		assetManager.load(STAGGER_X_MAP, TiledMap.class);
		assetManager.load(STAGGER_Y_MAP, TiledMap.class);
	}

    @Override
    public void initialise(GameContainer gc) {
    	assetManager.finishLoading();
        try {
            staggerXTiledMap = assetManager.get(STAGGER_X_MAP, TiledMap.class);
            staggerYTiledMap = assetManager.get(STAGGER_Y_MAP, TiledMap.class);
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
    	staggerXTiledMap.update(delta);
    	staggerYTiledMap.update(delta);
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Colors.WHITE());
        g.setColor(Colors.RED());

        renderStaggerXOnTopLeft(g);
        renderPartOfStaggerX(g);
        renderStaggerYOnTopLeft(g);
        renderPartOfStaggerY(g);
    }
    
    private void renderStaggerXOnTopLeft(Graphics g) {
        staggerXTiledMap.draw(g, 0, 0);
    }
    
    private void renderPartOfStaggerX(Graphics g) {
        staggerXTiledMap.draw(g, 0, staggerXTiledMap.getPixelHeight(), 1, 1, 4, 8);
    }
    
    private void renderStaggerYOnTopLeft(Graphics g) {
        staggerYTiledMap.draw(g, staggerXTiledMap.getPixelWidth() + 8, 0);
    }
    
    private void renderPartOfStaggerY(Graphics g) {
        staggerYTiledMap.draw(g, staggerXTiledMap.getPixelWidth() + 8, staggerYTiledMap.getPixelHeight(), 1, 1, 4, 8);
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
        return ScreenIds.getScreenId(HexagonalTiledMapUAT.class);
    }
}
