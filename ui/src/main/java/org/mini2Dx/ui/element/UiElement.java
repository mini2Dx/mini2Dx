/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.element;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.effect.UiEffect;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.PixelLayoutUtils;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.listener.HoverListener;
import org.mini2Dx.ui.listener.NodeStateListener;
import org.mini2Dx.ui.listener.UiEffectListener;
import org.mini2Dx.ui.render.NodeState;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.util.DeferredRunnable;
import org.mini2Dx.ui.util.IdAllocator;

/**
 * Base class for all user interface elements
 */
@NonConcrete
public abstract class UiElement implements Hoverable {
	private final String id;
	protected final Queue<UiEffect> effects = new Queue<UiEffect>();

	protected final Array<DeferredRunnable> deferredLayout = new Array<DeferredRunnable>(true,1, DeferredRunnable.class);
	protected final Array<DeferredRunnable> deferredUpdate = new Array<DeferredRunnable>(true,1, DeferredRunnable.class);
	protected final Array<DeferredRunnable> deferredRender = new Array<DeferredRunnable>(true,1, DeferredRunnable.class);

	@Field(optional = true)
	protected Visibility visibility = UiContainer.getDefaultVisibility();
	@Field(optional = true)
	protected String styleId = UiTheme.DEFAULT_STYLE_ID;
	@Field(optional = true)
	protected int zIndex = 0;

	protected float x;
	protected float y;
	protected float width;
	protected float height;

