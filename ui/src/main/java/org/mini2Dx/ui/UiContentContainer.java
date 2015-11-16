/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.geom.CollisionBox;

/**
 *
 */
public interface UiContentContainer extends PositionChangeListener<CollisionBox> {
	public int getRenderX();

	public int getRenderY();

	public int getRenderWidth();

	public int getRenderHeight();
	
	public float getContentWidth();
	
	public float getContentHeight();
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
}
