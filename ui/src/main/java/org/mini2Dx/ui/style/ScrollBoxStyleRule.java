/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.ScrollBox;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

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
	private int scrollTrackNinePatchTop, scrollTrackNinePatchBottom, scrollTrackNinePatchLeft, scrollTrackNinePatchRight;
	@Field
	private String scrollThumbNormal;
	@Field
	private String scrollThumbHover;
	@Field
	private String scrollThumbAction;
	@Field
	private int scrollThumbNinePatchTop, scrollThumbNinePatchBottom, scrollThumbNinePatchLeft, scrollThumbNinePatchRight;
	
	private NinePatch scrollTrackNinePatch, scrollThumbNormalNinePatch, scrollThumbHoverNinePatch, scrollThumbActiveNinePatch;
	
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
		scrollTrackNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(scrollTrack)), scrollTrackNinePatchLeft,
				scrollTrackNinePatchRight, scrollTrackNinePatchTop, scrollTrackNinePatchBottom);
		
		scrollThumbNormalNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(scrollThumbNormal)), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
		scrollThumbHoverNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(scrollThumbHover)), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
		scrollThumbActiveNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(scrollThumbAction)), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
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

	public int getScrollTrackNinePatchTop() {
		return scrollTrackNinePatchTop;
	}

	public void setScrollTrackNinePatchTop(int scrollTrackNinePatchTop) {
		this.scrollTrackNinePatchTop = scrollTrackNinePatchTop;
	}

	public int getScrollTrackNinePatchBottom() {
		return scrollTrackNinePatchBottom;
	}

	public void setScrollTrackNinePatchBottom(int scrollTrackNinePatchBottom) {
		this.scrollTrackNinePatchBottom = scrollTrackNinePatchBottom;
	}

	public int getScrollTrackNinePatchLeft() {
		return scrollTrackNinePatchLeft;
	}

	public void setScrollTrackNinePatchLeft(int scrollTrackNinePatchLeft) {
		this.scrollTrackNinePatchLeft = scrollTrackNinePatchLeft;
	}

	public int getScrollTrackNinePatchRight() {
		return scrollTrackNinePatchRight;
	}

	public void setScrollTrackNinePatchRight(int scrollTrackNinePatchRight) {
		this.scrollTrackNinePatchRight = scrollTrackNinePatchRight;
	}

	public int getScrollThumbNinePatchTop() {
		return scrollThumbNinePatchTop;
	}

	public void setScrollThumbNinePatchTop(int scrollThumbNinePatchTop) {
		this.scrollThumbNinePatchTop = scrollThumbNinePatchTop;
	}

	public int getScrollThumbNinePatchBottom() {
		return scrollThumbNinePatchBottom;
	}

	public void setScrollThumbNinePatchBottom(int scrollThumbNinePatchBottom) {
		this.scrollThumbNinePatchBottom = scrollThumbNinePatchBottom;
	}

	public int getScrollThumbNinePatchLeft() {
		return scrollThumbNinePatchLeft;
	}

	public void setScrollThumbNinePatchLeft(int scrollThumbNinePatchLeft) {
		this.scrollThumbNinePatchLeft = scrollThumbNinePatchLeft;
	}

	public int getScrollThumbNinePatchRight() {
		return scrollThumbNinePatchRight;
	}

	public void setScrollThumbNinePatchRight(int scrollThumbNinePatchRight) {
		this.scrollThumbNinePatchRight = scrollThumbNinePatchRight;
	}

	public NinePatch getScrollTrackNinePatch() {
		return scrollTrackNinePatch;
	}

	public NinePatch getScrollThumbNormalNinePatch() {
		return scrollThumbNormalNinePatch;
	}

	public NinePatch getScrollThumbHoverNinePatch() {
		return scrollThumbHoverNinePatch;
	}

	public NinePatch getScrollThumbActiveNinePatch() {
		return scrollThumbActiveNinePatch;
	}

	
}
