/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.UiElement;

/**
 *
 * @author Thomas Cashman
 */
public class LayoutRow implements UiElement {
	private Rectangle dimensions;
	private List<LayoutColumn> columns;
	
	public LayoutRow() {
		dimensions = new Rectangle(-1f, -1f, 0f, 0f);
		columns = new ArrayList<LayoutColumn>();
	}
	
	@Override
	public void update(GameContainer gc, float delta) {
		for(LayoutColumn column : columns) {
			column.update(gc, delta);
		}
	}
	
	@Override
	public void interpolate(GameContainer gc, float alpha) {
		for(LayoutColumn column : columns) {
			column.interpolate(gc, alpha);
		}
	}
	
	@Override
	public void render(Graphics g) {
		for(LayoutColumn column : columns) {
			column.render(g);
		}
	}

	@Override
	public ColumnSize getSize(DeviceSize deviceSize) {
		switch(deviceSize) {
		case XL:
			return ColumnSize.XL_12;
		case L:
			return ColumnSize.L_12;
		case M:
			return ColumnSize.M_12;
		case S:
			return ColumnSize.S_12;
		default:
			return ColumnSize.XS_12;
		}
	}

	@Override
	public ColumnSize getOffset(DeviceSize deviceSize) {
		switch(deviceSize) {
		case XL:
			return ColumnSize.XL_0;
		case L:
			return ColumnSize.L_0;
		case M:
			return ColumnSize.M_0;
		case S:
			return ColumnSize.S_0;
		default:
			return ColumnSize.XS_0;
		}
	}

	@Override
	public float getX() {
		return dimensions.getX();
	}

	@Override
	public float getY() {
		return dimensions.getY();
	}

	@Override
	public float getWidth() {
		return dimensions.getWidth();
	}

	@Override
	public float getHeight() {
		return dimensions.getHeight();
	}
}
