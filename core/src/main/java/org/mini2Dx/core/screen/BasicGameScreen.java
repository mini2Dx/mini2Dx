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
package org.mini2Dx.core.screen;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;

/**
 * A basic implementation of {@link GameScreen} for quick setup
 */
public abstract class BasicGameScreen implements GameScreen {

	@Override
	public void interpolate(GameContainer gc, float alpha) {}

	@Override
	public void onResize(int width, int height) {
	}
	
	@Override
	public void onPause() {
		Mdx.log.info("INFO", "Game window paused");
	}

	@Override
	public void onResume() {
		Mdx.log.info("INFO", "Game window resumed");
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
}
