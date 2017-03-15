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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.navigation.ControllerHotKeyOperation;
import org.mini2Dx.ui.navigation.KeyboardHotKeyOperation;
import org.mini2Dx.ui.navigation.TabViewUiNavigation;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.render.ActionableRenderNode;
import org.mini2Dx.ui.render.NavigatableRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TabViewRenderNode;

import com.badlogic.gdx.Input.Keys;

/**
 * A {@link UiElement} of tabs that can be switched between by the player
 */
public class TabView extends ParentUiElement implements Navigatable {
	private static final String DEFAULT_CHANGE_TAB_BTN_LAYOUT = "xs-3c sm-2c lg-1c";

	private final Queue<ControllerHotKeyOperation> controllerHotKeyOperations = new LinkedList<ControllerHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new LinkedList<KeyboardHotKeyOperation>();

	private final Row tabMenuRow;
	private final TabButton previousTabButton, nextTabButton;
	private final List<TabButton> tabButtons = new ArrayList<TabButton>(1);
	
	protected TabViewRenderNode renderNode;
	private int currentTabIndex = 0;
	private int tabButtonViewIndex = 0;

	@Field(optional=true)
	private String layout = LayoutRuleset.DEFAULT_LAYOUT;
	@Field(optional=true)
	private String tabButtonLayout = "xs-6c sm-4c md-2c xl-1c";
	@Field(optional = true)
	protected final List<Tab> tabs = new ArrayList<Tab>(1);

	private final TabViewUiNavigation navigation = new TabViewUiNavigation(this, tabs);
	
