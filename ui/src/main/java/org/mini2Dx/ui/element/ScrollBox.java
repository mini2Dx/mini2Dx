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

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.animation.ScrollTo;
import org.mini2Dx.ui.layout.PixelLayoutUtils;
import org.mini2Dx.ui.listener.ScrollListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.ScrollBoxRenderNode;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.render.UiContainerRenderTree;

/**
 * A scrollable view for {@link UiElement}s
 */
public class ScrollBox extends Div {
	private static final float DEFAULT_SCROLL_FACTOR = 0.005f;

	@Field(optional = true)
	private float scrollFactor = DEFAULT_SCROLL_FACTOR;
	@Field(optional = true)
	private float minHeight = Float.MIN_VALUE;
	@Field(optional = true)
	private float maxHeight = Float.MAX_VALUE;
	@Field(optional = true)
	private Visibility scrollTrackVisibility = Visibility.VISIBLE;

	private final Queue<ScrollTo> scrollTos = new Queue<ScrollTo>();
	private Array<ScrollListener> scrollListeners;

	private float scrollContentHeight;

	/**
	 * Constructor. Generates a unique ID for this {@link ScrollBox}
	 */
	public ScrollBox() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link ScrollBox}
	 */
	public ScrollBox(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 300f, 300f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ScrollBox(@ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ScrollBox(@ConstructorArg(clazz = String.class, name = "id") String id,
						   @ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
		scrollContentHeight = height;
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new ScrollBoxRenderNode(parent, this);
	}

	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		super.syncWithUpdate(rootNode);

		if (renderNode == null) {
			return;
		}
		scrollContentHeight = ((ScrollBoxRenderNode) renderNode).getScrollContentHeight();

		if(scrollTos.size == 0) {
			return;
		}
		final ScrollTo scrollTo = scrollTos.first();
		if (((ScrollBoxRenderNode) renderNode).offerScrollTo(scrollTo)) {
			scrollTos.removeFirst();
		}
	}

	/**
	 * Moves the scroll thumb until the {@link UiElement} is visible
	 * 
	 * @param element
	 *            The {@link UiElement} to scroll to
	 * @param immediate
	 *            True if the {@link ScrollBox} should "jump" to the element.
	 *            False if the scrolling should be smooth.
	 */
	public void scrollTo(UiElement element, boolean immediate) {
		scrollTos.addLast(new ScrollTo(element, immediate));
	}

	/**
	 * Scrolls to the first element in the {@link ScrollBox}
	 * 
	 * @param immediate
	 *            True if the {@link ScrollBox} should "jump" to the element.
	 *            False if the scrolling should be smooth.
	 */
	public void scrollToTop(boolean immediate) {
		if (children.size == 0) {
			return;
		}
		scrollTos.addLast(new ScrollTo(children.get(0), immediate));
	}

	/**
	 * Scrolls to the last element in the {@link ScrollBox}
	 * 
	 * @param immediate
	 *            True if the {@link ScrollBox} should "jump" to the element.
	 *            False if the scrolling should be smooth.
	 */
	public void scrollToBottom(boolean immediate) {
		if (children.size == 0) {
			return;
		}
		scrollTos.addLast(new ScrollTo(children.get(children.size - 1), immediate));
	}

	/**
	 * Returns the minimum height for this {@link ScrollBox}. When set to
	 * {@link Float#MIN_VALUE} the value will be taken from the style.
	 * 
	 * @return {@link Float#MIN_VALUE} by default
	 */
	public float getMinHeight() {
		return minHeight;
	}

	/**
	 * Sets the minimum height for this {@link ScrollBox}. Setting this value
	 * overrides the style's minHeight value.
	 * 
	 * @param minHeight
	 *            The minimum height to set
	 */
	public void setMinHeight(float minHeight) {
		if(MathUtils.isEqual(this.minHeight, minHeight, MathUtils.FLOAT_ROUNDING_ERROR)) {
			return;
		}
		
		this.minHeight = minHeight;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Returns the maximum height for this {@link ScrollBox}
	 * 
	 * @return {@link Float#MAX_VALUE} by default
	 */
	public float getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Sets the maximum height for this {@link ScrollBox}
	 * 
	 * @param maxHeight
	 *            The maximum height to set
	 */
	public void setMaxHeight(float maxHeight) {
		if(MathUtils.isEqual(this.maxHeight, maxHeight, MathUtils.FLOAT_ROUNDING_ERROR)) {
			return;
		}
		
		this.maxHeight = maxHeight;
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Returns how much this {@link ScrollBox} scrolls when the up/down buttons
	 * are clicked (expressed as a % between 0.0 and 1.0)
	 * 
	 * @return {@link #DEFAULT_SCROLL_FACTOR} by default
	 */
	public float getScrollFactor() {
		return scrollFactor;
	}

	/**
	 * Sets how much this {@link ScrollBox} scrolls when the up/down buttons are
	 * clicked
	 * 
	 * @param scrollFactor
	 *            A % between 0.0 and 1.0
	 */
	public void setScrollFactor(float scrollFactor) {
		this.scrollFactor = scrollFactor;
	}

	/**
	 * Adds a {@link ScrollListener} to this {@link ScrollBox}
	 * 
	 * @param listener
	 *            The {@link ScrollListener} to add
	 */
	public void addScrollListener(ScrollListener listener) {
		if (scrollListeners == null) {
			scrollListeners = new Array<ScrollListener>(true, 1, ScrollListener.class);
		}
		scrollListeners.add(listener);
	}

	/**
	 * Removes a {@link ScrollListener} from this {@link ScrollBox}
	 * 
	 * @param listener
	 *            The {@link ScrollListener} to remove
	 */
	public void removeScrollListener(ScrollListener listener) {
		if (scrollListeners == null) {
			return;
		}
		scrollListeners.removeValue(listener, false);
	}

	/**
	 * Notifies all {@link ScrollListener} instances that a scroll occurred on
	 * this {@link ScrollBox}
	 * 
	 * @param scrollThumbPosition
	 *            The position of the scroll thumb (between 0.0 and 1.0)
	 */
	public void notifyScrollListeners(float scrollThumbPosition) {
		if (scrollListeners == null) {
			return;
		}
		for (int i = scrollListeners.size - 1; i >= 0; i--) {
			scrollListeners.get(i).onScroll(this, scrollThumbPosition);
		}
	}

	/**
	 * Returns the height of the content within the scroll view
	 * @return
	 */
	public float getScrollContentHeight() {
		return scrollContentHeight;
	}

	/**
	 * Sets the scroll view height. Note: If using flexLayout this will be overriden during layout()
	 * @param scrollContentHeight
	 */
	public void setScrollContentHeight(float scrollContentHeight) {
		if(MathUtils.isEqual(scrollContentHeight, this.scrollContentHeight)) {
			return;
		}

		this.scrollContentHeight = scrollContentHeight;
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	public Visibility getScrollTrackVisibility() {
		return scrollTrackVisibility;
	}

	public void setScrollTrackVisibility(Visibility scrollTrackVisibility) {
		if(scrollTrackVisibility == null) {
			return;
		}
		this.scrollTrackVisibility = scrollTrackVisibility;
	}

	/**
	 * Sets the scroll view height to be equal to the max Y of the child elements
	 */
	public void resizeScrollContentHeightToContents() {
		PixelLayoutUtils.resizeScrollContentHeightToContents(this);
	}
}
