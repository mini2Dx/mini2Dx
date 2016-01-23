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
package org.mini2Dx.ui.element;

import java.util.LinkedList;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.input.ControllerHotKeyOperation;
import org.mini2Dx.ui.input.KeyboardHotKeyOperation;
import org.mini2Dx.ui.input.UiNavigation;
import org.mini2Dx.ui.input.VerticalUiNavigation;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.render.ActionableRenderNode;
import org.mini2Dx.ui.render.ModalRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 *
 */
public class Modal extends Container {
	private final Queue<ControllerHotKeyOperation> controllerHotKeyOperations = new LinkedList<ControllerHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new LinkedList<KeyboardHotKeyOperation>();
	
	private UiNavigation navigation = new VerticalUiNavigation();
	
	public Modal() {
		this(null);
	}
	
	public Modal(String id) {
		super(id);
	}
	
	public ActionableRenderNode navigate(int keycode) {
		if(renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).navigate(keycode);
	}
	
	public ActionableRenderNode hotkey(int keycode) {
		if(renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).hotkey(keycode);
	}
	
	public ActionableRenderNode hotkey(ControllerButton button) {
		if(renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).hotkey(button);
	}
	
	public void setHotkey(ControllerButton button, Actionable actionable) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, actionable, true));
	}
	
	public void setHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, actionable, true));
	}
	
	public void unsetHotkey(ControllerButton button, Actionable actionable) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, actionable, false));
	}
	
	public void unsetHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, actionable, false));
	}

	public UiNavigation getNavigation() {
		return navigation;
	}

	public void setNavigation(UiNavigation navigation) {
		if(navigation == null) {
			return;
		}
		this.navigation = navigation;
	}
}
