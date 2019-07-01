/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.style.ruleset;

import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ParentStyleRule;
import org.mini2Dx.ui.style.StyleRuleset;
import org.mini2Dx.ui.style.UiTheme;

/**
 * {@link StyleRuleset} implementation for {@link ParentStyleRule}s
 */
public class ColumnStyleRuleset extends StyleRuleset<ParentStyleRule> {
	@Field
	private ObjectMap<ScreenSize, ParentStyleRule> rules;
	
	@Override
	public void putStyleRule(ScreenSize screenSize, ParentStyleRule rule) {
		if(rules == null) {
			rules = new ObjectMap<ScreenSize, ParentStyleRule>();
		}
		rules.put(screenSize, rule);
	}

	@Override
	public ParentStyleRule getStyleRule(ScreenSize screenSize) {
		return getStyleRule(screenSize, rules);
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
