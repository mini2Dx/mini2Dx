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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 * Base class for implementing rendering of {@link UiElement} implementations
 */
public abstract class RenderNode<T extends UiElement, S extends StyleRule> implements HoverableRenderNode {
	private static final String LOGGING_TAG = RenderNode.class.getSimpleName();

	protected final Array<UiEffect> effects = new Array<UiEffect>(true, 1, UiEffect.class);
	protected final CollisionBox outerArea = new CollisionBox();
	protected final Rectangle innerArea = new Rectangle();
	protected final Rectangle targetOuterArea = new Rectangle();
	protected final ParentRenderNode<?, ?> parent;
	protected final T element;

	protected UiContainerRenderTree rootNode;
	protected S style;
	protected float preferredContentWidth, preferredContentHeight;
	protected float xOffset, yOffset;
	protected int zIndex;
	protected boolean hiddenByLayoutRule = false;
	protected boolean initialLayoutOccurred = false, initialUpdateOccurred = false;
	private float relativeX, relativeY;
	private boolean dirty;
	private boolean includeInRender = false;
	private NodeState state = NodeState.NORMAL;

	public RenderNode(ParentRenderNode<?, ?> parent, T element) {
		this.parent = parent;
		this.element = element;
		this.zIndex = element.getZIndex();

		setDirty();
	}

	public void update(UiContainerRenderTree uiContainer, float delta) {
		if (!initialLayoutOccurred) {
			if (element.isDebugEnabled()) {
				Mdx.log.debug(element.getId(), "UPDATE - initial layout not occurred");
			}
			return;
		}
		if (style == null) {
			throw new MdxException("No style found for element: " + getId());
		}
		if (parent == null) {
			targetOuterArea.set(relativeX, relativeY,
					getPreferredOuterWidth(), getPreferredOuterHeight());
		} else {
			targetOuterArea.set(parent.getInnerX() + relativeX,
					parent.getInnerY() + relativeY, getPreferredOuterWidth(),
					getPreferredOuterHeight());
		}
		outerArea.preUpdate();

		boolean visible = isScheduledToRender();
		if (effects.size == 0) {
			outerArea.forceTo(targetOuterArea.getX(), targetOuterArea.getY(),
					targetOuterArea.getWidth(), targetOuterArea.getHeight());
		} else {
			for (int i = 0; i < effects.size; i++) {
				UiEffect effect = effects.get(i);
				if (effect.isFinished()) {
					effect.postEnd(element);
					effects.removeIndex(i);
					element.notifyEffectListenersOnFinished(effect);
					i--;
					continue;
				}

				visible &= effect.update(uiContainer, outerArea, targetOuterArea, delta);
			}
		}
		includeInRender = visible;

		if (element.isDebugEnabled()) {
			Mdx.log.debug(element.getId(), "UPDATE - outerArea: " + outerArea + ", targetArea: " + targetOuterArea
					+ ", visibility: " + element.getVisibility());
		}

		innerArea.set(getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
		initialUpdateOccurred = true;

		element.syncWithUpdate(rootNode);
	}

	public void interpolate(float alpha) {
		if (!initialLayoutOccurred) {
			return;
		}
		outerArea.interpolate(alpha);
	}

	public void render(Graphics g) {
		if (!isIncludedInRender()) {
			if (element.isDebugEnabled()) {
				Mdx.log.debug(element.getId(), "RENDER - Element not included in render");
			}
			return;
		}
		if (element.isDebugEnabled()) {
			Mdx.log.debug(element.getId(), "RENDER - x,y: " + getOuterRenderX() + "," + getOuterRenderY() + " width: "
					+ getOuterRenderWidth() + ", height: " + getOuterRenderHeight());
		}

		for (int i = 0; i < effects.size; i++) {
			effects.get(i).preRender(g);
		}
		renderElement(g);
		for (int i = 0; i < effects.size; i++) {
			effects.get(i).postRender(g);
		}

		element.syncWithRender(rootNode);
	}

	public void beginFakeHover() {
		mouseMoved(MathUtils.round(innerArea.getCenterX()), MathUtils.round(innerArea.getCenterY()));
		if(rootNode == null) {
			return;
		}
		if(this instanceof ActionableRenderNode) {
			rootNode.getElement().setActiveAction((ActionableRenderNode) this);
		}
	}

	public void endFakeHover() {
		mouseMoved(MathUtils.round(innerArea.getX()) - 1, MathUtils.round(innerArea.getY() - 1));
		if(rootNode == null) {
			return;
		}
		if(this instanceof ActionableRenderNode) {
			rootNode.getElement().clearActiveAction();
		}
	}

	public boolean mouseMoved(int screenX, int screenY) {
		if (innerArea.contains(screenX, screenY)) {
			beginHover();
			return true;
		} else if (state != NodeState.NORMAL) {
			endHover();
		}
		return false;
	}
	
	public boolean mouseScrolled(int screenX, int screenY, float amount) {
		return false;
	}

	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		return null;
	}

