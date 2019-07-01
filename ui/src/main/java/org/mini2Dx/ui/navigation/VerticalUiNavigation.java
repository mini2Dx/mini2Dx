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
