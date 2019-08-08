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
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.SpriteCache;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link SpriteCache} APIs
 */
public class ShapeClippingUAT extends BasicGameScreen {


	public ShapeClippingUAT() {
	}

	@Override
	public void initialise(GameContainer gc) {
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setColor(Colors.GREEN());
		g.setClip(100, 100, 100, 100);
		g.drawRect(101, 101, 98, 98);
		g.setColor(Colors.RED());
		g.drawRect(50, 50, 100, 100);
		g.removeClip();
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(ShapeClippingUAT.class);
	}
}
