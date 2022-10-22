/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.render;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.collision.util.StaticCollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.ui.animation.ScrollTo;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.ScrollBoxStyleRule;
import org.mini2Dx.ui.style.UiTheme;

/**
 * {@link RenderNode} implementation for {@link ScrollBox}
 */
public class ScrollBoxRenderNode extends ParentRenderNode<ScrollBox, ScrollBoxStyleRule>
		implements ActionableRenderNode {
	private static final String LOGGING_TAG = ScrollBoxRenderNode.class.getSimpleName();

	protected final StaticCollisionBox topScrollButton = new StaticCollisionBox();
    protected final StaticCollisionBox bottomScrollButton = new StaticCollisionBox();
    protected final StaticCollisionBox scrollThumb = new StaticCollisionBox();
    protected final StaticCollisionBox scrollTrack = new StaticCollisionBox();

    protected ScrollTo scrollTo = null;

    protected ButtonStyleRule topScrollButtonStyleRule, bottomScrollButtonStyleRule;
    protected float contentHeight, boxHeight;
    protected float scrollThumbPosition;

    protected NodeState topScrollButtonState = NodeState.NORMAL;
    protected NodeState bottomScrollButtonState = NodeState.NORMAL;
    protected NodeState scrollThumbState = NodeState.NORMAL;

    protected int scrollTranslationY;
    protected float thumbDragStartY;

	public ScrollBoxRenderNode(ParentRenderNode<?, ?> parent, ScrollBox row) {
		super(parent, row);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
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

		topScrollButton.setXY(getInnerX() + getInnerWidth() - style.getScrollBarWidth(), getInnerY());
		scrollTrack.setXY(topScrollButton.getX(), topScrollButton.getY() + topScrollButton.getHeight());
		bottomScrollButton.setXY(scrollTrack.getX(), scrollTrack.getY() + scrollTrack.getHeight());
		scrollThumb.setXY(scrollTrack.getX(), scrollTrack.getY() + (scrollThumbPosition * scrollTrack.getHeight()));

		if(scrollTranslationY > contentHeight) {
			scrollTranslationY = MathUtils.round(scrollThumbPosition * contentHeight);
		}
 	}

	protected void renderBackground(Graphics g) {
		switch(getState()) {
		case NORMAL:
			if (style.getNormalBackgroundRenderer() != null) {
				style.getNormalBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case HOVER:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case ACTION:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		}
	}

	protected void renderScrollbar(Graphics g) {
		style.getScrollTrackRenderer().render(g, scrollTrack.getRenderX(), scrollTrack.getRenderY(),
				scrollTrack.getRenderWidth(), scrollTrack.getRenderHeight());

		if(isScrollThumbAtTop()) {
			topScrollButtonStyleRule.getDisabledBackgroundRenderer().render(g, topScrollButton.getRenderX(),
					topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
		} else {
			switch (topScrollButtonState) {
				case ACTION:
					topScrollButtonStyleRule.getActionBackgroundRenderer().render(g, topScrollButton.getRenderX(),
							topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
					break;
				case HOVER:
					topScrollButtonStyleRule.getHoverBackgroundRenderer().render(g, topScrollButton.getRenderX(),
							topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
					break;
				case NORMAL:
				default:
					topScrollButtonStyleRule.getNormalBackgroundRenderer().render(g, topScrollButton.getRenderX(),
							topScrollButton.getRenderY(), topScrollButton.getRenderWidth(), topScrollButton.getRenderHeight());
					break;
			}
		}

		switch (scrollThumbState) {
			case ACTION:
				style.getScrollThumbActiveRenderer().render(g, scrollThumb.getRenderX(), scrollThumb.getRenderY(),
						scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
				break;
			case HOVER:
				style.getScrollThumbHoverRenderer().render(g, scrollThumb.getRenderX(), scrollThumb.getRenderY(),
						scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
				break;
			default:
				style.getScrollThumbNormalRenderer().render(g, scrollThumb.getRenderX(), scrollThumb.getRenderY(),
						scrollThumb.getRenderWidth(), scrollThumb.getRenderHeight());
				break;
		}

		if(isScrollThumbAtBottom()) {
			bottomScrollButtonStyleRule.getDisabledBackgroundRenderer().render(g, bottomScrollButton.getRenderX(),
					bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
					bottomScrollButton.getRenderHeight());
		} else {
			switch (bottomScrollButtonState) {
				case ACTION:
					bottomScrollButtonStyleRule.getActionBackgroundRenderer().render(g, bottomScrollButton.getRenderX(),
							bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
							bottomScrollButton.getRenderHeight());
					break;
				case HOVER:
					bottomScrollButtonStyleRule.getHoverBackgroundRenderer().render(g, bottomScrollButton.getRenderX(),
							bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
							bottomScrollButton.getRenderHeight());
					break;
				case NORMAL:
				default:
					bottomScrollButtonStyleRule.getNormalBackgroundRenderer().render(g, bottomScrollButton.getRenderX(),
							bottomScrollButton.getRenderY(), bottomScrollButton.getRenderWidth(),
							bottomScrollButton.getRenderHeight());
					break;
			}
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		renderBackground(g);
		g.translate(0f, scrollTranslationY);

		Rectangle existingClip = g.removeClip();
		g.setClip(getInnerRenderX(), getInnerRenderY() + scrollTranslationY, getInnerRenderWidth(),
				getInnerRenderHeight());

		final IntMap.Keys keys = layers.ascendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
			final RenderLayer layer = layers.get(layerIndex);
			layer.render(g);
		}

		if (existingClip != null) {
			g.setClip(existingClip);
		} else {
			g.removeClip();
		}
		g.translate(0f, -scrollTranslationY);

		if(!element.getScrollTrackVisibility().equals(Visibility.VISIBLE)) {
			return;
		}

		renderScrollbar(g);
	}

	private void updateScrollTo(float delta) {
		if (scrollTo == null) {
			return;
		}
		RenderNode<?, ?> scrollToNode = scrollTo.getTargetRenderNode();
		if (scrollToNode == null) {
			scrollToNode = getElementById(scrollTo.getTargetElement().getId());
			if (scrollToNode == null) {
				scrollTo = null;
				return;
			}
		}

		float currentScrollY = getInnerY() + scrollTranslationY;
		float scrollFactor = ((ScrollBox) element).getScrollFactor() * contentHeight;
		if (scrollToNode.getOuterY() + scrollToNode.getOuterHeight() > currentScrollY + getInnerHeight()) {
			if (scrollTo.isImmediate()) {
				// TODO: Optimise this
				int previousScrollTranslationY = Integer.MAX_VALUE;
				while (scrollToNode.getOuterY() + scrollToNode.getOuterHeight() > currentScrollY + getInnerHeight()
						+ scrollFactor) {
					if(previousScrollTranslationY == scrollTranslationY) {
						break;
					}
					setScrollThumbPosition(scrollThumbPosition + ((ScrollBox) element).getScrollFactor());
					currentScrollY = getInnerY() + scrollTranslationY;
					previousScrollTranslationY = scrollTranslationY;
				}
				if (scrollToNode.getOuterY() + scrollToNode.getOuterHeight() > currentScrollY + getInnerHeight()){
					float scrollAmount = (scrollToNode.getOuterY() + scrollToNode.getOuterHeight()) - (currentScrollY + getInnerHeight());
					setScrollThumbPosition(scrollThumbPosition + (scrollAmount / contentHeight));
				}
			} else {
				setScrollThumbPosition(scrollThumbPosition + ((ScrollBox) element).getScrollFactor());
			}
		} else if (scrollToNode.getOuterY() < currentScrollY) {
			if (scrollTo.isImmediate()) {
				// TODO: Optimise this
				int previousScrollTranslationY = Integer.MAX_VALUE;
				while (scrollToNode.getOuterY() < currentScrollY - scrollFactor) {
					if(previousScrollTranslationY == scrollTranslationY) {
						break;
					}
					setScrollThumbPosition(scrollThumbPosition - ((ScrollBox) element).getScrollFactor());
					currentScrollY = getInnerY() + scrollTranslationY;
					previousScrollTranslationY = scrollTranslationY;
				}
				if (scrollToNode.getOuterY() < currentScrollY){
					float scrollAmount = currentScrollY - scrollToNode.getOuterY();
					setScrollThumbPosition(scrollThumbPosition - (scrollAmount / contentHeight));
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
		boolean innerAreaContains = false;
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
			innerAreaContains = true;
		} else {
			setState(NodeState.NORMAL);
		}
		boolean result = handleScrollThumbMouseMoved(innerAreaContains, screenX, screenY);
		result |= handleTopScrollButtonMouseMoved(innerAreaContains, screenX, screenY);
		result |= handleBottomScrollButtonMouseMoved(innerAreaContains, screenX, screenY);

		if (!result && innerAreaContains) {
			final IntMap.Keys keys = layers.descendingKeys();
			keys.reset();
			while(keys.hasNext) {
				final int layerIndex = keys.next();
				if (layers.get(layerIndex).mouseMoved(screenX, screenY + scrollTranslationY)) {
					result = true;
				}
			}
		}
		return result;
	}

	private boolean handleScrollThumbMouseMoved(boolean innerAreaContains, int screenX, int screenY) {
		switch (scrollThumbState) {
		case ACTION:
			float yDiff = screenY - thumbDragStartY;
			setScrollThumbPosition(scrollThumbPosition + (yDiff / scrollTrack.getHeight()));
			thumbDragStartY = screenY;
			return true;
		case HOVER:
		case NORMAL:
		default:
			if (innerAreaContains) {
				if (scrollThumb.contains(screenX, screenY) && element.isHoverEnabled()) {
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

	private boolean handleTopScrollButtonMouseMoved(boolean innerAreaContains, int screenX, int screenY) {
		if (scrollThumbState == NodeState.ACTION) {
			return false;
		}
		switch (topScrollButtonState) {
		case ACTION:
			return false;
		case HOVER:
		case NORMAL:
		default:
			if (topScrollButton.contains(screenX, screenY) && element.isHoverEnabled()) {
				topScrollButtonState = NodeState.HOVER;
			} else {
				topScrollButtonState = NodeState.NORMAL;
			}
			return false;
		}
	}

	private boolean handleBottomScrollButtonMouseMoved(boolean innerAreaContains, int screenX, int screenY) {
		if (scrollThumbState == NodeState.ACTION) {
			return false;
		}
		switch (bottomScrollButtonState) {
		case ACTION:
			return false;
		case HOVER:
		case NORMAL:
		default:
			if (bottomScrollButton.contains(screenX, screenY) && element.isHoverEnabled()) {
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

		if(innerArea.contains(screenX, screenY)) {
			final IntMap.Keys keys = layers.descendingKeys();
			keys.reset();
			while(keys.hasNext) {
				final int layerIndex = keys.next();
				ActionableRenderNode result = layers.get(layerIndex).mouseDown(screenX, screenY + scrollTranslationY,
						pointer, button);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
		params.setMouseX(screenX);
		params.setMouseY(screenY);
		endAction(EventTrigger.getTriggerForMouseClick(button), params);
		EventTriggerParamsPool.release(params);
	}

	@Override
	public boolean mouseScrolled(int screenX, int screenY, float amountX, float amountY) {
		if (innerArea.contains(screenX, screenY)) {
			setScrollThumbPosition(scrollThumbPosition + (amountY * ((ScrollBox) element).getScrollFactor()));
		}
		return false;
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		contentHeight = super.determinePreferredContentHeight(layoutState);
		float result = contentHeight;

		if (result > ((ScrollBox) element).getMaxHeight()) {
			result = ((ScrollBox) element).getMaxHeight();
		} else {
			if (((ScrollBox) element).getMinHeight() > Float.MIN_VALUE) {
				if (result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
						+ style.getMarginBottom() < ((ScrollBox) element).getMinHeight()) {
					result = ((ScrollBox) element).getMinHeight() - style.getPaddingTop() - style.getPaddingBottom()
							- style.getMarginTop() - style.getMarginBottom();
				}
			} else if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom()
					+ style.getMarginTop() + style.getMarginBottom() < style.getMinHeight()) {
				result = style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
						- style.getMarginBottom();
			}
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

		if(scrollThumbHeightPercentage >= 1f) {
			scrollThumb.setHeight(scrollTrackHeight);
		} else {
			scrollThumb.setHeight(scrollTrackHeight * scrollThumbHeightPercentage);
		}

		boxHeight = result;
		return result;
	}

	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
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

	/**
	 * Sets the scroll position
	 * @param position A value between 0.0 and 1.0
	 */
	public void setScrollThumbPosition(float position) {
		final float maxPosition = (scrollTrack.getHeight() - scrollThumb.getHeight()) / scrollTrack.getHeight();
		scrollThumbPosition = position;
		if (scrollThumbPosition < 0f) {
			scrollThumbPosition = 0f;
		} else if (scrollThumbPosition > maxPosition) {
			scrollThumbPosition = maxPosition;
		}
		scrollTranslationY = MathUtils.round(scrollThumbPosition * contentHeight);
		((ScrollBox) element).notifyScrollListeners(scrollThumbPosition);
	}

	public boolean isScrollThumbAtTop() {
		return scrollThumbPosition <= 0f;
	}

	public boolean isScrollThumbAtBottom() {
		final float maxPosition = (scrollTrack.getHeight() - scrollThumb.getHeight()) / scrollTrack.getHeight();
		return scrollThumbPosition >= maxPosition;
	}

	public boolean offerScrollTo(ScrollTo scrollTo) {
		if (scrollTo == null) {
			return false;
		}
		if (this.scrollTo != null) {
			return false;
		}
		this.scrollTo = scrollTo;
		return true;
	}

	@Override
	public float getPreferredContentWidth() {
		return preferredContentWidth - scrollTrack.getWidth();
	}

	@Override
	public float getPreferredInnerWidth() {
		return preferredContentWidth + style.getPaddingLeft() + style.getPaddingRight() + scrollTrack.getWidth();
	}

	public float getBoxHeight() {
		return boxHeight;
	}

	public float getScrollContentHeight() {
		return contentHeight;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public int getScrollTranslationY() {
		return scrollTranslationY;
	}
}
