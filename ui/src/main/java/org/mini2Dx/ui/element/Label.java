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
import org.mini2Dx.ui.animation.TextAnimation;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.render.LabelRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.graphics.Color;

/**
 * A text label {@link UiElement}
 */
public class Label extends UiElement {
	/**
	 * A blending-safe white {@link Color} value
	 */
	public static final Color COLOR_WHITE = new Color(254f / 255f, 254f / 255f, 254f / 255f, 1f);
	/**
	 * A blending-safe black {@link Color} value
	 */
	public static final Color COLOR_BLACK = new Color(1f / 255f, 1f / 255f, 1f / 255f, 1f);
	
	private LabelRenderNode renderNode;
	private Color color = null;
	
	@Field(optional=true)
	private String text = "";
	@Field(optional=true)
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	@Field(optional=true)
	private boolean responsive = false;
	@Field(optional=true)
	private TextAnimation textAnimation;
	
	/**
	 * Constructor. Generates a unique ID for the {@link Label}
	 */
	public Label() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param id The unique ID of the {@link Label}
	 */
	public Label(@ConstructorArg(clazz=String.class, name = "id") String id) {
		super(id);
	}

	/**
	 * Returns the current text of the label
	 * @return A non-null {@link String}
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the current text of the label
	 * @param text A non-null {@link String}
	 */
	public void setText(String text) {
		if(text == null) {
			return;
		}
		this.text = text;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new LabelRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
	}
	
	@Override
	public void setVisibility(Visibility visibility) {
		if(this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	@Override
	public void syncWithRenderNode() {
		while(!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
	}
	
	@Override
	public void setStyleId(String styleId) {
		if(styleId == null) {
			return;
		}
		if(this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Returns the {@link HorizontalAlignment} of the text
	 * @return {@link HorizontalAlignment#LEFT} by default
	 */
	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * Sets the {@link HorizontalAlignment} of the text
	 * @param horizontalAlignment
	 */
	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		if(horizontalAlignment == null) {
			return;
		}
		this.horizontalAlignment = horizontalAlignment;
	}
	
	/**
	 * Returns if this label should fill its available space
	 * @return False if the width matches the text width
	 */
	public boolean isResponsive() {
		return responsive;
	}

	/**
	 * Sets if this label should fill its available space
	 * @param responsive False if the width should match the text width
	 */
	public void setResponsive(boolean responsive) {
		this.responsive = responsive;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Returns the {@link Color} of the label
	 * @return Null if the {@link Color} is set by the {@link UiTheme}
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the {@link Color} of the label
	 * @param color Null if the {@link Color} should be retrieved from the {@link UiTheme}
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the current {@link TextAnimation} of the label
	 * @return Null by default
	 */
	public TextAnimation getTextAnimation() {
		return textAnimation;
	}

	/**
	 * Sets the current {@link TextAnimation} of the label
	 * @param textAnimation Null if no animation is to be played
	 */
	public void setTextAnimation(TextAnimation textAnimation) {
		this.textAnimation = textAnimation;
	}
}
