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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.TabView;

/**
 * Extends {@link StyleRule} for {@link TabView} styling
 */
public class TabStyleRule extends ParentStyleRule {
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
