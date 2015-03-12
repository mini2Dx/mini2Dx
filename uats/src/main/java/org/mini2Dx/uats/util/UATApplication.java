/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.core.game.ScreenBasedGame;
import org.mini2Dx.uats.BlendingUAT;
import org.mini2Dx.uats.ClippingUAT;
import org.mini2Dx.uats.GeometryUAT;
import org.mini2Dx.uats.GraphicsUAT;
import org.mini2Dx.uats.TiledMapNoCachingUAT;
import org.mini2Dx.uats.TiledMapWithCachingUAT;

/**
 *
 * @author Thomas Cashman
 */
public class UATApplication extends ScreenBasedGame {
    
    @Override
    public void initialise() {
        addScreen(new UATSelectionScreen());
        addScreen(new BlendingUAT());
        addScreen(new ClippingUAT());
        addScreen(new GeometryUAT());
        addScreen(new GraphicsUAT());
        addScreen(new TiledMapNoCachingUAT());
        addScreen(new TiledMapWithCachingUAT());
    }

    @Override
    public int getInitialScreenId() {
        return 0;
    }
}
