/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.style.ruleset;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.StyleRuleset;
import org.mini2Dx.ui.style.TabStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;

/**
 * {@link StyleRuleset} implementation for {@link TabStyleRule}s
 */
public class TabStyleRuleset extends StyleRuleset<TabStyleRule> {
	@Field
	private Map<ScreenSize, TabStyleRule> rules;
	
	@Override
	public void putStyleRule(ScreenSize screenSize, TabStyleRule rule) {
		if(rules == null) {
			rules = new HashMap<ScreenSize, TabStyleRule>();
		}
		rules.put(screenSize, rule);
	}

	@Override
	public TabStyleRule getStyleRule(ScreenSize screenSize, float screenSizeScale) {
		return getStyleRule(screenSize, screenSizeScale, rules);
	}

	@Override
	public void validate(UiTheme theme) {
		validate(theme, rules);
	}

	@Override
	public void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies) {
		loadDependencies(theme, dependencies, rules);
	}

	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		prepareAssets(theme, fileHandleResolver, assetManager, rules);
	}
}
