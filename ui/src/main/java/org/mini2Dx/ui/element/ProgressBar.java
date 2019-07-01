/**
 * Copyright (c) 2016 See AUTHORS file
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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.ProgressBarRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * Implements a progress/loading bar
 */
public class ProgressBar extends UiElement implements FlexUiElement {
	@Field(optional=true)
	private float min;
	@Field(optional=true)
	private float max;
	@Field(optional=true)
	private float value;
	@Field(optional=true)
	private String flexLayout = null;
	
	protected ProgressBarRenderNode renderNode;
	
	public ProgressBar() {
		this(null);
	}
	
	public ProgressBar(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 300f, 64f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ProgressBar(@ConstructorArg(clazz = Float.class, name = "x") float x,
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
	public ProgressBar(@ConstructorArg(clazz = String.class, name = "id") String id,
				   @ConstructorArg(clazz = Float.class, name = "x") float x,
				   @ConstructorArg(clazz = Float.class, name = "y") float y,
				   @ConstructorArg(clazz = Float.class, name = "width") float width,
				   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
		max = 1f;
	}
	
	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		while (effects.size > 0) {
			renderNode.applyEffect(effects.removeFirst());
		}
		super.syncWithUpdate(rootNode);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new ProgressBarRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
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
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getProgressBarStyleRule(styleId, ScreenSize.XS);
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

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		if(min >= max) {
			throw new MdxException("Min must be less than max");
		}
		this.min = min;
		if(value < min) {
			value = min;
		}
		if(renderNode == null) {
			return;
		}
		renderNode.updateFillWidth();
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		if(max <= min) {
			throw new MdxException("Max must be greater than min");
		}
		this.max = max;
		if(value > max) {
			value = max;
		}
		if(renderNode == null) {
			return;
		}
		renderNode.updateFillWidth();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		if(value < min) {
			value = min;
		} else if(value > max) {
			value = max;
		}
		this.value = value;
		if(renderNode == null) {
			return;
		}
		renderNode.updateFillWidth();
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
