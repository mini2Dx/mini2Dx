/**
 * Copyright 2016 Thomas Cashman
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
public class ScrollBoxStyleRule extends ColumnStyleRule {
	@Field(optional=true)
	private String scrollButtonStyle;
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
		
		if(scrollButtonStyle != null && !theme.containsButtonStyleRuleset(scrollButtonStyle)) {
			throw new MdxException("No style with id '" + scrollButtonStyle + "' for buttons. Required by " + ScrollBoxStyleRule.class.getSimpleName());
		}
	}

	@Override
	public void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies) {
		super.loadDependencies(theme, dependencies);
		dependencies.add(new AssetDescriptor<Texture>(scrollTrack, Texture.class));
		dependencies.add(new AssetDescriptor<Texture>(scrollThumbNormal, Texture.class));
		dependencies.add(new AssetDescriptor<Texture>(scrollThumbHover, Texture.class));
		dependencies.add(new AssetDescriptor<Texture>(scrollThumbAction, Texture.class));
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		System.out.println(scrollTrackNinePatchLeft + " " + scrollTrackNinePatchRight + " " + scrollTrackNinePatchTop + " " + scrollTrackNinePatchBottom);
		scrollTrackNinePatch = new NinePatch(assetManager.get(scrollTrack, Texture.class), scrollTrackNinePatchLeft,
				scrollTrackNinePatchRight, scrollTrackNinePatchTop, scrollTrackNinePatchBottom);
		System.out.println(scrollTrackNinePatch.getLeftWidth() + " " + scrollTrackNinePatch.getRightWidth() + " " + scrollTrackNinePatch.getTopHeight() + " " + scrollTrackNinePatch.getBottomHeight());
		
		scrollThumbNormalNinePatch = new NinePatch(assetManager.get(scrollThumbNormal, Texture.class), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
		scrollThumbHoverNinePatch = new NinePatch(assetManager.get(scrollThumbHover, Texture.class), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
		scrollThumbActiveNinePatch = new NinePatch(assetManager.get(scrollThumbAction, Texture.class), scrollThumbNinePatchLeft,
				scrollThumbNinePatchRight, scrollThumbNinePatchTop, scrollThumbNinePatchBottom);
	}

	public String getScrollButtonStyle() {
		return scrollButtonStyle;
	}

	public void setScrollButtonStyle(String scrollButtonStyle) {
		this.scrollButtonStyle = scrollButtonStyle;
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
