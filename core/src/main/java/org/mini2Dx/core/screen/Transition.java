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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;

/**
 * A common interface to {@link GameScreen} transition implementations
 * 
 * NOTE: Based on Slick implementation by Kevin Glass
 */
public interface Transition {
	
	public void initialise(GameScreen outScreen, GameScreen inScreen);
	
	public void update(GameContainer gc, float delta);

	public void preRender(GameContainer gc, Graphics g);
	
	public void postRender(GameContainer gc, Graphics g);
	
	public boolean isFinished();
}
