/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.ui.style.ruleset;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ScrollBoxStyleRule;
import org.mini2Dx.ui.style.StyleRuleset;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;

/**
 * {@link StyleRuleset} implementation for {@link ScrollBoxStyleRule}s
 */
public class ScrollBoxStyleRuleset extends StyleRuleset<ScrollBoxStyleRule> {
	@Field
	private Map<ScreenSize, ScrollBoxStyleRule> rules;
	
	@Override
	public void putStyleRule(ScreenSize screenSize, ScrollBoxStyleRule rule) {
		if(rules == null) {
			rules = new HashMap<ScreenSize, ScrollBoxStyleRule>();
		}
		rules.put(screenSize, rule);
	}

	@Override
	public ScrollBoxStyleRule getStyleRule(ScreenSize screenSize, float screenSizeScale) {
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
