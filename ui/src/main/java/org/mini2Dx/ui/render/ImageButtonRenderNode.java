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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.ui.element.ImageButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ButtonStyleRule;

import com.badlogic.gdx.Input.Buttons;

/**
 *
 */
public class ImageButtonRenderNode extends RenderNode<ImageButton, ButtonStyleRule>implements ActionableRenderNode {
	private TextureRegion textureRegion;

	public ImageButtonRenderNode(ParentRenderNode<?, ?> parent, ImageButton element) {
		super(parent, element);
	}

	@Override
	public void layout(LayoutState layoutState) {
		textureRegion = element.getTextureRegion(layoutState.getAssetManager());
		super.layout(layoutState);
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (button != Buttons.LEFT) {
			return null;
		}
		if (currentArea.contains(screenX, screenY)) {
			setState(NodeState.ACTION);
			return this;
		}
		return null;
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if (getState() != NodeState.ACTION) {
			return;
		}
		if (currentArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
		} else {
			setState(NodeState.NORMAL);
		}
		endAction();
	}

	@Override
	protected void renderElement(Graphics g) {
		NinePatch ninePatch = style.getNormalNinePatch();
		if (element.isEnabled()) {
			switch (getState()) {
			case ACTION:
				ninePatch = style.getActionNinePatch();
				break;
			case HOVER:
				ninePatch = style.getHoverNinePatch();
				break;
			default:
				break;
			}
		} else {
			ninePatch = style.getDisabledNinePatch();
		}

		if (ninePatch != null) {
			g.drawNinePatch(ninePatch, getRenderX(), getRenderY(), getRenderWidth(), getRenderHeight());
		}

		float imageRenderX = getRenderX() + style.getPaddingLeft();
		float imageRenderY = getRenderY() + style.getPaddingTop();
		float imageRenderWidth = getRenderWidth() - style.getPaddingLeft() - style.getPaddingRight();
		float imageRenderHeight = getRenderHeight() - style.getPaddingTop() - style.getPaddingBottom();

		g.drawTextureRegion(textureRegion, imageRenderX, imageRenderY, imageRenderWidth, imageRenderHeight);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if(element.getLayout().isHiddenByInputSource(layoutState.getLastInputSource())) {
			return 0f;
		}
		float layoutRuleResult = element.getLayout().getPreferredWidth(layoutState);
		if(layoutRuleResult <= 0f) {
			element.setVisibility(Visibility.HIDDEN);
			return 0f;
		} else if(layoutState.isScreenSizeChanged() && element.getVisibility() == Visibility.HIDDEN) {
			element.setVisibility(Visibility.VISIBLE);
		}
		return layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (textureRegion == null) {
			return 0f;
		}
		if (element.isResponsive()) {
			return textureRegion.getRegionHeight() * (preferredContentWidth / textureRegion.getRegionWidth());
		} else {
			return textureRegion.getRegionHeight();
		}
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected ButtonStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	public void beginAction() {
		element.notifyActionListenersOfBeginEvent();
	}

	@Override
	public void endAction() {
		element.notifyActionListenersOfEndEvent();
	}
}
