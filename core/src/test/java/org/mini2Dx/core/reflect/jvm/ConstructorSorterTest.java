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
package org.mini2Dx.core.reflect.jvm;

import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.ConstructorSorter;
import org.mini2Dx.core.serialization.dummy.TestComplexConstructorArgObject;

public class ConstructorSorterTest {

	@Test
	public void testSort() {
		final JvmReflection reflection = new JvmReflection();

		final Constructor [] constructors = reflection.getConstructors(TestComplexConstructorArgObject.class);
		ConstructorSorter.sort(constructors);

		Assert.assertEquals(0, constructors[0].getParameterTypes().length);
		Assert.assertEquals(1, constructors[1].getParameterTypes().length);
		Assert.assertEquals(3, constructors[2].getParameterTypes().length);
		Assert.assertEquals(5, constructors[3].getParameterTypes().length);
	}
}
