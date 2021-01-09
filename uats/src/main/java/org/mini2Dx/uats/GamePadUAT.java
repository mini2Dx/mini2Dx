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
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadConnectionListener;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.uats.gamepad.GamePadDebugger;
import org.mini2Dx.uats.gamepad.PS4GamePadDebugger;
import org.mini2Dx.uats.gamepad.XboxGamePadDebugger;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * UAT for debugging {@link org.mini2Dx.core.input.GamePad}s
 */
public class GamePadUAT extends BasicGameScreen implements GamePadConnectionListener {
	private static final String LOGGING_TAG = GamePadUAT.class.getSimpleName();

	private final Array<GamePadDebugger> gamePadDebuggers = new Array<>();

	@Override
	public void initialise(GameContainer gc) {
		Mdx.input.setGamePadConnectionListener(this, true);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if(Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		for(GamePadDebugger gamePadDebugger : gamePadDebuggers) {
			gamePadDebugger.render(g);
		}
	}

	@Override
    public int getId() {
    	return ScreenIds.getScreenId(GamePadUAT.class);
    }

	@Override
	public void onConnect(GamePad gamePad) {
		Mdx.log.info(LOGGING_TAG, gamePad.getInstanceId() + " connected (Player Index: " + gamePad.getPlayerIndex() + ")(" + Mdx.input.getGamePads().size + " total connected)");

		for(GamePadDebugger debugger : gamePadDebuggers) {
			if(debugger.getGamePad().getPlayerIndex() == gamePad.getPlayerIndex())  {
				return;
			}
		}

		switch(gamePad.getGamePadType()) {
		case XBOX:
			XboxGamePadDebugger xbox360Debugger = new XboxGamePadDebugger(gamePad,gamePadDebuggers.size * 512f);
			try {
				Mdx.input.newXboxGamePad(gamePad).addListener(xbox360Debugger);
				gamePadDebuggers.add(xbox360Debugger);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		case PS4:
			PS4GamePadDebugger ps4Debugger = new PS4GamePadDebugger(gamePad,gamePadDebuggers.size * 512f);
			try {
				Mdx.input.newPS4GamePad(gamePad).addListener(ps4Debugger);
				gamePadDebuggers.add(ps4Debugger);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		case UNKNOWN:
		default:
			break;
		}
	}

	@Override
	public void onDisconnect(GamePad gamePad) {
		Mdx.log.info(LOGGING_TAG, gamePad.getInstanceId() + " disconnected (" + Mdx.input.getGamePads().size + " total connected)");
	}
}
