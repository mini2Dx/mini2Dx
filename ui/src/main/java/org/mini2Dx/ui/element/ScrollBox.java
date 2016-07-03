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

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.ScrollBoxRenderNode;

/**
 * A scrollable view for {@link UiElement}s
 */
public class ScrollBox extends Column {
	private static final float DEFAULT_SCROLL_FACTOR = 0.005f;

	@Field(optional=true)
	private float scrollFactor = DEFAULT_SCROLL_FACTOR;
	@Field(optional=true)
	private float maxHeight = Float.MAX_VALUE;

	/**
	 * Constructor. Generates a unique ID for this {@link ScrollBox}
	 */
	public ScrollBox() {
		this(null);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this {@link ScrollBox}
	 */
	public ScrollBox(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new ScrollBoxRenderNode(parentRenderNode, this);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	/**
	 * Returns the maximum height for this {@link ScrollBox}
	 * @return {@link Float#MAX_VALUE} by default
	 */
	public float getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Sets the maximum height for this {@link ScrollBox}
	 * @param maxHeight The maximum height to set
	 */
	public void setMaxHeight(float maxHeight) {
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
	 * Sets how much this {@link ScrollBox} scrolls when the up/down buttons
	 * are clicked
	 * 
	 * @param scrollFactor A % between 0.0 and 1.0
	 */
	public void setScrollFactor(float scrollFactor) {
		this.scrollFactor = scrollFactor;
	}
}
