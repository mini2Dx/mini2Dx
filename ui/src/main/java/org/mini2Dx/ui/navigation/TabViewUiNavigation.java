/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.ui.navigation;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Hoverable;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.layout.ScreenSize;

/**
 *
 */
public class TabViewUiNavigation implements UiNavigation {
	private final TabView tabView;
	private final Array<Tab> tabs;

	public TabViewUiNavigation(TabView tabView, Array<Tab> tabs) {
		this.tabView = tabView;
		this.tabs = tabs;
	}

	@Override
	public void layout(ScreenSize screenSize) {
		for(int i = 0; i < tabs.size; i++) {
			UiNavigation navigation = tabs.get(i).getNavigation();
			if(navigation == null) {
				continue;
			}
			navigation.layout(screenSize);
		}
	}
	
	@Override
	public Actionable getCursor() {
		if(tabs.size == 0) {
			return null;
		}
		UiNavigation navigation = tabs.get(tabView.getCurrentTabIndex()).getNavigation();
		if(navigation == null) {
			return null;
		}
		return navigation.getCursor();
	}

	@Override
	public Actionable resetCursor() {
		return resetCursor(false);
	}

	@Override
	public Actionable resetCursor(boolean triggerHoverEvent) {
		if(tabs.size == 0) {
			return null;
		}
		UiNavigation navigation = tabs.get(tabView.getCurrentTabIndex()).getNavigation();
		if(navigation == null) {
			return null;
		}
		final Actionable result = navigation.resetCursor();
		if(result == null) {
			return null;
		}
		if(triggerHoverEvent) {
			result.invokeBeginHover();
		}
		return result;
	}

	@Override
	public void add(Actionable actionable) {
		throw new MdxException("UiNavigation#add() not supported by " + TabViewUiNavigation.class.getSimpleName());
	}

	@Override
	public void remove(Actionable actionable) {
		throw new MdxException("UiNavigation#remove() not supported by " + TabViewUiNavigation.class.getSimpleName());
	}
	
	@Override
	public void removeAll() {
		throw new MdxException("UiNavigation#removeAll() not supported by " + TabViewUiNavigation.class.getSimpleName());
	}

	@Override
	public void set(int index, Actionable actionable) {
		throw new MdxException("UiNavigation#set() not supported by " + TabViewUiNavigation.class.getSimpleName());
	}

	@Override
	public Actionable navigate(int keycode) {
		if(tabs.size == 0) {
			return null;
		}
		UiNavigation navigation = tabs.get(tabView.getCurrentTabIndex()).getNavigation();
		if(navigation == null) {
			return null;
		}
		return navigation.navigate(keycode);
	}

	@Override
	public void onHoverBegin(Hoverable source) {
	}

	@Override
	public void onHoverEnd(Hoverable source) {
	}
}
