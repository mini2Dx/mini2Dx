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

import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.layout.ScreenSize;

import java.util.Iterator;

/**
 * Base class for a group of {@link StyleRule}s for multiple {@link ScreenSize}s
 */
public abstract class StyleRuleset<T extends StyleRule> {
	
	public abstract T getStyleRule(ScreenSize screenSize);
	
	public abstract void putStyleRule(ScreenSize screenSize, T rule);
	
	public abstract void validate(UiTheme theme);
	
	public abstract void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies);
	
	public abstract void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager);
	
	protected T getStyleRule(ScreenSize screenSize, ObjectMap<ScreenSize, T> rules) {
		final Array<ScreenSize> largestToSmallestScreenSizes = ScreenSize.sharedLargestToSmallest();
		for(int i = 0; i < largestToSmallestScreenSizes.size; i++) {
			final ScreenSize nextSize = largestToSmallestScreenSizes.get(i);
			if(nextSize.isGreaterThan(screenSize)) {
				continue;
			}
			if(!rules.containsKey(nextSize)) {
				continue;
			}
			return rules.get(nextSize);
		}
		return null;
	}
	
	protected void validate(UiTheme theme, ObjectMap<ScreenSize, T> rules) {
		if(!rules.containsKey(ScreenSize.XS)) {
			throw new MdxException("XS screen size style required for all style rules");
		}
		for(T rule : rules.values()) {
			rule.validate(theme);
		}
	}
	
	protected void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies, ObjectMap<ScreenSize, T> rules) {
		for(T rule : rules.values()) {
			rule.loadDependencies(theme, dependencies);
		}
	}
	
	protected void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager, ObjectMap<ScreenSize, T> rules) {
		for(T rule : rules.values()) {
			rule.prepareAssets(theme, fileHandleResolver, assetManager);
		}
	}
}
