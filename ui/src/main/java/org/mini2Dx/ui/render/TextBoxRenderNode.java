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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.TextBoxStyleRule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Clipboard;

/**
 *
 */
public class TextBoxRenderNode extends RenderNode<TextBox, TextBoxStyleRule>implements TextInputableRenderNode {
	private static final float CURSOR_VISIBLE_DURATION = 0.5f;

	private final Clipboard clipboard = Gdx.app.getClipboard();
	private final GlyphLayout glyphLayout = new GlyphLayout();

	private int cursor;
	private float cursorTimer = 1.0f;
	private boolean cursorVisible;
	private float renderCursorX;
	private float renderCursorHeight;
	
	protected LayoutRuleset layoutRuleset;

	public TextBoxRenderNode(ParentRenderNode<?, ?> parent, TextBox element) {
		super(parent, element);
		layoutRuleset = new LayoutRuleset(element.getLayout());
	}
	
	@Override
	public void layout(LayoutState layoutState) {
		if(!layoutRuleset.equals(element.getLayout())) {
			layoutRuleset = new LayoutRuleset(element.getLayout());
		}
		super.layout(layoutState);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);

		if (cursorTimer <= CURSOR_VISIBLE_DURATION) {
			cursorVisible = true;
		} else {
			cursorVisible = false;
		}
		if (cursorTimer <= 0f) {
			cursorTimer += CURSOR_VISIBLE_DURATION * 2f;
		}
		cursorTimer -= delta;
	}

	@Override
	protected void renderElement(Graphics g) {
		BitmapFont font = style.getBitmapFont();
		if (font == null) {
			return;
		}

		NinePatch ninePatch = style.getNormalNinePatch();
		if (element.isEnabled()) {
			switch (getState()) {
			case ACTION:
				ninePatch = style.getActionNinePatch();
				break;
			case HOVER:
				ninePatch = style.getHoverNinePatch();
				break;
			default:
				break;
			}
		} else {
			ninePatch = style.getDisabledNinePatch();
		}

		float textRenderX = getContentRenderX();
		float textRenderY = getContentRenderY();

		g.drawNinePatch(ninePatch, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(), getInnerRenderHeight());

		BitmapFont previousFont = g.getFont();
		Color previousColor = g.getColor();

		g.setFont(font);
		g.setColor(style.getColor());

		g.drawString(element.getValue(), textRenderX, textRenderY);
		if (cursorVisible && isReceivingInput()) {
			g.drawLineSegment(textRenderX + renderCursorX, textRenderY, textRenderX + renderCursorX,
					textRenderY + renderCursorHeight);
		}

		g.setFont(previousFont);
		g.setColor(previousColor);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if(layoutRuleset.isHiddenByInputSource(layoutState.getLastInputSource())) {
			return 0f;
		}
		float layoutRuleResult = layoutRuleset.getPreferredWidth(layoutState);
		if(layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		return layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft() - style.getMarginRight();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		float result = style.getFontSize();
		if(result < style.getMinHeight()) {
			return style.getMinHeight();
		}
		return result;
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected TextBoxStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		switch (getState()) {
		case ACTION:
			return false;
		case NORMAL:
			if (outerArea.contains(screenX, screenY)) {
				super.setState(NodeState.HOVER);
			}
			break;
		case HOVER:
			if (!outerArea.contains(screenX, screenY)) {
				super.setState(NodeState.NORMAL);
			}
			break;
		}
		return false;
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (button != Buttons.LEFT) {
			return null;
		}
		switch (getState()) {
		case ACTION:
			if (!outerArea.contains(screenX, screenY)) {
				endAction();
				return null;
			}
			return this;
		default:
			if (outerArea.contains(screenX, screenY)) {
				return this;
			}
			return null;
		}
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return;
		}
		switch (getState()) {
		case ACTION:
			if (outerArea.contains(screenX, screenY)) {
				setCursorIndex(screenX, screenY);
			} else {
				endAction();
			}
			break;
		default:
			if (outerArea.contains(screenX, screenY)) {
				beginAction();
				switch (Mdx.os) {
				case ANDROID:
				case IOS:
					Gdx.input.setOnscreenKeyboardVisible(true);
					break;
				default:
					break;
				}
			} else {
				super.setState(NodeState.NORMAL);
			}
			break;
		}
	}

	@Override
	public void beginAction() {
		super.setState(NodeState.ACTION);
		element.notifyActionListenersOfBeginEvent();
	}

	@Override
	public void endAction() {
		super.setState(NodeState.NORMAL);
		element.notifyActionListenersOfEndEvent();
	}

	@Override
	public void characterReceived(char c) {
		if (!isValidCharacter(c)) {
			return;
		}
		switch (cursor) {
		case 0:
			element.setValue(c + element.getValue());
			break;
		default:
			if (cursor == element.getValue().length()) {
				element.setValue(element.getValue() + c);
			} else {
				element.setValue(element.getValue().substring(0, cursor) + c + element.getValue().substring(cursor));
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
			element.setValue(element.getValue().substring(1));
			break;
		default:
			if (cursor == element.getValue().length() - 1) {
				element.setValue(element.getValue().substring(0, element.getValue().length() - 1));
			} else {
				element.setValue(element.getValue().substring(0, cursor - 1) + element.getValue().substring(cursor));
			}
			break;
		}
		cursor--;
		setCursorRenderX();
	}

	@Override
	public boolean enter() {
		endAction();
		setState(NodeState.HOVER);
		return true;
	}

	@Override
	public void moveCursorRight() {
		if (cursor == element.getValue().length() - 1) {
			return;
		}
		cursor++;
		setCursorRenderX();
	}

	@Override
	public void moveCursorLeft() {
		if (cursor == 0) {
			return;
		}
		cursor--;
		setCursorRenderX();
	}

	@Override
	public void cut() {
		if (clipboard == null) {
			return;
		}
		clipboard.setContents(element.getValue());
		element.setValue("");
	}

	@Override
	public void copy() {
		if (clipboard == null) {
			return;
		}
		clipboard.setContents(element.getValue());
	}

	@Override
	public void paste() {
		if (clipboard == null) {
			return;
		}
		element.setValue(clipboard.getContents());
	}

	@Override
	public boolean isReceivingInput() {
		return getState() == NodeState.ACTION;
	}

	protected boolean isValidCharacter(char c) {
		if (c == '\n') {
			return false;
		}
		if (c == '\t') {
			return false;
		}
		if (c == '\r') {
			return false;
		}
		if (c == '\b') {
			return false;
		}
		if (Character.getName(c).equals("NULL")) {
			return false;
		}
		if (Character.getName(c).contains("PRIVATE USE")) {
			return false;
		}
		return true;
	}

	private void setCursorIndex(float screenX, float screenY) {
		if (element.getValue().length() == 0) {
			cursor = 0;
			setCursorRenderX();
			return;
		}
		if (style == null || style.getBitmapFont() == null) {
			cursor = 0;
			setCursorRenderX();
			return;
		}
		BitmapFont font = style.getBitmapFont();

		float clickX = screenX - getOuterRenderX() - style.getPaddingLeft();

		for (int i = 0; i < element.getValue().length(); i++) {
			glyphLayout.setText(font, element.getValue().substring(0, i + 1));
			if (clickX < glyphLayout.width) {
				float result = glyphLayout.width;
				glyphLayout.setText(font, element.getValue().charAt(i) + "");
				result -= glyphLayout.width;
				cursor = i;
				setCursorRender(result - 1f, glyphLayout.height);
				return;
			}
		}
		setCursorRender(glyphLayout.width + 1f, glyphLayout.height);
	}

	private void setCursorRenderX() {
		switch (cursor) {
		case 0:
			setCursorRender(0f, style.getFontSize());
			return;
		default:
			if (style == null || style.getBitmapFont() == null) {
				return;
			}
			BitmapFont font = style.getBitmapFont();
			if(cursor > element.getValue().length()) {
				glyphLayout.setText(font, element.getValue());
			} else {
				glyphLayout.setText(font, element.getValue().substring(0, cursor));
			}
			setCursorRender(glyphLayout.width + 1f, glyphLayout.height);
			break;
		}
	}

	private void setCursorRender(float renderX, float renderHeight) {
		this.renderCursorX = renderX;
		this.renderCursorHeight = renderHeight;
	}
	
	@Override
	public void setState(NodeState state) {
		if(getState() == NodeState.ACTION) {
			return;
		}
		super.setState(state);
	}

	public LayoutRuleset getLayoutRuleset() {
		return layoutRuleset;
	}
}
