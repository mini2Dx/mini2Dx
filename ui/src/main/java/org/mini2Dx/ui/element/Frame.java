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

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.ui.layout.AlignToBottomPositionRule;
import org.mini2Dx.ui.layout.AutoYPositionRule;
import org.mini2Dx.ui.layout.DefaultYPositionRule;
import org.mini2Dx.ui.layout.PositionRule;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.FrameStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public class Frame extends Column<FrameStyle> {
	private final Map<ScreenSize, PositionRule> yPositionRules = new HashMap<ScreenSize, PositionRule>();
	
	private FrameStyle currentStyle;
	
	public Frame() {
		super();
		yPositionRules.put(ScreenSize.XS, new DefaultYPositionRule());
	}

	@Override
	public void accept(UiRenderer renderer) {
		if(!visible) {
			return;
		}
		renderer.render(this);
		super.accept(renderer);
	}
	
	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getFrameStyle(screenSize, styleId);
		super.applyStyle(theme, screenSize);
	}
	
	@Override
	protected void applyRules(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		super.applyRules(screenSize, theme, columnWidth, totalHeight);
		yRule = determinePositionRule(screenSize, yPositionRules);
	}

	@Override
	public FrameStyle getCurrentStyle() {
		return currentStyle;
	}
	
	public void setYRules(String rules) {
		switch(rules) {
		case "auto":
			yPositionRules.put(ScreenSize.XL, new AutoYPositionRule());
			yPositionRules.put(ScreenSize.LG, new AutoYPositionRule());
			yPositionRules.put(ScreenSize.MD, new AutoYPositionRule());
			yPositionRules.put(ScreenSize.SM, new AutoYPositionRule());
			yPositionRules.put(ScreenSize.XS, new AutoYPositionRule());
			return;
		case "top":
			yPositionRules.put(ScreenSize.XL, new DefaultYPositionRule());
			yPositionRules.put(ScreenSize.LG, new DefaultYPositionRule());
			yPositionRules.put(ScreenSize.MD, new DefaultYPositionRule());
			yPositionRules.put(ScreenSize.SM, new DefaultYPositionRule());
			yPositionRules.put(ScreenSize.XS, new DefaultYPositionRule());
			return;
		case "bottom":
			yPositionRules.put(ScreenSize.XL, new AlignToBottomPositionRule());
			yPositionRules.put(ScreenSize.LG, new AlignToBottomPositionRule());
			yPositionRules.put(ScreenSize.MD, new AlignToBottomPositionRule());
			yPositionRules.put(ScreenSize.SM, new AlignToBottomPositionRule());
			yPositionRules.put(ScreenSize.XS, new AlignToBottomPositionRule());
			return;
		}
		
		String[] positionData = rules.split(" ");
		for (String positionRule : positionData) {
			if (positionRule.endsWith("auto")) {
				if (positionRule.startsWith("xs")) {
					yPositionRules.put(ScreenSize.XS, new AutoYPositionRule());
				} else if (positionRule.startsWith("sm")) {
					yPositionRules.put(ScreenSize.SM, new AutoYPositionRule());
				} else if (positionRule.startsWith("md")) {
					yPositionRules.put(ScreenSize.MD, new AutoYPositionRule());
				} else if (positionRule.startsWith("lg")) {
					yPositionRules.put(ScreenSize.LG, new AutoYPositionRule());
				} else if (positionRule.startsWith("xl")) {
					yPositionRules.put(ScreenSize.XL, new AutoYPositionRule());
				}
			} else if(positionRule.endsWith("top")) {
				if (positionRule.startsWith("xs")) {
					yPositionRules.put(ScreenSize.XS, new DefaultYPositionRule());
				} else if (positionRule.startsWith("sm")) {
					yPositionRules.put(ScreenSize.SM, new DefaultYPositionRule());
				} else if (positionRule.startsWith("md")) {
					yPositionRules.put(ScreenSize.MD, new DefaultYPositionRule());
				} else if (positionRule.startsWith("lg")) {
					yPositionRules.put(ScreenSize.LG, new DefaultYPositionRule());
				} else if (positionRule.startsWith("xl")) {
					yPositionRules.put(ScreenSize.XL, new DefaultYPositionRule());
				}
			} else {
				if (positionRule.startsWith("xs")) {
					yPositionRules.put(ScreenSize.XS, new AlignToBottomPositionRule());
				} else if (positionRule.startsWith("sm")) {
					yPositionRules.put(ScreenSize.SM, new AlignToBottomPositionRule());
				} else if (positionRule.startsWith("md")) {
					yPositionRules.put(ScreenSize.MD, new AlignToBottomPositionRule());
				} else if (positionRule.startsWith("lg")) {
					yPositionRules.put(ScreenSize.LG, new AlignToBottomPositionRule());
				} else if (positionRule.startsWith("xl")) {
					yPositionRules.put(ScreenSize.XL, new AlignToBottomPositionRule());
				}
			}
		}
	}
}
