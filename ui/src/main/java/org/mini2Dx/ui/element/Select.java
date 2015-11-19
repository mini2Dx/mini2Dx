/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.layout.ScreenSize;
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
