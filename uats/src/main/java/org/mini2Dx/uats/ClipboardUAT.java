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
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link Graphics} clipping APIs
 */
public class ClipboardUAT extends BasicGameScreen {

	private String pastedText;

	public ClipboardUAT() {
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
		if (Mdx.input.isKeyJustPressed(Input.Keys.C)) {
			Mdx.input.setClipboardContents("mini2Dx sample copied content");
		}
		if (Mdx.input.isKeyJustPressed(Input.Keys.V)) {
			pastedText = Mdx.input.getClipboardContents();
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		int x = 10, y = 10;
		g.drawString("Clipboard supported: " + Mdx.input.isClipboardSupported(), x, y);
		y += 20;
		g.drawString("Clipboard has contents: " + Mdx.input.hasClipboardContents(), x, y);
		y += 20;
		g.drawString("Clipboard contents (press V to update): " + (pastedText == null ? "null" : ('"' + pastedText + '"')), x, y);
		y += 20;
		g.drawString("Press C to copy sample text", x, y);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(ClipboardUAT.class);
	}
}