	private int availableColumnsForTabButtons = 0;
	private int columnsPerTabButton = 0;

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
		this(id, null, null);
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
	public TabView(String id, TabButton previousTabButton, TabButton nextTabButton) {
		super(id);
		tabMenuRow = new Row(getId() + "-tabMenuRow");
		tabMenuRow.setVisibility(Visibility.VISIBLE);

		if (previousTabButton == null) {
			TabButton previousButton = new TabButton(getId() + "-previousTabButton");
			previousButton.setLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			previousButton.setText("<");
			previousButton.setVisibility(Visibility.VISIBLE);
			this.previousTabButton = previousButton;
		} else {
			previousTabButton.setLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			this.previousTabButton = previousTabButton;
		}
		this.previousTabButton.setEnabled(false);

		if (nextTabButton == null) {
			TabButton nextButton = new TabButton(getId() + "-nextTabButton");
			nextButton.setLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
			nextButton.setText(">");
			nextButton.setVisibility(Visibility.VISIBLE);
			this.nextTabButton = nextButton;
		} else {
			nextTabButton.setLayout(DEFAULT_CHANGE_TAB_BTN_LAYOUT);
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
		if (tabs.size() == 0) {
			tab.activateTab();
		} else {
			tab.deactivateTab();
		}
		tabs.add(tab);

		if (renderNode == null) {
			return;
		}
		tab.attach(renderNode);
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
		if (tabs.size() == 0) {
			tab.activateTab();
		} else {
			tab.deactivateTab();
		}
		tabs.add(index, tab);
		if (renderNode == null) {
			return;
		}
		tab.attach(renderNode);
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
		return tabs.remove(tab);
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
		return tabs.remove(index);
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
		for(int i = 0; i < tabs.size(); i++) {
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
		return tabs.size();
	}

	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
		((NavigatableRenderNode) renderNode).syncHotkeys(controllerHotKeyOperations, keyboardHotKeyOperations);
		syncTabTitles();
		syncChildStyles();
	}

	private void syncTabTitles() {
		if (tabs.size() > tabButtons.size()) {
			// Tabs added
			for (int i = tabButtons.size(); i < tabs.size(); i++) {
				TabButton tabButton = new TabButton(getId() + "-tabButton-" + i);
				tabButton.setLayout(tabButtonLayout);
				tabButton.setText(tabs.get(i).getTitle());
				tabButton.setIconPath(tabs.get(i).getIconPath());
				tabButton.addActionListener(new TabButtonActionListener(this, i));
				tabButtons.add(tabButton);
			}
			tabMenuRow.removeAll();

			tabMenuRow.add(previousTabButton);
			for (int i = 0; i < tabButtons.size(); i++) {
				tabMenuRow.add(tabButtons.get(i));
			}
			tabMenuRow.add(nextTabButton);
		} else if (tabs.size() < tabButtons.size()) {
			// Tabs removed
			for (int i = tabButtons.size() - 1; tabButtons.size() > tabs.size(); i--) {
				TabButton button = tabButtons.remove(i);
				tabMenuRow.remove(button);
			}
		}

		availableColumnsForTabButtons = 12 - previousTabButton.getCurrentSizeInColumns()
				- nextTabButton.getCurrentSizeInColumns();
		if (tabButtons.size() > 0) {
			columnsPerTabButton = tabButtons.get(0).getCurrentSizeInColumns();
		}

		int tabButtonViewOffset = tabButtonViewIndex * columnsPerTabButton;
		int currentTabButtonViewOffset = currentTabIndex * columnsPerTabButton;

		// Handle tab buttons shifting right into view
		while (currentTabButtonViewOffset < tabButtonViewOffset) {
			tabButtonViewIndex--;
			tabButtonViewOffset = tabButtonViewIndex * columnsPerTabButton;
		}

		// Handle tab buttons shifting left into view
		while (tabButtonViewOffset + availableColumnsForTabButtons < currentTabButtonViewOffset + columnsPerTabButton) {
			tabButtonViewIndex++;
			tabButtonViewOffset = tabButtonViewIndex * columnsPerTabButton;
		}

		for (int i = 0; i < tabs.size(); i++) {
			Tab tab = tabs.get(i);
			TabButton tabButton = tabButtons.get(i);

			int tabButtonColumnOffset = i * columnsPerTabButton;
			if (tabButtonColumnOffset + columnsPerTabButton <= tabButtonViewOffset) {
				tabButton.setVisibility(Visibility.HIDDEN);
			} else if (tabButtonColumnOffset >= tabButtonViewOffset + availableColumnsForTabButtons) {
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
		tabMenuRow.setStyleId(renderNode.getTabMenuStyleId());
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).setStyleId(renderNode.getTabContentStyleId());
			tabButtons.get(i).setStyleId(renderNode.getTabButtonStyleId());
			tabButtons.get(i).setLabelStyle(renderNode.getTabButtonLabelStyleId());
			tabButtons.get(i).setIconStyle(renderNode.getTabButtonImageStyleId());
		}
		previousTabButton.setStyleId(renderNode.getPreviousTabButtonStyleId());
		previousTabButton.setLabelStyle(renderNode.getTabButtonLabelStyleId());
		previousTabButton.setIconStyle(renderNode.getTabButtonImageStyleId());

		nextTabButton.setStyleId(renderNode.getNextTabButtonStyleId());
		nextTabButton.setLabelStyle(renderNode.getTabButtonLabelStyleId());
		nextTabButton.setIconStyle(renderNode.getTabButtonImageStyleId());
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new TabViewRenderNode(parent, this);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new TabViewRenderNode(parentRenderNode, this);
		tabMenuRow.attach(renderNode);
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).detach(renderNode);
		}
		tabMenuRow.detach(renderNode);
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
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
		renderNode.setDirty(true);
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
		renderNode.setDirty(true);
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Returns the currently visible {@link Tab}
	 * 
	 * @return Null if no {@link Tab} is visible
	 */
	public Tab getCurrentTab() {
		if (currentTabIndex >= tabs.size()) {
			return null;
		}
		return tabs.get(currentTabIndex);
	}

