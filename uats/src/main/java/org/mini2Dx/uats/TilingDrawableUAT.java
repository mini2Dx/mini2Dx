package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.TilingDrawable;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

public class TilingDrawableUAT extends BasicGameScreen {

    private static final String TILING_DRAWABLE = "tank";
    private final FileHandleResolver fileHandleResolver;
    private TilingDrawable tilingDrawable;
    private int side = 100;
    private int sign = 1;

    public TilingDrawableUAT(FileHandleResolver fileHandleResolver) {
        this.fileHandleResolver = fileHandleResolver;
    }

    @Override
    public void initialise(GameContainer gc) {
        tilingDrawable = Mdx.graphics.newTilingDrawable(Mdx.graphics.newTextureRegion(Mdx.graphics.newTexture(fileHandleResolver.resolve(TILING_DRAWABLE + ".png"))));
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
        if (Mdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
                    new FadeInTransition());
        }
        side += sign;
        if (side == 100 || side == 250){
            sign = -sign;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        tilingDrawable.draw(g, 50, 50, side, side);
        g.drawRect(50, 50, side, side);
    }

    @Override
    public int getId() {
        return ScreenIds.getScreenId(TilingDrawableUAT.class);
    }
}
