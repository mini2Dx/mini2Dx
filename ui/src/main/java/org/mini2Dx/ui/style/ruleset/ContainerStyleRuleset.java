/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.style.ruleset;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ContainerStyleRule;
import org.mini2Dx.ui.style.StyleRuleset;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class ContainerStyleRuleset extends StyleRuleset<ContainerStyleRule> {
	@Field
	private Map<ScreenSize, ContainerStyleRule> rules;
	
	@Override
	public void putStyleRule(ScreenSize screenSize, ContainerStyleRule rule) {
		if(rules == null) {
			rules = new HashMap<ScreenSize, ContainerStyleRule>();
		}
		rules.put(screenSize, rule);
	}

	@Override
	public ContainerStyleRule getStyleRule(ScreenSize screenSize) {
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
