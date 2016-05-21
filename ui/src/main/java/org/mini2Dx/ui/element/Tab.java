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

import java.util.LinkedList;
import java.util.Queue;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.input.ControllerHotKeyOperation;
import org.mini2Dx.ui.input.KeyboardHotKeyOperation;
import org.mini2Dx.ui.input.UiNavigation;
import org.mini2Dx.ui.input.VerticalUiNavigation;
import org.mini2Dx.ui.render.ActionableRenderNode;
import org.mini2Dx.ui.render.NavigatableRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TabRenderNode;

/**
 *
 */
public class Tab extends Row implements Navigatable {
	private final Queue<ControllerHotKeyOperation> controllerHotKeyOperations = new LinkedList<ControllerHotKeyOperation>();
	private final Queue<KeyboardHotKeyOperation> keyboardHotKeyOperations = new LinkedList<KeyboardHotKeyOperation>();
	
	private UiNavigation navigation = new VerticalUiNavigation();
	private boolean titleOrIconChanged = true;
	
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
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new TabRenderNode(parentRenderNode, this);
		for(int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
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
	public void syncWithRenderNode() {
		super.syncWithRenderNode();
		((NavigatableRenderNode) renderNode).syncHotkeys(controllerHotKeyOperations, keyboardHotKeyOperations);
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
	public ActionableRenderNode hotkey(ControllerButton button) {
		if(renderNode == null) {
			return null;
		}
		return ((NavigatableRenderNode) renderNode).hotkey(button);
	}
	
	@Override
	public void setHotkey(ControllerButton button, Actionable actionable) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, actionable, true));
	}
	
	@Override
	public void setHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, actionable, true));
	}
	
	@Override
	public void unsetHotkey(ControllerButton button, Actionable actionable) {
		controllerHotKeyOperations.offer(new ControllerHotKeyOperation(button, actionable, false));
	}
	
	@Override
	public void unsetHotkey(int keycode, Actionable actionable) {
		keyboardHotKeyOperations.offer(new KeyboardHotKeyOperation(keycode, actionable, false));
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
}
