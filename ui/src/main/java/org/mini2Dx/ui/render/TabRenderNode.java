/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.navigation.GamePadHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;

/**
 * {@link RenderNode} implementation for {@link Tab}
 */
public class TabRenderNode extends DivRenderNode implements NavigatableRenderNode {
	private IntMap<String> keyboardHotkeys = new IntMap<String>();
	private ObjectMap<String, String> controllerHotkeys = new ObjectMap<String, String>();

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
	public ActionableRenderNode hotkey(GamePadButton controllerButton) {
		String id = controllerHotkeys.get(controllerButton.getInternalName());
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
	public void syncHotkeys(Queue<GamePadHotKeyOperation> controllerHotKeyOperations,
							Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations) {
		while (controllerHotKeyOperations.size > 0) {
			GamePadHotKeyOperation hotKeyOperation = controllerHotKeyOperations.removeFirst();
			if (hotKeyOperation.isMapOperation()) {
				controllerHotkeys.put(hotKeyOperation.getGamePadButton().getInternalName(),
						hotKeyOperation.getActionable().getId());
			} else {
				if (hotKeyOperation.getGamePadButton() == null) {
					controllerHotkeys.clear();
				} else {
					controllerHotkeys.remove(hotKeyOperation.getGamePadButton().getInternalName());
				}
			}
			hotKeyOperation.dispose();
		}
		while (keyboardHotKeyOperations.size > 0) {
			KeyboardHotKeyOperation hotKeyOperation = keyboardHotKeyOperations.removeFirst();
			if (hotKeyOperation.isMapOperation()) {
				keyboardHotkeys.put(hotKeyOperation.getKeycode(), hotKeyOperation.getActionable().getId());
			} else {
				if (hotKeyOperation.getKeycode() == Integer.MAX_VALUE) {
					keyboardHotkeys.clear();
				} else {
					keyboardHotkeys.remove(hotKeyOperation.getKeycode());
				}
			}
			hotKeyOperation.dispose();
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
