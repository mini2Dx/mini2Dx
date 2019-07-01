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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.animation.TextAnimation;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.LabelRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.LabelStyleRule;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.UiTheme;

/**
 * A text label {@link UiElement}
 */
public class Label extends UiElement {
	private final Color white = Mdx.graphics.newColor(1f, 1f, 1f, 1f);
	protected LabelRenderNode renderNode;
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
		this(id, 0f, 0f, 300f, 128f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public Label(@ConstructorArg(clazz = Float.class, name = "x") float x,
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
	public Label(@ConstructorArg(clazz = String.class, name = "id") String id,
				 @ConstructorArg(clazz = Float.class, name = "x") float x,
				 @ConstructorArg(clazz = Float.class, name = "y") float y,
				 @ConstructorArg(clazz = Float.class, name = "width") float width,
				 @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}

	public void shrinkToTextSize() {
		if(renderNode == null || !UiContainer.isThemeApplied()) {
			deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					shrinkToTextSize();
				}
			});
			return;
		}
		if(text == null) {
			return;
		}
		final LabelStyleRule styleRule = UiContainer.getTheme().getLabelStyleRule(styleId, ScreenSize.XS);
		final GameFont font = styleRule.getGameFont();
		if(font == null) {
			return;
		}
		font.getSharedGlyphLayout().setText(text);
		setContentWidth(font.getSharedGlyphLayout().getWidth());
		setContentHeight(font.getSharedGlyphLayout().getHeight());
	}

	public void shrinkToTextSize(float maxWidth) {
		if(renderNode == null || !UiContainer.isThemeApplied()) {
			deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					shrinkToTextSize();
				}
			});
			return;
		}
		if(text == null) {
			return;
		}
		final LabelStyleRule styleRule = UiContainer.getTheme().getLabelStyleRule(styleId, ScreenSize.XS);
		final GameFont font = styleRule.getGameFont();
		if(font == null) {
			return;
		}
		font.getSharedGlyphLayout().setText(text, white, maxWidth, Align.LEFT, true);
		setContentWidth(font.getSharedGlyphLayout().getWidth());
		setContentHeight(font.getSharedGlyphLayout().getHeight());
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
		if(text.equals(this.text)) {
			return;
		}
		this.text = text;
		
		if(renderNode == null) {
			return;
		}
		((LabelRenderNode) renderNode).updateBitmapFontCache();
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
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getLabelStyleRule(styleId, ScreenSize.XS);
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
		renderNode.setDirty();
	}
	
	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		while (effects.size > 0) {
			renderNode.applyEffect(effects.removeFirst());
		}
		super.syncWithUpdate(rootNode);
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
		renderNode.setDirty();
	}
	
	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
		if(horizontalAlignment.equals(this.horizontalAlignment)) {
			return;
		}
		this.horizontalAlignment = horizontalAlignment;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
		renderNode.setDirty();
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
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
}
