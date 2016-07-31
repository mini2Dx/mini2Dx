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
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.render.AbstractColumnRenderNode;
import org.mini2Dx.ui.render.ColumnRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 * A column of {@link UiElement}s. A column can be multiple columns wide with
 * {@link UiElement}s added horizontally and wrapping as required.
 */
public class Column extends UiElement {
	@Field(optional = true)
	protected final List<UiElement> children = new ArrayList<UiElement>(1);

	protected final Queue<UiElement> asyncAddQueue = new ConcurrentLinkedQueue<UiElement>();
	protected final Queue<UiElement> asyncRemoveQueue = new ConcurrentLinkedQueue<UiElement>();
	protected final AtomicBoolean asyncRemoveAll = new AtomicBoolean(false);
	
	protected AbstractColumnRenderNode<?> renderNode;
	
	@Field(optional=true)
	private String layout = LayoutRuleset.DEFAULT_LAYOUT;

	/**
	 * Constructor. Generates a unique ID for this {@link Column}
	 */
	public Column() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link Column}
	 */
	public Column(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	/**
	 * Adds a {@link UiElement} to this {@link Column}
	 * 
	 * @param element
	 *            The {@link UiElement} to be added
	 */
	public void add(UiElement element) {
		if (element == null) {
			throw new MdxException("Cannot add null element to Column");
		}
		children.add(element);
		if (renderNode == null) {
			return;
		}
		element.attach(renderNode);
	}

	/**
	 * Inserts a {@link UiElement} at a specific index into this {@link Column}
	 * 's child elements
	 * 
	 * @param index
	 *            The index to insert at
	 * @param element
	 *            The {@link UiElement} to be inserted
	 */
	public void add(int index, UiElement element) {
		if (element == null) {
			throw new MdxException("Cannot add null element to Column");
		}
		children.add(index, element);
		if (renderNode == null) {
			return;
		}
		element.attach(renderNode);
	}

	/**
	 * Adds a {@link UiElement} safely from a non-OpenGL thread
	 * 
	 * @param element
	 *            The {@link UiElement} to be added
	 */
	public void addAsync(UiElement element) {
		asyncAddQueue.offer(element);
	}

	/**
	 * Removes a {@link UiElement} from this {@link Column}
	 * 
	 * @param element
	 *            The {@link UiElement} to be removed
	 * @return True if the {@link Column} contained the {@link UiElement}
	 */
	public boolean remove(UiElement element) {
		if (renderNode != null) {
			element.detach(renderNode);
		}
		return children.remove(element);
	}
	
	/**
	 * Removes a {@link UiElement} safely from a non-OpenGL thread
	 * 
	 * @param element
	 *            The {@link UiElement} to be remove
	 */
	public void removeAsync(UiElement element) {
		asyncRemoveQueue.offer(element);
	}

	/**
	 * Removes a child {@link UiElement} at a specific index
	 * 
	 * @param index
	 *            The index to remove at
	 * @return The {@link UiElement} that was removed
	 */
	public UiElement remove(int index) {
		if (renderNode != null) {
			children.get(index).detach(renderNode);
		}
		return children.remove(index);
	}

	/**
	 * Removes all children from this {@link Column}
	 */
	public void removeAll() {
		for (int i = children.size() - 1; i >= 0; i--) {
			UiElement element = children.remove(i);
			if (renderNode != null) {
				element.detach(renderNode);
			}
		}
	}
	
	/**
	 * Removes all children safely from a non-OpenGL thread
	 */
	public void removeAllAsync() {
		asyncRemoveAll.set(true);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new ColumnRenderNode(parentRenderNode, this);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		for (int i = 0; i < children.size(); i++) {
			children.get(i).detach(renderNode);
		}
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
	}

	/**
	 * Returns the current layout
	 * @return
	 */
	public String getLayout() {
		return layout;
	}
	
	/**
	 * Sets the current layout
	 * @param layout
	 */
	public void setLayout(String layout) {
		if (layout == null) {
			return;
		}
		this.layout = layout;
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void setVisibility(Visibility visibility) {
		if (this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
		if(asyncRemoveAll.get()) {
			removeAll();
			asyncRemoveAll.set(false);
		}
		while (!asyncAddQueue.isEmpty()) {
			add(asyncAddQueue.poll());
		}
		while (!asyncRemoveQueue.isEmpty()) {
			remove(asyncRemoveQueue.poll());
		}
	}

	@Override
	public void setStyleId(String styleId) {
		if (styleId == null) {
			return;
		}
		if (this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public UiElement getElementById(String id) {
		if (getId().equals(id)) {
			return this;
		}
		for (int i = 0; i < children.size(); i++) {
			UiElement result = children.get(i).getElementById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Creates a {@link Visibility#VISIBLE} {@link Column} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param elements The {@link UiElement}s to add to the {@link Column}
	 * @return A new {@link Column} containing the {@link UiElement}s
	 */
	public static Column withElements(UiElement... elements) {
		return withElements(null, elements);
	}

	/**
	 * Creates a {@link Visibility#VISIBLE} {@link Column} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param columnId The unique ID of the {@link Column}
	 * @param elements The {@link UiElement}s to add to the {@link Column}
	 * @return A new {@link Column} containing the {@link UiElement}s
	 */
	public static Column withElements(String columnId, UiElement... elements) {
		Column result = new Column(columnId);
		for (int i = 0; i < elements.length; i++) {
			result.add(elements[i]);
		}
		result.setVisibility(Visibility.VISIBLE);
		return result;
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
}
