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
 * Performance tests for {@link Triangle}
 */
public class TrianglePerformanceTest {
	@State(Scope.Thread)
    public static class TestState {
		public Triangle triangle = new Triangle(0f, 0f, 25f, 25f, -25f, 25f);
		
		public Rectangle intersectingRectangle = new Rectangle(-20f, 10f, 50f, 50f);
		public Rectangle nonIntersectingRectangle = new Rectangle(100f, 100f, 50f, 50f);
		
		public LineSegment intersectingLineSegment = new LineSegment(-15f, 0f, 20f, 40f);
		public LineSegment nonIntersectingLineSegment = new LineSegment(100f, 100f, 200f, 200f);
		
		public Triangle intersectingTriangle = new Triangle(0f, -10f, 25f, 15f, -25f, 15f);
		public Triangle nonIntersectingTriangle = new Triangle(100f, 100f, 200f, 200f, 0f, 200f);
    }

	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testContainsXY(TestState state) {
		state.triangle.contains(0f, 15f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testNotContainsXY(TestState state) {
		state.triangle.contains(100f, 100f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_Rotate")
	public void testRotate(TestState state) {
		state.triangle.rotate(45f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateContains(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.contains(-5f, 15f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testNotRotateContains(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.contains(100f, 100f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testIntersectsRectangle(TestState state) {
		state.triangle.intersects(state.intersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testNotIntersectsRectangle(TestState state) {
		state.triangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateIntersectsRectangle(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.intersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateNotIntersectsRectangle(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testIntersectsLineSegment(TestState state) {
		state.triangle.intersects(state.intersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testNotIntersectsLineSegment(TestState state) {
		state.triangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateIntersectsLineSegment(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.intersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateNotIntersectsLineSegment(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testIntersectsTriangle(TestState state) {
		state.triangle.intersects(state.intersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle")
	public void testNotIntersectsTriangle(TestState state) {
		state.triangle.intersects(state.nonIntersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateIntersectsTriangle(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.intersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Triangle_SetRotation")
	public void testRotateNotIntersectsTriangle(TestState state) {
		state.triangle.setRotation(45f);
		state.triangle.intersects(state.nonIntersectingTriangle);
	}
}
