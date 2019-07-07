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
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * Geometry classes and their rendering
 */
public class GeometryUAT extends BasicGameScreen {
	private final Color BLACK = Colors.rgbToColor("1,1,1");
	
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
        g.setBackgroundColor(Colors.WHITE());
        g.setColor(Colors.RED());
        rect.draw(g);
        
		g.setColor(BLACK);
		//Mdx.performanceTracker.drawInBottomRight(g);
    }
    
    public void detectKeyPress(ScreenManager<? extends GameScreen> screenManager) {
        if(Mdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            playerX++;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            playerX--;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.UP)) {
            playerY--;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            playerY++;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.E)) {
            scaleX++;
            scaleY++;
            scaleX--;
            scaleY--;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.R)) {
            rotation++;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.W)) {
            originY--;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.S)) {
            originY++;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.A)) {
            originX--;
        } else if(Mdx.input.isKeyJustPressed(Input.Keys.D)) {
            originX++;
        } else if(Mdx.input.justTouched()) {
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
