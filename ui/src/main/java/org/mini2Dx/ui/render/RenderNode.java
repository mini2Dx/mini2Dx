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

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

import com.badlogic.gdx.Gdx;

/**
 *
 */
public abstract class RenderNode<T extends UiElement, S extends StyleRule> implements HoverableRenderNode {
	protected final List<UiEffect> effects = new ArrayList<UiEffect>(1);
	protected final CollisionBox outerArea = new CollisionBox();
	protected final CollisionBox innerArea = new CollisionBox();
	protected final Rectangle targetOuterArea = new Rectangle();
	protected final ParentRenderNode<?, ?> parent;
	protected final T element;

	protected S style;
	protected float preferredContentWidth, preferredContentHeight;
	protected float xOffset, yOffset;
	protected int zIndex;
	protected boolean hiddenByLayoutRule = false;
	protected boolean initialLayoutOccurred = false;
	private float relativeX, relativeY;
	private boolean dirty;
	private boolean includeInRender = false;
	private NodeState state = NodeState.NORMAL;

	public RenderNode(ParentRenderNode<?, ?> parent, T element) {
		this.parent = parent;
		this.element = element;
		this.zIndex = element.getZIndex();

		setDirty(true);
	}

	private void setInnerArea(boolean force) {
		if(force) {
			innerArea.forceTo(outerArea.getX() + style.getMarginLeft(), outerArea.getY() + style.getMarginTop(),
					outerArea.getWidth() - style.getMarginLeft() - style.getMarginRight(),
					outerArea.getHeight() - style.getMarginTop() - style.getMarginBottom());
		} else {
			innerArea.set(outerArea.getX() + style.getMarginLeft(), outerArea.getY() + style.getMarginTop(),
					outerArea.getWidth() - style.getMarginLeft() - style.getMarginRight(),
					outerArea.getHeight() - style.getMarginTop() - style.getMarginBottom());
		}
	}

	public void update(UiContainerRenderTree uiContainer, float delta) {
		if (!initialLayoutOccurred) {
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
		innerArea.preUpdate();

		element.syncWithRenderNode();

		boolean visible = isScheduledToRender();
		if (effects.size() == 0) {
			outerArea.forceTo(targetOuterArea);
			setInnerArea(true);
		} else {
			for (int i = 0; i < effects.size(); i++) {
				UiEffect effect = effects.get(i);
				if (effect.isFinished()) {
					effect.postEnd(element);
					effects.remove(i);
					element.notifyEffectListenersOnFinished(effect);
					i--;
					continue;
				}

				visible &= effect.update(uiContainer, outerArea, targetOuterArea, delta);
			}
			setInnerArea(false);
		}
		includeInRender = visible;

		if (element.isDebugEnabled()) {
			Gdx.app.log(element.getId(), "UPDATE - outerArea: " + outerArea + ", innerArea: " + innerArea + ", targetArea: " + targetOuterArea
					+ ", visibility: " + element.getVisibility());
		}
	}

	public void interpolate(float alpha) {
		if (!initialLayoutOccurred) {
			return;
		}
		outerArea.interpolate(null, alpha);
		innerArea.interpolate(null, alpha);
	}

	public void render(Graphics g) {
		if (!isIncludedInRender()) {
			if (element.isDebugEnabled()) {
				Gdx.app.log(element.getId(), "RENDER - Element not visible");
			}
			return;
		}
		if (element.isDebugEnabled()) {
			Gdx.app.log(element.getId(), "RENDER - x,y: " + getOuterRenderX() + "," + getOuterRenderY() + " width: "
					+ getOuterRenderWidth() + ", height: " + getOuterRenderHeight());
		}

		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).preRender(g);
		}
		renderElement(g);
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).postRender(g);
		}
	}

	public boolean mouseMoved(int screenX, int screenY) {
		if (outerArea.contains(screenX, screenY)) {
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
		return outerArea.contains(screenX, screenY);
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

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (parent == null) {
			return;
		}
		parent.setChildDirty(dirty);
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
		return preferredContentWidth + style.getPaddingLeft() + style.getPaddingRight();
	}

	public float getPreferredOuterWidth() {
		return preferredContentWidth + style.getPaddingLeft() + style.getPaddingRight() + style.getMarginLeft()
				+ style.getMarginRight();
	}

	public float getPreferredContentHeight() {
		return preferredContentHeight;
	}

	public float getPreferredInnerHeight() {
		return preferredContentHeight + style.getPaddingTop() + style.getPaddingBottom();
	}

	public float getPreferredOuterHeight() {
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
		return innerArea.getX();
	}

	public float getInnerY() {
		return innerArea.getY();
	}

	public float getInnerWidth() {
		return innerArea.getWidth();
	}

	public float getInnerHeight() {
		return innerArea.getHeight();
	}

	public int getInnerRenderX() {
		return innerArea.getRenderX();
	}

	public int getInnerRenderY() {
		return innerArea.getRenderY();
	}

	public int getInnerRenderWidth() {
		return innerArea.getRenderWidth();
	}

	public int getInnerRenderHeight() {
		return innerArea.getRenderHeight();
	}
	
	public int getContentRenderX() {
		return innerArea.getRenderX() + style.getPaddingLeft();
	}
	
	public int getContentRenderY() {
		return innerArea.getRenderY() + style.getPaddingTop();
	}
	
	public int getContentRenderWidth() {
		return innerArea.getRenderWidth() - style.getPaddingLeft() - style.getPaddingRight();
	}
	
	public int getContentRenderHeight() {
		return innerArea.getRenderHeight() - style.getPaddingTop() - style.getPaddingBottom();
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		NodeState previousState = this.state;
		this.state = state;
		if (previousState != state) {
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

	public ParentRenderNode<?, ?> getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return "RenderNode [currentArea=" + outerArea + ", targetArea=" + targetOuterArea + ", parent=" + parent.getId()
				+ ", style=" + style + ", preferredWidth=" + preferredContentWidth + ", preferredHeight="
				+ preferredContentHeight + ", xOffset=" + xOffset + ", yOffset=" + yOffset + "]";
	}
}
