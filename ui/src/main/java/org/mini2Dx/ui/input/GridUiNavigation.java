/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;

import com.badlogic.gdx.Input.Keys;

/**
 * A {@link UiNavigation} implementation that treats all {@link UiElement}s as
 * placed inside a grid
 */
public class GridUiNavigation implements UiNavigation {
	private final List<Actionable> navigation = new ArrayList<Actionable>();
	private final Map<ScreenSize, Integer> screenSizeColumns = new HashMap<ScreenSize, Integer>();

	private int columns;
	private int cursorX, cursorY;

	/**
	 * Constructor
	 * @param xsColumns The amount of columns of the grid on a {@link ScreenSize#XS} screen
	 */
	public GridUiNavigation(int xsColumns) {
		screenSizeColumns.put(ScreenSize.XS, xsColumns);
		columns = xsColumns;
	}

	@Override
	public void layout(ScreenSize screenSize) {
		Iterator<ScreenSize> screenSizes = ScreenSize.largestToSmallest();
		while (screenSizes.hasNext()) {
			ScreenSize nextSize = screenSizes.next();
			if (nextSize.getMinSize() > screenSize.getMinSize()) {
				continue;
			}
			if (!screenSizeColumns.containsKey(nextSize)) {
				continue;
			}
			columns = screenSizeColumns.get(nextSize);
			break;
		}
		resetCursor();
	}

	@Override
	public void add(Actionable actionable) {
		navigation.add(actionable);
	}

	@Override
	public void remove(Actionable actionable) {
		navigation.remove(actionable);
	}
	
	@Override
	public void set(int index, Actionable actionable) {
		if (navigation.size() > index) {
			navigation.set(index, actionable);
		} else {
			navigation.add(index, actionable);
		}
	}

	/**
	 * Replaces the {@link Actionable} at the specified grid coordinate
	 * @param x The grid x coordinate
	 * @param y The grid y coordinate
	 * @param actionable The new {@link Actionable}
	 */
	public void set(int x, int y, Actionable actionable) {
		set((y * columns) + x, actionable);
	}

	@Override
	public Actionable navigate(int keycode) {
		if (navigation.size() == 0) {
			return null;
		}
		switch (keycode) {
		case Keys.UP:
			if (cursorY > 0) {
				cursorY--;
			}
			break;
		case Keys.DOWN:
			if (cursorY < getTotalRows() - 1) {
				cursorY++;
			} else {
				cursorY = getTotalRows() - 1;
			}
			break;
		case Keys.LEFT:
			if (cursorX > 0) {
				cursorX--;
			}
			break;
		case Keys.RIGHT:
			if (cursorX < columns - 1) {
				cursorX++;
			} else {
				cursorX = columns - 1;
			}
			break;
		}
		return navigation.get((cursorY * columns) + cursorX);
	}

	@Override
	public Actionable resetCursor() {
		cursorX = 0;
		cursorY = 0;
		return navigation.get((cursorY * columns) + cursorX);
	}

	/**
	 * Sets the amount columns of the grid on a specific {@link ScreenSize}
	 * @param screenSize The {@link ScreenSize}
	 * @param columns The amount of columns in the grid
	 */
	public void setWidth(ScreenSize screenSize, int columns) {
		if (screenSize == null) {
			return;
		}
		screenSizeColumns.put(screenSize, columns);
	}

	/**
	 * Returns the total amount of columns for the current {@link ScreenSize}
	 * @return
	 */
	public int getTotalColumns() {
		return columns;
	}

	/**
	 * Returns the total amount of rows for the current {@link ScreenSize}
	 * @return
	 */
	public int getTotalRows() {
		int rows = navigation.size() / columns;
		if (navigation.size() % columns != 0) {
			rows++;
		}
		return rows;
	}

	@Override
	public String toString() {
		return "GridUiNavigation [navigation=" + navigation + "]";
	}
}