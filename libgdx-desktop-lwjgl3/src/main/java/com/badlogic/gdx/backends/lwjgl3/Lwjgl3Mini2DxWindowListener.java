/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package com.badlogic.gdx.backends.lwjgl3;

import org.mini2Dx.core.game.GameContainer;

public interface Lwjgl3Mini2DxWindowListener extends Lwjgl3WindowListener {

	/**
	 * Called after the GLFW window is created. Before this callback is received, it's
	 * unsafe to use any {@link Lwjgl3Mini2DxWindow} member functions which, for their part,
	 * involve calling GLFW functions.
	 *
	 * For the main window, this is an immediate callback from inside
	 * {@link DesktopMini2DxGame#DesktopMini2DxGame(GameContainer, Lwjgl3Mini2DxConfig)}.
	 *
	 * @param window the window instance
	 *
	 * @see DesktopMini2DxGame#newWindow(Lwjgl3Mini2DxConfig)
	 */
	void created(Lwjgl3Mini2DxWindow window);

	/**
	 * Called when the window is resized
	 * @param window The window instance
	 */
	void resized(Lwjgl3Mini2DxWindow window);

	@Override
	default void created(Lwjgl3Window window) { }
}
