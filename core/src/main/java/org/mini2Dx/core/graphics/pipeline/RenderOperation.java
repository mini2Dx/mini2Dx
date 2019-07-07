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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;

/**
 * A rendering operation that can be applied to {@link Graphics} and
 * optionally unapplied
 */
public interface RenderOperation {

	public void update(GameContainer gc, float delta);

	public void interpolate(GameContainer gc, float alpha);

	public void apply(GameContainer gc, Graphics g);

	public void unapply(GameContainer gc, Graphics g);
}
