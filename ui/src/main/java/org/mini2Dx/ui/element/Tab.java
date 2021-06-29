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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.navigation.GamePadHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.navigation.VerticalUiNavigation;
import org.mini2Dx.ui.render.*;

/**
 * A tab that contains {@link UiElement}s. Add to a {@link TabView}
 */
public class Tab extends Div implements Navigatable {
	private final Queue<GamePadHotKeyOperation> controllerHotKeyOperations = new Queue<GamePadHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new Queue<KeyboardHotKeyOperation>();

	private static final String FLEX_LAYOUT = "flex-column:xs-12c";
	
	private UiNavigation navigation = new VerticalUiNavigation();
	private boolean titleOrIconChanged = true;

	private FlexRow tabMenuFlexRow;
	
	@Field(optional=true)
	private String title = null;
	@Field(optional=true)
	private String iconPath = null;
	
	public Tab() {
		this(null);
	}
	
	public Tab(@ConstructorArg(clazz=String.class, name = "id") String id) {
		this(id, "");
	}
	
	public Tab(String id, String title) {
		super(id);
		this.title = title;
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new TabRenderNode(parent, this);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(this.title == null && title == null) {
			return;
		}
		if(this.title != null && this.title.equals(title)) {
			return;
		}
		this.title = title;
		titleOrIconChanged = true;
	}
	
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		if(this.iconPath == null && iconPath == null) {
			return;
		}
		if(this.iconPath != null && this.iconPath.equals(iconPath)) {
			return;
		}
		this.iconPath = iconPath;
		titleOrIconChanged = true;
	}

	@Override
	public void setZIndex(int zIndex) {
		throw new MdxException(Tab.class.getSimpleName() + " instances cannot change Z index");
	}
	
	@Override
	public void setVisibility(Visibility visibility) {
		throw new MdxException(Tab.class.getSimpleName() + " visibility is managed by " + TabView.class.getSimpleName());
	}
	
	boolean titleOrIconChanged() {
		return titleOrIconChanged;
	}
	
	void clearTitleOrIconChanged() {
		titleOrIconChanged = false;
	}
	
	void activateTab() {
		super.setVisibility(Visibility.VISIBLE);
	}
	
	void deactivateTab() {
		super.setVisibility(Visibility.HIDDEN);
	}

	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		super.syncWithUpdate(rootNode);
		((NavigatableRenderNode) renderNode).syncHotkeys(controllerHotKeyOperations, keyboardHotKeyOperations);

		if(tabMenuFlexRow == null) {
			return;
		}

		final ParentRenderNode<? extends ParentUiElement, ?> parentRenderNode = renderNode.getParent();
		final ParentUiElement parentUiElement = parentRenderNode.getElement();
		if(parentUiElement.getFlexLayout() == null) {
			boolean alignRequired = !MathUtils.isEqual(getY(), tabMenuFlexRow.getHeight());
			alignRequired |= setWidth(parentRenderNode.getContentRenderWidth());
			alignRequired |= setHeight(parentRenderNode.getContentRenderHeight() - tabMenuFlexRow.getRenderHeight());

			if(alignRequired && tabMenuFlexRow != null) {
				alignBelow(tabMenuFlexRow, HorizontalAlignment.LEFT);
			}
		} else {
			setFlexLayout(FLEX_LAYOUT);
		}
	}
	
	@Override
	public ActionableRenderNode navigate(int keycode) {
		if(renderNode == null) {
			return null;
		}
		return ((NavigatableRenderNode) renderNode).navigate(keycode);
	}
	
	@Override
	public ActionableRenderNode hotkey(int keycode) {
		if(renderNode == null) {
			return null;
		}
		return ((NavigatableRenderNode) renderNode).hotkey(keycode);
	}
	
	@Override
	public ActionableRenderNode hotkey(GamePadButton button) {
		if(renderNode == null) {
			return null;
		}
		return ((NavigatableRenderNode) renderNode).hotkey(button);
	}
	
	@Override
	public void setHotkey(GamePadButton button, Actionable actionable) {
		controllerHotKeyOperations.addLast(GamePadHotKeyOperation.allocate(button, actionable, true));
	}
	
	@Override
	public void setHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.addLast(KeyboardHotKeyOperation.allocate(keycode, actionable, true));
	}
	
	@Override
	public void unsetHotkey(GamePadButton button) {
		controllerHotKeyOperations.addLast(GamePadHotKeyOperation.allocate(button, null, false));
	}
	
	@Override
	public void unsetHotkey(int keycode) {
		keyboardHotKeyOperations.addLast(KeyboardHotKeyOperation.allocate(keycode, null, false));
	}
	
	@Override
	public void clearGamePadHotkeys() {
		controllerHotKeyOperations.addLast(GamePadHotKeyOperation.allocate(null, null, false));
	}

	@Override
	public void clearKeyboardHotkeys() {
		keyboardHotKeyOperations.addLast(KeyboardHotKeyOperation.allocate(Integer.MAX_VALUE, null, false));
	}
	
	@Override
	public void clearHotkeys() {
		clearGamePadHotkeys();
		clearKeyboardHotkeys();
	}

	@Override
	public UiNavigation getNavigation() {
		return navigation;
	}

	public void setNavigation(UiNavigation navigation) {
		if(navigation == null) {
			return;
		}
		this.navigation = navigation;
	}

	void setTabMenuFlexRow(FlexRow tabMenuFlexRow) {
		this.tabMenuFlexRow = tabMenuFlexRow;
	}
}
