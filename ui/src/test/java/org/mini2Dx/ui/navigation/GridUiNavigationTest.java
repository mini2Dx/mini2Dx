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
package org.mini2Dx.ui.navigation;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Button;
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

	@Test
	public void testRemoveAllWithNullValues(){
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				final Actionable actionable = elements[x][y];
				mockery.checking(new Expectations() {
					{
						exactly(9).of(actionable).removeHoverListener(navigation);
					}
				});
			}
		}

		final Actionable mockedActionable = mockery.mock(Actionable.class);

		mockery.checking(new Expectations() {
			{
				oneOf(mockedActionable).addHoverListener(navigation);
				oneOf(mockedActionable).removeHoverListener(navigation);
			}
		});

		navigation.set(2, 3, mockedActionable);

		addElementsToGrid();

		navigation.removeAll();
	}

	@Test
	public void testNavigateWithNullItems(){
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				final Actionable actionable = elements[x][y];
				mockery.checking(new Expectations() {
					{
						exactly(9).of(actionable).removeHoverListener(navigation);
					}
				});
			}
		}

		String elementId = "actionable-" + 2 + "," + 3;
		final Actionable mockedActionable = mockery.mock(Actionable.class, elementId);

		mockery.checking(new Expectations() {
			{
				oneOf(mockedActionable).addHoverListener(navigation);
				oneOf(mockedActionable).invokeEndHover();
				atLeast(1).of(mockedActionable).getId();
				will(returnValue(elementId));
			}
		});

		navigation.set(2, 3, mockedActionable);
		Assert.assertEquals(mockedActionable, navigation.updateCursor(mockedActionable.getId()));
	}

	@Test
	public void testNavigateUpFromNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][1].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[1][0], navigation.navigate(Keys.UP));
	}

	@Test
	public void testNavigateDownFromNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][1].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[1][2], navigation.navigate(Keys.DOWN));
	}

	@Test
	public void testNavigateLeftFromNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][1].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[0][1], navigation.navigate(Keys.LEFT));
	}

	@Test
	public void testNavigateRightFromNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][1].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[2][1], navigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testNavigateUpOverNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][2].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[1][0], navigation.navigate(Keys.UP));
	}

	@Test
	public void testNavigateDownOverNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[1][0].getId());
		navigation.set(1, 1, null);
		Assert.assertEquals(navigationElements[1][2], navigation.navigate(Keys.DOWN));
	}

	@Test
	public void testNavigateLeftOverNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[2][1].getId());

		//set entire middle column to null elements to move over
		for (int i = 0; i < ROWS; i++) {
			navigation.set(1, i, null);
		}

		Assert.assertEquals(navigationElements[0][1], navigation.navigate(Keys.LEFT));
	}

	@Test
	public void testNavigateLeftClosestVerticalElementDownwards(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[2][1].getId());

		navigation.set(1, 0, null);
		navigation.set(1, 1, null);

		Assert.assertEquals(navigationElements[1][2], navigation.navigate(Keys.LEFT));
	}

	@Test
	public void testNavigateLeftClosestVerticalElementUpwards(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[2][1].getId());

		navigation.set(1, 1, null);
		navigation.set(1, 2, null);

		Assert.assertEquals(navigationElements[1][0], navigation.navigate(Keys.LEFT));
	}

	@Test
	public void testNavigateRightOverNullElement(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[0][1].getId());

		for (int i = 0; i < ROWS; i++) {
			navigation.set(1, i, null);
		}
		Assert.assertEquals(navigationElements[2][1], navigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testNavigateRightClosestVerticalElementDownwards(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[0][1].getId());

		navigation.set(1, 0, null);
		navigation.set(1, 1, null);

		Assert.assertEquals(navigationElements[1][2], navigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testNavigateRightClosestVerticalElementUpwards(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[0][1].getId());

		navigation.set(1, 1, null);
		navigation.set(1, 2, null);

		Assert.assertEquals(navigationElements[1][0], navigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testNavigateRightClosestVerticalElementUpwardsUnevenGridSplit(){
		int cols = 2;
		int rows = 3;

		GridUiNavigation gridUiNavigation = new GridUiNavigation(cols);


		Button[][] navigationElements =  new Button[cols][rows];
		for(int x = 0; x < cols; x++) {
			for(int y = 0; y < rows; y++) {
				if (x == 1 && y == 1){
					break;
				}
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				gridUiNavigation.set(x, y, navigationElements[x][y]);
			}
		}

		gridUiNavigation.updateCursor(navigationElements[0][1].getId());

		gridUiNavigation.set(1, 1, null);

		Assert.assertEquals(navigationElements[1][0], gridUiNavigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testNavigateDownNoElementToMoveTo(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[2][0].getId());
		navigation.set(2, 1, null);
		navigation.set(2, 2, null);
		Assert.assertEquals(navigationElements[2][0], navigation.navigate(Keys.DOWN));
	}

	@Test
	public void testNavigateLeftNoElementToMoveTo(){
		Button[][] navigationElements =  new Button[COLUMNS][ROWS];
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigationElements[x][y] = new Button("button-" + x + "," + y);
				navigation.set(x, y, navigationElements[x][y]);}
		}

		navigation.updateCursor(navigationElements[0][0].getId());

		//set both rows to null so there is nothing to move to
		for (int i = 0; i < ROWS; i++) {
			navigation.set(1, i, null);
			navigation.set(2, i, null);
		}

		Assert.assertEquals(navigationElements[0][0], navigation.navigate(Keys.RIGHT));
	}

	@Test
	public void testSetNullElement(){
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				final Actionable actionable = elements[x][y];
				mockery.checking(new Expectations() {
					{
						exactly(9).of(actionable).removeHoverListener(navigation);
					}
				});
			}
		}

		navigation.set(2, 3, null);
		Assert.assertEquals(12, navigation.getNavigationSize());
	}

	private void addElementsToGrid() {
		for(int x = 0; x < COLUMNS; x++) {
			for(int y = 0; y < ROWS; y++) {
				navigation.set(x, y, elements[x][y]);
			}
		}
	}
}
