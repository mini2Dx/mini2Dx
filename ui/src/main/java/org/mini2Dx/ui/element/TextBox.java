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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.ui.UiContentContainer;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.LabelStyle;
import org.mini2Dx.ui.theme.TextBoxStyle;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Clipboard;

/**
 *
 */
public class TextBox extends BasicUiElement<TextBoxStyle>implements Hoverable, TextInputable {
	private static final float CURSOR_VISIBLE_DURATION = 0.5f;
	
	private final List<ActionListener> listeners = new ArrayList<ActionListener>(1);
	private final Clipboard clipboard;
	private final GlyphLayout glyphLayout;

	private TextBoxStyle currentStyle;
	private LabelStyle textStyle;
	private Color textColor;
	
	private boolean enabled = true;
	private String text;
	private int cursor;
	
	private float cursorTimer = 1.0f;
	private boolean cursorVisible;
	private float renderCursorX;
	private float renderCursorHeight;

	public TextBox() {
		clipboard = Gdx.app.getClipboard();
		glyphLayout = new GlyphLayout();
		textColor = Label.COLOR_BLACK;
		text = "";
		cursor = 0;
	}
	
	@Override
	public void update(UiContentContainer uiContainer, float delta) {
		super.update(uiContainer, delta);
		
		if(cursorTimer <= CURSOR_VISIBLE_DURATION) {
			cursorVisible = true;
		} else {
			cursorVisible = false;
		}
		if(cursorTimer <= 0f) {
			cursorTimer += CURSOR_VISIBLE_DURATION * 2f;
		}
		cursorTimer -= delta;
	}

	private void setCursorIndex(float screenX, float screenY) {
		if (text.length() == 0) {
			cursor = 0;
			setCursorRenderX();
			return;
		}
		if (textStyle == null || textStyle.getBitmapFont() == null) {
			cursor = 0;
			setCursorRenderX();
			return;
		}
		BitmapFont font = textStyle.getBitmapFont();

		float clickX = screenX - getRenderX() - getPaddingLeft();

		for (int i = 0; i < text.length(); i++) {
			glyphLayout.setText(font, text.substring(0, i + 1));
			if (clickX < glyphLayout.width) {
				float result = glyphLayout.width;
				glyphLayout.setText(font, text.charAt(i) + "");
				result -= glyphLayout.width;
				cursor = i;
				setCursorRenderX(result - 1f);
				return;
			}
		}
		setCursorRenderX(glyphLayout.width + 1f);
	}

	private void setCursorRenderX() {
		switch (cursor) {
		case 0:
			renderCursorX = 0f;
			return;
		default:
			if (textStyle == null || textStyle.getBitmapFont() == null) {
				return;
			}
			BitmapFont font = textStyle.getBitmapFont();
			glyphLayout.setText(font, text.substring(0, cursor));
			setCursorRenderX(glyphLayout.width + 1f);
			break;
		}
	}

	private void setCursorRenderX(float renderX) {
		this.renderCursorX = renderX;
	}

	@Override
	public void accept(UiRenderer renderer) {
		if (!isVisible()) {
			return;
		}
		renderer.render(this);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		switch (getState()) {
		case ACTION:
			return false;
		case NORMAL:
			if (currentArea.contains(screenX, screenY)) {
				super.setState(ElementState.HOVER);
			}
			break;
		case HOVER:
			if (!currentArea.contains(screenX, screenY)) {
				super.setState(ElementState.NORMAL);
			}
			break;
		}
		return false;
	}

