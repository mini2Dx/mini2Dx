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
package org.mini2Dx.ui;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Modal;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.input.InputSource;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.render.ActionableRenderNode;
import org.mini2Dx.ui.render.NodeState;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TextInputableRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class UiContainer extends UiElement implements InputProcessor {
	private static final String LOGGING_TAG = UiContainer.class.getSimpleName();
	
	private final List<Container> children = new ArrayList<Container>(1);
	private final UiContainerRenderTree renderTree;
	
	private InputSource lastInputSource;
	private int width, height;
	private boolean themeWarningIssued, initialThemeLayoutComplete;
	private UiTheme theme;
	
	private Modal activeModal;
	private ActionableRenderNode activeAction;
	private TextInputableRenderNode activeTextInput;
	
	public UiContainer(GameContainer gc, AssetManager assetManager) {
		super("ui-container-root");
		this.width = gc.getWidth();
		this.height = gc.getHeight();
		
		switch(Mdx.os) {
		case ANDROID:
		case IOS:
			lastInputSource = InputSource.TOUCHSCREEN;
			break;
		case MAC:
		case UNIX:
		case WINDOWS:
			lastInputSource = InputSource.KEYBOARD_MOUSE;
			break;
		default:
			break;
		}
		
		renderTree = new UiContainerRenderTree(this, assetManager);
		setVisibility(Visibility.VISIBLE);
	}
	
	public void update(float delta) {
		if(!isThemeApplied()) {
			if(!themeWarningIssued) {
				Gdx.app.error(LOGGING_TAG, "No theme applied to UI - cannot update or render UI.");
				themeWarningIssued = true;
			}
			return;
		}
		if(renderTree.isDirty()) {
			renderTree.layout();
			initialThemeLayoutComplete = true;
		}
		renderTree.update(delta);
	}
	
	public void interpolate(float alpha) {
		if(!isThemeApplied()) {
			return;
		}
		renderTree.interpolate(alpha);
	}
	
	public void render(Graphics g) {
		if(!isThemeApplied()) {
			return;
		}
		if(!initialThemeLayoutComplete) {
			return;
		}
		switch (visibility) {
		case HIDDEN:
			return;
		case NO_RENDER:
			return;
		default:
			renderTree.render(g);
			break;
		}
	}
	
	public void add(Container container) {
		container.attach(renderTree);
		children.add(container);
	}

	public void remove(Container container) {
		children.remove(container);
		container.detach(renderTree);
	}
	
	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {}
	
	@Override
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	@Override
	public void syncWithRenderNode() {
		while(!effects.isEmpty()) {
			renderTree.applyEffect(effects.poll());
		}
	}
	
	public void addScreenSizeListener(ScreenSizeListener listener) {
		renderTree.addScreenSizeListener(listener);
	}
	
	public void removeScreenSizeListener(ScreenSizeListener listener) {
		renderTree.removeScreenSizeListener(listener);
	}

	public boolean isThemeApplied() {
		return theme != null;
	}

	public UiTheme getTheme() {
		return theme;
	}

	public void setTheme(UiTheme theme) {
		if(theme == null) {
			return;
		}
		if(this.theme != null && theme.equals(this.theme)) {
			return;
		}
		this.theme = theme;
		renderTree.setDirty(true);
		initialThemeLayoutComplete = false;
		Gdx.app.log(LOGGING_TAG, "Applied theme - " + theme.getId());
	}

	@Override
	public void setStyleId(String styleId) {}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(activeTextInput != null && activeTextInput.mouseDown(screenX, screenY, pointer, button) == null) {
			//Release textbox control
			activeTextInput = null;
			activeAction = null;
		}
		
		ActionableRenderNode result = renderTree.mouseDown(screenX, screenY, pointer, button);
		if(result != null) {
			result.beginAction();
			setActiveAction(result);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(activeAction == null) {
			return false;
		}
		activeAction.mouseUp(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return renderTree.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		if(activeTextInput == null) {
			return false;
		}
		if(activeTextInput.isReceivingInput()) {
			activeTextInput.characterReceived(character);
		}
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(activeTextInput != null) {
			return true;
		}
		if(handleModalKeyDown(keycode)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(handleTextInputKeyUp(keycode)) {
			return true;
		}
		if(handleModalKeyUp(keycode)) {
			return true;
		}
		return false;
	}
	
	private boolean handleModalKeyDown(int keycode) {
		if(activeModal == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeModal.hotkey(keycode);
		if(hotkeyAction == null) {
			return true;
		}
		hotkeyAction.beginAction();
		return true;
	}
	
	private boolean handleModalKeyUp(int keycode) {
		if(activeModal == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeModal.hotkey(keycode);
		if(hotkeyAction == null) {
			if(activeAction != null) {
				activeAction.setState(NodeState.NORMAL);
			}
			ActionableRenderNode result = activeModal.navigate(keycode);
			result.setState(NodeState.HOVER);
			setActiveAction(result);
		} else {
			hotkeyAction.endAction();
		}
		return true;
	}
	
	private boolean handleTextInputKeyUp(int keycode) {
		if(activeTextInput == null) {
			return false;
		}
		if(activeTextInput.isReceivingInput()) {
			switch(keycode) {
			case Keys.BACKSPACE:
				activeTextInput.backspace();
				break;
			case Keys.ENTER:
				if(activeTextInput.enter()) {
					activeTextInput = null;
					activeAction = null;
				}
				break;
			case Keys.RIGHT:
				activeTextInput.moveCursorRight();
				break;
			case Keys.LEFT:
				activeTextInput.moveCursorLeft();
				break;
			}
			return true;
		}
		
		switch(keycode) {
		case Keys.ENTER:
			activeTextInput.beginAction();
			return true;
		}
		return false;
	}
	
	private void setActiveAction(ActionableRenderNode actionable) {
		if(actionable instanceof TextInputableRenderNode) {
			activeTextInput = (TextInputableRenderNode) actionable;
		}
		activeAction = actionable;
	}

	public void setActiveModal(Modal activeModal) {
		this.activeModal = activeModal;
	}
	
	public void clearActiveModal() {
		this.activeTextInput = null;
		this.activeAction = null;
		this.activeModal = null;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void set(int width, int height) {
		this.width = width;
		this.height = height;
		renderTree.onResize(width, height);
	}

	public InputSource getLastInputSource() {
		return lastInputSource;
	}
}
