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
package org.mini2Dx.core.collision;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.geom.PositionChangeListener;
import org.mini2Dx.core.geom.SizeChangeListener;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Unit tests for {@link CollisionPolygon}
 */
public class CollisionPolygonTest implements PositionChangeListener<CollisionPolygon>, SizeChangeListener<CollisionPolygon> {
	private CollisionPolygon polygon1, polygon2;
	private int positionNotificationReceived, sizeNotificationReceived;
	
	@Before
	public void setup() {
		positionNotificationReceived = 0;
		sizeNotificationReceived = 0;
		
		polygon1 = new CollisionPolygon(new float [] { 0f, 0f,
				10f, 0f,
				10f, 10f,
				0f, 10f
				});
	}
	
	@Test
	public void testSet() {
		polygon1.addPostionChangeListener(this);
		polygon1.setXY(10f, 10f);
		Assert.assertEquals(1, positionNotificationReceived);
	}
	
	@Test
	public void testAddPoint() {
		polygon1.addPostionChangeListener(this);
		polygon1.addPoint(new Vector2(-5f, 5f));
		Assert.assertEquals(1, positionNotificationReceived);
	}

	@Override
	public void positionChanged(CollisionPolygon moved) {
		positionNotificationReceived++;
	}
	
	@Override
	public void sizeChanged(CollisionPolygon changed) {
		sizeNotificationReceived++;
	}
}