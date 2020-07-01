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
import org.junit.Test;

public class LerperTest {

	@Test
	public void testLerpBoundless() {
		testLerper(Lerper.Mode.BOUNDLESS);
	}

	@Test
	public void testLerpClamp() {
		testLerper(Lerper.Mode.CLAMP);
	}

	@Test
	public void testLerpMonotonic() {
		testLerper(Lerper.Mode.MONOTONIC);
	}

	@Test
	public void testLerpPrecise() {
		testLerper(Lerper.Mode.PRECISE);
	}

	private void testLerper(Lerper.Mode mode) {
		Lerper.MODE = mode;
		final float [] alphas = new float[] {
				0.0024753602f,
				0.045277353f,
				0.122890204f,
				0.13843498f,
				0.00279573f,
				0.019303411f,
				0.00310215f,
				0.0536541f,
				0.028448012f,
				0.106877886f,
				0.0059668203f,
				0.0076277703f,
				0.05966379f,
				0.939256934f,
				0.999423456f
		};
		for(int i = 0; i < alphas.length; i++) {
			switch(mode) {
			case BOUNDLESS:
				Assert.assertEquals(38603f, Lerper.lerp(38603f, 38603f, alphas[i]), 0.01f);
				break;
			default:
				Assert.assertEquals(38603f, Lerper.lerp(38603f, 38603f, alphas[i]), 0f);
				break;
			}
		}
	}
}
