/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.TabView;

/**
 * Extends {@link StyleRule} for {@link TabView} styling
 */
public class TabStyleRule extends ColumnStyleRule {
	@Field
	private String tabButtonStyle;
	@Field(optional=true)
	private String previousTabButtonStyle;
	@Field(optional=true)
	private String nextTabButtonStyle;
	@Field(optional=true)
	private String buttonLabelStyle;
	@Field(optional=true)
	private String buttonImageStyle;
	@Field
	private String menuStyle;
	@Field(optional=true)
	private String tabStyle;
	
	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		if(!theme.containsButtonStyleRuleset(tabButtonStyle)) {
			throw new MdxException("No style with id '" + tabButtonStyle + "' for buttons. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(previousTabButtonStyle != null && !theme.containsButtonStyleRuleset(previousTabButtonStyle)) {
			throw new MdxException("No style with id '" + previousTabButtonStyle + "' for buttons. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(nextTabButtonStyle != null && !theme.containsButtonStyleRuleset(nextTabButtonStyle)) {
			throw new MdxException("No style with id '" + nextTabButtonStyle + "' for buttons. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(buttonLabelStyle != null && !theme.containsLabelStyleRuleset(buttonLabelStyle)) {
			throw new MdxException("No style with id '" + buttonLabelStyle + "' for labels. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(buttonImageStyle != null && !theme.containsImageStyleRuleset(buttonImageStyle)) {
			throw new MdxException("No style with id '" + buttonImageStyle + "' for images. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(!theme.containsColumnStyleRuleset(menuStyle)) {
			throw new MdxException("No style with id '" + menuStyle + "' for columns/rows. Required by " + TabStyleRule.class.getSimpleName());
		}
		if(tabStyle != null && !theme.containsColumnStyleRuleset(tabStyle)) {
			throw new MdxException("No style with id '" + tabStyle + "' for columns/rows. Required by " + TabStyleRule.class.getSimpleName());
		}
	}

	public String getTabButtonStyle() {
		return tabButtonStyle;
	}

	public void setTabButtonStyle(String buttonStyle) {
		this.tabButtonStyle = buttonStyle;
	}

	public String getPreviousTabButtonStyle() {
		if(previousTabButtonStyle == null) {
			return tabButtonStyle;
		}
		return previousTabButtonStyle;
	}

	public void setPreviousTabButtonStyle(String previousTabButtonStyle) {
		this.previousTabButtonStyle = previousTabButtonStyle;
	}

	public String getNextTabButtonStyle() {
		if(nextTabButtonStyle == null) {
			return tabButtonStyle;
		}
		return nextTabButtonStyle;
	}

	public void setNextTabButtonStyle(String nextTabButtonStyle) {
		this.nextTabButtonStyle = nextTabButtonStyle;
	}

	public String getButtonLabelStyle() {
		return buttonLabelStyle;
	}

	public void setButtonLabelStyle(String buttonLabelStyle) {
		this.buttonLabelStyle = buttonLabelStyle;
	}

	public String getButtonImageStyle() {
		return buttonImageStyle;
	}

	public void setButtonImageStyle(String buttonImageStyle) {
		this.buttonImageStyle = buttonImageStyle;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuColumnStyle) {
		this.menuStyle = menuColumnStyle;
	}

	public String getTabStyle() {
		return tabStyle;
	}

	public void setTabStyle(String tabStyle) {
		this.tabStyle = tabStyle;
	}
}
