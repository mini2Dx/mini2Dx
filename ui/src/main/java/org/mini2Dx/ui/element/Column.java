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

import org.mini2Dx.ui.UiContentContainer;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ContentSizeListener;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.UiElementStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public abstract class Column<T extends UiElementStyle> extends BasicUiElement<T>implements ContentSizeListener {
	protected final List<Row> rows = new ArrayList<Row>(1);

	private float contentWidth, contentHeight;
	private boolean rowAdded;

	@Override
	public void update(UiContentContainer uiContainer, float delta) {
		if (rowAdded) {
			notifyContentSizeListeners();
			rowAdded = false;
		}

		super.update(uiContainer, delta);

		boolean rowsRemoved = false;

		for (int i = 0; i < rows.size(); i++) {
			UiElement<?> row = rows.get(i);
			if (row.disposed()) {
				rows.remove(i);
				removeContentPositionListener(row);
				i--;
				rowsRemoved = true;
			} else {
				row.update(this, delta);
			}
		}

		if (!rowsRemoved) {
			return;
		}
		calculateContentDimensions();
	}

	@Override
	public void interpolate(UiContentContainer uiContainer, float alpha) {
		super.interpolate(uiContainer, alpha);
		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).interpolate(this, alpha);
		}
	}

	@Override
	public void resize(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		applyStyle(theme, screenSize);
		applyRules(screenSize, theme, columnWidth, totalHeight);
		notifyRules(screenSize, theme, columnWidth, totalHeight);

		columnWidth = ((widthRule.getTargetSize() - getPaddingLeft() - getPaddingRight()) / theme.getColumns());

		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).resize(screenSize, theme, columnWidth, heightRule.getTargetSize());
		}

		rulesChanged = true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(currentArea.contains(screenX, screenY)) {
			setState(ElementState.HOVER);
			boolean result = false;
			for(int i = rows.size() - 1; i >= 0; i--) {
				if(rows.get(i).mouseMoved(screenX, screenY)) {
					result = true;
				}
			}
			return result;
		} else if(getState() != ElementState.NORMAL) {
			setState(ElementState.NORMAL);
		}
		return false;
	}

	@Override
	public UiElement<?> getById(String id) {
		if (id.equals(getId())) {
			return this;
		}
		for (UiElement<?> element : rows) {
			UiElement<?> result = element.getById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	@Override
	public Actionable mouseDown(int screenX, int screenY, int pointer, int button) {
		if(!visible) {
			return null;
		}
		for(int i = rows.size() - 1; i >= 0; i--) {
			Actionable result = rows.get(i).mouseDown(screenX, screenY, pointer, button);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

	public void addRow(Row row) {
		row.addContentSizeListener(this);
		addContentPositionListener(row);
		rows.add(row);

		contentWidth = Math.max(contentWidth, row.getContentWidth());
		contentHeight += row.getContentHeight();
		rowAdded = true;
	}

	public void removeRow(Row row) {
		removeContentPositionListener(row);
		row.removeContentSizeListener(this);
		row.dispose();
	}

	@Override
	public void accept(UiRenderer renderer) {
		if (!visible) {
			return;
		}
		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).accept(renderer);
		}
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).applyStyle(theme, screenSize);
		}
	}

	private void calculateContentDimensions() {
		float contentWidth = 0f;
		float contentHeight = 0f;

		for (Row row : rows) {
			row.setRowYOffset(contentHeight);
			contentWidth = Math.max(contentWidth, row.getElementWidth());
			contentHeight += row.getElementHeight();
		}

		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
	}

	@Override
	public void onContentSizeChanged(UiElement<?> element) {
		calculateContentDimensions();
		notifyContentSizeListeners();
	}

	@Override
	public float getContentWidth() {
		return contentWidth;
	}

	@Override
	public float getContentHeight() {
		return contentHeight;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).setVisible(visible);
		}
	}
	
	@Override
	public void setState(ElementState state) {
		super.setState(state);
		if(state != ElementState.NORMAL) {
			return;
		}
		for(int i = rows.size() -1; i >= 0; i--) {
			rows.get(i).setState(ElementState.NORMAL);
		}
	}
}
