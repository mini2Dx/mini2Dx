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
	protected final CollisionBox currentArea = new CollisionBox();
	protected final Rectangle targetArea = new Rectangle();
	protected final ParentRenderNode<?, ?> parent;
	protected final T element;

	protected S style;
	protected float preferredWidth, preferredHeight;
	protected float xOffset, yOffset;
	private float relativeX, relativeY;
	private boolean dirty;
	private NodeState state = NodeState.NORMAL;

	public RenderNode(ParentRenderNode<?, ?> parent, T element) {
		this.parent = parent;
		this.element = element;
		setDirty(true);
	}

	public void update(UiContainerRenderTree uiContainer, float delta) {
		if(style == null) {
			throw new MdxException("No style found for element: " + getId());
		}
		if (parent == null) {
			targetArea.set(relativeX + style.getMarginLeft(), relativeY + style.getMarginTop(), preferredWidth,
					preferredHeight);
		} else {
			targetArea.set(parent.getX() + relativeX + style.getMarginLeft(),
					parent.getY() + style.getMarginTop() + relativeY, preferredWidth, preferredHeight);
		}
		currentArea.preUpdate();

		element.pushEffectsToRenderNode();

		boolean visible = isIncludedInRender();
		if (effects.size() == 0) {
			currentArea.forceTo(targetArea);
		} else {
			for (int i = 0; i < effects.size(); i++) {
				UiEffect effect = effects.get(i);
				if (effect.isFinished()) {
					effects.remove(i);
					i--;
					continue;
				}

				visible |= effect.update(uiContainer, currentArea, targetArea, delta);
			}
		}
		if (visible) {
			element.setVisibility(Visibility.VISIBLE);
		}
		if (element.isDebugEnabled()) {
			Gdx.app.log(element.getId(), "UPDATE - currentArea: " + currentArea + ", targetArea: " + targetArea + ", visibility: " + element.getVisibility());
		}
	}

	public void interpolate(float alpha) {
		currentArea.interpolate(null, alpha);
	}

	public void render(Graphics g) {
		if (!isIncludedInRender()) {
			if (element.isDebugEnabled()) {
				Gdx.app.log(element.getId(), "RENDER - Element not visible");
			}
			return;
		}
		if (element.isDebugEnabled()) {
			Gdx.app.log(element.getId(), "RENDER - x,y: " + getRenderX() + "," + getRenderY() + " width: "
					+ getRenderWidth() + ", height: " + getRenderHeight());
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
		if (currentArea.contains(screenX, screenY)) {
			beginHover();
			return true;
		} else if (state != NodeState.NORMAL) {
			endHover();
		}
		return false;
	}

	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		return null;
	}

	public void mouseUp(int screenX, int screenY, int pointer, int button) {
	}

	public boolean contains(float screenX, float screenY) {
		return currentArea.contains(screenX, screenY);
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

	protected abstract float determinePreferredWidth(LayoutState layoutState);

	protected abstract float determinePreferredHeight(LayoutState layoutState);

	protected abstract float determineXOffset(LayoutState layoutState);

	protected abstract float determineYOffset(LayoutState layoutState);

	public void layout(LayoutState layoutState) {
		if(!isDirty() && !layoutState.isScreenSizeChanged()) {
			return;
		}
		style = determineStyleRule(layoutState);

		switch (element.getVisibility()) {
		case HIDDEN:
			preferredWidth = 0f;
			preferredHeight = 0f;
			xOffset = 0f;
			yOffset = 0f;
			return;
		default:
			preferredWidth = determinePreferredWidth(layoutState);
			preferredHeight = determinePreferredHeight(layoutState);
			xOffset = determineXOffset(layoutState);
			yOffset = determineYOffset(layoutState);
			break;
		}
		dirty = false;
	}

	public boolean isIncludedInLayout() {
		return element.getVisibility() != Visibility.HIDDEN;
	}

	public boolean isIncludedInRender() {
		return style != null && element.getVisibility() == Visibility.VISIBLE;
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

	public float getPreferredWidth() {
		return preferredWidth;
	}

	public float getPreferredHeight() {
		return preferredHeight;
	}

	public float getPreferredContentWidth() {
		return preferredWidth - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft()
				- style.getMarginRight();
	}

	public float getPreferredContentHeight() {
		return preferredHeight - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
				- style.getMarginBottom();
	}

	public float getXOffset() {
		return xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public float getX() {
		return currentArea.getX();
	}

	public float getY() {
		return currentArea.getY();
	}

	public float getWidth() {
		return currentArea.getWidth();
	}

	public float getHeight() {
		return currentArea.getHeight();
	}

	public int getRenderX() {
		return currentArea.getRenderX();
	}

	public int getRenderY() {
		return currentArea.getRenderY();
	}

	public int getRenderWidth() {
		return currentArea.getRenderWidth();
	}

	public int getRenderHeight() {
		return currentArea.getRenderHeight();
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

	public RenderNode<?, ?> getElementById(String id) {
		if (element.getId().equals(id)) {
			return this;
		}
		return null;
	}

	public String getId() {
		return element.getId();
	}

	@Override
	public String toString() {
		return "RenderNode [currentArea=" + currentArea + ", targetArea=" + targetArea + ", parent=" + parent.getId()
				+ ", style=" + style + ", preferredWidth=" + preferredWidth + ", preferredHeight=" + preferredHeight
				+ ", xOffset=" + xOffset + ", yOffset=" + yOffset + "]";
	}
}