	public void mouseUp(int screenX, int screenY, int pointer, int button) {
	}

	public boolean contains(float screenX, float screenY) {
		return innerArea.contains(screenX, screenY);
	}

	public void beginHover() {
		state = NodeState.HOVER;
		element.notifyHoverListenersOnBeginHover();
	}

	public void endHover() {
		state = NodeState.NORMAL;
		element.notifyHoverListenersOnEndHover();
	}

	protected abstract void renderElement(Graphics g);

	protected abstract S determineStyleRule(LayoutState layoutState);

	protected abstract float determinePreferredContentWidth(LayoutState layoutState);

	protected abstract float determinePreferredContentHeight(LayoutState layoutState);

	protected abstract float determineXOffset(LayoutState layoutState);

	protected abstract float determineYOffset(LayoutState layoutState);

	public void layout(LayoutState layoutState) {
		if (!isDirty() && !layoutState.isScreenSizeChanged()) {
			return;
		}
		if (element.isDebugEnabled()) {
			Mdx.log.debug(LOGGING_TAG, "Layout triggered");
		}
		rootNode = layoutState.getUiContainerRenderTree();
		style = determineStyleRule(layoutState);

		if (this.zIndex != element.getZIndex()) {
			parent.removeChild(this);
			zIndex = element.getZIndex();
			parent.addChild(this);
		}

		switch (element.getVisibility()) {
		case HIDDEN:
			preferredContentWidth = 0f;
			preferredContentHeight = 0f;
			xOffset = 0f;
			yOffset = 0f;
			element.syncWithLayout(rootNode);
			return;
		default:
			preferredContentWidth = determinePreferredContentWidth(layoutState);
			preferredContentHeight = determinePreferredContentHeight(layoutState);
			xOffset = determineXOffset(layoutState);
			yOffset = determineYOffset(layoutState);
			break;
		}

		dirty = false;
		initialLayoutOccurred = true;
		element.syncWithLayout(rootNode);
	}

	public boolean isIncludedInLayout() {
		if (hiddenByLayoutRule) {
			return false;
		}
		if (element.getVisibility() == Visibility.HIDDEN) {
			return false;
		}
		return getPreferredInnerWidth() > 0f || getPreferredInnerHeight() > 0f;
	}

	private boolean isScheduledToRender() {
		if (!initialLayoutOccurred) {
			return false;
		}
		if (element.isDebugEnabled()) {
			Mdx.log.debug(element.getId(), "SCHEDULED_TO_RENDER - hiddenByLayoutRule:" +
					hiddenByLayoutRule + ", style:" + style + ", visible:" + element.getVisibility() +
					", preferredWidth:" + getPreferredInnerWidth() + ", preferredHeight:" + getPreferredInnerHeight());
		}
		if (hiddenByLayoutRule) {
			return false;
		}
		if (style == null) {
			return false;
		}
		if (element.getVisibility() != Visibility.VISIBLE) {
			return false;
		}
		return getPreferredInnerWidth() > 0f || getPreferredInnerHeight() > 0f;
	}

	public boolean isIncludedInRender() {
		return includeInRender;
	}

	public boolean isDirty() {
		return dirty;
	}

	protected void clearDirty() {
		this.dirty = false;
	}

	public boolean setDirty() {
		final boolean result = this.dirty != true;
		if(this.dirty) {
			return result;
		}
		this.dirty = true;

		if(parent != null) {
			parent.setChildDirty();
		}
		return result;
	}

	public void applyEffect(UiEffect effect) {
		effect.preBegin(element);
		effects.add(effect);
	}

	public float getRelativeX() {
		return relativeX;
	}

	public void setRelativeX(float relativeX) {
		this.relativeX = relativeX;
	}

	public float getRelativeY() {
		return relativeY;
	}

	public void setRelativeY(float relativeY) {
		this.relativeY = relativeY;
	}

	public float getPreferredContentWidth() {
		return preferredContentWidth;
	}

	public float getPreferredInnerWidth() {
		if(style == null) {
			return preferredContentWidth;
		}
		return preferredContentWidth + style.getPaddingLeft() + style.getPaddingRight();
	}

	public float getPreferredOuterWidth() {
		if(style == null) {
			return preferredContentWidth;
		}
		return preferredContentWidth + style.getPaddingLeft() + style.getPaddingRight() + style.getMarginLeft()
				+ style.getMarginRight();
	}

	public float getPreferredContentHeight() {
		return preferredContentHeight;
	}

