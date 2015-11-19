/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

/**
 *
 */
public interface TextInputable extends Actionable {
	
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button);

	public void characterReceived(char c);
	
	public void backspace();
	
	public boolean enter();
	
	public void moveCursorRight();
	
	public void moveCursorLeft();
	
	public void cut();
	
	public void copy();
	
	public void paste();
}
