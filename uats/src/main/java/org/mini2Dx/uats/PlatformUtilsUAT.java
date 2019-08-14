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
import org.mini2Dx.core.PerformanceTracker;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link org.mini2Dx.core.PlatformUtils} APIs
 */
public class PlatformUtilsUAT extends BasicGameScreen {

    private String gameThreadTestStatus = "in progress";

    @Override
    public void initialise(GameContainer gc) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if(Mdx.platformUtils.isGameThread()){
                    PlatformUtilsUAT.this.gameThreadTestStatus = "failed";
                } else {
                    PlatformUtilsUAT.this.gameThreadTestStatus = "in progress...";
                }
            }
        }).start();
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
        if (Mdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
                    new FadeInTransition());
        } else if (Mdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Mdx.platformUtils.exit(false);
        } else if (Mdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Mdx.platformUtils.exit(true);
        }
        if (gameThreadTestStatus.contains("...")){
            if (Mdx.platformUtils.isGameThread()){
                gameThreadTestStatus = "SUCCESS!";
            } else {
                gameThreadTestStatus = "failed";
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        int renderX, renderY;
        renderX = renderY = 16;
        g.drawString("PlatformUtils.isGameThread() test: " + gameThreadTestStatus, renderX, renderY);
        renderY += 16;
        g.drawString("Press SPACE to test PlatformUtils.exit(false)", renderX, renderY);
        renderY += 16;
        g.drawString("Press ENTER to test PlatformUtils.exit(true)", renderX, renderY);
        PerformanceTracker.drawInBottomLeft(g);
    }

    @Override
    public int getId() {
        return ScreenIds.getScreenId(PlatformUtilsUAT.class);
    }
}
