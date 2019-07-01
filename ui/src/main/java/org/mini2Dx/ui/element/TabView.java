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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.navigation.GamePadHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.navigation.TabViewUiNavigation;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.render.*;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A {@link UiElement} of tabs that can be switched between by the player
 */
public class TabView extends ParentUiElement implements Navigatable {
	private static final String DEFAULT_CHANGE_TAB_BTN_LAYOUT = "flex-col:xs-3c sm-2c md-2c lg-1c";

	private final Queue<GamePadHotKeyOperation> controllerHotKeyOperations = new Queue<GamePadHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new Queue<KeyboardHotKeyOperation>();

	private final FlexRow tabMenuFlexRow;
	private final TabButton previousTabButton, nextTabButton;
	private final Array<TabButton> tabButtons = new Array<TabButton>(true, 1, TabButton.class);

	private int currentTabIndex = 0;
	private int tabButtonViewIndex = 0;

	@Field(optional=true)
	private String tabButtonLayout = "flex-column:xs-3c sm-4c md-2c lg-2c";
	@Field(optional = true)
	protected final Array<Tab> tabs = new Array<Tab>(true, 1, Tab.class);

	private final TabViewUiNavigation navigation = new TabViewUiNavigation(this, tabs);
	
	private int availablePixelsForTabButtons = 0;
	private int pixelsPerTabButton = 0;

	/**
	 * Constructor. Generates a unique ID for this {@link TabView}
	 */
	public TabView() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID of this {@link TabView}
	 */
	public TabView(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 300f, 300f, null, null);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public TabView(@ConstructorArg(clazz = String.class, name = "id") String id,
				  @ConstructorArg(clazz = Float.class, name = "x") float x,
				  @ConstructorArg(clazz = Float.class, name = "y") float y,
				  @ConstructorArg(clazz = Float.class, name = "width") float width,
				  @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this(id, x, y, width, height, null, null);
	}

