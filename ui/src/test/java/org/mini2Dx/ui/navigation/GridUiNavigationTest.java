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
package org.mini2Dx.ui.navigation;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.layout.ScreenSize;

import com.badlogic.gdx.Input.Keys;

import junit.framework.Assert;

/**
 * Unit tests for {@link GridUiNavigation}
 */
public class GridUiNavigationTest {
	private final int COLUMNS = 3;
	private final int ROWS = 3;
	
	private final Mockery mockery = new Mockery();
	private final GridUiNavigation navigation = new GridUiNavigation(COLUMNS);
	private final Actionable [][] elements = new Actionable[COLUMNS][ROWS];
	
	@Before
	public void setUp() {
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				elements[x][y] = mockery.mock(Actionable.class, "actionable-" + x + "," + y);

				final Actionable actionable = elements[x][y];
				mockery.checking(new Expectations() {
					{
						atLeast(1).of(actionable).addHoverListener(navigation);
						allowing(actionable).invokeEndHover();
					}
				});
			}
		}


	}
	
	@Test
	public void testSetXY() {
		addElementsToGrid();
		
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				Assert.assertEquals(elements[x][y], navigation.get(x, y));
			}
		}
	}
	
	@Test
	public void testNavigate() {
		addElementsToGrid();
		
		Actionable lastActionable = null;
		for(int x = 0; x <= COLUMNS; x++) {
			lastActionable = navigation.navigate(Keys.RIGHT);
		}
		Assert.assertEquals(elements[2][0], lastActionable);
		
		for(int y = 0; y <= ROWS; y++) {
			lastActionable = navigation.navigate(Keys.DOWN);
		}
		Assert.assertEquals(elements[2][2], lastActionable);
		
		for(int x = 0; x <= COLUMNS; x++) {
			lastActionable = navigation.navigate(Keys.LEFT);
		}
		Assert.assertEquals(elements[0][2], lastActionable);
		
		for(int y = 0; y <= ROWS; y++) {
			lastActionable = navigation.navigate(Keys.UP);
		}
		Assert.assertEquals(elements[0][0], lastActionable);
	}
	
	@Test
	public void testCursorReset() {
		addElementsToGrid();
		
		Actionable lastActionable = null;
		for(int x = 0; x <= COLUMNS; x++) {
			lastActionable = navigation.navigate(Keys.RIGHT);
		}
		Assert.assertEquals(elements[2][0], lastActionable);
		
		navigation.layout(ScreenSize.XS);
		Assert.assertEquals(elements[2][0], navigation.getCursor());
	}
	
	private void addElementsToGrid() {
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigation.set(x, y, elements[x][y]);
			}
		}
	}
}
