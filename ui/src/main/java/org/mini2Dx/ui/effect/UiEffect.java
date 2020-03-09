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
package org.mini2Dx.ui.effect;

import org.mini2Dx.core.collision.CollisionArea;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.UiContainerRenderTree;

/**
 * An effect to be applied to a {@link UiElement}
 */
public interface UiEffect {
	/**
	 * Called before the effect begins
	 * @param element The {@link UiElement} the effect is applied to
	 */
	public void preBegin(UiElement element);
	
	/**
	 * Called after the effect ends
	 * @param element The {@link UiElement} the effect was applied to
	 */
	public void postEnd(UiElement element);
	
	/**
	 * Updates the effect
	 * 
	 * @param uiContainer
	 *            The {@link UiContainerRenderTree} the {@link UiElement}
	 *            belongs to
	 * @param currentArea
	 *            The current area that the {@link UiElement} is set to render
	 *            in. Manipulate this to change where the element renders.
	 * @param targetArea
	 *            The target area that the {@link UiElement} is set to render in
	 *            as specified by the render tree and layout.
	 * @param delta
	 *            The frame delta
	 * @return True if the {@link UiElement} should be rendered
	 */
	public boolean update(UiContainerRenderTree uiContainer, CollisionArea currentArea, Rectangle targetArea,
	                      float delta);

	/**
	 * Called before rendering the {@link UiElement}
	 * 
	 * @param g
	 *            The {@link Graphics} context
	 */
	public void preRender(Graphics g);

	/**
	 * Called after rendering the {@link UiElement}
	 * 
	 * @param g
	 *            The {@link Graphics} context
	 */
	public void postRender(Graphics g);

	/**
	 * True if the effect has completed and should be removed from the
	 * {@link UiElement}
	 * 
	 * @return
	 */
	public boolean isFinished();
}
