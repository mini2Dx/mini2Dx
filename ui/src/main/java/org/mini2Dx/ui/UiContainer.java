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
package org.mini2Dx.ui;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.viewport.Viewport;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntArray;
import org.mini2Dx.gdx.utils.IntSet;
import org.mini2Dx.gdx.utils.ObjectSet;
import org.mini2Dx.ui.gamepad.GamePadUiInput;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.GamePadEventTriggerParams;
import org.mini2Dx.ui.event.params.KeyboardEventTriggerParams;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.PixelLayoutUtils;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.listener.UiContainerListener;
import org.mini2Dx.ui.listener.UiInputSourceListener;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.render.*;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.util.IdAllocator;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The container for all UI elements. {@link #update(float)} and
 * {@link #render(Graphics)} must be called by your {@link GameContainer}
 */
public class UiContainer extends ParentUiElement implements InputProcessor {
	private static final String LOGGING_TAG = UiContainer.class.getSimpleName();
	private static final Vector2 SHARED_VECTOR = new Vector2();
	private static final Array<UiContainer> uiContainerInstances = new Array<UiContainer>(true, 2, UiContainer.class);
	private static Visibility defaultVisibility = Visibility.HIDDEN;
	private static UiTheme UI_THEME;
	private static UiContainerState STATE = UiContainerState.NOOP;

	private final Array<GamePadUiInput<?>> controllerInputs = new Array<GamePadUiInput<?>>(true,1, GamePadUiInput.class);

	private final Array<UiInputSourceListener> inputSourceListeners = new Array<UiInputSourceListener>(true,1, UiInputSourceListener.class);
	private final Array<UiContainerListener> containerListeners = new Array<UiContainerListener>(true,1, UiContainerListener.class);

	private final IntSet receivedKeyDowns = new IntSet();
	private final ObjectSet<String> receivedButtonDowns = new ObjectSet<String>();

	private final AtomicBoolean forceRenderTreeLayout = new AtomicBoolean(false);
	private final UiContainerRenderTree renderTree;

	private InputSource lastInputSource, nextInputSource;
	private GamePadType lastGamePadType = GamePadType.UNKNOWN, nextGamePadType = GamePadType.UNKNOWN;
	private int lastMouseX, lastMouseY;
	private float scaleX = 1f;
	private float scaleY = 1f;
	private String lastThemeId;
	private boolean themeWarningIssued, initialThemeLayoutComplete;
	private float inputSourceChangeTimer = 0f, inputSourceChangeThreshold = 0.1f;

	private final IntSet actionKeys = new IntSet();
	private Navigatable activeNavigation;
	private ActionableRenderNode activeAction;
	private TextInputableRenderNode activeTextInput;

	private NavigationMode navigationMode = NavigationMode.BUTTON_OR_POINTER;
	private boolean textInputIgnoredFirstEnter = false;
	private ScreenSizeScaleMode screenSizeScaleMode = ScreenSizeScaleMode.NO_SCALING;

	private boolean passThroughMouseMovement = false;

	private Viewport viewport;

	/**
	 * Constructor
	 * 
	 * @param gc
	 *            Your game's {@link GameContainer}
	 * @param assetManager
	 *            The {@link AssetManager} for the game
	 */
	public UiContainer(GameContainer gc, AssetManager assetManager) {
		this(gc.getWidth(), gc.getHeight(), assetManager);
	}

	public UiContainer(int width, int height, AssetManager assetManager) {
		super(IdAllocator.getNextId("ui-container-root"));
		this.width = width;
		this.height = height;

		actionKeys.add(Input.Keys.ENTER);

		switch (Mdx.platform) {
		case ANDROID:
		case IOS:
			lastInputSource = InputSource.TOUCHSCREEN;
			break;
		case MAC:
		case LINUX:
		case WINDOWS:
			lastInputSource = InputSource.KEYBOARD_MOUSE;
			break;
		default:
			break;
		}

		renderTree = new UiContainerRenderTree(this, assetManager);
		super.renderNode = renderTree;

		setVisibility(Visibility.VISIBLE);
		uiContainerInstances.add(this);
	}
	
	public static void relayoutAllUiContainers() {
		Mdx.log.info(LOGGING_TAG, "Triggering re-layout for all UiContainer instances");
		for(int i = uiContainerInstances.size - 1; i >= 0; i--) {
			uiContainerInstances.get(i).forceRenderTreeLayout();
		}
	}
	
	private void forceRenderTreeLayout() {
		forceRenderTreeLayout.set(true);
	}
	
	public void dispose() {
		uiContainerInstances.removeValue(this, false);
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return renderTree;
	}

	/**
	 * Updates all {@link UiElement}s
	 * 
	 * @param delta
	 *            The time since the last frame (in seconds)
	 */
	public void update(float delta) {
		updateLastInputSource(delta);
		updateLastGamePadType();
		if (!isThemeApplied()) {
			if (!themeWarningIssued) {
				if (Mdx.log != null) {
					Mdx.log.error(LOGGING_TAG, "No theme applied to UI - cannot update or render UI.");
				}
				themeWarningIssued = true;
			}
			return;
		}
		if(lastThemeId == null || (lastThemeId != null && !lastThemeId.equals(UI_THEME.getId()))) {
			renderTree.setDirty();
			initialThemeLayoutComplete = false;
			Mdx.log.info(LOGGING_TAG, "Applied theme - " + UI_THEME.getId());
		}
		lastThemeId = UI_THEME.getId();

		if(forceRenderTreeLayout.get()) {
			renderTree.onResize(width, height);
			forceRenderTreeLayout.set(false);
		}
		
		notifyPreUpdate(delta);
		for (int i = controllerInputs.size - 1; i >= 0; i--) {
			controllerInputs.get(i).update(delta);
		}
		if (renderTree.isDirty()) {
			STATE = UiContainerState.LAYOUT;
			renderTree.layout();
			STATE = UiContainerState.NOOP;
			renderTree.processLayoutDeferred();
			initialThemeLayoutComplete = true;
		}
		STATE = UiContainerState.UPDATE;
		renderTree.update(delta);
		notifyPostUpdate(delta);
		STATE = UiContainerState.NOOP;
		renderTree.processUpdateDeferred();

		PixelLayoutUtils.update(delta);
	}

	/**
	 * Renders all visible {@link UiElement}s
	 * 
	 * @param g
	 *            The {@link Graphics} context
	 */
	public void render(Graphics g) {
		if (!isThemeApplied()) {
			return;
		}
		if (!initialThemeLayoutComplete) {
			return;
		}
		STATE = UiContainerState.RENDER;
		notifyPreRender(g);
		switch (visibility) {
		case HIDDEN:

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
		notifyPostRender(g);
		STATE = UiContainerState.NOOP;
		renderTree.processRenderDeferred();
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

	/**
	 * Adds a {@link ScreenSizeListener} to listen for {@link ScreenSize} change
	 * 
	 * @param listener
	 *            The {@link ScreenSizeListener} to add
	 */
	public void addScreenSizeListener(ScreenSizeListener listener) {
		renderTree.addScreenSizeListener(listener);
	}

	/**
	 * Removes a {@link ScreenSizeListener} from this {@link UiContainer}
	 * 
	 * @param listener
	 *            The {@link ScreenSizeListener} to remove
	 */
	public void removeScreenSizeListener(ScreenSizeListener listener) {
		renderTree.removeScreenSizeListener(listener);
	}

	/**
	 * Returns if a {@link UiTheme} has been applied to thi {@link UiContainer}
	 * 
	 * @return True if the {@link UiTheme} has been applied
	 */
	public static boolean isThemeApplied() {
		return UI_THEME != null;
	}

	/**
	 * Returns the {@link UiTheme} currently applied to this {@link UiContainer}
	 * 
	 * @return Null if no {@link UiTheme} has been applied
	 */
	public static UiTheme getTheme() {
		return UI_THEME;
	}

	/**
	 * Sets the current {@link UiTheme} for this {@link UiContainer}
	 * 
	 * @param theme
	 *            The {@link UiTheme} to apply
	 */
	public static void setTheme(UiTheme theme) {
		if (theme == null) {
			return;
		}
		if (UI_THEME != null && UI_THEME.getId().equals(theme.getId())) {
			return;
		}
		UI_THEME = theme;
	}

	/**
	 * Returns the current {@link UiContainerState}
	 * @return
	 */
	public static UiContainerState getState() {
		return STATE;
	}

	@Override
	public void setStyleId(String styleId) {
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!pointerNavigationAllowed()) {
			return false;
		}
		if (viewport != null) {
			SHARED_VECTOR.x = screenX;
			SHARED_VECTOR.y = screenY;
			viewport.toWorldCoordinates(SHARED_VECTOR);
			screenX = MathUtils.round(SHARED_VECTOR.x);
			screenY = MathUtils.round(SHARED_VECTOR.y);
		} else {
			screenX = MathUtils.round(screenX / scaleX);
			screenY = MathUtils.round(screenY / scaleY);
		}

		updateLastInputSource(screenX, screenY);

		lastMouseX = screenX;
		lastMouseY = screenY;

		if (activeTextInput != null && activeTextInput.mouseDown(screenX, screenY, pointer, button) == null) {
			// Release textbox control
			activeTextInput = null;
			activeAction = null;

			switch (Mdx.platform) {
			case ANDROID:
			case IOS:
				Mdx.input.setOnScreenKeyboardVisible(false);
				break;
			default:
				break;
			}
		}

		ActionableRenderNode result = renderTree.mouseDown(screenX, screenY, pointer, button);
		if (result != null) {
			MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
			params.setMouseX(screenX);
			params.setMouseY(screenY);
			result.beginAction(EventTrigger.getTriggerForMouseClick(button), params);
			EventTriggerParamsPool.release(params);

			setActiveAction(result);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!pointerNavigationAllowed()) {
			return false;
		}
		if (viewport != null) {
			SHARED_VECTOR.x = screenX;
			SHARED_VECTOR.y = screenY;
			viewport.toWorldCoordinates(SHARED_VECTOR);
			screenX = MathUtils.round(SHARED_VECTOR.x);
			screenY = MathUtils.round(SHARED_VECTOR.y);
		} else {
			screenX = MathUtils.round(screenX / scaleX);
			screenY = MathUtils.round(screenY / scaleY);
		}

		updateLastInputSource(screenX, screenY);

		lastMouseX = screenX;
		lastMouseY = screenY;

		if (activeAction == null) {
			return false;
		}
		activeAction.mouseUp(screenX, screenY, pointer, button);
		activeAction = null;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!pointerNavigationAllowed()) {
			return false;
		}
		if (viewport != null) {
			SHARED_VECTOR.x = screenX;
			SHARED_VECTOR.y = screenY;
			viewport.toWorldCoordinates(SHARED_VECTOR);
			screenX = MathUtils.round(SHARED_VECTOR.x);
			screenY = MathUtils.round(SHARED_VECTOR.y);
		} else {
			screenX = MathUtils.round(screenX / scaleX);
			screenY = MathUtils.round(screenY / scaleY);
		}

		updateLastInputSource(screenX, screenY);

		lastMouseX = screenX;
		lastMouseY = screenY;

		if(passThroughMouseMovement) {
			renderTree.mouseMoved(screenX, screenY);
			return false;
		}
		return renderTree.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		if (viewport != null) {
			SHARED_VECTOR.x = screenX;
			SHARED_VECTOR.y = screenY;
			viewport.toWorldCoordinates(SHARED_VECTOR);
			screenX = MathUtils.round(SHARED_VECTOR.x);
			screenY = MathUtils.round(SHARED_VECTOR.y);
		} else {
			screenX = MathUtils.round(screenX / scaleX);
			screenY = MathUtils.round(screenY / scaleY);
		}

		updateLastInputSource(screenX, screenY);

		lastMouseX = screenX;
		lastMouseY = screenY;

		if (!pointerNavigationAllowed()) {
			return false;
		}
		if(passThroughMouseMovement) {
			renderTree.mouseMoved(screenX, screenY);
			return false;
		}
		return renderTree.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (!pointerNavigationAllowed()) {
			return false;
		}
		return renderTree.mouseScrolled(lastMouseX, lastMouseY, amountX, amountY);
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
		setLastInputSource(InputSource.KEYBOARD_MOUSE);
		return keyDownNoInputChange(keycode);
	}

	/**
	 * Simulates a key down event without changing the input source
	 * @param keycode
	 */
	public boolean keyDownNoInputChange(int keycode) {
		if (activeTextInput != null && activeTextInput.isReceivingInput()) {
			receivedKeyDowns.add(keycode);
			return true;
		}
		if (actionKeys.contains(keycode) && activeAction != null) {
			receivedKeyDowns.add(keycode);

			KeyboardEventTriggerParams params = EventTriggerParamsPool.allocateKeyboardParams();
			params.setKey(keycode);
			activeAction.setState(NodeState.ACTION);
			activeAction.beginAction(EventTrigger.KEYBOARD, params);
			EventTriggerParamsPool.release(params);

			if (activeTextInput != null) {
				textInputIgnoredFirstEnter = false;
			}
			return true;
		}
		if (handleModalKeyDown(keycode)) {
			receivedKeyDowns.add(keycode);
			return true;
		}
		receivedKeyDowns.remove(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// Key down was sent before this UI Container accepted input
		if (!receivedKeyDowns.remove(keycode)) {
			return false;
		}
		if (handleTextInputKeyUp(keycode)) {
			return true;
		}
		if (actionKeys.contains(keycode) && activeAction != null) {
			KeyboardEventTriggerParams params = EventTriggerParamsPool.allocateKeyboardParams();
			params.setKey(keycode);
			activeAction.setState(NodeState.NORMAL);
			activeAction.endAction(EventTrigger.KEYBOARD, params);
			EventTriggerParamsPool.release(params);

			switch (Mdx.platform) {
			case ANDROID:
			case IOS:
				Mdx.input.setOnScreenKeyboardVisible(false);
				break;
			default:
				break;
			}
			return true;
		}
		if (handleModalKeyUp(keycode)) {
			return true;
		}
		return false;
	}

	public boolean buttonDown(GamePadUiInput<?> controllerUiInput, GamePadButton button) {
		if (activeNavigation == null) {
			return false;
		}
		receivedButtonDowns.add(button.getInternalName());
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(button);
		if (hotkeyAction != null) {
			if(!hotkeyAction.isEnabled()) {
				return true;
			}
			GamePadEventTriggerParams params = EventTriggerParamsPool.allocateGamePadParams();
			params.setGamePadButton(button);
			hotkeyAction.setState(NodeState.ACTION);
			hotkeyAction.beginAction(EventTrigger.CONTROLLER, params);
			EventTriggerParamsPool.release(params);
		} else if (activeAction != null) {
			if (button.equals(controllerUiInput.getActionButton())) {
				if (activeTextInput != null) {
					if (!textInputIgnoredFirstEnter) {
						GamePadEventTriggerParams params = EventTriggerParamsPool.allocateGamePadParams();
						params.setGamePadButton(button);
						activeAction.setState(NodeState.ACTION);
						activeAction.beginAction(EventTrigger.CONTROLLER, params);
						EventTriggerParamsPool.release(params);
					}
				} else {
					GamePadEventTriggerParams params = EventTriggerParamsPool.allocateGamePadParams();
					params.setGamePadButton(button);
					activeAction.setState(NodeState.ACTION);
					activeAction.beginAction(EventTrigger.CONTROLLER, params);
					EventTriggerParamsPool.release(params);
				}
			}
		}
		return true;
	}

	public boolean buttonUp(GamePadUiInput<?> controllerUiInput, GamePadButton button) {
		// Button down was sent before this UI Container accepted input
		if (!receivedButtonDowns.remove(button.getInternalName())) {
			return false;
		}
		if (activeNavigation == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(button);
		if (hotkeyAction != null) {
			if(!hotkeyAction.isEnabled()) {
				return true;
			}
			GamePadEventTriggerParams params = EventTriggerParamsPool.allocateGamePadParams();
			params.setGamePadButton(button);
			hotkeyAction.setState(NodeState.NORMAL);
			hotkeyAction.endAction(EventTrigger.CONTROLLER, params);
			EventTriggerParamsPool.release(params);
		} else if (activeAction != null) {
			if (activeTextInput != null && !textInputIgnoredFirstEnter) {
				textInputIgnoredFirstEnter = true;
				return true;
			}
			if (button.equals(controllerUiInput.getActionButton())) {
				GamePadEventTriggerParams params = EventTriggerParamsPool.allocateGamePadParams();
				params.setGamePadButton(button);
				activeAction.setState(NodeState.NORMAL);
				activeAction.endAction(EventTrigger.CONTROLLER, params);
				EventTriggerParamsPool.release(params);
				textInputIgnoredFirstEnter = false;
			}
		}
		return true;
	}

	private boolean handleModalKeyDown(int keycode) {
		if (activeNavigation == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(keycode);
		if (hotkeyAction == null) {
			if (keyNavigationAllowed()) {
				if (activeAction != null) {
					activeAction.setState(NodeState.NORMAL);
				}
				ActionableRenderNode result = activeNavigation.navigate(keycode);
				if (result != null) {
					result.setState(NodeState.HOVER);
					setActiveAction(result);
				}
			}
		} else {
			if(!hotkeyAction.isEnabled()) {
				return true;
			}
			KeyboardEventTriggerParams params = EventTriggerParamsPool.allocateKeyboardParams();
			params.setKey(keycode);
			hotkeyAction.setState(NodeState.ACTION);
			hotkeyAction.beginAction(EventTrigger.KEYBOARD, params);
			EventTriggerParamsPool.release(params);
		}
		return true;
	}

	private boolean handleModalKeyUp(int keycode) {
		if (activeNavigation == null) {
			return false;
		}
		ActionableRenderNode hotkeyAction = activeNavigation.hotkey(keycode);
		if (hotkeyAction != null) {
			if(!hotkeyAction.isEnabled()) {
				return true;
			}
			KeyboardEventTriggerParams params = EventTriggerParamsPool.allocateKeyboardParams();
			params.setKey(keycode);
			hotkeyAction.setState(NodeState.NORMAL);
			hotkeyAction.endAction(EventTrigger.KEYBOARD, params);
			EventTriggerParamsPool.release(params);
		}
		return true;
	}

	private boolean handleTextInputKeyUp(int keycode) {
		if (activeTextInput == null) {
			return false;
		}
		if (!activeTextInput.isReceivingInput()) {
			return false;
		}
		switch (keycode) {
		case Input.Keys.BACKSPACE:
			activeTextInput.backspace();
			break;
		case Input.Keys.ENTER:
			if (!textInputIgnoredFirstEnter) {
				textInputIgnoredFirstEnter = true;
				return true;
			}
			if (activeTextInput.enter()) {
				activeTextInput = null;
				activeAction = null;
				switch (Mdx.platform) {
				case ANDROID:
				case IOS:
					Mdx.input.setOnScreenKeyboardVisible(false);
					break;
				default:
					break;
				}
			}
			break;
		case Input.Keys.RIGHT:
			activeTextInput.moveCursorRight();
			break;
		case Input.Keys.LEFT:
			activeTextInput.moveCursorLeft();
			break;
		case Input.Keys.ESCAPE:
			unsetActiveAction();
			break;
		}
		return true;
	}

	public void setActiveAction(ActionableRenderNode actionable) {
		if (activeAction != null && !activeAction.getId().equals(actionable.getId())) {
			activeAction.setState(NodeState.NORMAL);
		}
		if (actionable instanceof TextInputableRenderNode) {
			activeTextInput = (TextInputableRenderNode) actionable;
			switch (Mdx.platform) {
			case ANDROID:
			case IOS:
				Mdx.input.setOnScreenKeyboardVisible(true);
				break;
			default:
				break;
			}
		}
		activeAction = actionable;
		notifyElementActivated(actionable);
	}

	public void unsetActiveAction() {
		if (activeAction == null) {
			return;
		}
		if (activeAction instanceof TextInputableRenderNode) {
			switch (Mdx.platform) {
			case ANDROID:
			case IOS:
				Mdx.input.setOnScreenKeyboardVisible(false);
				break;
			default:
				break;
			}
		}
		activeAction = null;
		activeTextInput = null;
	}

	/**
	 * Sets the current {@link Navigatable} for UI navigation
	 * 
	 * @param activeNavigation
	 *            The current {@link Navigatable} being navigated
	 */
	public void setActiveNavigation(Navigatable activeNavigation) {
		setActiveNavigation(activeNavigation, true);
	}

	/**
	 * Sets the current {@link Navigatable} for UI navigation
	 * 
	 * @param activeNavigation
	 *            The current {@link Navigatable} being navigated
	 * @param resetCursor Should the cursor position reset
	 */
	public void setActiveNavigation(Navigatable activeNavigation, boolean resetCursor) {
		if (this.activeNavigation != null && activeNavigation != null
				&& this.activeNavigation.getId().equals(activeNavigation.getId())) {
			return;
		}
		unsetExistingNavigationHover();
		this.activeNavigation = activeNavigation;

		if (renderTree == null) {
			return;
		}
		if (!keyNavigationAllowed()) {
			return;
		}
		if (activeAction != null) {
			activeAction.setState(NodeState.NORMAL);
		}
		UiNavigation navigation = activeNavigation.getNavigation();
		if (navigation == null) {
			return;
		}
		Actionable actionable;
		if (resetCursor) {
			actionable = navigation.resetCursor();
		} else {
			actionable = navigation.getCursor();
		}
		if (actionable == null) {
			return;
		}
		RenderNode<?, ?> renderNode = renderTree.getElementById(actionable.getId());
		if (renderNode == null) {
			return;
		}
		setActiveAction(((ActionableRenderNode) renderNode));
		((ActionableRenderNode) renderNode).setState(NodeState.HOVER);
	}

	private void unsetExistingNavigationHover() {
		if (activeNavigation == null) {
			return;
		}
		if (renderTree == null) {
			return;
		}
		if (activeAction == null) {
			return;
		}
		if (activeAction.getState() != NodeState.HOVER) {
			return;
		}
		activeAction.setState(NodeState.NORMAL);
	}

	public void clearActiveAction() {
		unsetExistingNavigationHover();
		this.activeAction = null;
	}

	/**
	 * Clears the current {@link Navigatable} being navigated
	 */
	public void clearActiveNavigation() {
		clearActiveNavigation(true);
	}

	/**
	 * Clears the current {@link Navigatable} being navigated
	 *
	 * @param unsetHoveredItem
	 *            Boolean for unsetting hovered item
	 */
	public void clearActiveNavigation(boolean unsetHoveredItem) {
		if (unsetHoveredItem) {
			unsetExistingNavigationHover();
		}
		this.activeTextInput = null;
		this.activeAction = null;
		this.activeNavigation = null;
	}

	/**
	 * Returns the currently active {@link Navigatable}
	 * 
	 * @return null if there is nothing active
	 */
	public Navigatable getActiveNavigation() {
		return activeNavigation;
	}

	/**
	 * Returns the currently hovered {@link ActionableRenderNode}
	 * @return Null if nothing is hovered
	 */
	public ActionableRenderNode getActiveAction() {
		return activeAction;
	}

	/**
	 * Returns the width of the {@link UiContainer}
	 * 
	 * @return The width in pixels
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height of the {@link UiContainer}
	 * 
	 * @return The height in pixels
	 */
	public float getHeight() {
		return height;
	}

	@Override
	public StyleRule getStyleRule() {
		return StyleRule.NOOP;
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
	 * Returns the configured {@link Graphics} scaling during rendering
	 * 
	 * @return 1f by default
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * Returns the configured {@link Graphics} scaling during rendering
	 * 
	 * @return 1f by default
	 */
	public float getScaleY() {
		return scaleY;
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
		if(scaleX == this.scaleX && scaleY == this.scaleY) {
			return;
		}
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		renderTree.onResize(width, height);
	}

	/**
	 * Returns the last {@link InputSource} used on the {@link UiContainer}
	 * 
	 * @return
	 */
	public InputSource getLastInputSource() {
		if(lastInputSource == null) {
			if(Mdx.platform.isConsole()) {
				lastInputSource = InputSource.CONTROLLER;
			} else if(Mdx.platform.isMobile()) {
				lastInputSource = InputSource.TOUCHSCREEN;
			}
		}
		return lastInputSource;
	}

	private void updateLastInputSource(int screenX, int screenY) {
		if(Math.abs(screenX - lastMouseX) > 2 || Math.abs(screenY - lastMouseY) > 2) {
			switch(Mdx.platform) {
			case WINDOWS:
			case MAC:
			case LINUX:
			default:
				setLastInputSource(InputSource.KEYBOARD_MOUSE);
				break;
			case ANDROID:
			case IOS:
				setLastInputSource(InputSource.TOUCHSCREEN);
				break;
			}
		}
	}

	private void updateLastInputSource(float delta) {
		if (nextInputSource == null) {
			return;
		}
		if (this.lastInputSource.equals(nextInputSource)) {
			inputSourceChangeTimer = 0f;
			return;
		}
		inputSourceChangeTimer += delta;
		if(inputSourceChangeTimer < inputSourceChangeThreshold) {
			return;
		}
		inputSourceChangeTimer = 0f;

		InputSource oldInputSource = this.lastInputSource;
		this.lastInputSource = nextInputSource;
		notifyInputSourceChange(oldInputSource, lastInputSource);
	}

	/**
	 * Sets the last {@link InputSource} used on the {@link UiContainer}
	 * 
	 * @param lastInputSource
	 *            The {@link InputSource} last used
	 */
	public void setLastInputSource(InputSource lastInputSource) {
		this.nextInputSource = lastInputSource;
	}

	/**
	 * Returns the last {@link GamePadType} used on the {@link UiContainer}
	 * 
	 * @return
	 */
	public GamePadType getLastGamePadType() {
		return lastGamePadType;
	}

	private void updateLastGamePadType() {
		if (nextGamePadType == null) {
			return;
		}
		if (this.lastGamePadType.equals(nextGamePadType)) {
			return;
		}
		GamePadType oldGamePadType = this.lastGamePadType;
		this.lastGamePadType = nextGamePadType;
		notifyGamePadTypeChange(oldGamePadType, lastGamePadType);
	}

	/**
	 * Sets the last {@link GamePadType} used on the {@link UiContainer}
	 * 
	 * @param lastGamePadType
	 *            The {@link GamePadType} last used
	 */
	public void setLastGamePadType(GamePadType lastGamePadType) {
		this.nextGamePadType = lastGamePadType;
	}

	/**
	 * Adds a {@link GamePadUiInput} instance to this {@link UiContainer}
	 * 
	 * @param input
	 *            The instance to add
	 */
	public void addGamePadInput(GamePadUiInput<?> input) {
		controllerInputs.add(input);
	}

	/**
	 * Removes a {@link GamePadUiInput} instance from this
	 * {@link UiContainer}
	 * 
	 * @param input
	 *            The instance to remove
	 */
	public void removeGamePadInput(GamePadUiInput<?> input) {
		controllerInputs.removeValue(input, false);
	}

	@Override
	public void setZIndex(int zIndex) {
	}

	/**
	 * Add the key used for triggering actions (i.e. selecting a menu option)
	 * 
	 * @param keycode
	 *            The {@link Input.Keys} value
	 */
	public void addActionKey(int keycode) {
		actionKeys.add(keycode);
	}

	/**
	 * Removes a key used for triggering actions (i.e. selecting a menu option)
	 *
	 * @param keycode
	 *            The {@link Input.Keys} value
	 */
	public void removeActionKey(int keycode) {
		actionKeys.remove(keycode);
	}

	/**
	 * Clears the keys used for triggering actions (i.e. selecting a menu option)
	 */
	public void clearActionKeys() {
		actionKeys.clear();
	}

	/**
	 * Set to true if mouseMoved() events to should pass through this input handler regardless
	 * @param passThroughMouseMovement
	 */
	public void setPassThroughMouseMovement(boolean passThroughMouseMovement) {
		this.passThroughMouseMovement = passThroughMouseMovement;
	}

	/**
	 * Sets the duration of new input that needs to occur before the input source is considered changed
	 * @param inputSourceChangeThreshold The time in seconds
	 */
	public void setInputSourceChangeThreshold(float inputSourceChangeThreshold) {
		this.inputSourceChangeThreshold = inputSourceChangeThreshold;
	}

	/**
	 * Returns if this {@link UiContainer} can be navigated by keyboard/gamepad
	 * @return True by default
	 */
	public boolean keyNavigationAllowed() {
		switch (Mdx.platform) {
		case ANDROID:
		case IOS:
			return false;
		case MAC:
		case LINUX:
		case WINDOWS:
		default:
			switch(navigationMode) {
			case BUTTON_ONLY:
			case BUTTON_OR_POINTER:
				return true;
			default:
			case POINTER_ONLY:
				return false;
			}
		}
	}

	/**
	 * Returns if this {@link UiContainer} can be navigated by touch/mouse
	 * @return True by default
	 */
	public boolean pointerNavigationAllowed() {
		switch (Mdx.platform) {
		case ANDROID:
		case IOS:
			return true;
		case MAC:
		case LINUX:
		case WINDOWS:
		default:
			switch(navigationMode) {
			default:
			case BUTTON_ONLY:
				return false;
			case BUTTON_OR_POINTER:
			case POINTER_ONLY:
				return true;
			}
		}
	}

	/**
	 * Sets the {@link NavigationMode} on this {@link UiContainer}
	 * @param navigationMode The {@link NavigationMode}
	 */
	public void setNavigationMode(NavigationMode navigationMode) {
		if(navigationMode == null) {
			return;
		}
		this.navigationMode = navigationMode;
	}

	/**
	 * Returns the scaling mode used for {@link ScreenSize} values
	 * @return {@link ScreenSizeScaleMode#NO_SCALING} by default
	 */
	public ScreenSizeScaleMode getScreenSizeScaleMode() {
		return screenSizeScaleMode;
	}

	/**
	 * Sets the scaling mode used for {@link ScreenSize} values
	 * @param screenSizeScaleMode The {@link ScreenSizeScaleMode} to set
	 */
	public void setScreenSizeScaleMode(ScreenSizeScaleMode screenSizeScaleMode) {
		if(screenSizeScaleMode == null) {
			return;
		}
		if(this.screenSizeScaleMode == screenSizeScaleMode) {
			return;
		}
		this.screenSizeScaleMode = screenSizeScaleMode;
		renderTree.onResize(width, height);
	}

	/**
	 * Adds a {@link UiContainerListener} to this {@link UiContainer}
	 * 
	 * @param listener
	 *            The {@link UiContainerListener} to be notified of events
	 */
	public void addUiContainerListener(UiContainerListener listener) {
		containerListeners.add(listener);
		inputSourceListeners.add(listener);
		addScreenSizeListener(listener);
	}

	/**
	 * Removes a {@link UiContainerListener} from this {@link UiContainer}
	 * 
	 * @param listener
	 *            The {@link UiContainerListener} to stop receiving events
	 */
	public void removeUiContainerListener(UiContainerListener listener) {
		containerListeners.removeValue(listener, false);
		inputSourceListeners.removeValue(listener, false);
		removeScreenSizeListener(listener);
	}

	/**
	 * Adds a {@link UiInputSourceListener} to this {@link UiContainer}
	 *
	 * @param listener
	 *            The {@link UiInputSourceListener} to be notified of events
	 */
	public void addInputSourceListener(UiInputSourceListener listener) {
		inputSourceListeners.add(listener);
	}

	/**
	 * Removes a {@link UiInputSourceListener} from this {@link UiContainer}
	 *
	 * @param listener
	 *            The {@link UiInputSourceListener} to stop receiving events
	 */
	public void removeUiContainerListener(UiInputSourceListener listener) {
		inputSourceListeners.removeValue(listener, false);
	}

	private void notifyPreUpdate(float delta) {
		for (int i = containerListeners.size - 1; i >= 0; i--) {
			containerListeners.get(i).preUpdate(this, delta);
		}
	}

	private void notifyPostUpdate(float delta) {
		for (int i = containerListeners.size - 1; i >= 0; i--) {
			containerListeners.get(i).postUpdate(this, delta);
		}
	}

	private void notifyPreRender(Graphics g) {
		for (int i = containerListeners.size - 1; i >= 0; i--) {
			containerListeners.get(i).preRender(this, g);
		}
	}

	private void notifyPostRender(Graphics g) {
		for (int i = containerListeners.size - 1; i >= 0; i--) {
			containerListeners.get(i).postRender(this, g);
		}
	}

	/**
	 * Sends input source change event to all containers with the current input source as both the old and new
	 */
	public void notifyInputSource() {
		notifyInputSourceChange(getLastInputSource(), getLastInputSource());
	}

	/**
	 * Sends game pad change event to all containers with the current game pad type as both the old and new
	 */
	public void notifyGamePadType() {
		notifyGamePadTypeChange(lastGamePadType, lastGamePadType);
	}

	private void notifyInputSourceChange(InputSource oldSource, InputSource newSource) {
		for(int i = uiContainerInstances.size - 1; i >= 0; i--) {
			uiContainerInstances.get(i).notifyInputSourceChange(uiContainerInstances.get(i), oldSource, newSource);
		}
	}

	private void notifyInputSourceChange(UiContainer uiContainer, InputSource oldSource, InputSource newSource) {
		for (int i = uiContainer.inputSourceListeners.size - 1; i >= 0; i--) {
			uiContainer.inputSourceListeners.get(i).inputSourceChanged(uiContainer, oldSource, newSource);
		}
	}

	private void notifyGamePadTypeChange(GamePadType oldGamePadType, GamePadType newGamePadType) {
		for(int i = uiContainerInstances.size - 1; i >= 0; i--) {
			uiContainerInstances.get(i).notifyGamePadTypeChange(uiContainerInstances.get(i), oldGamePadType, newGamePadType);
		}
	}

	private void notifyGamePadTypeChange(UiContainer uiContainer, GamePadType oldGamePadType, GamePadType newGamePadType) {
		for (int i = uiContainer.inputSourceListeners.size - 1; i >= 0; i--) {
			uiContainer.inputSourceListeners.get(i).gamePadTypeChanged(uiContainer, oldGamePadType, newGamePadType);
		}
	}

	private void notifyElementActivated(ActionableRenderNode actionable) {
		for (int i = containerListeners.size - 1; i >= 0; i--) {
			containerListeners.get(i).onElementAction(this, actionable.getElement());
		}
	}

	/**
	 * Returns the default {@link Visibility} for newly created
	 * {@link UiElement} objects
	 * 
	 * @return A non-null {@link Visibility} value. {@link Visibility#HIDDEN} by
	 *         default
	 */
	public static Visibility getDefaultVisibility() {
		return defaultVisibility;
	}

	/**
	 * Sets the default {@link Visibility} for newly created {@link UiElement}
	 * objects
	 * 
	 * @param defaultVisibility
	 *            The {@link Visibility} to set as default
	 */
	public static void setDefaultVisibility(Visibility defaultVisibility) {
		if (defaultVisibility == null) {
			return;
		}
		UiContainer.defaultVisibility = defaultVisibility;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}
}
