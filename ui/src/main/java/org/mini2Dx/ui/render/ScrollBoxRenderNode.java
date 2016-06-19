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
package org.mini2Dx.ui.render;

import java.util.NavigableSet;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ScrollBoxStyleRule;

/**
 *
 */
public class ScrollBoxRenderNode extends AbstractColumnRenderNode<ScrollBoxStyleRule>implements ActionableRenderNode {
	private final CollisionBox scrollThumb = new CollisionBox();
	private final CollisionBox scrollTrack = new CollisionBox();

	private float contentHeight;
	private float scrollThumbPosition;
	private NodeState scrollThumbState = NodeState.NORMAL;
	
	private float thumbDragStartY;

	public ScrollBoxRenderNode(ParentRenderNode<?, ?> parent, ScrollBox row) {
		super(parent, row);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		scrollTrack.preUpdate();
		scrollThumb.preUpdate();
		super.update(uiContainer, delta);
		scrollTrack.set(innerArea.getX() + innerArea.getWidth() - style.getScrollBarSize(), innerArea.getY());
		scrollThumb.set(scrollTrack.getX(), innerArea.getY() + (scrollThumbPosition * scrollTrack.getHeight()));
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		scrollTrack.interpolate(null, alpha);
		scrollThumb.interpolate(null, alpha);
	}

	@Override
	protected void renderElement(Graphics g) {
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
		float scrollTranslation = scrollThumbPosition * contentHeight;
		g.translate(0f, scrollTranslation);

		Rectangle existingClip = g.removeClip();
		g.setClip(getInnerRenderX(), getInnerRenderY() + scrollTranslation, getInnerRenderWidth(),
				getInnerRenderHeight());

		for (RenderLayer layer : layers.values()) {
			layer.render(g);
		}

		if (existingClip != null) {
			g.setClip(existingClip);
		} else {
			g.removeClip();
		}
		g.translate(0f, -scrollTranslation);

		NinePatch scrollTrackPatch = style.getScrollTrackNinePatch();
		g.drawNinePatch(scrollTrackPatch, scrollTrack.getRenderX(), scrollTrack.getRenderY(),
				scrollTrack.getRenderWidth(), scrollTrack.getRenderHeight());

		switch (scrollThumbState) {
		case ACTION:
			g.drawNinePatch(style.getScrollThumbActiveNinePatch(), scrollThumb.getRenderX(), scrollThumb.getRenderY(),
					scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
			break;
		case HOVER:
			g.drawNinePatch(style.getScrollThumbHoverNinePatch(), scrollThumb.getRenderX(), scrollThumb.getRenderY(),
					scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
			break;
		default:
			g.drawNinePatch(style.getScrollThumbNormalNinePatch(), scrollThumb.getRenderX(), scrollThumb.getRenderY(),
					scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
			break;
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		switch (scrollThumbState) {
		case ACTION:
			float yDiff = screenY - thumbDragStartY;
			float maxPosition = (innerArea.getHeight() - scrollThumb.getHeight()) / innerArea.getHeight();
			scrollThumbPosition = ((scrollThumb.getY() + yDiff) - innerArea.getY()) / innerArea.getHeight();
			if(scrollThumbPosition < 0f) {
				scrollThumbPosition = 0f;
			} else if(scrollThumbPosition > maxPosition) {
				scrollThumbPosition = maxPosition;
			}
			System.out.println(scrollThumbPosition);
			return true;
		case HOVER:
		case NORMAL:
		default:
			if (outerArea.contains(screenX, screenY)) {
				setState(NodeState.HOVER);
				if (scrollThumb.contains(screenX, screenY)) {
					scrollThumbState = NodeState.HOVER;
				} else {
					scrollThumbState = NodeState.NORMAL;
				}
				boolean result = false;
				NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
				for (Integer layerIndex : descendingLayerKeys) {
					if (layers.get(layerIndex).mouseMoved(screenX, screenY)) {
						result = true;
					}
				}
				return result;
			} else {
				if (getState() != NodeState.NORMAL) {
					setState(NodeState.NORMAL);
				}
				if (scrollThumbState != NodeState.NORMAL) {
					scrollThumbState = NodeState.NORMAL;
				}
			}
			return false;
		}
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (scrollThumb.contains(screenX, screenY)) {
			thumbDragStartY = screenY;
			return this;
		}

		NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
		for (Integer layerIndex : descendingLayerKeys) {
			ActionableRenderNode result = layers.get(layerIndex).mouseDown(screenX, screenY, pointer, button);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		endAction();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		contentHeight = super.determinePreferredContentHeight(layoutState);
		float result = contentHeight;

		if (result > ((ScrollBox) element).getMaxHeight()) {
			result = ((ScrollBox) element).getMaxHeight();
		} else if (result < ((ScrollBox) element).getMinHeight()) {
			result = ((ScrollBox) element).getMinHeight();
		}

		float scrollThumbHeightPercentage = result / contentHeight;
		if (scrollThumbHeightPercentage < 0.05f) {
			scrollThumbHeightPercentage = 0.05f;
		} else if (scrollThumbHeightPercentage > 1f) {
			scrollThumbHeightPercentage = 1f;
		}

		scrollTrack.setWidth(style.getScrollBarSize());
		scrollTrack.setHeight(result);

		scrollThumb.setWidth(style.getScrollBarSize());
		scrollThumb.setHeight(result * scrollThumbHeightPercentage);
		return result;
	}

	@Override
	public void beginAction() {
		scrollThumbState = NodeState.ACTION;
	}

	@Override
	public void endAction() {
		scrollThumbState = NodeState.NORMAL;
	}

	@Override
	protected ScrollBoxStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(((ScrollBox) element), layoutState.getScreenSize());
	}

}
