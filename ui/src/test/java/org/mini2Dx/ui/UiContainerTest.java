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

import com.badlogic.gdx.Input.Keys;
import junit.framework.Assert;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.Platform;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.button.XboxButton;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.UiContainerListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for {@link UiContainer}
 */
public class UiContainerTest implements UiContainerListener {
	private final Mockery mockery = new Mockery();
	private final Set<String> listenerEvents = new HashSet<String>();
	
	private GameContainer gameContainer;
	private AssetManager assetManager;
	private Container container;
	
	private UiContainer uiContainer;
	
	@Before
	public void setUp() {
		Mdx.platform = Platform.WINDOWS;

		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		gameContainer = mockery.mock(GameContainer.class);
		assetManager = mockery.mock(AssetManager.class);
		container = mockery.mock(Container.class);
		
		mockery.checking(new Expectations() {
			{
				oneOf(gameContainer).getWidth();
				will(returnValue(800));
				oneOf(gameContainer).getHeight();
				will(returnValue(600));
			}
		});
		
		uiContainer = new UiContainer(gameContainer, assetManager);
		uiContainer.addUiContainerListener(this);
		uiContainer.setNavigationMode(NavigationMode.POINTER_ONLY);
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testDeferredInputSourceChangeNotification() {
		uiContainer.setLastInputSource(InputSource.TOUCHSCREEN);
	
		Assert.assertEquals(true, listenerEvents.isEmpty());
		uiContainer.update(1f);
		Assert.assertEquals(true, listenerEvents.contains("inputSourceChanged"));
	}

	@Test
	public void testKeyDownChangeInputSource() {
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.update(1f);

		Assert.assertEquals(InputSource.CONTROLLER, uiContainer.getLastInputSource());
		uiContainer.keyDown(Keys.A);
		uiContainer.update(1f);
		Assert.assertEquals(InputSource.KEYBOARD_MOUSE, uiContainer.getLastInputSource());
	}
	
	@Test
	public void testDeferredControllerTypeChangeNotification() {
		uiContainer.setLastGamePadType(GamePadType.PS4);
	
		Assert.assertEquals(true, listenerEvents.isEmpty());
		uiContainer.update(1f);
		Assert.assertEquals(true, listenerEvents.contains("gamePadTypeChanged"));
	}
	
	@Test
	public void testBlocksKeyUpWhenKeyDownPreviouslyReceived() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(container).hotkey(Keys.LEFT);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(container);
		uiContainer.keyDown(Keys.LEFT);
		Assert.assertEquals(true, uiContainer.keyUp(Keys.LEFT));
	}
	
	@Test
	public void testIgnoresKeyUpWhenNoActiveNavigation() {
		uiContainer.keyDown(Keys.LEFT);
		Assert.assertEquals(false, uiContainer.keyUp(Keys.LEFT));
	}

	@Test
	public void testIgnoresKeyUpWhenKeyDownNotPreviouslyReceived() {
		uiContainer.setActiveNavigation(container);
		Assert.assertEquals(false, uiContainer.keyUp(Keys.LEFT));
	}
	
