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
import org.mini2Dx.ui.element.Navigatable;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.input.InputSource;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.ScreenSize;
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
import com.badlogic.gdx.math.MathUtils;

/**
 * The container for all UI elements. {@link #update(float)},
 * {@link #interpolate(float)} and {@link #render(Graphics)} must be called by
 * your {@link GameContainer}
 */
public class UiContainer extends UiElement implements InputProcessor {
	private static final String LOGGING_TAG = UiContainer.class.getSimpleName();

	private final List<Container> children = new ArrayList<Container>(1);
	private final UiContainerRenderTree renderTree;

	private InputSource lastInputSource;
	private int width, height;
	private float scaleX = 1f;
	private float scaleY = 1f;
	private boolean themeWarningIssued, initialThemeLayoutComplete;
	private UiTheme theme;

	private Navigatable activeNavigation;
	private ActionableRenderNode activeAction;
	private TextInputableRenderNode activeTextInput;

	/**
	 * Constructor
	 * @param gc Your game's {@link GameContainer}
	 * @param assetManager The {@link AssetManager} for the game
	 */
	public UiContainer(GameContainer gc, AssetManager assetManager) {
		super("ui-container-root");
		this.width = gc.getWidth();
		this.height = gc.getHeight();

		switch (Mdx.os) {
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

	/**
	 * Updates all {@link UiElement}s
	 * @param delta The time since the last frame (in seconds)
	 */
	public void update(float delta) {
		if (!isThemeApplied()) {
			if (!themeWarningIssued) {
				Gdx.app.error(LOGGING_TAG, "No theme applied to UI - cannot update or render UI.");
				themeWarningIssued = true;
			}
			return;
		}
		if (renderTree.isDirty()) {
			renderTree.layout();
			initialThemeLayoutComplete = true;
		}
		renderTree.update(delta);
	}

	/**
	 * Interpolates all {@link UiElement}s
	 * @param alpha The interpolation alpha
	 */
	public void interpolate(float alpha) {
		if (!isThemeApplied()) {
			return;
		}
		renderTree.interpolate(alpha);
	}

	/**
	 * Renders all visible {@link UiElement}s
	 * @param g The {@link Graphics} context
	 */
	public void render(Graphics g) {
		if (!isThemeApplied()) {
			return;
		}
		if (!initialThemeLayoutComplete) {
			return;
		}
		switch (visibility) {
		case HIDDEN:
			return;
		case NO_RENDER:
			return;
		default:
			float previousScaleX = g.getScaleX();
			float previousScaleY = g.getScaleY();

			if (scaleX != 1f || scaleY != 1f) {
				g.setScale(scaleX, scaleY);
			}
			renderTree.render(g);
			if (scaleX != 1f || scaleY != 1f) {
				g.setScale(previousScaleX, previousScaleY);
			}
			break;
		}
	}

	/**
	 * Adds a {@link Container} to this {@link UiContainer}
	 * @param container The {@link Container} to add
	 */
	public void add(Container container) {
		container.attach(renderTree);
		children.add(container);
	}

	/**
	 * Removes a {@link Container} from this {@link UiContainer}
	 * @param container The {@link Container} to remove
	 */
	public void remove(Container container) {
		children.remove(container);
		container.detach(renderTree);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
	}
	
	@Override
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderTree.applyEffect(effects.poll());
		}
	}

	/**
	 * Adds a {@link ScreenSizeListener} to listen for {@link ScreenSize} change
	 * @param listener The {@link ScreenSizeListener} to add
	 */
	public void addScreenSizeListener(ScreenSizeListener listener) {
		renderTree.addScreenSizeListener(listener);
	}

	/**
	 * Removes a {@link ScreenSizeListener} from this {@link UiContainer}
	 * @param listener The {@link ScreenSizeListener} to remove
	 */
	public void removeScreenSizeListener(ScreenSizeListener listener) {
		renderTree.removeScreenSizeListener(listener);
	}

	/**
	 * Returns if a {@link UiTheme} has been applied to thi {@link UiContainer}
	 * @return True if the {@link UiTheme} has been applied
	 */
	public boolean isThemeApplied() {
		return theme != null;
	}

	/**
	 * Returns the {@link UiTheme} currently applied to this {@link UiContainer}
	 * @return Null if no {@link UiTheme} has been applied
	 */
	public UiTheme getTheme() {
		return theme;
	}

