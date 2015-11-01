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
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.engine.SizeChangeListener;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.layout.AutoHeightRule;
import org.mini2Dx.ui.layout.AutoXPositionRule;
import org.mini2Dx.ui.layout.DefaultYPositionRule;
import org.mini2Dx.ui.layout.MinHeightRule;
import org.mini2Dx.ui.layout.PositionRule;
import org.mini2Dx.ui.layout.ResponsivePositionRule;
import org.mini2Dx.ui.layout.ResponsiveWidthRule;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.layout.SizeRule;
import org.mini2Dx.ui.theme.UiElementStyle;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public abstract class BasicUiElement<T extends UiElementStyle> implements UiElement<T> {
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
	private ElementState state = ElementState.NORMAL;
	
	public BasicUiElement() {
		this(null);
	}
	
	public BasicUiElement(String id) {
		if(id == null) {
			id = String.valueOf(currentArea.getId());
		}
		this.id = id;
	}
	
	@Override
	public void update(UiContainer uiContainer, float delta) {
		targetArea.set(parentX + xRule.getTargetPosition(), parentY + yRule.getTargetPosition(),
				widthRule.getTargetSize(), heightRule.getTargetSize());
		currentArea.preUpdate();
		
		cleanupFinishedEffects();
		
		if(effects.isEmpty()) {
			if(rulesChanged) {
				currentArea.set(targetArea);
			}
			rulesChanged = false;
			return;
		}
		
		updateEffects(uiContainer, delta);
	}
	
	private void cleanupFinishedEffects() {
		if(finishedEffects.isEmpty()) {
			return;
		}
		effects.removeAll(finishedEffects);
		finishedEffects.clear();
	}
	
	private void updateEffects(UiContainer uiContainer, float delta) {
		for(int i = 0; i < effects.size(); i++) {
			UiEffect effect = effects.get(i);
			effect.update(uiContainer, currentArea, targetArea, delta);
			if(effect.isFinished()) {
				finishedEffects.add(effect);
			}
		}
	}

	@Override
	public void interpolate(UiContainer uiContainer, float alpha) {
		currentArea.interpolate(null, alpha);
	}

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
		
		UiElementStyle currentStyle = getCurrentStyle();
		
		widthRule = widthRules.get(screenSize);
		widthRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		
		if(currentStyle.getMinHeight() > 0) {
			heightRule = new MinHeightRule(this);
		} else {
			heightRule = new AutoHeightRule(this);
		}
		heightRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		
		xRule = xPositionRules.get(screenSize);
		xRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		yRule.onScreenResize(theme, currentStyle, columnWidth, totalHeight);
		
		rulesChanged = true;
	}
	
	@Override
	public void positionChanged(CollisionBox moved) {
		parentX = moved.getX();
		parentY = moved.getY();
	}

	@Override
	public void applyEffect(UiEffect effect) {
		effects.add(effect);
	}
	
	public void setXRules(String rules) {
		String[] positionData = rules.split(" ");
		for (String positionRule : positionData) {
			if (positionRule.equals("auto")) {
				xPositionRules.put(ScreenSize.XL, new AutoXPositionRule(this));
				xPositionRules.put(ScreenSize.LG, new AutoXPositionRule(this));
				xPositionRules.put(ScreenSize.MD, new AutoXPositionRule(this));
				xPositionRules.put(ScreenSize.SM, new AutoXPositionRule(this));
				xPositionRules.put(ScreenSize.XS, new AutoXPositionRule(this));
				return;
			} else {
				String[] ruleData = positionRule.split("-");
				int value = Integer.parseInt(ruleData[1]);
				
				if (positionRule.startsWith("xs")) {
					xPositionRules.put(ScreenSize.XS, new ResponsivePositionRule(value));
				} else if (positionRule.startsWith("sm")) {
					xPositionRules.put(ScreenSize.SM, new ResponsivePositionRule(value));
				} else if (positionRule.startsWith("md")) {
					xPositionRules.put(ScreenSize.MD, new ResponsivePositionRule(value));
				} else if (positionRule.startsWith("lg")) {
					xPositionRules.put(ScreenSize.LG, new ResponsivePositionRule(value));
				} else if (positionRule.startsWith("xl")) {
					xPositionRules.put(ScreenSize.XL, new ResponsivePositionRule(value));
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
	public int getRenderX() {
		return MathUtils.round(currentArea.getRenderX());
	}

	@Override
	public int getRenderY() {
		return MathUtils.round(currentArea.getRenderY());
	}

	@Override
	public int getRenderWidth() {
		return MathUtils.round(currentArea.getRenderWidth());
	}

	@Override
	public int getRenderHeight() {
		return MathUtils.round(currentArea.getRenderHeight());
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
		if (id == null) {
			return String.valueOf(currentArea.getId());
		}
		return null;
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
	
	public void addSizeChangeListener(SizeChangeListener<CollisionBox> listener) {
		currentArea.addSizeChangeListener(listener);
	}
}
