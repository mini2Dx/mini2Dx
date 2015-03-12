/**
 * Copyright (c) 2015, mini2Dx Project
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
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.exception.TiledException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * {@link Graphics} clipping functionality
 */
public class ClippingUAT implements GameScreen {
    public static final int SCREEN_ID = 2;
    
	private TiledMap tiledMap;

    @Override
    public void initialise(GameContainer gc) {
        try {
            tiledMap = new TiledMap(Gdx.files.classpath("simple.tmx"));
        } catch (TiledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.RED);

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
        return SCREEN_ID;
    }
}
