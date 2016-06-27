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
package org.mini2Dx.ui.layout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.input.InputSource;

/**
 * The width and offset ruleset of a {@link UiElement} for different
 * {@link ScreenSize}s
 */
public class LayoutRuleset {
	public static final String DEFAULT_LAYOUT = "xs-12c";
	public static final LayoutRuleset DEFAULT_RULESET = new LayoutRuleset(DEFAULT_LAYOUT);

	protected static final String PIXEL_SUFFIX = "px";
	protected static final String COLUMN_SUFFIX = "c";
	protected static final String EMPTY_STRING = "";

	protected final Map<ScreenSize, SizeRule> sizeRules = new HashMap<ScreenSize, SizeRule>();
	protected final Set<InputSource> hiddenByInput = new HashSet<InputSource>();
	protected final Map<ScreenSize, OffsetRule> offsetRules = new HashMap<ScreenSize, OffsetRule>();

	private final String rules;
	private boolean hiddenByInputSource = false;
	
	private SizeRule currentSizeRule = null;
	private OffsetRule currentOffsetRule = null;

	/**
	 * Constructor
	 * @param rules The ruleset, e.g. xs-12c xs-offset-4c sm-500px sm-offset-20px
	 */
	public LayoutRuleset(String rules) {
		this.rules = rules;
		
		String[] rule = rules.split(" ");
		for (int i = 0; i < rule.length; i++) {
			String[] ruleDetails = rule[i].split("-");
			switch (ruleDetails.length) {
			case 1:
				break;
			case 2:
				// e.g. xs-12, hidden-controller, visible-touchscreen,
				// hidden-keyboardmouse
				storeWidthRule(ruleDetails);
				break;
			case 3:
				// e.g. xs-offset-12
				storeOffsetRule(ruleDetails);
				break;
			}
		}
		finaliseRuleset();
	}

	private void storeWidthRule(String[] ruleDetails) {
		switch (ruleDetails[0].toLowerCase()) {
		case "hidden": {
			switch (InputSource.fromString(ruleDetails[1])) {
			case CONTROLLER:
				hiddenByInput.add(InputSource.CONTROLLER);
				break;
			case KEYBOARD_MOUSE:
				hiddenByInput.add(InputSource.KEYBOARD_MOUSE);
				break;
			case TOUCHSCREEN:
				hiddenByInput.add(InputSource.TOUCHSCREEN);
				break;
			}
			break;
		}
		default:
			ScreenSize screenSize = ScreenSize.fromString(ruleDetails[0]);
			if (ruleDetails[1].endsWith(PIXEL_SUFFIX)) {
				sizeRules.put(screenSize,
						new AbsoluteSizeRule(Integer.parseInt(ruleDetails[1].replace(PIXEL_SUFFIX, EMPTY_STRING))));
			} else if (ruleDetails[1].endsWith(COLUMN_SUFFIX)) {
				sizeRules.put(screenSize,
						new ResponsiveSizeRule(Integer.parseInt(ruleDetails[1].replace(COLUMN_SUFFIX, EMPTY_STRING))));
			} else {
				throw new MdxException("Invalid size - must end with c (columns) or px (pixels");
			}
			break;
		}
	}

	private void storeOffsetRule(String[] ruleDetails) {
		ScreenSize screenSize = ScreenSize.fromString(ruleDetails[0]);
		if (ruleDetails[2].endsWith(PIXEL_SUFFIX)) {
			offsetRules.put(screenSize,
					new AbsoluteOffsetRule(Integer.parseInt(ruleDetails[2].replace(PIXEL_SUFFIX, EMPTY_STRING))));
		} else if (ruleDetails[2].endsWith(COLUMN_SUFFIX)) {
			offsetRules.put(screenSize,
					new ResponsiveOffsetRule(Integer.parseInt(ruleDetails[2].replace(COLUMN_SUFFIX, EMPTY_STRING))));
		} else {
			throw new MdxException("Invalid offset - must end with c (columns) or px (pixels");
		}
	}

	private void finaliseRuleset() {
		Iterator<ScreenSize> screenSizes = ScreenSize.smallestToLargest();
		SizeRule lastSizeRule = new ResponsiveSizeRule(12);
		OffsetRule lastOffsetRule = new AbsoluteOffsetRule(0);

		while (screenSizes.hasNext()) {
			ScreenSize nextSize = screenSizes.next();

			if (!sizeRules.containsKey(nextSize)) {
				sizeRules.put(nextSize, lastSizeRule);
			} else {
				lastSizeRule = sizeRules.get(nextSize);
			}

			if (!offsetRules.containsKey(nextSize)) {
				offsetRules.put(nextSize, lastOffsetRule);
			} else {
				lastOffsetRule = offsetRules.get(nextSize);
			}
		}
	}

	public boolean isHiddenByInputSource(InputSource lastInputSource) {
		hiddenByInputSource = hiddenByInput.contains(lastInputSource);
		return hiddenByInputSource;
	}

	public float getPreferredWidth(LayoutState layoutState) {
		currentSizeRule = sizeRules.get(layoutState.getScreenSize());
		return currentSizeRule.getWidth(layoutState);
	}

	public float getXOffset(LayoutState layoutState) {
		currentOffsetRule = offsetRules.get(layoutState.getScreenSize());
		return currentOffsetRule.getOffset(layoutState);
	}

	public boolean isHiddenByInputSource() {
		return hiddenByInputSource;
	}
	
	public SizeRule getCurrentSizeRule() {
		return currentSizeRule;
	}

	public OffsetRule getCurrentOffsetRule() {
		return currentOffsetRule;
	}

	public boolean equals(String rules) {
		return this.rules.equals(rules);
	}
}
