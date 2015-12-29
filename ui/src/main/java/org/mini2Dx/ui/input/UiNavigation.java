/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.input;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.layout.ScreenSize;

/**
 *
 */
public interface UiNavigation {
	
	public void layout(ScreenSize screenSize);

	public void resetCursor();
	
	public void set(int index, Actionable actionable);
	
	public Actionable navigate(int keycode);
}
