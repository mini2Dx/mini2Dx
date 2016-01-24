/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.ui.element.ImageButton;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ButtonStyleRule;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 */
public class ImageButtonRenderNode extends RenderNode<ImageButton, ButtonStyleRule> implements ActionableRenderNode {
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

		if(ninePatch != null) {
			g.drawNinePatch(ninePatch, getRenderX(), getRenderY(), getRenderWidth(), getRenderHeight());
		}

		float imageRenderX = getRenderX() + style.getPaddingLeft();
		float imageRenderY = getRenderY() + style.getPaddingTop();
		float imageRenderWidth = getRenderWidth() - style.getPaddingLeft() - style.getPaddingRight();
		float imageRenderHeight = getRenderHeight() - style.getPaddingTop() - style.getPaddingBottom();

		g.drawTextureRegion(textureRegion, imageRenderX, imageRenderY, imageRenderWidth, imageRenderHeight);
	}

	@Override
	protected float determinePreferredWidth(LayoutState layoutState) {
		return layoutState.getParentWidth();
	}

	@Override
	protected float determinePreferredHeight(LayoutState layoutState) {
		if (textureRegion == null) {
			return style.getPaddingTop() + style.getPaddingBottom();
		}
		if (element.isResponsive()) {
			return style.getPaddingTop() + (textureRegion.getRegionHeight()
					* ((preferredWidth - style.getPaddingLeft() - style.getPaddingRight())
							/ textureRegion.getRegionWidth()))
					+ style.getPaddingBottom();
		} else {
			return style.getPaddingTop() + textureRegion.getRegionHeight() + style.getPaddingBottom();
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
