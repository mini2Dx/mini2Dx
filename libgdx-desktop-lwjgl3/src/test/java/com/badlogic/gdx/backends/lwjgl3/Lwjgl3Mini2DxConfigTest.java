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
package com.badlogic.gdx.backends.lwjgl3;

import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.gdx.math.MathUtils;

public class Lwjgl3Mini2DxConfigTest {

	@Test
	public void testTargetTimestep() {
		final Lwjgl3Mini2DxConfig config = new Lwjgl3Mini2DxConfig("com.test");
		config.targetFPS = 60;

		Assert.assertEquals(16666666L, config.targetTimestepNanos());
		Assert.assertEquals(1f / 60f, config.targetTimestepSeconds(), MathUtils.FLOAT_ROUNDING_ERROR);
	}
}
