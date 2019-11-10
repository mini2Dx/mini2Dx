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
package org.mini2Dx.ui.element;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.PixelLayoutUtils;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for {@link UiElement}s that can contain child {@link UiElement}s
 */
public abstract class ParentUiElement extends UiElement implements FlexUiElement {
	@Field(optional = true)
	protected final Array<UiElement> children = new Array<UiElement>(true,1, UiElement.class);

	protected final AtomicBoolean asyncRemoveAll = new AtomicBoolean(false);

	@Field(optional = true)
	private String flexLayout = null;
	@Field(optional = true)
	private boolean overflowClipped = false;

	protected ParentRenderNode<?, ?> renderNode;

	/**
	 * Constructor. Generates a unique ID for this {@link ParentUiElement}
	 */
	public ParentUiElement() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link ParentUiElement}
	 */
	public ParentUiElement(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 50f, 50f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ParentUiElement(@ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ParentUiElement(@ConstructorArg(clazz = String.class, name = "id") String id,
				 @ConstructorArg(clazz = Float.class, name = "x") float x,
				 @ConstructorArg(clazz = Float.class, name = "y") float y,
				 @ConstructorArg(clazz = Float.class, name = "width") float width,
				 @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}

	/**
	 * Creates the {@link ParentRenderNode} for this {@link UiElement}
	 * 
	 * @param parent
	 *            The parent of this node
	 * @return A new instance of {@link ParentRenderNode}
	 */
	protected abstract ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent);

	/**
	 * Returns the child {@link UiElement} at the specified index
	 * @param index The index of the child element
	 * @return The {@link UiElement} instance
	 */
	public UiElement get(int index) {
		return children.get(index);
	}

	/**
	 * Adds a {@link UiElement} to this {@link ParentUiElement}
	 * 
	 * @param element
	 *            The {@link UiElement} to be added
	 */
	public void add(UiElement element) {
		if (element == null) {
			throw new MdxException("Cannot add null element to ParentUiElement");
		}
		children.add(element);

		if (renderNode == null) {
			return;
		}
		element.attach(renderNode);
	}

	/**
	 * Inserts a {@link UiElement} at a specific index into this
	 * {@link ParentUiElement} 's child elements
	 * 
	 * @param index
	 *            The index to insert at
	 * @param element
	 *            The {@link UiElement} to be inserted
	 */
	public void add(int index, UiElement element) {
		if (element == null) {
			throw new MdxException("Cannot add null element to ParentUiElement");
		}
		children.insert(index, element);

		if (renderNode == null) {
			return;
		}
		element.attach(renderNode);
	}

	/**
	 * Removes a {@link UiElement} from this {@link ParentUiElement}
	 * 
	 * @param element
	 *            The {@link UiElement} to be removed
	 * @return True if the {@link ParentUiElement} contained the
	 *         {@link UiElement}
	 */
	public boolean remove(UiElement element) {
		if (renderNode != null) {
			element.detach(renderNode);
		}
		return children.removeValue(element, false);
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
		return children.removeIndex(index);
	}

	/**
	 * Removes all children from this {@link ParentUiElement}
	 */
	public void removeAll() {
		for (int i = children.size - 1; i >= 0; i--) {
			UiElement element = children.removeIndex(i);
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

	/**
	 * Returns the child {@link UiElement}
	 * @param i The child index
	 * @return
	 */
	public UiElement getChild(int i) {
		return children.get(i);
	}

	/**
	 * Shrinks the width and height for this element based on its children
	 */
	public void shrinkToContents(boolean recursive) {
		shrinkToContents(recursive, null);
	}

	public void shrinkToContents(boolean recursive, Runnable callback) {
		PixelLayoutUtils.shrinkToContents(this, recursive, callback);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = createRenderNode(parentRenderNode);
		for (int i = 0; i < children.size; i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		for (int i = 0; i < children.size; i++) {
			children.get(i).detach(renderNode);
		}
		parentRenderNode.removeChild(renderNode);
		renderNode.dispose();
		renderNode = null;
	}

	@Override
	public void invokeBeginHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.beginFakeHover();
	}

	@Override
	public void invokeEndHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.endFakeHover();
	}

	/**
	 * Returns if child elements that overflow this element's bounds (e.g. using offsets
	 * or margins) have their rendering clipped to the element's bounds
	 * @return False by default
	 */
	public boolean isOverflowClipped() {
		return overflowClipped;
	}

	/**
	 * Sets if child elements that overflow this element's bounds (e.g. using offsets
	 * or margins) have their rendering clipped to the element's bounds
	 * 
	 * @param overflowClipped True if child elements should have their rendering clipped
	 */
	public void setOverflowClipped(boolean overflowClipped) {
		this.overflowClipped = overflowClipped;
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
		renderNode.setDirty();
	}

	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		while (effects.size > 0) {
			renderNode.applyEffect(effects.removeFirst());
		}
		if (asyncRemoveAll.get()) {
			removeAll();
			asyncRemoveAll.set(false);
		}
		super.syncWithUpdate(rootNode);
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
		renderNode.setDirty();
	}

	@Override
	public void setZIndex(int zIndex) {
		if(this.zIndex == zIndex) {
			return;
		}
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Returns the total number of child elements for this element
	 * 
	 * @return 0 is there are no children
	 */
	public int getTotalChildren() {
		return children.size;
	}

	public String getFlexLayout() {
		return flexLayout;
	}

	public void setFlexLayout(String flexLayout) {
		if(flexLayout == null) {
			return;
		}
		if(this.flexLayout != null && this.flexLayout.equals(flexLayout)) {
			return;
		}
		this.flexLayout = flexLayout;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public UiElement getElementById(String id) {
		if (getId().equals(id)) {
			return this;
		}
		for (int i = 0; i < children.size; i++) {
			UiElement result = children.get(i).getElementById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public boolean isRenderNodeDirty() {
		if (renderNode == null) {
			return true;
		}
		return renderNode.isDirty();
	}

	@Override
	public void setRenderNodeDirty() {
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public boolean isInitialLayoutOccurred() {
		if (renderNode == null) {
			return false;
		}
		return renderNode.isInitialLayoutOccurred();
	}

	@Override
	public boolean isInitialUpdateOccurred() {
		if(renderNode == null) {
			return false;
		}
		return renderNode.isInitialUpdateOccurred();
	}

	@Override
	public int getRenderX() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderX();
	}

	@Override
	public int getRenderY() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderY();
	}

	@Override
	public int getRenderWidth() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderWidth();
	}

	@Override
	public int getRenderHeight() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderHeight();
	}

	@Override
	public boolean isFlexLayout() {
		return flexLayout != null;
	}

	/**
	 * Equivalent to {@link #set(float, float, float, float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 * @param width The width in pixels
	 * @param height The height in pixels
	 */
	public void setFlex(float x, float y, float width, float height) {
		flexLayout = FlexLayoutRuleset.set(flexLayout, x, y, width, height);
	}

	/**
	 * Equivalent to {@link #setXY(float, float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 */
	public void setXYFlex(float x, float y) {
		flexLayout = FlexLayoutRuleset.setXY(flexLayout, x, y);
	}

	/**
	 * Equivalent to {@link #setX(float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 */
	public void setXFlex(float x) {
		flexLayout = FlexLayoutRuleset.setX(flexLayout, x);
	}

	/**
	 * Equivalent to {@link #setY(float)} for usage with flex layout
	 * @param y The y coordinate (in pixels) relative to its parent
	 */
	public void setYFlex(float y) {
		flexLayout = FlexLayoutRuleset.setY(flexLayout, y);
	}

	/**
	 * Equivalent to {@link #setWidth(float)} for usage with flex layout
	 * @param width The width in pixels
	 */
	public void setWidthFlex(float width) {
		flexLayout = FlexLayoutRuleset.setWidth(flexLayout, width);
	}

	/**
	 * Equivalent to {@link #setHeight(float)} for usage with flex layout
	 * @param height The height in pixels
	 */
	public void setHeightFlex(float height) {
		flexLayout = FlexLayoutRuleset.setHeight(flexLayout, height);
	}
}