	/**
	 * Sets the current {@link UiTheme} for this {@link UiContainer}
	 * @param theme The {@link UiTheme} to apply
	 */
	public void setTheme(UiTheme theme) {
		if (theme == null) {
			return;
		}
		if (this.theme != null && theme.equals(this.theme)) {
			return;
		}
		this.theme = theme;
		renderTree.setDirty(true);
		initialThemeLayoutComplete = false;
		Gdx.app.log(LOGGING_TAG, "Applied theme - " + theme.getId());
	}

	@Override
	public void setStyleId(String styleId) {
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = MathUtils.round(screenX / scaleX);
		screenY = MathUtils.round(screenY / scaleY);

		if (activeTextInput != null && activeTextInput.mouseDown(screenX, screenY, pointer, button) == null) {
			// Release textbox control
			activeTextInput = null;
			activeAction = null;
		}

		ActionableRenderNode result = renderTree.mouseDown(screenX, screenY, pointer, button);
		if (result != null) {
			result.beginAction();
			setActiveAction(result);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = MathUtils.round(screenX / scaleX);
		screenY = MathUtils.round(screenY / scaleY);
		if (activeAction == null) {
			return false;
		}
		activeAction.mouseUp(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenX = MathUtils.round(screenX / scaleX);
		screenY = MathUtils.round(screenY / scaleY);
		return renderTree.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		screenX = MathUtils.round(screenX / scaleX);
		screenY = MathUtils.round(screenY / scaleY);
		return renderTree.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (activeTextInput == null) {
			return false;
		}
		if (activeTextInput.isReceivingInput()) {
			activeTextInput.characterReceived(character);
		}
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (activeTextInput != null) {
			return true;
		}
		if (handleModalKeyDown(keycode)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (handleTextInputKeyUp(keycode)) {
			return true;
		}
		if (handleModalKeyUp(keycode)) {
			return true;
		}
		return false;
	}

	private boolean handleModalKeyDown(int keycode) {
		if (activeNavigation == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(keycode);
		if (hotkeyAction == null) {
			return true;
		}
		hotkeyAction.beginAction();
		return true;
	}

	private boolean handleModalKeyUp(int keycode) {
		if (activeNavigation == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(keycode);
		if (hotkeyAction == null) {
			if (activeAction != null) {
				activeAction.setState(NodeState.NORMAL);
			}
			ActionableRenderNode result = activeNavigation.navigate(keycode);
			if (result != null) {
				result.setState(NodeState.HOVER);
				setActiveAction(result);
			}
		} else {
			hotkeyAction.endAction();
		}
		return true;
	}

	private boolean handleTextInputKeyUp(int keycode) {
		if (activeTextInput == null) {
			return false;
		}
		if (activeTextInput.isReceivingInput()) {
			switch (keycode) {
			case Keys.BACKSPACE:
				activeTextInput.backspace();
				break;
			case Keys.ENTER:
				if (activeTextInput.enter()) {
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

		switch (keycode) {
		case Keys.ENTER:
			activeTextInput.beginAction();
			return true;
		}
		return false;
	}

	private void setActiveAction(ActionableRenderNode actionable) {
		if (actionable instanceof TextInputableRenderNode) {
			activeTextInput = (TextInputableRenderNode) actionable;
		}
		activeAction = actionable;
	}

	/**
	 * Sets the current {@link Navigatable} for UI navigation
	 * @param activeNavigation The current {@link Navigatable} being navigated
	 */
	public void setActiveNavigation(Navigatable activeNavigation) {
		this.activeNavigation = activeNavigation;
	}

	/**
	 * Clears the current {@link Navigatable} being navigated
	 */
	public void clearActiveNavigation() {
		this.activeTextInput = null;
		this.activeAction = null;
		this.activeNavigation = null;
	}

	/**
	 * Returns the width of the {@link UiContainer}
	 * @return The width in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the {@link UiContainer} 
	 * @return The height in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the width and height of the {@link UiContainer}
	 * 
	 * @param width
	 *            The width in pixels
	 * @param height
	 *            The height in pixels
	 */
	public void set(int width, int height) {
		this.width = width;
		this.height = height;
		renderTree.onResize(width, height);
	}

	/**
	 * Sets the {@link Graphics} scaling during rendering. Mouse/touch
	 * coordinates will be scaled accordingly.
	 * 
	 * @param scaleX
	 *            Scaling along the X axis
	 * @param scaleY
	 *            Scaling along the Y axis
	 */
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	/**
	 * Returns the last {@link InputSource} the {@link UiContainer} detected
	 * @return
	 */
	public InputSource getLastInputSource() {
		return lastInputSource;
	}

	@Override
	public void setZIndex(int zIndex) {
	}
}
