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
	private Map<Integer, String> keyboardHotkeys = new HashMap<Integer, String>();
	private Map<String, String> controllerHotkeys = new HashMap<String, String>();

	public TabRenderNode(ParentRenderNode<?, ?> parent, Tab tab) {
		super(parent, tab);
	}

	@Override
	public ActionableRenderNode hotkey(int keycode) {
		String id = keyboardHotkeys.get(keycode);
		if (id == null) {
			return null;
		}
		RenderNode<?, ?> renderNode = searchTreeForElementById(id);
		if (renderNode == null) {
			return null;
		}
		return (ActionableRenderNode) renderNode;
	}

	@Override
	public ActionableRenderNode hotkey(ControllerButton controllerButton) {
		String id = controllerHotkeys.get(controllerButton.getAbsoluteValue());
		if (id == null) {
			return null;
		}
		RenderNode<?, ?> renderNode = searchTreeForElementById(id);
		if (renderNode == null) {
			return null;
		}
		return (ActionableRenderNode) renderNode;
	}

	@Override
	public void syncHotkeys(Queue<ControllerHotKeyOperation> controllerHotKeyOperations,
			Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations) {
		while (!controllerHotKeyOperations.isEmpty()) {
			ControllerHotKeyOperation hotKeyOperation = controllerHotKeyOperations.poll();
			if (hotKeyOperation.isMapOperation()) {
				controllerHotkeys.put(hotKeyOperation.getControllerButton().getAbsoluteValue(),
						hotKeyOperation.getActionable().getId());
			} else {
				if (hotKeyOperation.getControllerButton() == null) {
					controllerHotkeys.clear();
				} else {
					controllerHotkeys.remove(hotKeyOperation.getControllerButton().getAbsoluteValue());
				}
			}
		}
		while (!keyboardHotKeyOperations.isEmpty()) {
			KeyboardHotKeyOperation hotKeyOperation = keyboardHotKeyOperations.poll();
			if (hotKeyOperation.isMapOperation()) {
				keyboardHotkeys.put(hotKeyOperation.getKeycode(), hotKeyOperation.getActionable().getId());
			} else {
				if (hotKeyOperation.getKeycode() == Integer.MAX_VALUE) {
					keyboardHotkeys.clear();
				} else {
					keyboardHotkeys.remove(hotKeyOperation.getKeycode());
				}
			}
		}
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		Actionable actionable = ((Tab) element).getNavigation().navigate(keycode);
		if (actionable == null) {
			return null;
		}
		return (ActionableRenderNode) searchTreeForElementById(actionable.getId());
	}

}
