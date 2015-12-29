/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.listener.ActionListener;

import com.badlogic.gdx.graphics.Color;

/**
 *
 */
public class UiUtils {

	public static Label createHeader(String text) {
		return createLabel(text, "header", Label.COLOR_BLACK);
	}
	
	public static Label createLabel(String text) {
		return createLabel(text, "default", Label.COLOR_BLACK);
	}

	private static Label createLabel(String text, String styleId, Color color) {
		Label label = new Label("Label: " + text);
		label.setText(text);
		label.setStyleId(styleId);
		label.setColor(color);
		label.setVisibility(Visibility.VISIBLE);
		return label;
	}

	public static TextButton createButton(String text, ActionListener listener) {
		return createButton(text, false, listener);
	}
	
	public static TextButton createButton(String text, boolean debug, ActionListener listener) {
		TextButton button = new TextButton("TextButton: " + text);
		button.setText(text);
		button.addActionListener(listener);
		button.setDebugEnabled(debug);
		button.setVisibility(Visibility.VISIBLE);
		return button;
	}
	
	public static TextBox createTextBox(String id, ActionListener listener) {
		TextBox textBox = new TextBox(id);
		textBox.addActionListener(listener);
		textBox.setVisibility(Visibility.VISIBLE);
		return textBox;
	}
	
	public static Select<String> createSelect(String id, ActionListener listener) {
		Select<String> select = new Select<String>(id);
		select.addActionListener(listener);
		select.setVisibility(Visibility.VISIBLE);
		return select;
	}
}
