/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.effect;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
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
	public boolean update(UiContainerRenderTree uiContainer, CollisionBox currentArea, Rectangle targetArea,
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