	public float getPreferredInnerHeight() {
		if(style == null) {
			return preferredContentWidth;
		}
		return preferredContentHeight + style.getPaddingTop() + style.getPaddingBottom();
	}

	public float getPreferredOuterHeight() {
		if(style == null) {
			return preferredContentWidth;
		}
		return preferredContentHeight + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom();
	}

	public float getXOffset() {
		return xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public float getOuterX() {
		return outerArea.getX();
	}

	public float getOuterY() {
		return outerArea.getY();
	}

	public float getOuterWidth() {
		return outerArea.getWidth();
	}

	public float getOuterHeight() {
		return outerArea.getHeight();
	}

	public int getOuterRenderX() {
		return outerArea.getRenderX();
	}

	public int getOuterRenderY() {
		return outerArea.getRenderY();
	}

	public int getOuterRenderWidth() {
		return outerArea.getRenderWidth();
	}

	public int getOuterRenderHeight() {
		return outerArea.getRenderHeight();
	}
	
	public float getInnerX() {
		if(style == null) {
			return outerArea.getX();
		}
		return outerArea.getX() + style.getMarginLeft();
	}

	public float getInnerY() {
		if(style == null) {
			return outerArea.getY();
		}
		return outerArea.getY() + style.getMarginTop();
	}

	public float getInnerWidth() {
		if(style == null) {
			return outerArea.getWidth();
		}
		return outerArea.getWidth() - style.getMarginLeft() - style.getMarginRight();
	}

	public float getInnerHeight() {
		if(style == null) {
			return outerArea.getHeight();
		}
		return outerArea.getHeight() - style.getMarginTop() - style.getMarginBottom();
	}

	public int getInnerRenderX() {
		if(style == null) {
			return outerArea.getRenderX();
		}
		return outerArea.getRenderX() + style.getMarginLeft();
	}

	public int getInnerRenderY() {
		if(style == null) {
			return outerArea.getRenderY();
		}
		return outerArea.getRenderY() + style.getMarginTop();
	}

	public int getInnerRenderWidth() {
		if(style == null) {
			return outerArea.getRenderWidth();
		}
		return outerArea.getRenderWidth() - style.getMarginLeft() - style.getMarginRight();
	}

	public int getInnerRenderHeight() {
		if(style == null) {
			return outerArea.getRenderHeight();
		}
		return outerArea.getRenderHeight() - style.getMarginTop() - style.getMarginBottom();
	}
	
	public int getContentRenderX() {
		if(style == null) {
			return getInnerRenderX();
		}
		return getInnerRenderX()+ style.getPaddingLeft();
	}
	
	public int getContentRenderY() {
		if(style == null) {
			return getInnerRenderY();
		}
		return getInnerRenderY() + style.getPaddingTop();
	}
	
	public int getContentRenderWidth() {
		if(style == null) {
			return getInnerRenderWidth();
		}
		return getInnerRenderWidth() - style.getPaddingLeft() - style.getPaddingRight();
	}
	
	public int getContentRenderHeight() {
		if(style == null) {
			return getInnerRenderHeight();
		}
		return getInnerRenderHeight() - style.getPaddingTop() - style.getPaddingBottom();
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		NodeState previousState = this.state;
		this.state = state;
		if (previousState != state) {
			element.notifyNodeStateListeners(state);
			if (state == NodeState.HOVER) {
				element.notifyHoverListenersOnBeginHover();
			} else if (previousState == NodeState.HOVER) {
				element.notifyHoverListenersOnEndHover();
			}
		}
	}

	public S getStyle() {
		return style;
	}

	public RenderNode<?, ?> getElementById(String id) {
		if (element.getId().equals(id)) {
			return this;
		}
		return null;
	}

	public String getId() {
		return element.getId();
	}

	public int getZIndex() {
		return zIndex;
	}

	public T getElement() {
		return element;
	}

	public ParentRenderNode<? extends ParentUiElement, ?> getParent() {
		return parent;
	}

	public boolean isInitialLayoutOccurred() {
		return initialLayoutOccurred;
	}

	public boolean isInitialUpdateOccurred() {
		return initialUpdateOccurred;
	}

	public boolean isChildOfUiContainer() {
		if(parent == null) {
			return false;
		}
		if(rootNode == null) {
			return false;
		}
		return parent.getId().equals(rootNode.getId());
	}

	@Override
	public String toString() {
		return "RenderNode [outerArea=" + outerArea + ", targetOuterArea=" + targetOuterArea + ", parent=" + parent.getId()
				+ ", style=" + style + ", preferredWidth=" + preferredContentWidth + ", preferredHeight="
				+ preferredContentHeight + ", xOffset=" + xOffset + ", yOffset=" + yOffset + "]";
	}
}
