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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.ui.UiContentContainer;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.layout.AutoHeightRule;
import org.mini2Dx.ui.layout.AutoXPositionRule;
import org.mini2Dx.ui.layout.DefaultYPositionRule;
import org.mini2Dx.ui.layout.MinHeightRule;
import org.mini2Dx.ui.layout.PositionRule;
import org.mini2Dx.ui.layout.ResponsiveXPositionRule;
import org.mini2Dx.ui.layout.ResponsiveWidthRule;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.layout.SizeRule;
import org.mini2Dx.ui.listener.ContentPositionListener;
import org.mini2Dx.ui.listener.ContentSizeListener;
import org.mini2Dx.ui.theme.UiElementStyle;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public abstract class BasicUiElement<T extends UiElementStyle>
		implements UiElement<T>, PositionChangeListener<CollisionBox> {
	private final Map<ScreenSize, PositionRule> xPositionRules = new HashMap<ScreenSize, PositionRule>();
	private final Map<ScreenSize, SizeRule> widthRules = new HashMap<ScreenSize, SizeRule>();

	protected final String id;
	protected final CollisionBox currentArea = new CollisionBox();
	protected final Rectangle targetArea = new Rectangle();
	protected final List<UiEffect> effects = new ArrayList<UiEffect>(1);
	protected final List<UiEffect> finishedEffects = new ArrayList<UiEffect>(1);

	protected String styleId = UiTheme.DEFAULT_STYLE_ID;
	protected boolean rulesChanged, disposed;
	protected float parentX, parentY;

	protected PositionRule xRule;
	protected PositionRule yRule = new DefaultYPositionRule();
	protected SizeRule widthRule, heightRule;
	protected boolean visible = false;
	private boolean interpolate = false;

	private ElementState state = ElementState.NORMAL;
	private List<ContentSizeListener> contentSizeListeners;
	private List<ContentPositionListener> contentPositionListeners;

	public BasicUiElement() {
		this(null);
	}

	public BasicUiElement(String id) {
		if (id == null) {
			id = "ui-element-" + String.valueOf(currentArea.getId());
		}
		this.id = id;
		currentArea.addPostionChangeListener(this);
	}

	@Override
	public void update(UiContentContainer uiContainer, float delta) {
		targetArea.set(parentX + xRule.getTargetPosition(), parentY + yRule.getTargetPosition(),
				widthRule.getTargetSize(), heightRule.getTargetSize());
		currentArea.preUpdate();

		cleanupFinishedEffects();

		if (effects.isEmpty()) {
			if (rulesChanged) {
				currentArea.set(targetArea);
			}
			rulesChanged = false;
			return;
		}

		updateEffects(uiContainer, delta);
	}

	private void cleanupFinishedEffects() {
		if (finishedEffects.isEmpty()) {
			return;
		}
		effects.removeAll(finishedEffects);
		finishedEffects.clear();
	}

	private void updateEffects(UiContentContainer uiContainer, float delta) {
		boolean visibilityResult = false;
		for (int i = 0; i < effects.size(); i++) {
			UiEffect effect = effects.get(i);
			visibilityResult |= effect.update(uiContainer, currentArea, targetArea, delta);
			if (effect.isFinished()) {
				finishedEffects.add(effect);
			}
		}
		if (visibilityResult == visible) {
			return;
		}
		setVisible(visibilityResult);
	}

	@Override
	public void interpolate(UiContentContainer uiContainer, float alpha) {
		if (!interpolate) {
			return;
		}
		currentArea.interpolate(null, alpha);
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(currentArea.contains(screenX, screenY)) {
			state = ElementState.HOVER;
			return true;
		}
		state = ElementState.NORMAL;
		return false;
	}
	
	@Override
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button) {
		return null;
	}
	
	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {}

	@Override
	public UiElement<?> getById(String id) {
		if (id.equals(getId())) {
			return this;
		}
		return null;
	}

	@Override
	public void resize(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		applyStyle(theme, screenSize);
		applyRules(screenSize, theme, columnWidth, totalHeight);
		notifyRules(screenSize, theme, columnWidth, totalHeight);
		notifyContentSizeListeners();

		rulesChanged = true;
	}

	protected void applyRules(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		widthRule = determineSizeRule(screenSize, widthRules);

		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle.getMinHeight() > 0) {
			heightRule = new MinHeightRule(this);
		} else {
			heightRule = new AutoHeightRule(this);
		}

		xRule = determinePositionRule(screenSize, xPositionRules);
	}

	public void notifyRules(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		UiElementStyle currentStyle = getCurrentStyle();
		widthRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		heightRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		xRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		yRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
	}

	protected PositionRule determinePositionRule(ScreenSize screenSize, Map<ScreenSize, PositionRule> rules) {
		Iterator<ScreenSize> screenSizes = ScreenSize.largestToSmallest();
		while (screenSizes.hasNext()) {
			ScreenSize nextSize = screenSizes.next();
			if (nextSize.getMinSize() > screenSize.getMinSize()) {
				continue;
			}
			if (!rules.containsKey(nextSize)) {
				continue;
			}
			return rules.get(nextSize);
		}
		throw new MdxException("No position rule for element '" + getId() + "' with screen size " + screenSize);
	}

	private SizeRule determineSizeRule(ScreenSize screenSize, Map<ScreenSize, SizeRule> rules) {
		Iterator<ScreenSize> screenSizes = ScreenSize.largestToSmallest();
		while (screenSizes.hasNext()) {
			ScreenSize nextSize = screenSizes.next();
			if (nextSize.getMinSize() > screenSize.getMinSize()) {
				continue;
			}
			if (!rules.containsKey(nextSize)) {
				continue;
			}
			return rules.get(nextSize);
		}
		throw new MdxException("No size rule for element '" + getId() + "' with screen size " + screenSize);
	}

	@Override
	public void positionChanged(CollisionBox moved) {
		interpolate = true;
		notifyContentPositionListeners();
	}

	@Override
	public void onContentPositionChanged(UiElement<?> element) {
		parentX = element.getX() + element.getPaddingLeft();
		parentY = element.getY() + element.getPaddingTop();
		rulesChanged = true;
	}

	@Override
	public void applyEffect(UiEffect effect) {
		effects.add(effect);
	}

	public void addContentPositionListener(ContentPositionListener listener) {
		if (contentPositionListeners == null) {
			contentPositionListeners = new ArrayList<ContentPositionListener>();
		}
		contentPositionListeners.add(listener);
	}

	public void removeContentPositionListener(ContentPositionListener listener) {
		if (contentPositionListeners == null) {
			return;
		}
		contentPositionListeners.remove(listener);
	}

	protected void notifyContentPositionListeners() {
		if (contentPositionListeners == null) {
			return;
		}
		for (int i = contentPositionListeners.size() - 1; i >= 0; i--) {
			contentPositionListeners.get(i).onContentPositionChanged(this);
		}
	}

	public void addContentSizeListener(ContentSizeListener listener) {
		if (contentSizeListeners == null) {
			contentSizeListeners = new ArrayList<ContentSizeListener>(1);
		}
		contentSizeListeners.add(listener);
	}

	public void removeContentSizeListener(ContentSizeListener listener) {
		if (contentSizeListeners == null) {
			return;
		}
		contentSizeListeners.remove(listener);
	}

	protected void notifyContentSizeListeners() {
		if (widthRule != null && heightRule != null) {
			widthRule.onContentSizeChanged(this);
			heightRule.onContentSizeChanged(this);
			xRule.onContentSizeChanged(this, widthRule.getTargetSize(), heightRule.getTargetSize());
			yRule.onContentSizeChanged(this, widthRule.getTargetSize(), heightRule.getTargetSize());
		}

		if (contentSizeListeners == null) {
			return;
		}
		for (int i = contentSizeListeners.size() - 1; i >= 0; i--) {
			contentSizeListeners.get(i).onContentSizeChanged(this);
		}
	}

	public void setXRules(String rules) {
		if(rules.equals("auto")) {
			xPositionRules.put(ScreenSize.XL, new AutoXPositionRule(this));
			xPositionRules.put(ScreenSize.LG, new AutoXPositionRule(this));
			xPositionRules.put(ScreenSize.MD, new AutoXPositionRule(this));
			xPositionRules.put(ScreenSize.SM, new AutoXPositionRule(this));
			xPositionRules.put(ScreenSize.XS, new AutoXPositionRule(this));
			return;
		}
		
		String[] positionData = rules.split(" ");
		for (String positionRule : positionData) {
			if (positionRule.endsWith("auto")) {
				if (positionRule.startsWith("xs")) {
					xPositionRules.put(ScreenSize.XS, new AutoXPositionRule(this));
				} else if (positionRule.startsWith("sm")) {
					xPositionRules.put(ScreenSize.SM,  new AutoXPositionRule(this));
				} else if (positionRule.startsWith("md")) {
					xPositionRules.put(ScreenSize.MD, new AutoXPositionRule(this));
				} else if (positionRule.startsWith("lg")) {
					xPositionRules.put(ScreenSize.LG,  new AutoXPositionRule(this));
				} else if (positionRule.startsWith("xl")) {
					xPositionRules.put(ScreenSize.XL,  new AutoXPositionRule(this));
				}
			} else {
				String[] ruleData = positionRule.split("-");
				int value = Integer.parseInt(ruleData[1]);

				if (positionRule.startsWith("xs")) {
					xPositionRules.put(ScreenSize.XS, new ResponsiveXPositionRule(value));
				} else if (positionRule.startsWith("sm")) {
					xPositionRules.put(ScreenSize.SM, new ResponsiveXPositionRule(value));
				} else if (positionRule.startsWith("md")) {
					xPositionRules.put(ScreenSize.MD, new ResponsiveXPositionRule(value));
				} else if (positionRule.startsWith("lg")) {
					xPositionRules.put(ScreenSize.LG, new ResponsiveXPositionRule(value));
				} else if (positionRule.startsWith("xl")) {
					xPositionRules.put(ScreenSize.XL, new ResponsiveXPositionRule(value));
				}
			}
		}
	}

	public void setWidthRules(String rules) {
		String[] sizeData = rules.split(" ");
		for (String sizeRule : sizeData) {
			String[] ruleData = sizeRule.split("-");
			int value = Integer.parseInt(ruleData[1]);

			if (sizeRule.startsWith("xs")) {
				widthRules.put(ScreenSize.XS, new ResponsiveWidthRule(value));
			} else if (sizeRule.startsWith("sm")) {
				widthRules.put(ScreenSize.SM, new ResponsiveWidthRule(value));
			} else if (sizeRule.startsWith("md")) {
				widthRules.put(ScreenSize.MD, new ResponsiveWidthRule(value));
			} else if (sizeRule.startsWith("lg")) {
				widthRules.put(ScreenSize.LG, new ResponsiveWidthRule(value));
			} else if (sizeRule.startsWith("xl")) {
				widthRules.put(ScreenSize.XL, new ResponsiveWidthRule(value));
			}
		}
	}

	@Override
	public float getX() {
		return currentArea.getX();
	}

	@Override
	public float getY() {
		return currentArea.getY();
	}

	@Override
	public float getWidth() {
		return currentArea.getWidth();
	}

	@Override
	public float getHeight() {
		return currentArea.getHeight();
	}

	@Override
	public int getRenderX() {
		return currentArea.getRenderX();
	}

	@Override
	public int getRenderY() {
		return currentArea.getRenderY();
	}

	@Override
	public int getRenderWidth() {
		return currentArea.getRenderWidth();
	}

	@Override
	public int getRenderHeight() {
		return currentArea.getRenderHeight();
	}

	@Override
	public void dispose() {
		disposed = true;
	}

	@Override
	public boolean disposed() {
		return disposed;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getStyleId() {
		return styleId;
	}

	public ElementState getState() {
		return state;
	}

	public void setState(ElementState state) {
		this.state = state;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public float getElementWidth() {
		return getContentWidth() + getPaddingLeft() + getPaddingRight();
	}

	public float getElementHeight() {
		return getContentHeight() + getPaddingTop() + getPaddingBottom();
	}

	public int getPaddingTop() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getPaddingTop();
	}

	public int getPaddingBottom() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getPaddingBottom();
	}

	public int getPaddingLeft() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getPaddingLeft();
	}

	public int getPaddingRight() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getPaddingRight();
	}

	public int getMarginTop() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getMarginTop();
	}

	public int getMarginBottom() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getMarginBottom();
	}

	public int getMarginLeft() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getMarginLeft();
	}

	public int getMarginRight() {
		UiElementStyle currentStyle = getCurrentStyle();
		if (currentStyle == null) {
			return 0;
		}
		return currentStyle.getMarginRight();
	}

	@Override
	public String toString() {
		return "BasicUiElement [id=" + id + ", visible=" + visible + ", getX()=" + getX() + ", getY()=" + getY()
				+ ", getRenderX()=" + getRenderX() + ", getRenderY()=" + getRenderY() + ", getRenderWidth()="
				+ getRenderWidth() + ", getRenderHeight()=" + getRenderHeight() + ", getElementWidth()="
				+ getElementWidth() + ", getElementHeight()=" + getElementHeight() + ", getPaddingTop()="
				+ getPaddingTop() + ", getPaddingBottom()=" + getPaddingBottom() + ", getPaddingLeft()="
				+ getPaddingLeft() + ", getPaddingRight()=" + getPaddingRight() + ", getMarginTop()=" + getMarginTop()
				+ ", getMarginBottom()=" + getMarginBottom() + ", getMarginLeft()=" + getMarginLeft()
				+ ", getMarginRight()=" + getMarginRight() + "]";
	}
}
