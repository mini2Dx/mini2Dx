/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.listener;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.UiElement;

/**
 * Interface for listening to {@link UiContainer} events
 */
public interface UiContainerListener extends ScreenSizeListener {
	/**
	 * Called before the {@link UiContainer} is updated
	 * @param container The {@link UiContainer} that will be updated
	 * @param delta The frame delta value
	 */
	public void preUpdate(UiContainer container, float delta);
	
	/**
	 * Called after the {@link UiContainer} is updated
	 * @param container The {@link UiContainer} that was updated
	 * @param delta The frame delta value
	 */
	public void postUpdate(UiContainer container, float delta);
	
	/**
	 * Called before the {@link UiContainer} is interpolated
	 * @param container The {@link UiContainer} that will be interpolated
	 * @param alpha The frame interpolation value
	 */
	public void preInterpolate(UiContainer container, float alpha);
	
	/**
	 * Called after the {@link UiContainer} is interpolated
	 * @param container The {@link UiContainer} that was interpolated
	 * @param alpha The frame interpolation value
	 */
	public void postInterpolate(UiContainer container, float alpha);
	
	/**
	 * Called before the {@link UiContainer} is rendered
	 * @param container The {@link UiContainer} that will be rendered
	 * @param g The {@link Graphics} context
	 */
	public void preRender(UiContainer container, Graphics g);
	
	/**
	 * Called after the {@link UiContainer} is rendered
	 * @param container The {@link UiContainer} that was rendered
	 * @param g The {@link Graphics} context
	 */
	public void postRender(UiContainer container, Graphics g);
	
	/**
	 * Called when the {@link InputSource} changes
	 * @param container The {@link UiContainer} that the {@link InputSource} changed on
	 * @param oldInputSource The previous {@link InputSource}
	 * @param newInputSource The new {@link InputSource}
	 */
	public void inputSourceChanged(UiContainer container, InputSource oldInputSource, InputSource newInputSource);
	
	/**
	 * Called when a {@link UiElement} becomes active from user input (e.g. click events, etc.)
	 * @param container The {@link UiContainer} the element became active on
	 * @param element The {@link UiElement} that became active
	 */
	public void onElementAction(UiContainer container, UiElement element);
}
