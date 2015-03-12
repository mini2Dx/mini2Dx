/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.util.OsDetector;
import org.mini2Dx.uats.BlendingUAT;
import org.mini2Dx.uats.ClippingUAT;
import org.mini2Dx.uats.GeometryUAT;
import org.mini2Dx.uats.GraphicsUAT;
import org.mini2Dx.uats.TiledMapNoCachingUAT;
import org.mini2Dx.uats.TiledMapWithCachingUAT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author Thomas Cashman
 */
public class UATSelectionScreen implements GameScreen {
    public static final int SCREEN_ID = 0;

    @Override
    public void initialise(GameContainer gc) {
        
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
        switch(OsDetector.getOs()) {
        case ANDROID:
        case IOS:
            updateMobileMenu(gc, screenManager, delta);
            break;
        case WINDOWS:
        case MAC:
        case UNIX:
        case UNKNOWN:
            updateDesktopMenu(screenManager, delta);
            break;
        }
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.clearBlendFunction();
        g.clearShaderProgram();
        g.removeClip();
        
        g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.BLUE);
        
        switch(OsDetector.getOs()) {
        case ANDROID:
        case IOS:
            renderMobileMenu(gc, g);
            break;
        case WINDOWS:
        case MAC:
        case UNIX:
        case UNKNOWN:
            renderDesktopMenu(gc, g);
            break;
        }
    }
    
    private void updateDesktopMenu(ScreenManager<? extends GameScreen> screenManager,
            float delta) {
        if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            screenManager.enterGameScreen(BlendingUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        } else if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            screenManager.enterGameScreen(ClippingUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        } else if(Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
            screenManager.enterGameScreen(GeometryUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        } else if(Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
            screenManager.enterGameScreen(GraphicsUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        } else if(Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
            screenManager.enterGameScreen(TiledMapNoCachingUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        } else if(Gdx.input.isKeyJustPressed(Keys.NUM_6)) {
            screenManager.enterGameScreen(TiledMapWithCachingUAT.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        }
    }
    
    private void renderDesktopMenu(GameContainer gc, Graphics g) {
        float lineHeight = g.getFont().getLineHeight();
        g.drawString("1. Blending UAT", 32, 32);
        g.drawString("2. Clipping UAT", 32, 32 + lineHeight + 4);
        g.drawString("3. Geometry UAT", 32, 32 + (lineHeight * 2) + 8);
        g.drawString("4. Graphics UAT", 32, 32 + (lineHeight * 3) + 12);
        g.drawString("5. TiledMap (No Caching) UAT", 32, 32 + (lineHeight * 4) + 16);
        g.drawString("6. TiledMap (With Caching) UAT", 32, 32 + (lineHeight * 5) + 20);
    }
    
    private void updateMobileMenu(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
        if(Gdx.input.justTouched()) {
            
        }
    }
    
    private void renderMobileMenu(GameContainer gc, Graphics g) {
        float lineHeight = g.getFont().getLineHeight();
        g.drawString("1. Blending UAT", 32, 32);
        g.drawString("2. Clipping UAT", 32, 32 + lineHeight + 4);
        g.drawString("3. Geometry UAT", 32, 32 + (lineHeight * 2) + 8);
        g.drawString("4. Graphics UAT", 32, 32 + (lineHeight * 3) + 12);
        g.drawString("5. TiledMap (No Caching) UAT", 32, 32 + (lineHeight * 4) + 16);
        g.drawString("6. TiledMap (With Caching) UAT", 32, 32 + (lineHeight * 5) + 20);
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
