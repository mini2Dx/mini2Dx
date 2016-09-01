/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.navigation.ControllerHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;

/**
 * {@link RenderNode} implementation for {@link Tab}
 */
public class TabRenderNode extends RowRenderNode implements NavigatableRenderNode {
	private Map<Integer, ActionableRenderNode> keyboardHotkeys = new HashMap<Integer, ActionableRenderNode>();
	private Map<String, ActionableRenderNode> controllerHotkeys = new HashMap<String, ActionableRenderNode>();

	public TabRenderNode(ParentRenderNode<?, ?> parent, Tab tab) {
		super(parent, tab);
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
		Actionable actionable = ((Tab) element).getNavigation().navigate(keycode);
		if(actionable == null) {
			return null;
		}
		return (ActionableRenderNode) getElementById(actionable.getId());
	}

}
