/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui;

import org.mini2Dx.ui.listener.ContentPositionListener;

/**
 *
 */
public interface UiContentContainer extends ContentPositionListener {
	public float getX();
	
	public float getY();
	
	public float getWidth();
	
	public float getHeight();
	
	public int getRenderX();

	public int getRenderY();

	public int getRenderWidth();

	public int getRenderHeight();
	
	public float getContentWidth();
	
	public float getContentHeight();
	
	public int getPaddingTop();
	
	public int getPaddingBottom();
	
	public int getPaddingLeft();
	
	public int getPaddingRight();
	
	public int getMarginTop();
	
	public int getMarginBottom();
	
	public int getMarginLeft();
	
	public int getMarginRight();
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
}
