/**
 * Copyright (c) 2017 See AUTHORS file
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

/**
 *
 */
public class HexagonalTiledMapUAT extends BasicGameScreen {
    private TiledMap staggerXTiledMap, staggerYTiledMap;

    @Override
    public void initialise(GameContainer gc) {
        try {
            staggerXTiledMap = new TiledMap(Gdx.files.internal("hexagonal_x.tmx"), true, false);
            staggerYTiledMap = new TiledMap(Gdx.files.internal("hexagonal_y.tmx"), true, false);
        } catch (TiledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
    	if(Gdx.input.justTouched()) {
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
        g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.RED);

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
