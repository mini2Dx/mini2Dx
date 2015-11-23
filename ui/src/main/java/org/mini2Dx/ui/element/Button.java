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
import java.util.List;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.listener.HoverListener;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.ButtonStyle;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.Input.Buttons;

/**
 *
 */
public class Button extends Column<ButtonStyle> implements Actionable {
	private final List<ActionListener> listeners = new ArrayList<ActionListener>(1);
	
	private ButtonStyle currentStyle;
	private boolean enabled = true;

	@Override
	public void accept(UiRenderer renderer) {
		if(!visible) {
			return;
		}
		renderer.render(this);
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).accept(renderer);
		}
	}
	
	@Override
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button) {
		if(!visible) {
			return null;
		}
		if(button != Buttons.LEFT) {
			return null;
		}
		if(currentArea.contains(screenX, screenY)) {
			setState(ElementState.ACTION);
			return this;
		}
		return null;
	}
	
	
	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if(getState() != ElementState.ACTION) {
			return;
		}
		if(currentArea.contains(screenX, screenY)) {
			setState(ElementState.HOVER);
		} else {
			setState(ElementState.NORMAL);
		}
		endAction();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void beginAction() {
		for(int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionBegin(this);
		}
	}

	@Override
	public void endAction() {
		for(int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionEnd(this);
		}
	}
	
	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getButtonStyle(screenSize, styleId);
		super.applyStyle(theme, screenSize);
	}

	@Override
	public ButtonStyle getCurrentStyle() {
		return currentStyle;
	}
}
