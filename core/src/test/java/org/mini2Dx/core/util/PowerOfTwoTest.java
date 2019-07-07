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
package org.mini2Dx.core.util;

import junit.framework.Assert;
import org.junit.Test;

public class PowerOfTwoTest {
	@Test
	public void testNextPowerOfTwo() {
		for(int power = 1; power < 16; power++) {
			for(int i = (int) Math.pow(2, power) + 1; i <= (int) Math.pow(2, power); i++) {
				Assert.assertEquals((int) Math.pow(2, power), PowerOfTwo.nextPowerOfTwo(i));
			}
		}
	}

	@Test
	public void testPreviousPowerOfTwo() {
		for(int power = 16; power > 0; power--) {
			for(int i = (int) Math.pow(2, power) - 1; i >= (int) Math.pow(2, power - 1); i--) {
				Assert.assertEquals((int) Math.pow(2, power - 1), PowerOfTwo.previousPowerOfTwo(i));
			}
		}
		Assert.assertEquals(1, PowerOfTwo.previousPowerOfTwo(1));
	}
}
