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
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of
 * {@link Graphics} alpha blending functionality
 */
public class BlendingUAT extends BasicGameScreen {
	private final Color WHITE = Colors.rgbToColor("254,254,254");

    private final AssetManager assetManager;

	private Sprite sprite;

    public BlendingUAT(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;

        assetManager.load("unsealed.png", Texture.class);
    }

    @Override
    public void initialise(GameContainer gc) {
        while(!assetManager.update()) {}
        sprite = Mdx.graphics.newSprite(assetManager.get("unsealed.png", Texture.class));
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager,
            float delta) {
        if(Mdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        }
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Mdx.graphics.newColor(0.01f, 0.01f, 0.01f, 1f));
        
        /* Render lights to alpha mask */
        //g.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        g.setColor(Mdx.graphics.newColor(0.8f, 0.8f, 0.8f, 1f));
        g.fillCircle(sprite.getWidth() / 2f, sprite.getHeight() / 2f, MathUtils.round(sprite.getWidth() / 6f));
        g.flush();
        
        /* Render the scene */
        //g.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
        g.drawSprite(sprite);
        
        g.setColor(WHITE);
        //Mdx.performanceTracker.drawInTopRight(g);
    }

    @Override
    public int getId() {
        return ScreenIds.getScreenId(BlendingUAT.class);
    }
}