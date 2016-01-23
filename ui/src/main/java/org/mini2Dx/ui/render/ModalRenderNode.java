/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Modal;
import org.mini2Dx.ui.input.ControllerHotKeyOperation;
import org.mini2Dx.ui.input.KeyboardHotKeyOperation;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ContainerStyleRule;

/**
 *
 */
public abstract class ModalRenderNode extends ContainerRenderNode {
	private Map<Integer, ActionableRenderNode> keyboardHotkeys = new HashMap<Integer, ActionableRenderNode>();
	private Map<String, ActionableRenderNode> controllerHotkeys = new HashMap<String, ActionableRenderNode>();

	public ModalRenderNode(ParentRenderNode<?, ?> parent, Column column) {
		super(parent, column);
	}

	@Override
	public void layout(LayoutState layoutState) {
		((Modal) element).getNavigation().layout(layoutState.getScreenSize());
		super.layout(layoutState);
	}

	public ActionableRenderNode hotkey(int keycode) {
		return keyboardHotkeys.get(keycode);
	}

	public ActionableRenderNode hotkey(ControllerButton controllerButton) {
		return controllerHotkeys.get(controllerButton.getAbsoluteValue());
	}

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

	public ActionableRenderNode navigate(int keycode) {
		Actionable actionable = ((Modal) element).getNavigation().navigate(keycode);
		return (ActionableRenderNode) getElementById(actionable.getId());
	}

	@Override
	protected ContainerStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(((Modal) element), layoutState.getScreenSize());
	}

}
