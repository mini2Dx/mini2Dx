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
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * Geometry classes and their rendering
 */
public class GeometryUAT extends BasicGameScreen {
	private int playerX, playerY, originX, originY;
	private float scaleX, scaleY;
	private int rotation;
	
	private Rectangle rect;

    @Override
    public void initialise(GameContainer gc) {
        rect = new Rectangle(0, 0, 128, 128);
        
        playerX = 0;
        playerY = 0;
        originX = 0;
        originY = 0;
        scaleX = 2f;
        scaleY = 2f;
        rotation = 0;
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
        detectKeyPress(screenManager);
        rotation += 180f * delta;

        rect.set(playerX * 32f, playerY * 32f, 128, 128);
        rect.rotateAround(originX * 32f, originY * 32f, 180f * delta);
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
    	g.scale(scaleX, scaleY);
        g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.RED);
        rect.draw(g);
    }
    
    public void detectKeyPress(ScreenManager<? extends GameScreen> screenManager) {
        if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            playerX++;
        } else if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            playerX--;
        } else if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            playerY--;
        } else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            playerY++;
        } else if(Gdx.input.isKeyJustPressed(Keys.E)) {
            scaleX++;
            scaleY++;
        } else if(Gdx.input.isKeyJustPressed(Keys.Q)) {
            scaleX--;
            scaleY--;
        } else if(Gdx.input.isKeyJustPressed(Keys.R)) {
            rotation++;
        } else if(Gdx.input.isKeyJustPressed(Keys.W)) {
            originY--;
        } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
            originY++;
        } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
            originX--;
        } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
            originX++;
        } else if(Gdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        }
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
    	return ScreenIds.getScreenId(GeometryUAT.class);
    }
}
