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
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.listener.HoverListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.util.IdAllocator;

/**
 * Base class for all user interface elements
 */
@NonConcrete
public abstract class UiElement implements Hoverable {
	private final String id;
	protected final Queue<UiEffect> effects = new LinkedList<UiEffect>();

	protected Visibility visibility = Visibility.HIDDEN;
	protected String styleId = UiTheme.DEFAULT_STYLE_ID;
	@Field(optional = true)
	protected int zIndex = 0;

	private List<HoverListener> hoverListeners;
	private boolean debugEnabled = false;

	/**
	 * Constructor. Generates a unique ID for this element.
	 */
	public UiElement() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this element
	 */
	public UiElement(@ConstructorArg(clazz = String.class, name = "id") String id) {
		if (id == null) {
			id = IdAllocator.getNextId();
		}
		this.id = id;
	}

	/**
	 * Syncs data between the {@link UiElement} and {@link RenderNode}
	 */
	public abstract void syncWithRenderNode();

	/**
	 * Attaches a {@link RenderNode} for this element to a parent
	 * {@link RenderNode}
	 * 
	 * @param parentRenderNode
	 *            The parent {@link RenderNode} to attach to
	 */
	public abstract void attach(ParentRenderNode<?, ?> parentRenderNode);

	/**
	 * Detaches this element's {@link RenderNode} from a parent
	 * {@link RenderNode}
	 * 
	 * @param parentRenderNode
	 *            The parent {@link RenderNode} to detach from
	 */
	public abstract void detach(ParentRenderNode<?, ?> parentRenderNode);

	/**
	 * Applies a {@link UiEffect} to this element
	 * 
	 * @param effect
	 *            The {@link UiEffect} to be applied
	 */
	public void applyEffect(UiEffect effect) {
		effects.offer(effect);
	}

	@Override
	@ConstructorArg(clazz = String.class, name = "id")
	public String getId() {
		return id;
	}

	/**
	 * Returns the current {@link Visibility} of this {@link UiElement}
	 * 
	 * @return
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * Sets the current {@link Visibility} of this {@link UiElement}
	 * 
	 * @param visibility
	 *            The {@link Visibility} to set
	 */
	public abstract void setVisibility(Visibility visibility);

	/**
	 * Returns the current style id of this {@link UiElement}
	 * 
	 * @return {@link UiTheme.DEFAULT_STYLE_ID} by default
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * Sets the style if for this {@link UiElement}
	 * 
	 * @param styleId
	 *            The style id to set
	 */
	public abstract void setStyleId(String styleId);

	/**
	 * Returns the Z index of this {@link UiElement}
	 * 
	 * @return 0 by default
	 */
	public int getZIndex() {
		return zIndex;
	}

	/**
	 * Sets the Z index of this {@link UiElement}. Elements will be rendered on
	 * different Z layers in ascending Z order (negatives values first, positive values last)
	 * 
	 * @param zIndex The Z index
	 */
	public abstract void setZIndex(int zIndex);

	@Override
	public void addHoverListener(HoverListener listener) {
		if (hoverListeners == null) {
			hoverListeners = new ArrayList<HoverListener>(1);
		}
		hoverListeners.add(listener);
	}

	@Override
	public void removeHoverListener(HoverListener listener) {
		if (hoverListeners == null) {
			return;
		}
		hoverListeners.remove(listener);
	}

	/**
	 * Notifies all {@link HoverListener}s of the begin hover event
	 */
	public void notifyHoverListenersOnBeginHover() {
		if (hoverListeners == null) {
			return;
		}
		for (int i = hoverListeners.size() - 1; i >= 0; i--) {
			hoverListeners.get(i).onHoverBegin(this);
		}
	}

	/**
	 * Notifies all {@link HoverListener}s of the end hover event
	 */
	public void notifyHoverListenersOnEndHover() {
		if (hoverListeners == null) {
			return;
		}
		for (int i = hoverListeners.size() - 1; i >= 0; i--) {
			hoverListeners.get(i).onHoverEnd(this);
		}
	}

	/**
	 * Searches the UI for a {@link UiElement} with a given id. Warning: This
	 * can be an expensive operation for complex UIs. It is recommended you
	 * cache results.
	 * 
	 * @param id
	 *            The {@link UiElement} identifier to search for
	 * @return Null if there is no such {@link UiElement} with the given id
	 */
	public UiElement getElementById(String id) {
		if (getId().equals(id)) {
			return this;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UiElement other = (UiElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}
}
