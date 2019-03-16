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
package org.mini2Dx.core.geom;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * Performance tests for {@link Circle}
 */
public class CirclePerformanceTest {
	@State(Scope.Thread)
    public static class TestState {
		public Circle circle = new Circle(50f);
		
		public Circle intersectingCircle = new Circle(75f, 75f, 50f);
		public Circle nonIntersectingCircle = new Circle(500f, 500f, 50f);
		
		public Rectangle intersectingRectangle = new Rectangle(25f, 25f, 100f, 100f);
		public Rectangle nonIntersectingRectangle = new Rectangle(1000f, 1000f, 50f, 50f);
		
		public LineSegment intersectingLineSegment = new LineSegment(-100f, -100f, 0f, 0f);
		public LineSegment nonIntersectingLineSegment = new LineSegment(1000f, 1000f, 1050f, 1050f);
    }
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testContainsXY(TestState state) {
		state.circle.contains(0f, 15f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testNotContainsXY(TestState state) {
		state.circle.contains(100f, 100f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testIntersectsLineSegment(TestState state) {
		state.circle.intersects(state.intersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testNotIntersectsLineSegment(TestState state) {
		state.circle.intersects(state.nonIntersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testIntersectsCircle(TestState state) {
		state.circle.intersects(state.intersectingCircle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testNotIntersectsCircle(TestState state) {
		state.circle.intersects(state.nonIntersectingCircle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testIntersectsRectangle(TestState state) {
		state.circle.intersects(state.intersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Circle")
	public void testNotIntersectsRectangle(TestState state) {
		state.circle.intersects(state.nonIntersectingRectangle);
	}
}
