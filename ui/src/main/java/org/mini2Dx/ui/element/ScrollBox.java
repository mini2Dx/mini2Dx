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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.animation.ScrollTo;
import org.mini2Dx.ui.listener.ScrollListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.ScrollBoxRenderNode;

import com.badlogic.gdx.math.MathUtils;

/**
 * A scrollable view for {@link UiElement}s
 */
public class ScrollBox extends Column {
	private static final float DEFAULT_SCROLL_FACTOR = 0.005f;

	@Field(optional = true)
	private float scrollFactor = DEFAULT_SCROLL_FACTOR;
	@Field(optional = true)
	private float minHeight = Float.MIN_VALUE;
	@Field(optional = true)
	private float maxHeight = Float.MAX_VALUE;

	private final Queue<ScrollTo> scrollTos = new LinkedList<ScrollTo>();
	private List<ScrollListener> scrollListeners;

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
		super(id);
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new ScrollBoxRenderNode(parent, this);
	}

	@Override
	public void syncWithRenderNode() {
		super.syncWithRenderNode();

		if (renderNode == null) {
			return;
		}
		ScrollTo scrollTo = scrollTos.peek();
		if (scrollTo == null) {
			return;
		}
		if (((ScrollBoxRenderNode) renderNode).offerScrollTo(scrollTo)) {
			scrollTos.poll();
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
		scrollTos.offer(new ScrollTo(element, immediate));
	}

	/**
	 * Scrolls to the first element in the {@link ScrollBox}
	 * 
	 * @param immediate
	 *            True if the {@link ScrollBox} should "jump" to the element.
	 *            False if the scrolling should be smooth.
	 */
	public void scrollToTop(boolean immediate) {
		if (children.size() == 0) {
			return;
		}
		scrollTos.offer(new ScrollTo(children.get(0), immediate));
	}

	/**
	 * Scrolls to the last element in the {@link ScrollBox}
	 * 
	 * @param immediate
	 *            True if the {@link ScrollBox} should "jump" to the element.
	 *            False if the scrolling should be smooth.
	 */
	public void scrollToBottom(boolean immediate) {
		if (children.size() == 0) {
			return;
		}
		scrollTos.offer(new ScrollTo(children.get(children.size() - 1), immediate));
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
		renderNode.setDirty(true);
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
		renderNode.setDirty(true);
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
			scrollListeners = new ArrayList<ScrollListener>(1);
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
		scrollListeners.remove(listener);
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
		for (int i = scrollListeners.size() - 1; i >= 0; i--) {
			scrollListeners.get(i).onScroll(this, scrollThumbPosition);
		}
	}
}
