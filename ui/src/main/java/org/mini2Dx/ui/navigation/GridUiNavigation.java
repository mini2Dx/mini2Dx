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
package org.mini2Dx.ui.navigation;

import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Hoverable;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;

import java.util.Iterator;

/**
 * A {@link UiNavigation} implementation that treats all {@link UiElement}s as
 * placed inside a grid
 */
public class GridUiNavigation implements UiNavigation {
	private final static int MOVE_UP = -1;
	private final static int MOVE_DOWN = 1;
	private final static int MOVE_LEFT = -1;
	private final static int MOVE_RIGHT = 1;

	private final Array<Actionable> navigation = new Array<Actionable>(true, 1, Actionable.class);
	private final ObjectMap<ScreenSize, Integer> screenSizeColumns = new ObjectMap<ScreenSize, Integer>();

	private int columns;
	private int cursorX, cursorY;

	private boolean triedMovingOnEdge = false;

	/**
	 * Constructor
	 * @param xsColumns The amount of columns of the grid on a {@link ScreenSize#XS} screen
	 */
	public GridUiNavigation(int xsColumns) {
		screenSizeColumns.put(ScreenSize.XS, xsColumns);
		columns = xsColumns;
	}
	
	private boolean isCursorResetRequired() {
		if(cursorX >= columns) {
			return true;
		}
		if(cursorY >= getTotalRows()) {
			return true;
		}
		return false;
	}
	
	int getIndex(int x, int y) {
		return (y * columns) + x;
	}

	private int getX(int index) {
		return index % columns;
	}

	private int getY(int index) {
		return (index - (index % columns)) / columns;
	}

	@Override
	public void layout(ScreenSize screenSize) {
		final Array<ScreenSize> largestToSmallestScreenSizes = ScreenSize.sharedLargestToSmallest();
		for(int i = 0; i < largestToSmallestScreenSizes.size; i++) {
			final ScreenSize nextSize = largestToSmallestScreenSizes.get(i);
			if (nextSize.getMinSize(1f) > screenSize.getMinSize(1f)) {
				continue;
			}
			if (!screenSizeColumns.containsKey(nextSize)) {
				continue;
			}
			columns = screenSizeColumns.get(nextSize);
			break;
		}
		
		if(!isCursorResetRequired()) {
			return;
		}
		resetCursor();
	}

	@Override
	public void add(Actionable actionable) {
		actionable.addHoverListener(this);
		navigation.add(actionable);
	}

	@Override
	public void remove(Actionable actionable) {
		actionable.removeHoverListener(this);
		navigation.removeValue(actionable, true);
	}

	@Override
	public void removeAll() {
		for(int i = 0; i < navigation.size; i++) {
			if (navigation.get(i) == null){
				continue;
			}
			navigation.get(i).removeHoverListener(this);
		}
		navigation.clear();
		resetCursor();
	}

	@Override
	public void set(int index, Actionable actionable) {
		if (actionable != null) {
			actionable.addHoverListener(this);
		}
		if (index < navigation.size && navigation.get(index) != null){
			navigation.get(index).removeHoverListener(this);
		}
		while(navigation.size <= index) {
			navigation.add(null);
		}
		navigation.set(index, actionable);
		triedMovingOnEdge = false;
	}
	
	public Actionable get(int x, int y) {
		return navigation.get(getIndex(x, y));
	}

	/**
	 * Replaces the {@link Actionable} at the specified grid coordinate
	 * @param x The grid x coordinate
	 * @param y The grid y coordinate
	 * @param actionable The new {@link Actionable}
	 */
	public void set(int x, int y, Actionable actionable) {
		set(getIndex(x, y), actionable);
	}

	@Override
	public Actionable navigate(int keycode) {
		if (navigation.size == 0) {
			return null;
		}
		boolean invokeEndHover = false;
		Actionable previousElement = navigation.get(getIndex(cursorX, cursorY));
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
			if (cursorY > 0) {
				invokeEndHover = true;
				moveVerticalToNextNoneNullElement(MOVE_UP);
			}
			break;
		case Keys.S:
		case Keys.DOWN:
			if (getIndex(cursorX, cursorY + 1) >= navigation.size){
				invokeEndHover = true;
			} else if (cursorY < getTotalRows() - 1) {
				invokeEndHover = true;
				moveVerticalToNextNoneNullElement(MOVE_DOWN);
			} else {
				invokeEndHover = true;
				cursorY = getTotalRows() - 1;
			}
			break;
		case Keys.A:
		case Keys.LEFT:
			if (cursorX > 0) {
				invokeEndHover = true;
				moveHorizontalToNextNoneNullElement(MOVE_LEFT);
			}
			break;
		case Keys.D:
		case Keys.RIGHT:
			if (getIndex(cursorX + 1, cursorY) >= navigation.size) {
				invokeEndHover = true;
			}else if (cursorX < columns - 1) {
				invokeEndHover = true;
				moveHorizontalToNextNoneNullElement(MOVE_RIGHT);
			} else {
				invokeEndHover = true;
				cursorX = columns - 1;
			}
			break;
		}
		if (invokeEndHover && previousElement != null){
			previousElement.invokeEndHover();
		}

