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
 * A {@link GameScreen} that allows visual user acceptance testing of isometric
 * {@link TiledMap} rendering with layer caching disabled
 */
public class IsometricTiledMapUAT extends BasicGameScreen {
    private TiledMap tiledMap;

    @Override
    public void initialise(GameContainer gc) {
        try {
            tiledMap = new TiledMap(Gdx.files.classpath("isometric.tmx"), true, false);
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
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.RED);

        renderFullMapInTopMiddle(g);
        renderPartOfMapUnderTopMiddleMap(g);
        renderScaledAndRotatedMapOnLeftSide(g);
        renderTranslatedFullMapInBottomLeftCorner(g);
    }
    
    private void renderFullMapInTopMiddle(Graphics g) {
        tiledMap.draw(g, (int) g.getCurrentWidth() / 2, 0);
    }
    
    private void renderPartOfMapUnderTopMiddleMap(Graphics g) {
        tiledMap.draw(g, ((int) g.getCurrentWidth() / 2) + tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight(), 1, 1, 4, 8);
    }
    
    private void renderScaledAndRotatedMapOnLeftSide(Graphics g) {
        g.scale(1.25f, 1.25f);
        g.rotate(5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);

        //Render rotated map in bottom right corner
        tiledMap.draw(g, (int) g.getCurrentWidth() / 8, (tiledMap.getHeight() * tiledMap.getTileHeight()) / 2, 1, 1, 4, 8);

        g.rotate(-5f, 0f, (tiledMap.getHeight() * tiledMap.getTileHeight()) * 1.5f);
        g.scale(0.8f, 0.8f);
    }
    
    private void renderTranslatedFullMapInBottomLeftCorner(Graphics g) {
        int mapWidthInPixels = (tiledMap.getWidth() * tiledMap.getTileWidth()) / 2;
        float halfScreenWidth = (g.getCurrentWidth() / 2);
        float halfScreenHeight = (g.getCurrentHeight() / 2);
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
