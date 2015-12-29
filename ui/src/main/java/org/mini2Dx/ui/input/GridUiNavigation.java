/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.layout.ScreenSize;

import com.badlogic.gdx.Input.Keys;

/**
 *
 */
public class GridUiNavigation implements UiNavigation {
	private final List<Actionable> navigation = new ArrayList<Actionable>();
	private final Map<ScreenSize, Integer> widths = new HashMap<ScreenSize, Integer>();
	
	private int width;
	private int cursorX, cursorY;
	
	public GridUiNavigation(int xsWidth) {
		widths.put(ScreenSize.XS, xsWidth);
	}
	
	@Override
	public void layout(ScreenSize screenSize) {
		Iterator<ScreenSize> screenSizes = ScreenSize.largestToSmallest();
		while(screenSizes.hasNext()) {
			ScreenSize nextSize = screenSizes.next();
			if(nextSize.getMinSize() > screenSize.getMinSize()) {
				continue;
			}
			if(!widths.containsKey(nextSize)) {
				continue;
			}
			width = widths.get(nextSize);
			break;
		}
		resetCursor();
	}

	@Override
	public void set(int index, Actionable actionable) {
		navigation.set(index, actionable);
	}
	
	public void set(int x, int y, Actionable actionable) {
		navigation.set((y * width) + x, actionable);
	}

	@Override
	public Actionable navigate(int keycode) {
		switch(keycode) {
		case Keys.UP:
			if(cursorY > 0) {
				cursorY--;
			}
			break;
		case Keys.DOWN:
			if(cursorY < getTotalRows() - 1) {
				cursorY++;
			} else {
				cursorY = getTotalRows() - 1;
			}
			break;
		case Keys.LEFT:
			if(cursorX > 0) {
				cursorX--;
			}
			break;
		case Keys.RIGHT:
			if(cursorX < width - 1) {
				cursorX++;
			} else {
				cursorX = width - 1;
			}
			break;
		}
		return navigation.get((cursorY * width) + cursorX);
	}

	@Override
	public void resetCursor() {
		cursorX = 0;
		cursorY = 0;
	}
	
	public void setWidth(ScreenSize screenSize, int width) {
		if(screenSize == null) {
			return;
		}
		widths.put(screenSize, width);
	}
	
	public int getTotalColumns() {
		return width;
	}
	
	public int getTotalRows() {
		int rows = navigation.size() / width;
		if(navigation.size() % width != 0) {
			rows++;
		}
		return rows;
	}
}