	@Override
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isVisible()) {
			return null;
		}
		if (!isEnabled()) {
			return null;
		}
		if (button != Buttons.LEFT) {
			return null;
		}
		switch(getState()) {
		case ACTION:
			if (!currentArea.contains(screenX, screenY)) {
				endAction();
				return null;
			}
			return this;
		default:
			if (currentArea.contains(screenX, screenY)) {
				return this;
			}
			return null;
		}
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if(!isVisible()) {
			return;
		}
		switch(getState()) {
		case ACTION:
			if (currentArea.contains(screenX, screenY)) {
				setCursorIndex(screenX, screenY);
			} else {
				endAction();
			}
			break;
		default:
			if (currentArea.contains(screenX, screenY)) {
				beginAction();
				switch(Mdx.os) {
				case ANDROID:
				case IOS:
					Gdx.input.setOnscreenKeyboardVisible(true);
					break;
				default:
					break;
				}
			} else {
				super.setState(ElementState.NORMAL);
			}
			break;
		}
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getTextBoxStyle(screenSize, styleId);
		textStyle = theme.getLabelStyle(screenSize, currentStyle.getLabelStyle());
		renderCursorHeight = textStyle.getBitmapFont().getLineHeight();
		setCursorRenderX();
	}

	@Override
	public float getContentWidth() {
		if (textStyle == null || textStyle.getBitmapFont() == null) {
			return 0f;
		}
		glyphLayout.setText(textStyle.getBitmapFont(), text);
		return glyphLayout.width;
	}

	@Override
	public float getContentHeight() {
		if(textStyle == null) {
			return 0f;
		}
		return textStyle.getBitmapFont().getLineHeight();
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
		super.setState(ElementState.ACTION);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionBegin(this);
		}
	}

	@Override
	public void endAction() {
		super.setState(ElementState.NORMAL);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onActionEnd(this);
		}
	}

	@Override
	public TextBoxStyle getCurrentStyle() {
		return currentStyle;
	}

	@Override
	public void characterReceived(char c) {
		if (!isValidCharacter(c)) {
			return;
		}
		switch (cursor) {
		case 0:
			text = c + text;
			break;
		default:
			if (cursor == text.length()) {
				text += c;
			} else {
				text = text.substring(0, cursor) + c + text.substring(cursor);
			}
			break;
		}
		cursor++;
		setCursorRenderX();
	}

	@Override
	public void backspace() {
		switch (cursor) {
		case 0:
			return;
		case 1:
			text = text.substring(1);
			break;
		default:
			if (cursor == text.length() - 1) {
				text = text.substring(0, text.length() - 1);
			} else {
				text = text.substring(0, cursor - 1) + text.substring(cursor);
			}
			break;
		}
		cursor--;
		setCursorRenderX();
	}

	@Override
	public boolean enter() {
		endAction();
		setState(ElementState.HOVER);
		return true;
	}
	
	@Override
	public void moveCursorRight() {
		if(cursor == text.length() - 1) {
			return;
		}
		cursor++;
		setCursorRenderX();
	}

	@Override
	public void moveCursorLeft() {
		if(cursor == 0) {
			return;
		}
		cursor--;
		setCursorRenderX();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null) {
			return;
		}
		this.text = text;
		setCursorRenderX();
	}

	@Override
	public void cut() {
		if (clipboard == null) {
			return;
		}
		clipboard.setContents(text);
		setText("");
	}

	@Override
	public void copy() {
		if (clipboard == null) {
			return;
		}
		clipboard.setContents(text);
	}

	@Override
	public void paste() {
		if (clipboard == null) {
			return;
		}
		setText(clipboard.getContents());
	}

	public float getRenderCursorX() {
		return renderCursorX;
	}

	public float getRenderCursorHeight() {
		return renderCursorHeight;
	}

	public LabelStyle getTextStyle() {
		return textStyle;
	}

	public boolean isCursorVisible() {
		if(getState() == ElementState.ACTION) {
			return cursorVisible;
		}
		return false;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		if(textColor == null) {
			return;
		}
		this.textColor = textColor;
	}
	
	protected boolean isValidCharacter (char c) {
		if(c == '\n') {
			return false;
		}
		if(c == '\t') {
			return false;
		}
		if(c == '\r') {
			return false;
		}
		if(c == '\b') {
			return false;
		}
		if(Character.getName(c).equals("NULL")) {
			return false;
		}
		if(Character.getName(c).contains("PRIVATE USE")) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setState(ElementState state) {
		if(getState() == ElementState.ACTION) {
			return;
		}
		super.setState(state);
	}
	
	public boolean isReceivingInput() {
		return getState() == ElementState.ACTION;
	}
}
