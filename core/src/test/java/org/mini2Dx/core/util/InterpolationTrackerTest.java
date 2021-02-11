/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Logger;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.collision.CollisionCircle;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

public class InterpolationTrackerTest {

	@Before
	public void setUp() {
		Mdx.locks = new JvmLocks();
		Mdx.log = new Logger() {
			@Override
			public void info(String tag, String message) {
				System.out.println(tag + " - " + message);
			}

			@Override
			public void debug(String tag, String message) {
				System.out.println(tag + " - " + message);
			}

			@Override
			public void error(String tag, String message) {
				System.out.println(tag + " - " + message);
			}

			@Override
			public void error(String tag, String message, Exception e) {
				System.out.println(tag + " - " + message);
				e.printStackTrace();
			}

			@Override
			public void setLoglevel(int loglevel) {
			}
		};
		InterpolationTracker.deregisterAll();
	}

	@Test
	public void testRegisterDuplicateId() {
		final CollisionBox collisionBoxA = new CollisionBox(1);
		final CollisionBox collisionBoxB = new CollisionBox(1);
		final CollisionCircle collisionCircle = new CollisionCircle(1,1f);

		Assert.assertEquals(false, InterpolationTracker.register(collisionBoxA));
		Assert.assertEquals(false, InterpolationTracker.register(collisionBoxB));
		Assert.assertEquals(false, InterpolationTracker.register(collisionCircle));

		Assert.assertEquals(2, InterpolationTracker.getTotalObjects());
		Assert.assertEquals(true, InterpolationTracker.isRegistered(collisionBoxA));
		Assert.assertEquals(false, InterpolationTracker.isRegistered(collisionBoxB));
		Assert.assertEquals(true, InterpolationTracker.isRegistered(collisionCircle));
	}
}