		Actionable newElement = navigation.get(getIndex(cursorX, cursorY));

		if (previousElement == newElement && (keycode == Keys.A || keycode == Keys.D || keycode == Keys.LEFT || keycode == Keys.RIGHT)){
			triedMovingOnEdge = true;
		} else {
			triedMovingOnEdge = false;
		}
		return newElement;
	}

	Actionable updateCursor(String hoverElementId) {
		for(int i = 0; i < navigation.size; i++) {
			if (navigation.get(i) ==  null){
				continue;
			}
			if(navigation.get(i).getId().equals(hoverElementId)) {
				final int previousIndex = getIndex(cursorX, cursorY);
				if(i != previousIndex && navigation.get(previousIndex) != null) {
					navigation.get(previousIndex).invokeEndHover();
				}

				cursorX = getX(i);
				cursorY = getY(i);
				return navigation.get(i);
			}
		}
		return null;
	}

	@Override
	public Actionable getCursor() {
		if(navigation.size == 0) {
			return null;
		}
		return navigation.get(getIndex(cursorX, cursorY));
	}

	@Override
	public Actionable resetCursor() {
		return resetCursor(false);
	}

	@Override
	public Actionable resetCursor(boolean triggerHoverEvent) {
		if(navigation.size > 0) {
			navigation.get(getIndex(cursorX, cursorY)).invokeEndHover();
		}

		cursorX = 0;
		cursorY = 0;
		triedMovingOnEdge = false;
		if(navigation.size == 0) {
			return null;
		}
		final Actionable result = navigation.get(getIndex(cursorX, cursorY));
		if(result == null) {
			return null;
		}
		if(triggerHoverEvent) {
			result.invokeBeginHover();
		}
		return result;
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
		int rows = navigation.size / columns;
		if (navigation.size % columns != 0) {
			rows++;
		}
		return rows;
	}

	@Override
	public String toString() {
		return "GridUiNavigation [navigation=" + navigation + "]";
	}

	@Override
	public void onHoverBegin(Hoverable source) {
		updateCursor(source.getId());
	}

	@Override
	public void onHoverEnd(Hoverable source) {
	}

	private void moveVerticalToNextNoneNullElement(int direction) {
		int tempCursorY = cursorY;
		while ((cursorY + direction) >= 0 && getIndex(cursorX, cursorY + direction) < navigation.size && (cursorY + direction) < getTotalRows()){
			cursorY += direction;
			if (navigation.get(getIndex(cursorX, cursorY)) != null){
				return;
			}
		}
		cursorY = tempCursorY;
	}

	private void moveHorizontalToNextNoneNullElement(int direction) {
		int tempCursorX = cursorX;
		while ((cursorX + direction) >= 0 && (cursorX + direction) < getTotalColumns()){
			cursorX += direction;
			//after moving in a direction, try moving up/down to closest none null element
			if (navigation.get(getIndex(cursorX, cursorY)) == null){
				if (findClosestItemVertically()){
					return;
				}
			}
			if (navigation.get(getIndex(cursorX, cursorY)) != null){
				return;
			}
		}
		cursorX = tempCursorX;
	}

	private boolean findClosestItemVertically() {
		int tempY = cursorY;
		int direction = 1;
		while (tempY + direction < getTotalRows() || tempY - direction >= 0){
			if (tempY + direction < getTotalRows() && getIndex(cursorX, tempY + direction) < navigation.size && navigation.get(getIndex(cursorX, tempY + direction)) != null){
				cursorY = tempY + direction;
				return true;
			}
			if (tempY - direction >= 0 && navigation.get(getIndex(cursorX, tempY - direction)) != null){
				cursorY = tempY - direction;
				return true;
			}
			direction += 1;
		}
		return false;
	}

	int getNavigationSize(){
		return navigation.size;
	}

	public boolean isTriedMovingOnEdge() {
		return triedMovingOnEdge;
	}

	public void setTriedMovingOnEdge(boolean triedMovingOnEdge) {
		this.triedMovingOnEdge = triedMovingOnEdge;
	}

	public int getCursorX(){
		return cursorX;
	}

	public int getCursorY(){
		return cursorY;
	}
}