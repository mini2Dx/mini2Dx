/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.listener.ActionListener;

import com.badlogic.gdx.graphics.Color;

/**
 *
 */
public class UiUtils {

	public static Label createHeader(String text) {
		return createLabel(text, "xs-0", "xs-12", Label.COLOR_BLACK);
	}
	
	public static Label createLabel(String text, String xRules, String widthRules) {
		return createLabel(text, xRules, widthRules, Label.COLOR_WHITE);
	}

	private static Label createLabel(String text, String xRules, String widthRules, Color color) {
		Label label = new Label(text);
		label.setXRules(xRules);
		label.setWidthRules(widthRules);
		label.setColor(color);
		return label;
	}

	public static Button createButton(String text, String xRules, String widthRules, ActionListener listener) {
		Button button = new Button();
		button.setXRules(xRules);
		button.setWidthRules(widthRules);
		button.addActionListener(listener);
		button.addRow(Row.withElements(createLabel(text, "xs-0", "xs-12", Label.COLOR_WHITE)));
		return button;
	}
	
	public static TextBox createTextBox(String xRules, String widthRules, ActionListener listener) {
		TextBox textBox = new TextBox();
		textBox.setXRules(xRules);
		textBox.setWidthRules(widthRules);
		textBox.addActionListener(listener);
		return textBox;
	}
}
