/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

/**
 *
 */
public interface TextInputable extends Actionable {
	
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button);

	public void onCharacterReceived(char c);
	
	public void onBackspace();
	
	public boolean onEnter();
	
	public void cut();
	
	public void copy();
	
	public void paste();
}
