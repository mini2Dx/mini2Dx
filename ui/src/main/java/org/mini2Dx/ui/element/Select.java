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

import org.mini2Dx.ui.layout.HorizontalAlign;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.layout.VerticalAlign;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.theme.SelectStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 * An option field with a left/right value selector
 */
public class Select<V> extends Column<SelectStyle> implements ActionListener, Actionable {
	private final List<OptionItem<V>> options = new ArrayList<OptionItem<V>>(1);
	private final List<ActionListener> listeners = new ArrayList<ActionListener>(1);
	private final Button leftButton, rightButton;
	private final Label selectionLabel;

	private SelectStyle currentStyle;
	private int selectedIndex;
	private boolean enabled;

	public Select() {
		this(new Label(), new TextButton("<"), new TextButton(">"));
		leftButton.setXRules("xs-0");
		leftButton.setWidthRules("xs-1");
		rightButton.setXRules("xs-11");
		rightButton.setWidthRules("xs-1");
		
		selectionLabel.setXRules("xs-2");
		selectionLabel.setWidthRules("xs-8");
		selectionLabel.setHorizontalAlign(HorizontalAlign.CENTER);
	}

	public Select(Label selectionLabel, Button leftButton, Button rightButton) {
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.selectionLabel = selectionLabel;
		
		leftButton.addActionListener(this);
		rightButton.addActionListener(this);

		this.addRow(Row.withElements(leftButton, selectionLabel, rightButton));
	}

	@Override
	public SelectStyle getCurrentStyle() {
		return currentStyle;
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getSelectStyle(screenSize, styleId);
		leftButton.styleId = currentStyle.getButtonStyle();
		rightButton.styleId = currentStyle.getButtonStyle();
		selectionLabel.styleId = currentStyle.getLabelStyle();
		super.applyStyle(theme, screenSize);
	}

	@Override
	public void onActionBegin(Actionable source) {
		notifyActionListenersOnBeginEvent();
	}

	@Override
	public void onActionEnd(Actionable source) {
		if(source.getId().equals(leftButton.getId())) {
			setSelectedIndex(selectedIndex - 1);
		} else if(source.getId().equals(rightButton.getId())) {
			setSelectedIndex(selectedIndex + 1);
		}
		notifyActionListenersOnEndEvent();
	}
	
	public OptionItem<V> getSelectedItem() {
		if(options.size() == 0) {
			return null;
		}
		return options.get(selectedIndex);
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void setSelectedIndex(int index) {
		if(options.size() < 2) {
			selectedIndex = 0;
			return;
		}
		if(index > options.size() - 1) {
			index = 0;
		}
		if(index < 0) {
			index = options.size() - 1;
		}
		selectedIndex = index;
		selectionLabel.setText(options.get(selectedIndex).getLabel());
	}
	
	public OptionItem<V> addOption(String label, V value) {
		OptionItem<V> result = new OptionItem<V>(label, value);
		options.add(result);
		setSelectedIndex(selectedIndex);
		return result;
	}
	
	public void addOption(OptionItem<V> option) {
		options.add(option);
		setSelectedIndex(selectedIndex);
	}
	
	public void removeOption(OptionItem<V> option) {
		options.remove(option);
		setSelectedIndex(selectedIndex);
	}

	@Override
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	private void notifyActionListenersOnBeginEvent() {
		for(int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionBegin(this);
		}
	}
	
	private void notifyActionListenersOnEndEvent() {
		for(int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionEnd(this);
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		leftButton.setEnabled(enabled);
		rightButton.setEnabled(enabled);
	}
}
