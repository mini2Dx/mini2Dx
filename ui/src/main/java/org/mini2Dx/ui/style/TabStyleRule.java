/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class TabStyleRule extends StyleRule {
	@Field
	private String tabButtonStyle;
	@Field(optional=true)
	private int ninePatchTop, ninePatchBottom, ninePatchLeft, ninePatchRight;
	@Field
	private String background;
	
	private NinePatch backgroundNinePatch;
	
	@Override
	public void validate(UiTheme theme) {
		if(!theme.containsButtonStyleRuleset(tabButtonStyle)) {
			throw new MdxException("No style with id '" + tabButtonStyle + "' for buttons. Required by " + TabStyleRule.class.getSimpleName());
		}
	}
	
	@Override
	public void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies) {
		dependencies.add(new AssetDescriptor<Texture>(background, Texture.class));
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		backgroundNinePatch = new NinePatch(assetManager.get(background, Texture.class), getNinePatchLeft(),
				getNinePatchRight(), getNinePatchTop(), getNinePatchBottom());
	}

	public NinePatch getBackgroundNinePatch() {
		return backgroundNinePatch;
	}
	
	public String getTabButtonStyle() {
		return tabButtonStyle;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public int getNinePatchTop() {
		if(ninePatchTop <= 0) {
			return getPaddingTop();
		}
		return ninePatchTop;
	}

	public int getNinePatchBottom() {
		if(ninePatchBottom <= 0) {
			return getPaddingBottom();
		}
		return ninePatchBottom;
	}

	public int getNinePatchLeft() {
		if(ninePatchLeft <= 0) {
			return getPaddingLeft();
		}
		return ninePatchLeft;
	}

	public int getNinePatchRight() {
		if(ninePatchRight <= 0) {
			return getPaddingRight();
		}
		return ninePatchRight;
	}
}
