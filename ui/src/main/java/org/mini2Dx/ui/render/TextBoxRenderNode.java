/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.render;

import org.mini2Dx.core.ApiRuntime;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.font.FontGlyphLayout;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.KeyboardEventTriggerParams;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.*;
import org.mini2Dx.ui.style.BackgroundRenderer;
import org.mini2Dx.ui.style.TextBoxStyleRule;

/**
 * {@link RenderNode} implementation for {@link TextBox}
 */
public class TextBoxRenderNode extends RenderNode<TextBox, TextBoxStyleRule> implements TextInputableRenderNode {
	private static final float CURSOR_VISIBLE_DURATION = 0.5f;

	//private final Clipboard clipboard = Gdx.app.getClipboard();

	private int cursor;
	private float cursorTimer = 1.0f;
	private boolean cursorVisible;
	private float renderCursorX;
	private float renderCursorHeight;

	private boolean previouslyVisible = false;

	protected LayoutRuleset layoutRuleset;
	protected FontGlyphLayout glyphLayout = Mdx.fonts.defaultFont().newGlyphLayout();
	protected GameFontCache fontCache = Mdx.fonts.defaultFont().newCache();

	public TextBoxRenderNode(ParentRenderNode<?, ?> parent, TextBox element) {
		super(parent, element);
		initLayoutRuleset();
	}

	protected void initLayoutRuleset() {
		if(element.getFlexLayout() != null) {
			layoutRuleset = FlexLayoutRuleset.parse(element.getFlexLayout());
		} else {
			layoutRuleset = new ImmediateLayoutRuleset(element);
		}
	}

	@Override
	public void layout(LayoutState layoutState) {
		if (!layoutRuleset.equals(element.getFlexLayout())) {
			initLayoutRuleset();
		}
		if(isIncludedInRender() && isIncludedInRender() != previouslyVisible) {
			updateBitmapFontCache();
		}
		previouslyVisible = isIncludedInRender();
		super.layout(layoutState);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);

		if(isIncludedInRender() && isIncludedInRender() != previouslyVisible) {
			updateBitmapFontCache();
		}
		previouslyVisible = isIncludedInRender();

