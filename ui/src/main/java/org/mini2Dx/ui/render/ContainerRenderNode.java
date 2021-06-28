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
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Div;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.navigation.GamePadHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.style.ContainerStyleRule;

/**
 * Base class for {@link Container} {@link RenderNode} implementations
 */
public class ContainerRenderNode extends ParentRenderNode<Div, ContainerStyleRule> implements NavigatableRenderNode {
	private final IntMap<String> keyboardHotkeys = new IntMap<String>();
	private final ObjectMap<String, String> controllerHotkeys = new ObjectMap<String, String>();
	private final ObjectMap<String, RenderNode<?, ?>> elementIdLookupCache = new ObjectMap<String, RenderNode<?, ?>>();

	private boolean previouslyAllowedLayout = false, previouslyAllowedUpdate = false;
	private Visibility previousVisibility = null;
	
	public ContainerRenderNode(ParentRenderNode<?, ?> parent, Div div) {
		super(parent, div);
	}
	
	@Override
	public void layout(LayoutState layoutState) {
		if(isDirty()) {
			elementIdLookupCache.clear();
		}
		final boolean allowedLayout = isAllowedUpdate();
		if(allowedLayout || (previouslyAllowedLayout != allowedLayout)) {
			((Container) element).getNavigation().layout(layoutState.getScreenSize());
			super.layout(layoutState);
		}
		previouslyAllowedLayout = allowedLayout;
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		final boolean allowedUpdate = isAllowedUpdate();
		if(allowedUpdate || (previouslyAllowedUpdate != allowedUpdate)) {
			super.update(uiContainer, delta);
		}
		previousVisibility = getElement().getVisibility();
		previouslyAllowedUpdate = allowedUpdate;
	}

	private boolean isAllowedUpdate() {
		if(previousVisibility == null) {
			return true;
		}
		if(!previousVisibility.equals(getElement().getVisibility())) {
			return true;
		}
		if(!getElement().getVisibility().equals(Visibility.HIDDEN)) {
			return true;
		}
		if(effects.size > 0 || getElement().getTotalEffects() > 0) {
			return true;
		}
		if(!initialLayoutOccurred) {
			return true;
		}
		return false;
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
		}
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		Actionable actionable = ((Container) element).getNavigation().navigate(keycode);
		if (actionable == null) {
			return null;
		}
		return (ActionableRenderNode) searchTreeForElementById(actionable.getId());
	}

	@Override
	protected ContainerStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(((Container) element), layoutState.getScreenSize());
	}
}
