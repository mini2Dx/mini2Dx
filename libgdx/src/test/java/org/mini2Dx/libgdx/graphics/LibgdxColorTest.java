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
package org.mini2Dx.libgdx.graphics;

import org.junit.Assert;
import org.junit.Test;

public class LibgdxColorTest {

	@Test
	public void testByteValues() {
		final LibgdxColor color = new LibgdxColor(1f, 1f, 1f, 1f);
		final byte r = color.rb();
		final byte g = color.gb();
		final byte b = color.bb();
		final byte a = color.ab();
		color.set(r, g, b, a);
		Assert.assertEquals(1f, color.rf(), 0f);
		Assert.assertEquals(1f, color.gf(), 0f);
		Assert.assertEquals(1f, color.bf(), 0f);
		Assert.assertEquals(1f, color.af(), 0f);
	}
}
