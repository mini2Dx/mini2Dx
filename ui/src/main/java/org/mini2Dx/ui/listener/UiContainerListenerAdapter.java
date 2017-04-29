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

import org.mini2Dx.core.controller.ControllerType;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;

/**
 * Adapter class that implements {@link UiContainerListener}. All methods are
 * no-op and can be overridden individually.
 */
public class UiContainerListenerAdapter implements UiContainerListener {

	@Override
	public void onScreenSizeChanged(ScreenSize screenSize) {
	}

	@Override
	public void preUpdate(UiContainer container, float delta) {
	}

	@Override
	public void postUpdate(UiContainer container, float delta) {
	}

	@Override
	public void preInterpolate(UiContainer container, float alpha) {
	}

	@Override
	public void postInterpolate(UiContainer container, float alpha) {
	}

	@Override
	public void preRender(UiContainer container, Graphics g) {
	}

	@Override
	public void postRender(UiContainer container, Graphics g) {
	}

	@Override
	public void inputSourceChanged(UiContainer container, InputSource oldInputSource, InputSource newInputSource) {
	}
	
	@Override
	public void controllerTypeChanged(UiContainer container, ControllerType oldControllerType,
			ControllerType newControllerType) {
	}

	@Override
	public void onElementAction(UiContainer container, UiElement element) {
	}
}