	@Test
	public void testIgnoresRepeatedKeyUpEvents() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(container).hotkey(Keys.LEFT);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(container);
		uiContainer.keyDown(Keys.LEFT);
		Assert.assertEquals(true, uiContainer.keyUp(Keys.LEFT));
		Assert.assertEquals(false, uiContainer.keyUp(Keys.LEFT));
	}
	
	@Test
	public void testBlocksButtonUpWhenButtonDownPreviouslyReceived() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(container).hotkey(XboxButton.A);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(container);
		uiContainer.buttonDown(null, XboxButton.A);
		Assert.assertEquals(true, uiContainer.buttonUp(null, XboxButton.A));
	}
	
	@Test
	public void testIgnoresButtonUpWhenNoActiveNavigation() {
		uiContainer.buttonDown(null, XboxButton.A);
		Assert.assertEquals(false, uiContainer.buttonUp(null, XboxButton.A));
	}

	@Test
	public void testIgnoresButtonUpWhenButtonDownNotPreviouslyReceived() {
		uiContainer.setActiveNavigation(container);
		Assert.assertEquals(false, uiContainer.buttonUp(null, XboxButton.A));
	}
	
	@Test
	public void testIgnoresRepeatedButtonUpEvents() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(container).hotkey(XboxButton.A);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(container);
		uiContainer.buttonDown(null, XboxButton.A);
		Assert.assertEquals(true, uiContainer.buttonUp(null, XboxButton.A));
		Assert.assertEquals(false, uiContainer.buttonUp(null, XboxButton.A));
	}

	@Test
	public void testKeyDownChangeInputSourceMultipleUiContainers() {
		mockery.checking(new Expectations() {
			{
				oneOf(gameContainer).getWidth();
				will(returnValue(800));
				oneOf(gameContainer).getHeight();
				will(returnValue(600));
			}
		});
		UiContainer otherUiContainer = new UiContainer(gameContainer, assetManager);
		otherUiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.update(1f);
		otherUiContainer.update(1f);

		Assert.assertEquals(InputSource.CONTROLLER, uiContainer.getLastInputSource());
		Assert.assertEquals(InputSource.CONTROLLER, otherUiContainer.getLastInputSource());
		uiContainer.keyDown(Keys.A);
		uiContainer.update(1f);
		otherUiContainer.update(1f);
		Assert.assertEquals(InputSource.KEYBOARD_MOUSE, uiContainer.getLastInputSource());
		Assert.assertEquals(InputSource.KEYBOARD_MOUSE, otherUiContainer.getLastInputSource());
	}

	@Test
	public void testControllerChangeGamepadSourceMultipleUiContainers() {
		mockery.checking(new Expectations() {
			{
				oneOf(gameContainer).getWidth();
				will(returnValue(800));
				oneOf(gameContainer).getHeight();
				will(returnValue(600));
			}
		});
		UiContainer otherUiContainer = new UiContainer(gameContainer, assetManager);
		otherUiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		otherUiContainer.setLastGamePadType(GamePadType.UNKNOWN);
		uiContainer.setLastGamePadType(GamePadType.UNKNOWN);

		uiContainer.update(1f);
		otherUiContainer.update(1f);

		Assert.assertEquals(InputSource.CONTROLLER, uiContainer.getLastInputSource());
		Assert.assertEquals(InputSource.CONTROLLER, otherUiContainer.getLastInputSource());
		Assert.assertEquals(GamePadType.UNKNOWN, uiContainer.getLastGamePadType());
		Assert.assertEquals(GamePadType.UNKNOWN, otherUiContainer.getLastGamePadType());

		uiContainer.setLastGamePadType(GamePadType.XBOX);

		uiContainer.update(1f);
		otherUiContainer.update(1f);

		Assert.assertEquals(InputSource.CONTROLLER, uiContainer.getLastInputSource());
		Assert.assertEquals(InputSource.CONTROLLER, otherUiContainer.getLastInputSource());
		Assert.assertEquals(GamePadType.XBOX, uiContainer.getLastGamePadType());
		Assert.assertEquals(GamePadType.XBOX, otherUiContainer.getLastGamePadType());
	}


	@Override
	public void onScreenSizeChanged(ScreenSize screenSize) {
		listenerEvents.add("onScreenSizeChanged");
	}

	@Override
	public void preUpdate(UiContainer container, float delta) {
		listenerEvents.add("preUpdate");
	}

	@Override
	public void postUpdate(UiContainer container, float delta) {
		listenerEvents.add("postUpdate");
	}

	@Override
	public void preRender(UiContainer container, Graphics g) {
		listenerEvents.add("preRender");
	}

	@Override
	public void postRender(UiContainer container, Graphics g) {
		listenerEvents.add("postRender");
	}

	@Override
	public void inputSourceChanged(UiContainer container, InputSource oldInputSource, InputSource newInputSource) {
		listenerEvents.add("inputSourceChanged");
	}

	@Override
	public void gamePadTypeChanged(UiContainer container, GamePadType oldGamePadType,
	                               GamePadType newGamePadType) {
		listenerEvents.add("gamePadTypeChanged");
	}

	@Override
	public void onElementAction(UiContainer container, UiElement element) {
		listenerEvents.add("onElementAction");
	}
}