	private Array<NodeStateListener> nodeStateListeners;
	private Array<UiEffectListener> effectListeners;
	private Array<HoverListener> hoverListeners;
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
	 *            The unique ID for this element (if null an ID will be generated)
	 */
	public UiElement(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 50f, 50f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public UiElement(@ConstructorArg(clazz = Float.class, name = "x") float x,
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
	public UiElement(@ConstructorArg(clazz = String.class, name = "id") String id,
					 @ConstructorArg(clazz = Float.class, name = "x") float x,
					 @ConstructorArg(clazz = Float.class, name = "y") float y,
					 @ConstructorArg(clazz = Float.class, name = "width") float width,
					 @ConstructorArg(clazz = Float.class, name = "height") float height) {
		if (id == null || id.isEmpty()) {
			id = IdAllocator.getNextId();
		}
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract boolean isInitialLayoutOccurred();

	public abstract boolean isInitialUpdateOccurred();

	/**
	 * Returns if the {@link UiElement} has been initialised within the render tree
	 * @return False if a layout() and update() call has not yet occurred on the element
	 */
	public boolean isInitialised() {
		return isInitialLayoutOccurred() && isInitialUpdateOccurred();
	}

	public abstract boolean isRenderNodeDirty();

	public abstract void setRenderNodeDirty();

	/**
	 * Syncs data between the {@link UiElement} and {@link RenderNode} during UI layout
	 */
	public void syncWithLayout(UiContainerRenderTree rootNode) {
		rootNode.transferLayoutDeferred(deferredLayout);
	}

	/**
	 * Syncs data between the {@link UiElement} and {@link RenderNode} during update
	 */
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		rootNode.transferUpdateDeferred(deferredUpdate);
	}

	/**
	 * Syncs data between the {@link UiElement} and {@link RenderNode} during UI render
	 */
	public void syncWithRender(UiContainerRenderTree rootNode) {
		rootNode.transferRenderDeferred(deferredRender);
	}

	/**
	 * Aligns the edges of this {@link UiElement} to the edges of another element.
	 *
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the right-side of this element to the left side of the align element.
	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	{@link HorizontalAlignment#RIGHT} aligns the left-side of this element to the right-side of the align element.
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the bottom-side of this element to the top-side of the align element.
	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	{@link VerticalAlignment#BOTTOM} aligns the top-side of this element to the bottom-side of the align element.
	 */
	public void alignEdgeToEdge(UiElement alignToElement, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		PixelLayoutUtils.alignEdgeToEdge(this, alignToElement, horizontalAlignment, verticalAlignment);
	}

	/**
	 * Aligns the right edge of this element to the left edge of another element
	 *
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the top-side of this element to the top-side of the align element.
	 * 	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	 * 	{@link VerticalAlignment#BOTTOM} aligns the bottom-side of this element to the bottom-side of the align element.
	 */
	public void alignLeftOf(final UiElement alignToElement, final VerticalAlignment verticalAlignment) {
		PixelLayoutUtils.alignLeftOf(this, alignToElement, verticalAlignment);
	}

	/**
	 * Aligns the left edge of this element to the right edge of another element
	 *
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the top-side of this element to the top-side of the align element.
	 * 	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	 * 	{@link VerticalAlignment#BOTTOM} aligns the bottom-side of this element to the bottom-side of the align element.
	 */
	public void alignRightOf(final UiElement alignToElement, final VerticalAlignment verticalAlignment) {
		PixelLayoutUtils.alignRightOf(this, alignToElement, verticalAlignment);
	}

	/**
	 * Aligns the top edge of this element to the bottom of another element
	 *
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the left-side of this element to the left side of the align element.
	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	{@link HorizontalAlignment#RIGHT} aligns the right-side of this element to the right-side of the align element.
	 */
	public void alignBelow(final UiElement alignToElement, final HorizontalAlignment horizontalAlignment) {
		PixelLayoutUtils.alignBelow(this, alignToElement, horizontalAlignment);
	}

	/**
	 * Aligns the bottom edge of this element to the top of another element
	 *
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the left-side of this element to the left side of the align element.
	 * 	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	 * 	{@link HorizontalAlignment#RIGHT} aligns the right-side of this element to the right-side of the align element.
	 */
	public void alignAbove(final UiElement alignToElement, final HorizontalAlignment horizontalAlignment) {
		PixelLayoutUtils.alignAbove(this, alignToElement, horizontalAlignment);
	}

	/**
	 * Snaps this {@link UiElement} to the top-left corner of another {@link UiElement}
	 * @param snapToElement The {@link UiElement} to snap to. Note: This can also be the {@link UiContainer}
	 */
	public void snapTo(UiElement snapToElement) {
		PixelLayoutUtils.snapTo(this, snapToElement);
	}

	/**
	 * Snaps this {@link UiElement} to the same area of another element.
	 *
	 * @param snapToElement The {@link UiElement} to snap to. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment The {@link HorizontalAlignment} of this element within the area of the align element
	 * @param verticalAlignment The {@link VerticalAlignment} of this element within the area of the align element
	 */
	public void snapTo(UiElement snapToElement, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		PixelLayoutUtils.snapTo(this, snapToElement, horizontalAlignment, verticalAlignment);
	}

	/**
	 * Sets the width of this element to match the width of another {@link UiElement}
	 * @param otherElement The {@link UiElement} to get the width of
	 */
	public void setWidthToWidthOf(UiElement otherElement) {
		PixelLayoutUtils.setWidthToWidth(this, otherElement);
	}

	/**
	 * Sets the width of this element to match the content width of another {@link UiElement}. See {@link #getContentWidth()}
	 * @param otherElement The {@link UiElement} to get the content width of
	 */
	public void setWidthToContentWidthOf(UiElement otherElement) {
		PixelLayoutUtils.setWidthToContentWidth(this, otherElement);
	}

	/**
	 * Sets the height of this element to match the height of another {@link UiElement}
	 * @param otherElement The {@link UiElement} to get the height of
	 */
	public void setHeightToHeightOf(UiElement otherElement) {
		PixelLayoutUtils.setHeightToHeight(this, otherElement);
	}

	/**
	 * Sets the height of this element to match the content height of another {@link UiElement}. See {@link #getContentHeight()}
	 * @param otherElement The {@link UiElement} to get the content height of
	 */
	public void setHeightToContentHeightOf(UiElement otherElement) {
		PixelLayoutUtils.setHeightToContentHeight(this, otherElement);
	}

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
		effects.addLast(effect);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance until the next frame update
	 * @param runnable The {@link Runnable} to execute
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilUpdate(Runnable runnable) {
		return deferUntilUpdate(runnable, 0f);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance for a period of time
	 * @param runnable The {@link Runnable} to execute
	 * @param duration The time to wait (in seconds) until executing the {@link Runnable}
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilUpdate(Runnable runnable, float duration) {
		return deferUntilUpdate(runnable, duration, false);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance for a period of time
	 * @param runnable The {@link Runnable} to execute
	 * @param skipQueue True if the task should skip to the front of the deferred queue
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilUpdate(Runnable runnable, boolean skipQueue) {
		return deferUntilUpdate(runnable, 0f, skipQueue);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance for a period of time
	 * @param runnable The {@link Runnable} to execute
	 * @param duration The time to wait (in seconds) until executing the {@link Runnable}
	 * @param skipQueue True if the task should skip to the front of the deferred queue
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilUpdate(Runnable runnable, float duration, boolean skipQueue) {
		DeferredRunnable result = DeferredRunnable.allocate(runnable, duration);
		if(skipQueue && deferredUpdate.size > 0) {
			deferredUpdate.insert(0, result);
		} else {
			deferredUpdate.add(result);
		}
		return result;
	}

	/**
	 * Defers the execution of a {@link Runnable} instance until the next UI re-layout completes
	 * @param runnable The {@link Runnable} to execute
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilLayout(Runnable runnable) {
		return deferUntilLayout(runnable, false);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance until the next UI re-layout completes
	 * @param runnable The {@link Runnable} to execute
	 * @param skipQueue True if the task should skip to the front of the deferred queue
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilLayout(Runnable runnable, boolean skipQueue) {
		DeferredRunnable result = DeferredRunnable.allocate(runnable, 0f);
		if(skipQueue && deferredLayout.size > 0) {
			deferredLayout.insert(0, result);
		} else {
			deferredLayout.add(result);
		}
		return result;
	}

	/**
	 * Defers the execution of a {@link Runnable} instance until the UI render completes
	 * @param runnable The {@link Runnable} to execute
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilRender(Runnable runnable) {
		return deferUntilRender(runnable, false);
	}

	/**
	 * Defers the execution of a {@link Runnable} instance until the UI render completes
	 * @param runnable The {@link Runnable} to execute
	 * @param skipQueue True if the task should skip to the front of the deferred queue
	 * @return A {@link DeferredRunnable} that can be cancelled
	 */
	public DeferredRunnable deferUntilRender(Runnable runnable, boolean skipQueue) {
		DeferredRunnable result = DeferredRunnable.allocate(runnable, 0f);
		if(skipQueue && deferredRender.size > 0) {
			deferredRender.insert(0, result);
		} else {
			deferredRender.add(result);
		}
		return result;
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
	 * @return {@link UiTheme#DEFAULT_STYLE_ID} by default
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
			hoverListeners = new Array<HoverListener>(true,1, HoverListener.class);
		}
		hoverListeners.add(listener);
	}

	@Override
	public void removeHoverListener(HoverListener listener) {
		if (hoverListeners == null) {
			return;
		}
		hoverListeners.removeValue(listener, false);
	}

	/**
	 * Notifies all {@link HoverListener}s of the begin hover event
	 */
	public void notifyHoverListenersOnBeginHover() {
		if (hoverListeners == null) {
			return;
		}
		for (int i = hoverListeners.size - 1; i >= 0; i--) {
			hoverListeners.get(i).onHoverBegin(this);
		}
	}

	/**
	 * Adds a {@link UiEffectListener} to this {@link UiElement}
	 * @param listener The {@link UiEffectListener} to add
	 */
	public void addEffectListener(UiEffectListener listener) {
		if (effectListeners == null) {
			effectListeners = new Array<UiEffectListener>(true,1, UiEffectListener.class);
		}
		effectListeners.add(listener);
	}

	/**
	 * Removes a {@link UiEffectListener} from this {@link UiElement}
	 * @param listener The {@link UiEffectListener} to remove
	 */
	public void removeEffectListener(UiEffectListener listener) {
		if (effectListeners == null) {
			return;
		}
		effectListeners.removeValue(listener, false);
	}

	/**
	 * Notifies all {@link HoverListener}s of the end hover event
	 */
	public void notifyHoverListenersOnEndHover() {
		if (hoverListeners == null) {
			return;
		}
		for (int i = hoverListeners.size - 1; i >= 0; i--) {
			hoverListeners.get(i).onHoverEnd(this);
		}
	}

	/**
	 * Notifies all {@link UiEffectListener}s of the finished event
	 * @param effect The {@link UiEffect} that finished
	 */
	public void notifyEffectListenersOnFinished(UiEffect effect) {
		if (effectListeners == null) {
			return;
		}
		for (int i = effectListeners.size - 1; i >= 0; i--) {
			effectListeners.get(i).onEffectFinished(this, effect);
		}
	}

	public void addNodeStateListener(NodeStateListener listener) {
		if(nodeStateListeners == null) {
			nodeStateListeners = new Array<NodeStateListener>(true, 1, NodeStateListener.class);
		}
		nodeStateListeners.add(listener);
	}

	public void removeNodeStateListener(NodeStateListener listener) {
		if(nodeStateListeners == null) {
			return;
		}
		nodeStateListeners.removeValue(listener, false);
	}

	public void notifyNodeStateListeners(NodeState nodeState) {
		if(nodeStateListeners == null) {
			return;
		}
		for (int i = nodeStateListeners.size - 1; i >= 0; i--) {
			nodeStateListeners.get(i).onNodeStateChanged(this, nodeState);
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

	@ConstructorArg(clazz = Float.class, name = "x")
	public float getX() {
		return x;
	}

	@ConstructorArg(clazz = Float.class, name = "y")
	public float getY() {
		return y;
	}

	@ConstructorArg(clazz = Float.class, name = "width")
	public float getWidth() {
		return width;
	}

	@ConstructorArg(clazz = Float.class, name = "height")
	public float getHeight() {
		return height;
	}

	/**
	 * Sets the x, y, width and height of this element
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 * @param width The width in pixels
	 * @param height The height in pixels
	 * @return True if the x or y coordinate or width or height changed
	 */
	public boolean set(final float x, final float y, final float width, final float height) {
		if (MathUtils.isEqual(this.x, x) && MathUtils.isEqual(this.y, y)
				&& MathUtils.isEqual(this.width, width) && MathUtils.isEqual(this.height, height)) {
			return false;
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Sets the x and y coordinates of this element
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 * @return True if the x or y coordinate changed
	 */
	public boolean setXY(final float x, final float y) {
		if (MathUtils.isEqual(this.x, x) && MathUtils.isEqual(this.y, y)) {
			return false;
		}
		this.x = x;
		this.y = y;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Sets the x coordinate of this element.
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @return True if the x coordinate changed
	 */
	public boolean setX(final float x) {
		if (MathUtils.isEqual(this.x, x)) {
			return false;
		}
		this.x = x;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Sets the y coordinate of this element
	 * @param y The y coordinate (in pixels) relative to its parent
	 * @return True if the y coordinate changed
	 */
	public boolean setY(final float y) {
		if (MathUtils.isEqual(this.y, y)) {
			return false;
		}
		this.y = y;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Sets the width of this element
	 * @param width The width in pixels
	 * @return True if the width changed
	 */
	public boolean setWidth(final float width) {
		if (MathUtils.isEqual(this.width, width)) {
			return false;
		}
		this.width = width;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Sets the height of this element
	 * @param height The height in pixels
	 * @return True if the height changed
	 */
	public boolean setHeight(final float height) {
		if (MathUtils.isEqual(this.height, height)) {
			return false;
		}
		this.height = height;

		setRenderNodeDirty();
		return true;
	}

	/**
	 * Returns the width of this element minus its margin and padding
	 * @return
	 */
	public float getContentWidth() {
		return width - getMarginLeft() - getMarginRight() - getPaddingLeft() - getPaddingRight();
	}

	/**
	 * Returns the height of this element minus its margin and padding
	 * @return
	 */
	public float getContentHeight() {
		return height - getMarginTop() - getMarginBottom() - getPaddingTop() - getPaddingBottom();
	}

	/**
	 * Sets the content width. See: {@link #getContentWidth()}
	 * @param contentWidth
	 * @return True if the width changed
	 */
	public boolean setContentWidth(final float contentWidth) {
		return setWidth(contentWidth + getMarginLeft() + getMarginRight() + getPaddingLeft() + getPaddingRight());
	}

	/**
	 * Sets the content height. See: {@link #getContentHeight()}
	 * @param contentHeight
	 * @return True if the height changed
	 */
	public boolean setContentHeight(final float contentHeight) {
		return setHeight(contentHeight + getMarginTop() + getMarginBottom() + getPaddingTop() + getPaddingBottom());
	}

	public abstract StyleRule getStyleRule();

	public int getMarginTop() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getMarginTop();
	}

	public int getMarginBottom() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getMarginBottom();
	}

	public int getMarginLeft() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getMarginLeft();
	}

	public int getMarginRight() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getMarginRight();
	}

	public int getPaddingTop() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getPaddingTop();
	}

	public int getPaddingBottom() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getPaddingBottom();
	}

	public int getPaddingLeft() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getPaddingLeft();
	}

	public int getPaddingRight() {
		final StyleRule styleRule = getStyleRule();
		if(styleRule == null) {
			return 0;
		}
		return styleRule.getPaddingRight();
	}

	/**
	 * Returns X coordinate of where this element is currently rendering
	 * @return {@link Integer#MIN_VALUE} if not rendering
	 */
	public abstract int getRenderX();

	/**
	 * Returns Y coordinate of where this element is currently rendering
	 * @return {@link Integer#MIN_VALUE} if not rendering
	 */
	public abstract int getRenderY();

	/**
	 * Returns the width this element is currently rendering at
	 * @return -1 if not rendering
	 */
	public abstract int getRenderWidth();

	/**
	 * Returns the height this element is currently rendering at
	 * @return -1 if not rendering
	 */
	public abstract int getRenderHeight();

	/**
	 * Returns if this {@link UiElement} is using a flex layout
	 * @return
	 */
	public boolean isFlexLayout() {
		return false;
	}
}