	/**
	 * Sets the currently visible {@link Tab}
	 * 
	 * @param tab
	 *            The {@link Tab} to set as visible (must already be added to
	 *            the {@link TabView}
	 */
	public void setCurrentTab(Tab tab) {
		int tabIndex = tabs.indexOf(tab);
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
		if (currentTabIndex >= tabs.size()) {
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

		if (this.currentTabIndex >= tabs.size() - 1) {
			nextTabButton.setEnabled(false);
		} else {
			nextTabButton.setEnabled(true);
		}

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Navigates to the next {@link Tab}. If the current tab is the last tab,
	 * this will loop back to the first tab.
	 */
	public void nextTab() {
		if (currentTabIndex >= tabs.size() - 1) {
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
			setCurrentTabIndex(tabs.size() - 1);
		} else {
			setCurrentTabIndex(currentTabIndex - 1);
		}
	}

	@Override
	public ActionableRenderNode navigate(int keycode) {
		if (renderNode == null) {
			return null;
		}
		if (currentTabIndex >= tabs.size()) {
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
		if (currentTabIndex >= tabs.size()) {
			return null;
		}
		return tabs.get(currentTabIndex).hotkey(keycode);
	}

	@Override
	public ActionableRenderNode hotkey(ControllerButton button) {
		if (renderNode == null) {
			return null;
		}
		ActionableRenderNode result = ((NavigatableRenderNode) renderNode).hotkey(button);
		if (result != null) {
			return result;
		}
		if (currentTabIndex >= tabs.size()) {
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
	 * @param keycode The {@link Keys} keycode
	 */
	public void setPreviousTabHotkey(int keycode) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, previousTabButton, true));
	}

	/**
	 * Sets a {@link ControllerButton} as the hotkey for changing to the previous tab
	 * @param button The {@link ControllerButton}
	 */
	public void setPreviousTabHotkey(ControllerButton button) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, previousTabButton, true));
	}
	
	/**
	 * Sets a keyboard key as the hotkey for changing to the next tab
	 * @param keycode The {@link Keys} keycode
	 */
	public void setNextTabHotkey(int keycode) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, nextTabButton, true));
	}

	/**
	 * Sets a {@link ControllerButton} as the hotkey for changing to the next tab
	 * @param button The {@link ControllerButton}
	 */
	public void setNextTabHotkey(ControllerButton button) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, nextTabButton, true));
	}

	/**
	 * Unsets a keyboard key as the hotkey for changing to the previous tab
	 * @param keycode The {@link Keys} keycode
	 */
	public void unsetPreviousTabHotkey(int keycode) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, previousTabButton, false));
	}

	/**
	 * Unets a {@link ControllerButton} as the hotkey for changing to the previous tab
	 * @param button The {@link ControllerButton}
	 */
	public void unsetPreviousTabHotkey(ControllerButton button) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, previousTabButton, false));
	}

	/**
	 * Unsets a keyboard key as the hotkey for changing to the next tab
	 * @param keycode The {@link Keys} keycode
	 */
	public void unsetNextTabHotkey(int keycode) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, nextTabButton, false));
	}

	/**
	 * Unets a {@link ControllerButton} as the hotkey for changing to the next tab
	 * @param button The {@link ControllerButton}
	 */
	public void unsetNextTabHotkey(ControllerButton button) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, nextTabButton, false));
	}

	@Override
	public void setHotkey(ControllerButton button, Actionable actionable) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows setPreviousTabHotkey and setNextTabHotkey methods. Set hotkeys within individual Tab instances.");
	}

	@Override
	public void setHotkey(int keycode, Actionable actionable) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows setPreviousTabHotkey and setNextTabHotkey methods. Set hotkeys within individual Tab instances.");
	}

	@Override
	public void unsetHotkey(ControllerButton button) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Unset hotkeys within individual Tab instances.");
	}

	@Override
	public void unsetHotkey(int keycode) {
		throw new MdxException(TabView.class.getSimpleName()
				+ " only allows unsetPreviousTabHotkey and unsetNextTabHotkey methods. Unset hotkeys within individual Tab instances.");
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		if (layout == null) {
			return;
		}
		this.layout = layout;
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public void setPreviousTabButtonLayout(String layout) {
		previousTabButton.setLayout(layout);
	}

	public void setNextTabButtonLayout(String layout) {
		nextTabButton.setLayout(layout);
	}

	public void setTabButtonLayout(String layout) {
		this.tabButtonLayout = layout;
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).setLayout(layout);
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
		for (int i = 0; i < tabs.size(); i++) {
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
