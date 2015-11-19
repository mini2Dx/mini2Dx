/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

/**
 *
 */
public class TextButton extends Button {
	private final Label label;

	public TextButton() {
		this("");
	}
	
	public TextButton(String text) {
		super();
		label = new Label();
		label.setXRules("auto");
		label.setWidthRules("xs-12");
		label.setText(text);
		
		addRow(Row.withElements(label));
	}

	public String getText() {
		return label.getText();
	}

	public void setText(String text) {
		label.setText(text);
	}
}
