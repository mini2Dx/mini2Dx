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

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.ui.animation.ScrollTo;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.ScrollBoxStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public class ScrollBoxRenderNode extends AbstractColumnRenderNode<ScrollBoxStyleRule>implements ActionableRenderNode {
	private final CollisionBox topScrollButton = new CollisionBox();
	private final CollisionBox bottomScrollButton = new CollisionBox();
	private final CollisionBox scrollThumb = new CollisionBox();
	private final CollisionBox scrollTrack = new CollisionBox();
	
	private ScrollTo scrollTo = null;

	private ButtonStyleRule topScrollButtonStyleRule, bottomScrollButtonStyleRule;
	private float contentHeight;
	private float scrollThumbPosition;

	private NodeState topScrollButtonState = NodeState.NORMAL;
	private NodeState bottomScrollButtonState = NodeState.NORMAL;
	private NodeState scrollThumbState = NodeState.NORMAL;

	private int scrollTranslationY;
	private float thumbDragStartY;

	public ScrollBoxRenderNode(ParentRenderNode<?, ?> parent, ScrollBox row) {
		super(parent, row);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		scrollTrack.preUpdate();
		scrollThumb.preUpdate();
		topScrollButton.preUpdate();
		bottomScrollButton.preUpdate();

		super.update(uiContainer, delta);

		switch (topScrollButtonState) {
		case ACTION:
			setScrollThumbPosition(scrollThumbPosition - ((ScrollBox) element).getScrollFactor());
			break;
		default:
			break;
		}
		switch (bottomScrollButtonState) {
		case ACTION:
			setScrollThumbPosition(scrollThumbPosition + ((ScrollBox) element).getScrollFactor());
			break;
		default:
			break;
		}
		updateScrollTo(delta);

		topScrollButton.set(innerArea.getX() + innerArea.getWidth() - style.getScrollBarWidth(), innerArea.getY());
		scrollTrack.set(topScrollButton.getX(), topScrollButton.getY() + topScrollButton.getHeight());
		bottomScrollButton.set(scrollTrack.getX(), scrollTrack.getY() + scrollTrack.getHeight());
		scrollThumb.set(scrollTrack.getX(), scrollTrack.getY() + (scrollThumbPosition * scrollTrack.getHeight()));
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		scrollTrack.interpolate(null, alpha);
		scrollThumb.interpolate(null, alpha);
		topScrollButton.interpolate(null, alpha);
		bottomScrollButton.interpolate(null, alpha);
	}

	@Override
	protected void renderElement(Graphics g) {
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
		g.translate(0f, scrollTranslationY);

		Rectangle existingClip = g.removeClip();
		g.setClip(getInnerRenderX(), getInnerRenderY() + scrollTranslationY, getInnerRenderWidth(),
				getInnerRenderHeight());

		for (RenderLayer layer : layers.values()) {
			layer.render(g);
		}

		if (existingClip != null) {
			g.setClip(existingClip);
		} else {
			g.removeClip();
		}
		g.translate(0f, -scrollTranslationY);

		NinePatch scrollTrackPatch = style.getScrollTrackNinePatch();
		g.drawNinePatch(scrollTrackPatch, scrollTrack.getRenderX(), scrollTrack.getRenderY(),
				scrollTrack.getRenderWidth(), scrollTrack.getRenderHeight());

		switch (topScrollButtonState) {
		case ACTION:
			g.drawNinePatch(topScrollButtonStyleRule.getActionNinePatch(), topScrollButton.getRenderX(),
					topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
			break;
		case HOVER:
			g.drawNinePatch(topScrollButtonStyleRule.getHoverNinePatch(), topScrollButton.getRenderX(),
					topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
			break;
		case NORMAL:
		default:
			g.drawNinePatch(topScrollButtonStyleRule.getNormalNinePatch(), topScrollButton.getRenderX(),
					topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
			break;
		}

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

		switch (bottomScrollButtonState) {
		case ACTION:
			g.drawNinePatch(bottomScrollButtonStyleRule.getActionNinePatch(), bottomScrollButton.getRenderX(),
					bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
					bottomScrollButton.getRenderHeight());
			break;
		case HOVER:
			g.drawNinePatch(bottomScrollButtonStyleRule.getHoverNinePatch(), bottomScrollButton.getRenderX(),
					bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
					bottomScrollButton.getRenderHeight());
			break;
		case NORMAL:
		default:
			g.drawNinePatch(bottomScrollButtonStyleRule.getNormalNinePatch(), bottomScrollButton.getRenderX(),
					bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
					bottomScrollButton.getRenderHeight());
			break;
		}
	}
	
	private void updateScrollTo(float delta) {
		if(scrollTo == null) {
			return;
		}
		RenderNode<?, ?> scrollToNode = scrollTo.getTargetRenderNode();
		if(scrollToNode == null) {
			scrollToNode = getElementById(scrollTo.getTargetElement().getId());
			if(scrollToNode == null) {
				scrollTo = null;
				return;
			}
		}
		
		float currentScrollY = innerArea.getY() + scrollTranslationY;
		float scrollFactor = ((ScrollBox) element).getScrollFactor() * contentHeight;
		if(scrollToNode.getOuterY() + scrollToNode.getOuterHeight() > currentScrollY + innerArea.getHeight() + scrollFactor) {
			if(scrollTo.isImmediate()) {
				//TODO: Optimise this
				while(scrollToNode.getOuterY() + scrollToNode.getOuterHeight() > currentScrollY + innerArea.getHeight() + scrollFactor) {
					setScrollThumbPosition(scrollThumbPosition + ((ScrollBox) element).getScrollFactor());
					currentScrollY = innerArea.getY() + scrollTranslationY;
				}
			} else {
				setScrollThumbPosition(scrollThumbPosition + ((ScrollBox) element).getScrollFactor());
			}
		} else if(scrollToNode.getOuterY() < currentScrollY - scrollFactor) {
			if(scrollTo.isImmediate()) {
				//TODO: Optimise this
				while(scrollToNode.getOuterY() < currentScrollY - scrollFactor) {
					setScrollThumbPosition(scrollThumbPosition - ((ScrollBox) element).getScrollFactor());
					currentScrollY = innerArea.getY() + scrollTranslationY;
				}
			} else {
				setScrollThumbPosition(scrollThumbPosition - ((ScrollBox) element).getScrollFactor());
			}
		} else {
			scrollTo = null;
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean outerAreaContains = false;
		if (outerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
			outerAreaContains = true;
		} else {
			setState(NodeState.NORMAL);
		}
		boolean result = handleScrollThumbMouseMoved(outerAreaContains, screenX, screenY);
		result |= handleTopScrollButtonMouseMoved(outerAreaContains, screenX, screenY);
		result |= handleBottomScrollButtonMouseMoved(outerAreaContains, screenX, screenY);

		if (!result) {
			NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
			for (Integer layerIndex : descendingLayerKeys) {
				if (layers.get(layerIndex).mouseMoved(screenX, screenY + scrollTranslationY)) {
					result = true;
				}
			}
		}
		return result;
	}

	private boolean handleScrollThumbMouseMoved(boolean outerAreaContains, int screenX, int screenY) {
		switch (scrollThumbState) {
		case ACTION:
			float yDiff = screenY - thumbDragStartY;
			setScrollThumbPosition(((scrollThumb.getY() + yDiff) - scrollTrack.getY()) / scrollTrack.getHeight());
			thumbDragStartY = screenY;
			return true;
		case HOVER:
		case NORMAL:
		default:
			if (outerAreaContains) {
				if (scrollThumb.contains(screenX, screenY)) {
					scrollThumbState = NodeState.HOVER;
				} else {
					scrollThumbState = NodeState.NORMAL;
				}
			} else {
				if (scrollThumbState != NodeState.NORMAL) {
					scrollThumbState = NodeState.NORMAL;
				}
			}
			return false;
		}
	}

	private boolean handleTopScrollButtonMouseMoved(boolean outerAreaContains, int screenX, int screenY) {
		if (scrollThumbState == NodeState.ACTION) {
			return false;
		}
		switch (topScrollButtonState) {
		case ACTION:
			return false;
		case HOVER:
		case NORMAL:
		default:
			if (topScrollButton.contains(screenX, screenY)) {
				topScrollButtonState = NodeState.HOVER;
			} else {
				topScrollButtonState = NodeState.NORMAL;
			}
			return false;
		}
	}

	private boolean handleBottomScrollButtonMouseMoved(boolean outerAreaContains, int screenX, int screenY) {
		if (scrollThumbState == NodeState.ACTION) {
			return false;
		}
		switch (bottomScrollButtonState) {
		case ACTION:
			return false;
		case HOVER:
		case NORMAL:
		default:
			if (bottomScrollButton.contains(screenX, screenY)) {
				bottomScrollButtonState = NodeState.HOVER;
			} else {
				bottomScrollButtonState = NodeState.NORMAL;
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
			scrollThumbState = NodeState.ACTION;
			thumbDragStartY = screenY;
			return this;
		}
		if (topScrollButton.contains(screenX, screenY)) {
			topScrollButtonState = NodeState.ACTION;
			return this;
		}
		if (bottomScrollButton.contains(screenX, screenY)) {
			bottomScrollButtonState = NodeState.ACTION;
			return this;
		}

		NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
		for (Integer layerIndex : descendingLayerKeys) {
			ActionableRenderNode result = layers.get(layerIndex).mouseDown(screenX, screenY + scrollTranslationY, pointer, button);
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
	public boolean mouseScrolled(int screenX, int screenY, float amount) {
		if (outerArea.contains(screenX, screenY)) {
			setScrollThumbPosition(scrollThumbPosition + (amount * ((ScrollBox) element).getScrollFactor()));
		}
		return false;
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		contentHeight = super.determinePreferredContentHeight(layoutState);
		float result = contentHeight;

		if (result > ((ScrollBox) element).getMaxHeight()) {
			result = ((ScrollBox) element).getMaxHeight();
		} else if (result < style.getMinHeight()) {
			result = style.getMinHeight();
		}

		float scrollThumbHeightPercentage = result / contentHeight;
		if (scrollThumbHeightPercentage < 0.05f) {
			scrollThumbHeightPercentage = 0.05f;
		} else if (scrollThumbHeightPercentage > 1f) {
			scrollThumbHeightPercentage = 1f;
		}

		topScrollButton.setWidth(style.getScrollBarWidth());
		topScrollButton.setHeight(style.getScrollButtonHeight());

		bottomScrollButton.setWidth(style.getScrollBarWidth());
		bottomScrollButton.setHeight(style.getScrollButtonHeight());

		float scrollTrackHeight = result - topScrollButton.getHeight() - bottomScrollButton.getHeight();
		scrollTrack.setWidth(style.getScrollBarWidth());
		scrollTrack.setHeight(scrollTrackHeight);

		scrollThumb.setWidth(style.getScrollBarWidth());
		scrollThumb.setHeight(scrollTrackHeight * scrollThumbHeightPercentage);
		return result;
	}

	@Override
	public void beginAction() {
	}

	@Override
	public void endAction() {
		scrollThumbState = NodeState.NORMAL;
		topScrollButtonState = NodeState.NORMAL;
		bottomScrollButtonState = NodeState.NORMAL;
	}

	@Override
	protected ScrollBoxStyleRule determineStyleRule(LayoutState layoutState) {
		ScrollBoxStyleRule result = layoutState.getTheme().getStyleRule(((ScrollBox) element),
				layoutState.getScreenSize());
		if (result.getTopScrollButtonStyle() != null) {
			topScrollButtonStyleRule = layoutState.getTheme().getButtonStyleRule(result.getTopScrollButtonStyle(),
					layoutState.getScreenSize());
		} else {
			topScrollButtonStyleRule = layoutState.getTheme().getButtonStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		if (result.getBottomScrollButtonStyle() != null) {
			bottomScrollButtonStyleRule = layoutState.getTheme().getButtonStyleRule(result.getBottomScrollButtonStyle(),
					layoutState.getScreenSize());
		} else {
			bottomScrollButtonStyleRule = layoutState.getTheme().getButtonStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		return result;
	}
	
	public float getScrollThumbPosition() {
		return scrollThumbPosition;
	}

	public void setScrollThumbPosition(float position) {
		float maxPosition = (scrollTrack.getHeight() - scrollThumb.getHeight()) / scrollTrack.getHeight();
		scrollThumbPosition = position;
		if (scrollThumbPosition < 0f) {
			scrollThumbPosition = 0f;
		} else if (scrollThumbPosition > maxPosition) {
			scrollThumbPosition = maxPosition;
		}
		scrollTranslationY = MathUtils.round(scrollThumbPosition * contentHeight);
		((ScrollBox) element).notifyScrollListeners(scrollThumbPosition);
	}
	
	public boolean offerScrollTo(ScrollTo scrollTo) {
		if(scrollTo == null) {
			return false;
		}
		if(this.scrollTo != null) {
			return false;
		}
		this.scrollTo = scrollTo;
		return true;
	}
}
