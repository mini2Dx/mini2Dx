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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.UiElement;

/**
 * Wraps a {@link Frame} to allow for keyboard and controller based navigation
 * of {@link UiElement}s
 */
public class Modal extends Frame {
	private Map<Integer, Actionable> keyboardHotkeys;
	private Map<String, Actionable> controllerHotkeys;
	private List<Actionable> actionables;
	private int hoverIndex;

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (super.mouseMoved(screenX, screenY)) {
			if (actionables == null) {
				return true;
			}
			for (int i = actionables.size() - 1; i >= 0; i--) {
				if (actionables.get(i).contains(screenX, screenY)) {
					hoverIndex = i;
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	public Actionable hotkey(int keycode) {
		if(keyboardHotkeys == null) {
			return null;
		}
		return keyboardHotkeys.get(keycode);
	}
	
	public Actionable hotkey(ControllerButton controllerButton) {
		if(controllerHotkeys == null) {
			return null;
		}
		return controllerHotkeys.get(controllerButton.getAbsoluteValue());
	}

	public Actionable goToNextActionable() {
		if (actionables == null) {
			return null;
		}
		actionables.get(hoverIndex).setState(ElementState.NORMAL);
		hoverIndex = hoverIndex < actionables.size() - 1 ? hoverIndex + 1 : 0;
		Actionable result = actionables.get(hoverIndex);
		result.setState(ElementState.HOVER);
		return result;
	}

	public Actionable goToPreviousActionable() {
		if (actionables == null) {
			return null;
		}
		actionables.get(hoverIndex).setState(ElementState.NORMAL);
		hoverIndex = hoverIndex == 0 ? actionables.size() - 1 : hoverIndex - 1;
		Actionable result = actionables.get(hoverIndex);
		result.setState(ElementState.HOVER);
		return result;
	}

	public void setNavigation(int index, Actionable actionable) {
		if (actionables == null) {
			actionables = new ArrayList<Actionable>();
		}
		actionables.add(index, actionable);
	}
	
	public void setHotkey(ControllerButton button, Actionable actionable) {
		if(controllerHotkeys == null) {
			controllerHotkeys = new HashMap<String, Actionable>();
		}
		controllerHotkeys.put(button.getAbsoluteValue(), actionable);
	}
	
	public void setHotkey(int keycode, Actionable actionable) {
		if(keyboardHotkeys == null) {
			keyboardHotkeys = new HashMap<Integer, Actionable>();
		}
		keyboardHotkeys.put(keycode, actionable);
	}
}
