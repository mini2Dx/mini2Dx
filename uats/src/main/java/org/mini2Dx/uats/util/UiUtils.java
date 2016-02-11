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
package org.mini2Dx.uats.util;

import org.mini2Dx.ui.element.Label;
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
