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

import org.mini2Dx.core.util.Lerper;
import org.openjdk.jmh.annotations.*;

/**
 * Performance tests for {@link Lerper}
 */
public class LerperPerformanceTest {
	@State(Scope.Thread)
	public static class TestState {
		public float from = 38603f;
		public float to = 38603f;
		public float alpha = 0.0024753602f;
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("Lerp")
	public void testBoundlessLerp(TestState state) {
		Lerper.lerp0(state.from, state.to, state.alpha);
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("Lerp")
	public void testClampLerp(TestState state) {
		Lerper.lerp1(state.from, state.to, state.alpha);
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("Lerp")
	public void testMononticLerp(TestState state) {
		Lerper.lerp2(state.from, state.to, state.alpha);
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("Lerp")
	public void testPreciseLerp(TestState state) {
		Lerper.lerp3(state.from, state.to, state.alpha);
	}
}
