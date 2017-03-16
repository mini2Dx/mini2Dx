/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.CheckboxStyleRule;

import com.badlogic.gdx.Input.Buttons;

/**
 *
 */
public class CheckboxRenderNode extends RenderNode<Checkbox, CheckboxStyleRule> implements ActionableRenderNode {

	public CheckboxRenderNode(ParentRenderNode<?, ?> parent, Checkbox element) {
		super(parent, element);
	}
	
	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (button != Buttons.LEFT) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (outerArea.contains(screenX, screenY)) {
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
		if (outerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
			element.setChecked(!element.isChecked());
		} else {
			setState(NodeState.NORMAL);
		}
		
		MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
		params.setMouseX(screenX);
		params.setMouseY(screenY);
		endAction(EventTrigger.getTriggerForMouseClick(button), params);
		EventTriggerParamsPool.release(params);
	}

	@Override
	protected void renderElement(Graphics g) {
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
		
		NinePatch boxNinePatch = null;
		TextureRegion checkTextureRegion = null;
		
		if (element.isEnabled()) {
			boxNinePatch = style.getEnabledNinePatch();
			
			switch(getState()) {
			case HOVER:
				if(element.isChecked()) {
					checkTextureRegion = style.getEnabledCheckTextureRegion();
				} else {
					checkTextureRegion = style.getEnabledUncheckTextureRegion();
				}
				break;
			case ACTION:
			case NORMAL:
			default:
				if(element.isChecked()) {
					checkTextureRegion = style.getEnabledCheckTextureRegion();
				} else {
					checkTextureRegion = style.getEnabledUncheckTextureRegion();
				}
				break;
			}
		} else {
			boxNinePatch = style.getDisabledNinePatch();
			
			if(element.isChecked()) {
				checkTextureRegion = style.getDisabledCheckTextureRegion();
			} else {
				checkTextureRegion = style.getDisabledUncheckTextureRegion();
			}
		}
		
		g.drawNinePatch(boxNinePatch, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
				getInnerRenderHeight());
		if(checkTextureRegion == null) {
			return;
		}
		
		if(element.isResponsive()) {
			g.drawTextureRegion(checkTextureRegion, getContentRenderX() + (getContentRenderWidth() / 2) - (checkTextureRegion.getRegionWidth() / 2), getContentRenderY());
		} else {
			g.drawTextureRegion(checkTextureRegion, getContentRenderX(), getContentRenderY());
		}
	}
	
	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();
		
		if(element.isResponsive()) {
			return availableWidth;
		} else {
			return style.getEnabledCheckTextureRegion().getRegionWidth();
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		return style.getEnabledCheckTextureRegion().getRegionHeight();
	}
	
	@Override
	protected CheckboxStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}

}
