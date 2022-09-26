/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.math.RandomXS128;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

public class QuadElementsTest {
	private final QuadElements<CollisionPoint> quadElements = new QuadElements<CollisionPoint>();

	@BeforeClass
	public static void beforeClass() {
		Mdx.locks = new JvmLocks();
	}

	@Test
	public void testAdd() {
		final RandomXS128 random = new RandomXS128(12345);
		for(int i = 0; i < 100; i++) {
			final int id = random.nextInt();
			quadElements.add(new CollisionPoint(id, random.nextInt(), random.nextInt()));
			Assert.assertEquals(i, quadElements.objectIndices.get(id, -1));
		}
	}

	@Test
	public void testRemove() {
		final RandomXS128 random = new RandomXS128(12346);

		final CollisionPoint point1 = new CollisionPoint(101, random.nextInt(), random.nextInt());
		final CollisionPoint point2 = new CollisionPoint(102, random.nextInt(), random.nextInt());
		final CollisionPoint point3 = new CollisionPoint(103, random.nextInt(), random.nextInt());
		quadElements.add(point1);
		quadElements.add(point2);
		quadElements.add(point3);

		Assert.assertEquals(0, quadElements.objectIndices.get(point1.getId(), -1));
		Assert.assertEquals(1, quadElements.objectIndices.get(point2.getId(), -1));
		Assert.assertEquals(2, quadElements.objectIndices.get(point3.getId(), -1));

		quadElements.removeValue(point1, false);
		Assert.assertEquals(0, quadElements.objectIndices.get(point3.getId(), -1));
		Assert.assertEquals(1, quadElements.objectIndices.get(point2.getId(), -1));
	}
}
