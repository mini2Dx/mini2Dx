/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.render;

import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.navigation.GamePadHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.style.TabStyleRule;

/**
 * {@link RenderNode} implementation for {@link TabView}
 */
public class TabViewRenderNode extends ParentRenderNode<TabView, TabStyleRule>implements NavigatableRenderNode {
	private final ObjectMap<String, RenderNode<?, ?>> elementIdLookupCache = new ObjectMap<String, RenderNode<?, ?>>();
	private final IntMap<String> keyboardHotkeys = new IntMap<String>();
	private final ObjectMap<String, String> controllerHotkeys = new ObjectMap<String, String>();

	public TabViewRenderNode(ParentRenderNode<?, ?> parent, TabView tabView) {
		super(parent, tabView);
	}

	@Override
	public void layout(LayoutState layoutState) {
		if(isDirty()) {
			elementIdLookupCache.clear();
		}
		super.layout(layoutState);
	}

	@Override
	public ActionableRenderNode hotkey(int keycode) {
		String id = keyboardHotkeys.get(keycode);
		if (id == null) {
			return null;
		}
		RenderNode<?, ?> renderNode = getElementById(id);
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
		RenderNode<?, ?> renderNode = getElementById(id);
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
				if(hotKeyOperation.getGamePadButton() == null) {
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
				if(hotKeyOperation.getKeycode() == Integer.MAX_VALUE) {
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
		Actionable actionable = ((TabView) element).getNavigation().navigate(keycode);
		if (actionable == null) {
			return null;
		}
		return (ActionableRenderNode) getElementById(actionable.getId());
	}

	@Override
	protected TabStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}
	
	@Override
	public RenderNode<?, ?> getElementById(String id) {
		if (element.getId().equals(id)) {
			return this;
		}
		if(elementIdLookupCache.containsKey(id)) {
			return elementIdLookupCache.get(id);
		}
		for (RenderLayer layer : layers.values()) {
			RenderNode<?, ?> result = layer.getElementById(id);
			if (result != null) {
				elementIdLookupCache.put(id, result);
				return result;
			}
		}
		return null;
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
