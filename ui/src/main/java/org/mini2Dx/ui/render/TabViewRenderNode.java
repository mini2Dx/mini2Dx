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

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.input.ControllerHotKeyOperation;
import org.mini2Dx.ui.input.KeyboardHotKeyOperation;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.TabStyleRule;

/**
 *
 */
public class TabViewRenderNode extends ParentRenderNode<TabView, TabStyleRule> implements NavigatableRenderNode {
	private Map<Integer, ActionableRenderNode> keyboardHotkeys = new HashMap<Integer, ActionableRenderNode>();
	private Map<String, ActionableRenderNode> controllerHotkeys = new HashMap<String, ActionableRenderNode>();
	
	public TabViewRenderNode(ParentRenderNode<?, ?> parent, TabView tabView) {
		super(parent, tabView);
	}

	@Override
	public ActionableRenderNode hotkey(int keycode) {
		return keyboardHotkeys.get(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(ControllerButton controllerButton) {
		return controllerHotkeys.get(controllerButton.getAbsoluteValue());
	}

	@Override
	public void syncHotkeys(Queue<ControllerHotKeyOperation> controllerHotKeyOperations,
			Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations) {
		while (!controllerHotKeyOperations.isEmpty()) {
			ControllerHotKeyOperation hotKeyOperation = controllerHotKeyOperations.poll();
			if (hotKeyOperation.isMapOperation()) {
				controllerHotkeys.put(hotKeyOperation.getControllerButton().getAbsoluteValue(),
						(ActionableRenderNode) getElementById(hotKeyOperation.getActionable().getId()));
			} else {
				controllerHotkeys.remove(hotKeyOperation.getControllerButton().getAbsoluteValue());
			}
		}
		while (!keyboardHotKeyOperations.isEmpty()) {
			KeyboardHotKeyOperation hotKeyOperation = keyboardHotKeyOperations.poll();
			if(hotKeyOperation.isMapOperation()) {
				keyboardHotkeys.put(hotKeyOperation.getKeycode(), (ActionableRenderNode) getElementById(hotKeyOperation.getActionable().getId()));
			} else {
				keyboardHotkeys.remove(hotKeyOperation.getKeycode());
			}
		}
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		Actionable actionable = ((TabView) element).getNavigation().navigate(keycode);
		if(actionable == null) {
			return null;
		}
		return (ActionableRenderNode) getElementById(actionable.getId());
	}

	@Override
	protected TabStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if(preferredContentWidth <= 0f) {
			return 0f;
		}
		float maxHeight = 0f;

		for (RenderLayer layer : layers.values()) {
			float height = layer.determinePreferredContentHeight(layoutState);
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		return maxHeight;
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if(element.getLayout().isHiddenByInputSource(layoutState.getLastInputSource())) {
			return 0f;
		}
		float layoutRuleResult = element.getLayout().getPreferredWidth(layoutState);
		if(layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		return layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft() - style.getMarginRight();
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return element.getLayout().getXOffset(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}
	
	public String getTabMenuStyleId() {
		return style.getMenuStyle();
	}
	
	public String getTabButtonStyleId() {
		return style.getTabButtonStyle();
	}
	
	public String getNextTabButtonStyleId() {
		return style.getNextTabButtonStyle();
	}
	
	public String getPreviousTabButtonStyleId() {
		return style.getPreviousTabButtonStyle();
	}
	
	public String getTabButtonLabelStyleId() {
		return style.getButtonLabelStyle();
	}
	
	public String getTabButtonImageStyleId() {
		return style.getButtonImageStyle();
	}
	
	public String getTabContentStyleId() {
		return style.getTabStyle();
	}
}
