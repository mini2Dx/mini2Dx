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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.TextInputable;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class UiContainer implements GameResizeListener, InputProcessor, UiContentContainer {
	private final GameContainer gc;
	private final AssetManager assetManager;
	private final UiRenderer uiRenderer;
	
	private final List<UiElement<?>> elements = new ArrayList<UiElement<?>>(1);
	private final List<UiElement<?>> disposedElements = new ArrayList<UiElement<?>>(1);
	private final Map<String, UiElement<?>> elementsById = new HashMap<String, UiElement<?>>();
	private final List<ScreenSizeListener> screenSizeListeners = new ArrayList<ScreenSizeListener>(1);
	
	private UiTheme theme;
	private ScreenSize currentScreenSize;
	private boolean visible = true;
	private Actionable currentAction;
	private TextInputable currentTextInput;
	
	public UiContainer(GameContainer gc, AssetManager assetManager) {
		this.gc = gc;
		this.assetManager = assetManager;
		this.uiRenderer = new UiRenderer(this);
		
		onResize(gc.getWidth(), gc.getHeight());
		gc.addResizeListener(this);
	}
	
	public void applyTheme(String filepath) {
		this.theme = assetManager.get(filepath, UiTheme.class);
		onResize(gc.getWidth(), gc.getHeight());
	}
	
	public void update(float delta) {
		for(int i = 0; i < elements.size(); i++) {
			UiElement<?> element = elements.get(i);
			element.update(this, delta);
			if(element.disposed()) {
				disposedElements.add(element);
			}
		}
		
		if(disposedElements.isEmpty()) {
			return;
		}
		elements.removeAll(disposedElements);
		disposedElements.clear();
	}
	
	public void interpolate(float alpha) {
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).interpolate(this, alpha);;
		}
	}
	
	public void render(Graphics g) {
		if(uiRenderer == null) {
			return;
		}
		if(!visible) {
			return;
		}
		uiRenderer.setGraphics(g);
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).accept(uiRenderer);
		}
	}

	@Override
	public void onResize(int width, int height) {
		ScreenSize screenSize = ScreenSize.XS;
		if(width >= ScreenSize.SM.getMinSize()) {
			screenSize = ScreenSize.SM;
		}
		if(width >= ScreenSize.MD.getMinSize()) {
			screenSize = ScreenSize.MD;
		}
		if(width >= ScreenSize.LG.getMinSize()) {
			screenSize = ScreenSize.LG;
		}
		if(width >= ScreenSize.XL.getMinSize()) {
			screenSize = ScreenSize.XL;
		}
		this.currentScreenSize = screenSize;
		
		if(theme == null) {
			return;
		}
		
		float columnWidth = width / theme.getColumns();
		
		for(int i = 0; i < elements.size(); i++) {
			elements.get(i).resize(screenSize, theme, columnWidth, height);
		}
		for(int i = screenSizeListeners.size() - 1; i >= 0; i--) {
			screenSizeListeners.get(i).onScreenSizeChanged(screenSize, width, height);
		}
	}
	
	public UiElement<?> getById(String id) {
		if(!elementsById.containsKey(id)) {
			UiElement<?> result = null;
			for(int i = 0; i < elements.size(); i++) {
				result = elements.get(i).getById(id);
				if(result != null) {
					break;
				}
			}
			elementsById.put(id, result);
		}
		return elementsById.get(id);
	}
	
	public void add(UiElement<?> element) {
		if(theme != null) {
			element.applyStyle(theme, currentScreenSize);
		}
		elements.add(element);
	}
	
	public void remove(UiElement<?> element) {
		elements.remove(element);
	}
	
	public void clearLookupCache() {
		elementsById.clear();
	}
	
	public boolean isThemeApplied() {
		return theme != null;
	}

	public void dispose() {
		gc.removeResizeListener(this);
		elements.clear();
		disposedElements.clear();
		elementsById.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(currentTextInput == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(currentTextInput == null) {
			return false;
		}
		switch(keycode) {
		case Keys.BACKSPACE:
			currentTextInput.backspace();
			break;
		case Keys.ENTER:
			if(currentTextInput.enter()) {
				currentTextInput = null;
				currentAction = null;
			}
			break;
		case Keys.RIGHT:
			currentTextInput.moveCursorRight();
			break;
		case Keys.LEFT:
			currentTextInput.moveCursorLeft();
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if(currentTextInput == null) {
			return false;
		}
		currentTextInput.characterReceived(character);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(currentTextInput != null && currentTextInput.mouseDown(screenX, screenY, pointer, button) == null) {
			//Release textbox control
			currentTextInput = null;
			currentAction = null;
		}
		
		for(int i = elements.size() - 1; i >= 0; i--) {
			Actionable result = elements.get(i).mouseDown(screenX, screenY, pointer, button);
			if(result != null) {
				currentAction = result;
				if(currentAction instanceof TextInputable) {
					switch(Mdx.os) {
					case ANDROID:
					case IOS:
						Gdx.input.setOnscreenKeyboardVisible(true);
						break;
					default:
						break;
					}
					currentTextInput = (TextInputable) currentAction;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(currentAction == null) {
			return false;
		}
		currentAction.mouseUp(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(!visible) {
			return false;
		}
		boolean result = false;
		for(int i = elements.size() - 1; i >= 0; i--) {
			if(elements.get(i).mouseMoved(screenX, screenY)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public void addScreenSizeListener(ScreenSizeListener listener) {
		screenSizeListeners.add(listener);
	}
	
	public void removeScreenSizeListener(ScreenSizeListener listener) {
		screenSizeListeners.remove(listener);
	}

	@Override
	public int getRenderX() {
		return 0;
	}

	@Override
	public int getRenderY() {
		return 0;
	}

	@Override
	public int getRenderWidth() {
		return gc.getWidth();
	}

	@Override
	public int getRenderHeight() {
		return gc.getHeight();
	}

	@Override
	public float getContentWidth() {
		return gc.getWidth();
	}

	@Override
	public float getContentHeight() {
		return gc.getHeight();
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void onContentPositionChanged(UiElement<?> element) {
	}

	@Override
	public float getX() {
		return 0f;
	}

	@Override
	public float getY() {
		return 0f;
	}
	
	@Override
	public float getWidth() {
		return gc.getWidth();
	}
	
	@Override
	public float getHeight() {
		return gc.getHeight();
	}

	@Override
	public int getPaddingTop() {
		return 0;
	}

	@Override
	public int getPaddingBottom() {
		return 0;
	}

	@Override
	public int getPaddingLeft() {
		return 0;
	}

	@Override
	public int getPaddingRight() {
		return 0;
	}

	@Override
	public int getMarginTop() {
		return 0;
	}

	@Override
	public int getMarginBottom() {
		return 0;
	}

	@Override
	public int getMarginLeft() {
		return 0;
	}

	@Override
	public int getMarginRight() {
		return 0;
	}
}
