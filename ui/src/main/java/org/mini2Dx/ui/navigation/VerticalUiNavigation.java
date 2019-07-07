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
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Hoverable;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;

/**
 * A {@link UiNavigation} implementation that treats all {@link UiElement}s as
 * stacked vertically
 */
public class VerticalUiNavigation implements UiNavigation {
	private final Array<Actionable> navigation = new Array<Actionable>(true, 1, Actionable.class);
	private int cursor;
	
	@Override
	public void add(Actionable actionable) {
		actionable.addHoverListener(this);
		navigation.add(actionable);
	}

	@Override
	public void remove(Actionable actionable) {
		actionable.removeHoverListener(this);
		navigation.removeValue(actionable, false);
	}
	
	@Override
	public void removeAll() {
		for(int i = 0; i < navigation.size; i++) {
			navigation.get(i).removeHoverListener(this);
		}
		navigation.clear();
		resetCursor();
	}

	@Override
	public void set(int index, Actionable actionable) {
		actionable.addHoverListener(this);
		if (navigation.size > index) {
			navigation.set(index, actionable);
		} else {
			navigation.insert(index, actionable);
		}
	}

	@Override
	public Actionable navigate(int keycode) {
		if (navigation.size == 0) {
			return null;
		}
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
			navigation.get(cursor).invokeEndHover();
			cursor = cursor > 0 ? cursor - 1 : navigation.size - 1;
			break;
		case Keys.S:
		case Keys.DOWN:
			navigation.get(cursor).invokeEndHover();
			cursor = cursor < navigation.size - 1 ? cursor + 1 : 0;
			break;
		}
		return navigation.get(cursor);
	}

	private Actionable updateCursor(String hoverElementId) {
		for(int i = 0; i < navigation.size; i++) {
			if (navigation.get(i).getId().equals(hoverElementId)) {
				if(i != cursor) {
					navigation.get(cursor).invokeEndHover();
				}
				cursor = i;
				return navigation.get(i);
			}
		}
		return null;
	}

	@Override
	public Actionable resetCursor() {
		return resetCursor(false);
	}

	@Override
	public Actionable resetCursor(boolean triggerHoverEvent) {
		if(navigation.size > 0) {
			navigation.get(cursor).invokeEndHover();
		}
		cursor = 0;
		if(navigation.size == 0) {
			return null;
		}
		final Actionable result = navigation.get(cursor);
		if(result == null) {
			return null;
		}
		if(triggerHoverEvent) {
			result.invokeBeginHover();
		}
		return result;
	}

	@Override
	public Actionable getCursor() {
		if(navigation.size == 0) {
			return null;
		}
		return navigation.get(cursor);
	}

	@Override
	public void layout(ScreenSize screenSize) {
	}

	@Override
	public void onHoverBegin(Hoverable source) {
		updateCursor(source.getId());
	}

	@Override
	public void onHoverEnd(Hoverable source) {
	}
}
