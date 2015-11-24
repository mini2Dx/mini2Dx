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

import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.CheckBoxStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public class CheckBox extends BasicUiElement<CheckBoxStyle> implements Actionable, Hoverable {
	private final List<ActionListener> listeners = new ArrayList<ActionListener>(1);
	
	private CheckBoxStyle currentStyle;
	private boolean enabled = true;
	private boolean checked = false;

	@Override
	public void accept(UiRenderer renderer) {
		if(!isVisible()) {
			return;
		}
		renderer.render(this);
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getCheckBoxStyle(screenSize, styleId);
	}
	
	@Override
	public void beginAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endAction() {
		// TODO Auto-generated method stub
		
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
	public float getContentWidth() {
		return currentStyle.getPaddingLeft() + currentStyle.getPaddingRight();
	}

	@Override
	public float getContentHeight() {
		return currentStyle.getPaddingTop() + currentStyle.getPaddingBottom();
	}

	@Override
	public CheckBoxStyle getCurrentStyle() {
		return currentStyle;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
