/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.input;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.layout.ScreenSize;

import com.badlogic.gdx.Input.Keys;

/**
 *
 */
public class VerticalUiNavigation implements UiNavigation {
	private final List<Actionable> navigation = new ArrayList<Actionable>();
	private int cursor;

	@Override
	public void set(int index, Actionable actionable) {
		if(navigation.size() > index) {
			navigation.set(index, actionable);
		} else {
			navigation.add(index, actionable);
		}
	}

	@Override
	public Actionable navigate(int keycode) {
		switch (keycode) {
		case Keys.UP:
			cursor = cursor > 0 ? cursor - 1 : navigation.size() - 1;
			break;
		case Keys.DOWN:
			cursor = cursor < navigation.size() - 1 ? cursor + 1 : 0;
			break;
		}
		return navigation.get(cursor);
	}

	@Override
	public void resetCursor() {
		cursor = 0;
	}

	@Override
	public void layout(ScreenSize screenSize) {}
}