		if (cursorTimer <= CURSOR_VISIBLE_DURATION) {
			cursorVisible = true;
		} else {
			cursorVisible = false;
		}
		if (cursorTimer <= 0f) {
			cursorTimer += CURSOR_VISIBLE_DURATION * 2f;
		}
		if (cursor > element.getValue().length()) {
			cursor = element.getValue().length();
			setCursorRenderX();
		}
		cursorTimer -= delta;
	}

	@Override
	protected void renderElement(Graphics g) {
		BackgroundRenderer backgroundRenderer = style.getNormalBackgroundRenderer();
		if (element.isEnabled()) {
			switch (getState()) {
			case ACTION:
				backgroundRenderer = style.getActionBackgroundRenderer();
				break;
			case HOVER:
				backgroundRenderer = style.getHoverBackgroundRenderer();
				break;
			default:
				break;
			}
		} else {
			backgroundRenderer = style.getDisabledBackgroundRenderer();
		}

		float textRenderX = getContentRenderX();
		float textRenderY = getContentRenderY();

		backgroundRenderer.render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(), getInnerRenderHeight());

		fontCache.setPosition(textRenderX, textRenderY);
		g.drawFontCache(fontCache);
		if (cursorVisible && isReceivingInput()) {
			g.drawLineSegment(textRenderX + renderCursorX, textRenderY, textRenderX + renderCursorX,
					textRenderY + renderCursorHeight);
		}
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if (layoutRuleset.isHiddenByInputSource(layoutState)) {
			return 0f;
		}
		float layoutRuleResult = layoutRuleset.getPreferredElementWidth(layoutState);
		if (layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		return layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft()
				- style.getMarginRight();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		float result = style.getFontSize();
		if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			result = style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		float sizeRuleHeight = layoutRuleset.getPreferredElementHeight(layoutState) - style.getPaddingTop()
				- style.getPaddingBottom() - style.getMarginTop() - style.getMarginBottom();
		if (!layoutRuleset.getCurrentHeightRule().isAutoSize()) {
			result = Math.max(result, sizeRuleHeight);
		}
		return result;
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeX(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeY(layoutState);
	}

	@Override
	protected TextBoxStyleRule determineStyleRule(LayoutState layoutState) {
		if (fontCache != null) {
			fontCache.clear();
			fontCache = null;
		}
		if(glyphLayout != null) {
			glyphLayout.dispose();
			glyphLayout = null;
		}
		TextBoxStyleRule result = layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());

		if (result == null) {
			fontCache = Mdx.fonts.defaultFont().newCache();
			glyphLayout = Mdx.fonts.defaultFont().newGlyphLayout();
		} else {
			fontCache = result.getGameFont().newCache();
			glyphLayout = result.getGameFont().newGlyphLayout();
		}
		updateBitmapFontCache();
		return result;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		switch (getState()) {
		case ACTION:
			return false;
		case NORMAL:
			if (innerArea.contains(screenX, screenY)) {
				super.setState(NodeState.HOVER);
			}
			break;
		case HOVER:
			if (!innerArea.contains(screenX, screenY)) {
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
		if (button != Input.Buttons.LEFT) {
			return null;
		}
		switch (getState()) {
		case ACTION:
			if (!innerArea.contains(screenX, screenY)) {
				MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
				params.setMouseX(screenX);
				params.setMouseY(screenY);
				endAction(EventTrigger.getTriggerForMouseClick(button), params);
				EventTriggerParamsPool.release(params);
				return null;
			}
			return this;
		default:
			if (innerArea.contains(screenX, screenY)) {
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
			if (innerArea.contains(screenX, screenY)) {
				setCursorIndex(screenX, screenY);
			} else {
				MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
				params.setMouseX(screenX);
				params.setMouseY(screenY);
				endAction(EventTrigger.getTriggerForMouseClick(button), params);
				EventTriggerParamsPool.release(params);
			}
			break;
		default:
			if (innerArea.contains(screenX, screenY)) {
				MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
				params.setMouseX(screenX);
				params.setMouseY(screenY);
				beginAction(EventTrigger.getTriggerForMouseClick(button), params);
				EventTriggerParamsPool.release(params);

				switch (Mdx.platform) {
				case ANDROID:
				case IOS:
					Mdx.input.setOnScreenKeyboardVisible(true);
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
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		super.setState(NodeState.ACTION);
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		super.setState(NodeState.NORMAL);
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void characterReceived(char c) {
		if (!element.notifyTextInputListeners(c)) {
			return;
		}
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
		KeyboardEventTriggerParams params = EventTriggerParamsPool.allocateKeyboardParams();
		params.setKey(Input.Keys.ENTER);
		endAction(EventTrigger.KEYBOARD, params);
		EventTriggerParamsPool.release(params);
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
//		if (clipboard == null) {
//			return;
//		}
//		clipboard.setContents(element.getValue());
//		element.setValue("");
	}

	@Override
	public void copy() {
//		if (clipboard == null) {
//			return;
//		}
//		clipboard.setContents(element.getValue());
	}

	@Override
	public void paste() {
//		if (clipboard == null) {
//			return;
//		}
//		element.setValue(clipboard.getContents());
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
		if(Mdx.runtime.equals(ApiRuntime.LIBGDX)) {
			if (Character.getName(c).equals("NULL")) {
				return false;
			}
			if (Character.getName(c).contains("PRIVATE USE")) {
				return false;
			}
		}
		return true;
	}

	private void setCursorIndex(float screenX, float screenY) {
		if (element.getValue().length() == 0) {
			cursor = 0;
			setCursorRenderX();
			return;
		}
		if (style == null) {
			cursor = 0;
			setCursorRenderX();
			return;
		}

		float clickX = screenX - getOuterRenderX() - style.getPaddingLeft();

		for (int i = 0; i < element.getValue().length(); i++) {
			glyphLayout.setText(element.getValue().substring(0, i + 1));
			if (clickX < glyphLayout.getWidth()) {
				float result = glyphLayout.getWidth();
				glyphLayout.setText(element.getValue().charAt(i) + "");
				result -= glyphLayout.getWidth();
				cursor = i;
				setCursorRender(result - 1f, glyphLayout.getHeight());
				return;
			}
		}
		cursor = element.getValue().length();
		setCursorRender(glyphLayout.getWidth() + 1f, glyphLayout.getHeight());
	}

	private void setCursorRenderX() {
		switch (cursor) {
		case 0:
			setCursorRender(0f, style.getFontSize());
			return;
		default:
			if (style == null) {
				return;
			}
			if (cursor > element.getValue().length()) {
				glyphLayout.setText(element.getValue());
			} else {
				glyphLayout.setText(element.getValue().substring(0, cursor));
			}
			setCursorRender(glyphLayout.getWidth() + 1f, glyphLayout.getHeight());
			break;
		}
	}

	public void updateBitmapFontCache() {
		if (style == null) {
			return;
		}

		fontCache.clear();
		fontCache.setColor(style.getColor());
		fontCache.addText(element.getValue(), 0f, 0f, preferredContentWidth,
				HorizontalAlignment.LEFT.getAlignValue(), true);
	}

	private void setCursorRender(float renderX, float renderHeight) {
		this.renderCursorX = renderX;
		this.renderCursorHeight = renderHeight;
	}

	@Override
	public void setState(NodeState state) {
		if (getState() == NodeState.ACTION) {
			return;
		}
		super.setState(state);
	}

	public LayoutRuleset getLayoutRuleset() {
		return layoutRuleset;
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}
}