	/**
	 * Constructor
	 *
	 * @param id
	 *            The unique ID of this {@link TabView}
	 * @param previousTabButton
	 *            The {@link TabButton} to use for changing to the previous tab
	 * @param nextTabButton
	 *            The {@link TabButton} to use for changing to the next tab
	 */
	public TabView(String id,  TabButton previousTabButton, TabButton nextTabButton) {
		this(id, 0f, 0f, 300f, 300f, previousTabButton, nextTabButton);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID of this {@link TabView}
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 * @param previousTabButton
	 *            The {@link TabButton} to use for changing to the previous tab
	 * @param nextTabButton
	 *            The {@link TabButton} to use for changing to the next tab
	 */
	public TabView(String id, float x, float y, float width, float height,
				   TabButton previousTabButton, TabButton nextTabButton) {
		super(id);
		tabMenuFlexRow = new FlexRow(getId() + "-tabMenuFlexRow");
		tabMenuFlexRow.setVisibility(Visibility.VISIBLE);

		if (previousTabButton == null) {
			TabButton previousButton = new TabButton(getId() + "-previousTabButton");
			previousButton.setFlexLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			previousButton.setText("<");
			previousButton.setVisibility(Visibility.VISIBLE);
			this.previousTabButton = previousButton;
		} else {
			previousTabButton.setFlexLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			this.previousTabButton = previousTabButton;
		}
		this.previousTabButton.setEnabled(false);

		if (nextTabButton == null) {
			TabButton nextButton = new TabButton(getId() + "-nextTabButton");
			nextButton.setFlexLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			nextButton.setText(">");
			nextButton.setVisibility(Visibility.VISIBLE);
			this.nextTabButton = nextButton;
		} else {
			nextTabButton.setFlexLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			this.nextTabButton = nextTabButton;
		}
		this.previousTabButton.addActionListener(new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				previousTab();
			}
		});
		this.nextTabButton.addActionListener(new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextTab();
			}
		});
		add(tabMenuFlexRow);
	}

	/**
	 * Adds a {@link Tab} to this {@link TabView}
	 * 
	 * @param tab
	 *            The {@link Tab} to add
	 */
	public void add(Tab tab) {
		if (tab == null) {
			throw new MdxException("Cannot add null element to " + TabView.class.getSimpleName());
		}
		if (tabs.size == 0) {
			tab.activateTab();
		} else {
			tab.deactivateTab();
		}
		tabs.add(tab);
		super.add(tab);
	}

	/**
	 * Inserts a {@link Tab} into this {@link TabView}
	 * 
	 * @param index
	 *            The index to insert at
	 * @param tab
	 *            The {@link Tab} to insert
	 */
	public void add(int index, Tab tab) {
		if (tab == null) {
			throw new MdxException("Cannot add null element to " + TabView.class.getSimpleName());
		}
		if (tabs.size == 0) {
			tab.activateTab();
		} else {
			tab.deactivateTab();
		}
		tabs.insert(index, tab);
		super.add(index + 1, tab);
	}

	/**
	 * Removes a {@link Tab} from this {@link TabView}
	 * 
	 * @param tab
	 *            The {@link Tab} to remove
	 * @return True if this {@link TabView} contained the {@link Tab}
	 */
	public boolean remove(Tab tab) {
		if (renderNode != null) {
			tab.detach(renderNode);
		}
		children.removeValue(tab, false);
		return tabs.removeValue(tab, false);
	}

	/**
	 * Removes a {@link Tab} at a specific index from this {@link TabView}
	 * 
	 * @param index
	 *            The index to remove
	 * @return The {@link Tab} that was removed
	 */
	public Tab remove(int index) {
		if (renderNode != null) {
			tabs.get(index).detach(renderNode);
		}
		children.removeIndex(index + 1);
		return tabs.removeIndex(index);
	}

	/**
	 * Returns the {@link Tab} at a specific index
	 * 
	 * @param index
	 *            The index of the {@link Tab}
	 * @return The {@link Tab} at the specified index
	 */
	public Tab getTab(int index) {
		return tabs.get(index);
	}
	
	/**
	 * Returns if the {@link TabView} contains a {@link Tab}
	 * @param tab The {@link Tab} to search for
	 * @return True if the {@link Tab} is added to this {@link TabView}
	 */
	public boolean containsTab(Tab tab) {
		for(int i = 0; i < tabs.size; i++) {
			if(tabs.get(i).getId().equals(tab.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the total number of tabs in this {@link TabView}
	 * 
	 * @return 0 if no {@link Tab}s have been added
	 */
	public int getTotalTabs() {
		return tabs.size;
	}

	@Override
	public void syncWithLayout(UiContainerRenderTree rootNode) {
		super.syncWithLayout(rootNode);

		for(int i = 0; i < tabs.size; i++) {
			final Tab tab = tabs.get(i);
			tab.setTabMenuFlexRow(tabMenuFlexRow);
		}
	}

	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		((NavigatableRenderNode) renderNode).syncHotkeys(controllerHotKeyOperations, keyboardHotKeyOperations);
		syncTabTitles();
		syncChildStyles();

		super.syncWithUpdate(rootNode);
	}

	private void syncTabTitles() {
		if (tabs.size > tabButtons.size) {
			// Tabs added
			for (int i = tabButtons.size; i < tabs.size; i++) {
				TabButton tabButton = new TabButton(getId() + "-tabButton-" + i);
				tabButton.setFlexLayout(tabButtonLayout);
				tabButton.setText(tabs.get(i).getTitle());
				tabButton.setIconPath(tabs.get(i).getIconPath());
				tabButton.addActionListener(new TabButtonActionListener(this, i));
				tabButtons.add(tabButton);
			}
			tabMenuFlexRow.removeAll();

			tabMenuFlexRow.add(previousTabButton);
			for (int i = 0; i < tabButtons.size; i++) {
				tabMenuFlexRow.add(tabButtons.get(i));
			}
			tabMenuFlexRow.add(nextTabButton);
		} else if (tabs.size < tabButtons.size) {
			// Tabs removed
			for (int i = tabButtons.size - 1; tabButtons.size > tabs.size; i--) {
				TabButton button = tabButtons.removeIndex(i);
				tabMenuFlexRow.remove(button);
			}
		}

		availablePixelsForTabButtons = MathUtils.round(renderNode.getOuterRenderWidth() - previousTabButton.getWidth() - nextTabButton.getWidth());
		if (tabButtons.size > 0) {
			pixelsPerTabButton = MathUtils.round(tabButtons.get(0).getWidth());
		}

		int displayedButtonViewOffset = tabButtonViewIndex * pixelsPerTabButton;
		int currentTabButtonViewOffset = currentTabIndex * pixelsPerTabButton;

		// Handle tab buttons shifting right into view
		while (currentTabButtonViewOffset < displayedButtonViewOffset) {
			tabButtonViewIndex--;
			displayedButtonViewOffset = tabButtonViewIndex * pixelsPerTabButton;
		}

		// Handle tab buttons shifting left into view
		while (displayedButtonViewOffset + availablePixelsForTabButtons < currentTabButtonViewOffset + pixelsPerTabButton) {
			tabButtonViewIndex++;
			displayedButtonViewOffset = tabButtonViewIndex * pixelsPerTabButton;
		}

		for (int i = 0; i < tabs.size; i++) {
			Tab tab = tabs.get(i);
			TabButton tabButton = tabButtons.get(i);

			int tabButtonPixelOffset = i * pixelsPerTabButton;

			if (tabButtonPixelOffset + pixelsPerTabButton <= displayedButtonViewOffset) {
				tabButton.setVisibility(Visibility.HIDDEN);
			} else if (tabButtonPixelOffset < displayedButtonViewOffset) {
				tabButton.setVisibility(Visibility.HIDDEN);
			} else if (tabButtonPixelOffset >= displayedButtonViewOffset + availablePixelsForTabButtons) {
				tabButton.setVisibility(Visibility.HIDDEN);
			} else if (tabButtonPixelOffset + pixelsPerTabButton > displayedButtonViewOffset + availablePixelsForTabButtons) {
				tabButton.setVisibility(Visibility.HIDDEN);
			} else {
				tabButton.setVisibility(Visibility.VISIBLE);
			}

			if (tab.titleOrIconChanged()) {
				tabButton.setText(tab.getTitle());
				tabButton.setIconPath(tab.getIconPath());
				tab.clearTitleOrIconChanged();
			}
			if (i == currentTabIndex) {
				tabButton.setCurrentTab(true);
			} else {
				tabButton.setCurrentTab(false);
			}
		}
	}

	private void syncChildStyles() {
		final TabViewRenderNode tabViewRenderNode = (TabViewRenderNode) renderNode;
		tabMenuFlexRow.setStyleId(tabViewRenderNode.getTabMenuStyleId());
		for (int i = 0; i < tabs.size; i++) {
			tabs.get(i).setStyleId(tabViewRenderNode.getTabContentStyleId());
			tabButtons.get(i).setStyleId(tabViewRenderNode.getTabButtonStyleId());
			tabButtons.get(i).setLabelStyle(tabViewRenderNode.getTabButtonLabelStyleId());
			tabButtons.get(i).setIconStyle(tabViewRenderNode.getTabButtonImageStyleId());
		}
		previousTabButton.setStyleId(tabViewRenderNode.getPreviousTabButtonStyleId());
		previousTabButton.setLabelStyle(tabViewRenderNode.getTabButtonLabelStyleId());
		previousTabButton.setIconStyle(tabViewRenderNode.getTabButtonImageStyleId());

		nextTabButton.setStyleId(tabViewRenderNode.getNextTabButtonStyleId());
		nextTabButton.setLabelStyle(tabViewRenderNode.getTabButtonLabelStyleId());
		nextTabButton.setIconStyle(tabViewRenderNode.getTabButtonImageStyleId());
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new TabViewRenderNode(parent, this);
	}

	@Override
	public void setVisibility(Visibility visibility) {
		if (this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setStyleId(String styleId) {
		if (styleId == null) {
			return;
		}
		if (this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setZIndex(int zIndex) {
		if(this.zIndex == zIndex) {
			return;
		}
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getTabStyleRule(styleId, ScreenSize.XS);
	}

	/**
	 * Returns the currently visible {@link Tab}
	 * 
	 * @return Null if no {@link Tab} is visible
	 */
	public Tab getCurrentTab() {
		return tabs.get(getCurrentTabIndex());
	}

	/**
	 * Sets the currently visible {@link Tab}
	 * 
	 * @param tab
	 *            The {@link Tab} to set as visible (must already be added to
	 *            the {@link TabView}
	 */
	public void setCurrentTab(Tab tab) {
		int tabIndex = tabs.indexOf(tab, false);
		if (tabIndex < 0) {
			throw new MdxException(tab + " cannot be set to current tab as it was not added to "
					+ TabView.class.getSimpleName() + ":" + getId());
		}
		setCurrentTabIndex(tabIndex);
	}

	/**
	 * Returns the index of the currently visible {@link Tab}
	 * 
	 * @return 0 by default
	 */
	public int getCurrentTabIndex() {
		if(currentTabIndex < 0) {
			currentTabIndex = 0;
		} else if(currentTabIndex >= tabs.size) {
			currentTabIndex = 0;
		}
		return currentTabIndex;
	}

	/**
	 * Sets the currently visible {@link Tab}
	 * 
	 * @param currentTabIndex
	 *            The index of the {@link Tab}
	 */
	public void setCurrentTabIndex(int currentTabIndex) {
		if (currentTabIndex < 0) {
			return;
		}
		if (currentTabIndex >= tabs.size) {
			return;
		}
		if (this.currentTabIndex == currentTabIndex) {
			return;
		}

		tabs.get(this.currentTabIndex).deactivateTab();
		this.currentTabIndex = currentTabIndex;
		tabs.get(this.currentTabIndex).activateTab();

		if (this.currentTabIndex <= 0) {
			previousTabButton.setEnabled(false);
		} else {
			previousTabButton.setEnabled(true);
		}

		if (this.currentTabIndex >= tabs.size - 1) {
			nextTabButton.setEnabled(false);
		} else {
			nextTabButton.setEnabled(true);
		}

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Navigates to the next {@link Tab}. If the current tab is the last tab,
	 * this will loop back to the first tab.
	 */
	public void nextTab() {
		if (currentTabIndex >= tabs.size - 1) {
			setCurrentTabIndex(0);
		} else {
			setCurrentTabIndex(currentTabIndex + 1);
		}
	}

	/**
	 * Navigates to the previous {@link Tab}. If the current tab is the first
	 * tab, this will loop to the last tab.
	 */
	public void previousTab() {
		if (currentTabIndex <= 0) {
			setCurrentTabIndex(tabs.size - 1);
		} else {
			setCurrentTabIndex(currentTabIndex - 1);
		}
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		if (renderNode == null) {
			return null;
		}
		if (currentTabIndex >= tabs.size) {
			return null;
		}
		return tabs.get(currentTabIndex).navigate(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(int keycode) {
		if (renderNode == null) {
			return null;
		}
		ActionableRenderNode result = ((NavigatableRenderNode) renderNode).hotkey(keycode);
		if (result != null) {
			return result;
		}
		if (currentTabIndex >= tabs.size) {
			return null;
		}
		return tabs.get(currentTabIndex).hotkey(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(GamePadButton button) {
		if (renderNode == null) {
			return null;
		}
		ActionableRenderNode result = ((NavigatableRenderNode) renderNode).hotkey(button);
		if (result != null) {
			return result;
		}
		if (currentTabIndex >= tabs.size) {
			return null;
		}
		return tabs.get(currentTabIndex).hotkey(button);
	}

	@Override
	public UiNavigation getNavigation() {
		return navigation;
	}

	/**
	 * Sets a keyboard key as the hotkey for changing to the previous tab
	 * @param keycode The {@link Input.Keys} keycode
	 */
	public void setPreviousTabHotkey(int keycode) {
		keyboardHotKeyOperations.addLast(new KeyboardHotKeyOperation(keycode, previousTabButton, true));
	}

	/**
	 * Sets a {@link GamePadButton} as the hotkey for changing to the previous tab
	 * @param button The {@link GamePadButton}
	 */
	public void setPreviousTabHotkey(GamePadButton button) {
		controllerHotKeyOperations.addLast(new GamePadHotKeyOperation(button, previousTabButton, true));
	}
	
	/**
	 * Sets a keyboard key as the hotkey for changing to the next tab
	 * @param keycode The {@link Input.Keys} keycode
	 */
	public void setNextTabHotkey(int keycode) {
		keyboardHotKeyOperations.addLast(new KeyboardHotKeyOperation(keycode, nextTabButton, true));
	}

	/**
	 * Sets a {@link GamePadButton} as the hotkey for changing to the next tab
	 * @param button The {@link GamePadButton}
	 */
	public void setNextTabHotkey(GamePadButton button) {
		controllerHotKeyOperations.addLast(new GamePadHotKeyOperation(button, nextTabButton, true));
	}

	/**
	 * Unsets a keyboard key as the hotkey for changing to the previous tab
	 * @param keycode The {@link Input.Keys} keycode
	 */
	public void unsetPreviousTabHotkey(int keycode) {
		keyboardHotKeyOperations.addLast(new KeyboardHotKeyOperation(keycode, previousTabButton, false));
	}

	/**
	 * Unets a {@link GamePadButton} as the hotkey for changing to the previous tab
	 * @param button The {@link GamePadButton}
	 */
	public void unsetPreviousTabHotkey(GamePadButton button) {
		controllerHotKeyOperations.addLast(new GamePadHotKeyOperation(button, previousTabButton, false));
	}

	/**
	 * Unsets a keyboard key as the hotkey for changing to the next tab
	 * @param keycode The {@link Input.Keys} keycode
	 */
	public void unsetNextTabHotkey(int keycode) {
		keyboardHotKeyOperations.addLast(new KeyboardHotKeyOperation(keycode, nextTabButton, false));
	}

	/**
	 * Unets a {@link GamePadButton} as the hotkey for changing to the next tab
	 * @param button The {@link GamePadButton}
	 */
	public void unsetNextTabHotkey(GamePadButton button) {
		controllerHotKeyOperations.addLast(new GamePadHotKeyOperation(button, nextTabButton, false));
	}

	@Override
	public void setHotkey(GamePadButton button, Actionable actionable) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows setPreviousTabHotkey and setNextTabHotkey methods. Set hotkeys using Tab instances.");
	}

	@Override
	public void setHotkey(int keycode, Actionable actionable) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows setPreviousTabHotkey and setNextTabHotkey methods. Set hotkeys using Tab instances.");
	}

	@Override
	public void unsetHotkey(GamePadButton button) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Unset hotkeys using Tab instances.");
	}

	@Override
	public void unsetHotkey(int keycode) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Unset hotkeys using Tab instances.");
	}
	
	@Override
	public void clearGamePadHotkeys() {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Clear hotkeys using Tab instances.");
	}

	@Override
	public void clearKeyboardHotkeys() {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Clear hotkeys using Tab instances.");
	}
	
	@Override
	public void clearHotkeys() {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Clear hotkeys using Tab instances.");
	}

	public void setPreviousTabButtonLayout(String flexLayout) {
		previousTabButton.setFlexLayout(flexLayout);
	}

	public void setNextTabButtonLayout(String flexLayout) {
		nextTabButton.setFlexLayout(flexLayout);
	}

	public void setTabButtonLayout(String flexLayout) {
		this.tabButtonLayout = flexLayout;
		for (int i = 0; i < tabs.size; i++) {
			tabs.get(i).setFlexLayout(flexLayout);
		}
	}
	
	public TabButton getPreviousTabButton() {
		return previousTabButton;
	}
	
	public TabButton getNextTabButton() {
		return nextTabButton;
	}
	
	@Override
	public UiElement getElementById(String id) {
		if (getId().equals(id)) {
			return this;
		}
		for (int i = 0; i < tabs.size; i++) {
			UiElement result = tabs.get(i).getElementById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	private class TabButtonActionListener implements ActionListener {
		private final TabView tabView;
		private final int index;

		TabButtonActionListener(TabView tabView, int index) {
			this.tabView = tabView;
			this.index = index;
		}

		@Override
		public void onActionBegin(ActionEvent event) {
		}

		@Override
		public void onActionEnd(ActionEvent event) {
			tabView.setCurrentTabIndex(index);
		}
	}
}
