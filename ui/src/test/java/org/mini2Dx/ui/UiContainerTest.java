/**
 * Copyright (c) 2017 See AUTHORS file
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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Modal;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;

import junit.framework.Assert;

/**
 * Unit tests for {@link UiContainer}
 */
public class UiContainerTest {
	private final Mockery mockery = new Mockery();
	
	private GameContainer gameContainer;
	private AssetManager assetManager;
	private AlignedModal modal;
	
	private UiContainer uiContainer;
	
	@Before
	public void setUp() {
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		gameContainer = mockery.mock(GameContainer.class);
		assetManager = mockery.mock(AssetManager.class);
		modal = mockery.mock(AlignedModal.class);
		
		mockery.checking(new Expectations() {
			{
				oneOf(gameContainer).getWidth();
				will(returnValue(800));
				oneOf(gameContainer).getHeight();
				will(returnValue(600));
			}
		});
		
		uiContainer = new UiContainer(gameContainer, assetManager);
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testBlocksKeyUpWhenKeyDownPreviouslyReceived() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(modal).hotkey(Keys.LEFT);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(modal);
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
		uiContainer.setActiveNavigation(modal);
		Assert.assertEquals(false, uiContainer.keyUp(Keys.LEFT));
	}
	
	@Test
	public void testIgnoresRepeatedKeyUpEvents() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(modal).hotkey(Keys.LEFT);
				will(returnValue(null));
			}
		});
		
		uiContainer.setActiveNavigation(modal);
		uiContainer.keyDown(Keys.LEFT);
		Assert.assertEquals(true, uiContainer.keyUp(Keys.LEFT));
		Assert.assertEquals(false, uiContainer.keyUp(Keys.LEFT));
	}
}
