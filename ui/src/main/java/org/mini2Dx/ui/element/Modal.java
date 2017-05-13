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
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.navigation.ControllerHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.navigation.VerticalUiNavigation;
import org.mini2Dx.ui.render.ActionableRenderNode;
import org.mini2Dx.ui.render.ModalRenderNode;

/**
 * A {@link Container} implementation that can be navigated by player input and
 * supports hotkeys
 */
public abstract class Modal extends Container implements Navigatable {
	private final Queue<ControllerHotKeyOperation> controllerHotKeyOperations = new LinkedList<ControllerHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new LinkedList<KeyboardHotKeyOperation>();

	private UiNavigation navigation = new VerticalUiNavigation();

	public Modal() {
		this(null);
	}

	public Modal(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	@Override
	public void syncWithRenderNode() {
		super.syncWithRenderNode();
		((ModalRenderNode) renderNode).syncHotkeys(controllerHotKeyOperations, keyboardHotKeyOperations);
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		if (renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).navigate(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(int keycode) {
		if (renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).hotkey(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(ControllerButton button) {
		if (renderNode == null) {
			return null;
		}
		return ((ModalRenderNode) renderNode).hotkey(button);
	}

	@Override
	public void setHotkey(ControllerButton button, Actionable actionable) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, actionable, true));
	}

	@Override
	public void setHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, actionable, true));
	}

	@Override
	public void unsetHotkey(ControllerButton button) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, null, false));
	}

	@Override
	public void unsetHotkey(int keycode) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, null, false));
	}
	
	@Override
	public void clearControllerHotkeys() {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(null, null, false));
	}

	@Override
	public void clearKeyboardHotkeys() {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(Integer.MAX_VALUE, null, false));
	}
	
	@Override
	public void clearHotkeys() {
		clearControllerHotkeys();
		clearKeyboardHotkeys();
	}

	@Override
	public UiNavigation getNavigation() {
		return navigation;
	}

	public void setNavigation(UiNavigation navigation) {
		if (navigation == null) {
			return;
		}
		this.navigation = navigation;
	}
}
