/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.ScrollBox;

/**
 * Extends {@link StyleRule} for {@link ScrollBox} styling
 */
public class ScrollBoxStyleRule extends ParentStyleRule {
	@Field(optional=true)
	private String topScrollButtonStyle;
	@Field(optional=true)
	private String bottomScrollButtonStyle;
	@Field
	private int scrollButtonHeight;
	@Field
	private int scrollBarWidth;
	@Field
	private String scrollTrack;
	@Field
	private String scrollThumbNormal;
	@Field
	private String scrollThumbHover;
	@Field
	private String scrollThumbAction;
	
	private BackgroundRenderer scrollTrackRenderer, scrollThumbNormalRenderer, scrollThumbHoverRenderer, scrollThumbActiveRenderer;
	
	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		
		if(topScrollButtonStyle != null && !theme.containsButtonStyleRuleset(topScrollButtonStyle)) {
			throw new MdxException("No style with id '" + topScrollButtonStyle + "' for buttons. Required by " + ScrollBoxStyleRule.class.getSimpleName());
		}
		if(bottomScrollButtonStyle != null && !theme.containsButtonStyleRuleset(bottomScrollButtonStyle)) {
			throw new MdxException("No style with id '" + bottomScrollButtonStyle + "' for buttons. Required by " + ScrollBoxStyleRule.class.getSimpleName());
		}
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		scrollTrackRenderer = BackgroundRenderer.parse(scrollTrack);
		scrollTrackRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		scrollThumbNormalRenderer = BackgroundRenderer.parse(scrollThumbNormal);
		scrollThumbNormalRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		scrollThumbHoverRenderer = BackgroundRenderer.parse(scrollThumbHover);
		scrollThumbHoverRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		scrollThumbActiveRenderer = BackgroundRenderer.parse(scrollThumbAction);
		scrollThumbActiveRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
	}

	public String getTopScrollButtonStyle() {
		return topScrollButtonStyle;
	}

	public void setTopScrollButtonStyle(String scrollButtonStyle) {
		this.topScrollButtonStyle = scrollButtonStyle;
	}

	public String getBottomScrollButtonStyle() {
		return bottomScrollButtonStyle;
	}

	public void setBottomScrollButtonStyle(String bottomScrollButtonStyle) {
		this.bottomScrollButtonStyle = bottomScrollButtonStyle;
	}

	public int getScrollBarWidth() {
		return scrollBarWidth;
	}

	public void setScrollBarWidth(int scrollBarSize) {
		this.scrollBarWidth = scrollBarSize;
	}

	public int getScrollButtonHeight() {
		return scrollButtonHeight;
	}

	public void setScrollButtonHeight(int scrollButtonHeight) {
		this.scrollButtonHeight = scrollButtonHeight;
	}

	public String getScrollTrack() {
		return scrollTrack;
	}

	public void setScrollTrack(String scrollTrack) {
		this.scrollTrack = scrollTrack;
	}

	public String getScrollThumbNormal() {
		return scrollThumbNormal;
	}

	public void setScrollThumbNormal(String scrollThumbNormal) {
		this.scrollThumbNormal = scrollThumbNormal;
	}

	public String getScrollThumbHover() {
		return scrollThumbHover;
	}

	public void setScrollThumbHover(String scrollThumbHover) {
		this.scrollThumbHover = scrollThumbHover;
	}

	public String getScrollThumbAction() {
		return scrollThumbAction;
	}

	public void setScrollThumbAction(String scrollThumbAction) {
		this.scrollThumbAction = scrollThumbAction;
	}

	public BackgroundRenderer getScrollTrackRenderer() {
		return scrollTrackRenderer;
	}

	public BackgroundRenderer getScrollThumbNormalRenderer() {
		return scrollThumbNormalRenderer;
	}

	public BackgroundRenderer getScrollThumbHoverRenderer() {
		return scrollThumbHoverRenderer;
	}

	public BackgroundRenderer getScrollThumbActiveRenderer() {
		return scrollThumbActiveRenderer;
	}
}
