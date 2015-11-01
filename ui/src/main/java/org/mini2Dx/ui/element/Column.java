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
package org.mini2Dx.ui.element;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.UiElementStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public abstract class Column<T extends UiElementStyle> extends BasicUiElement<T> {
	protected final List<Row> rows = new ArrayList<Row>(1);
	
	private float contentWidth, contentHeight;
	private boolean rowsAdded = false;
	
	@Override
	public void update(UiContainer uiContainer, float delta) {
		if(rowsAdded) {
			calculateContentDimensions();
			rowsAdded = false;
		}
		
		super.update(uiContainer, delta);
		
		boolean rowsRemoved = false;
		
		for(int i = 0; i < rows.size(); i++) {
			UiElement row = rows.get(i);
			if(row.disposed()) {
				rows.remove(i);
				currentArea.removePositionChangeListener(row);
				i--;
				rowsRemoved = true;
			} else {
				row.update(uiContainer, delta);
			}
		}
		
		if(!rowsRemoved) {
			return;
		}
		calculateContentDimensions();
	}
	
	@Override
	public void resize(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		super.resize(screenSize, theme, columnWidth, totalHeight);
		
		columnWidth = widthRule.getTargetSize() / theme.getColumns();
		
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).resize(screenSize, theme, columnWidth, heightRule.getTargetSize());
		}
	}
	
	@Override
	public UiElement getById(String id) {
		if (id.equals(getId())) {
			return this;
		}
		for(UiElement element : rows) {
			UiElement result = element.getById(id);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	public void addRow(Row row) {
		currentArea.addPostionChangeListener(row);
		rows.add(row);
		rowsAdded = true;
	}
	
	public void removeRow(Row row) {
		row.dispose();
	}

	@Override
	public void accept(UiRenderer renderer) {
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).accept(renderer);
		}
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).applyStyle(theme, screenSize);
		}
	}
	
	@Override
	public float getContentWidth() {
		return contentWidth;
	}

	@Override
	public float getContentHeight() {
		return contentHeight;
	}
	
	private void calculateContentDimensions() {
		float contentWidth = 0f;
		float contentHeight = 0f;
		
		for (Row row : rows) {
			contentWidth += row.getContentWidth();
			contentHeight += row.getContentHeight();
		}
		
		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
	}
}
