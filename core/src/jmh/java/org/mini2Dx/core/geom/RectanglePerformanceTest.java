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
 * Performance tests for {@link Rectangle}
 */
public class RectanglePerformanceTest {
	@State(Scope.Thread)
    public static class TestState {
		public Rectangle rectangle = new Rectangle(0f, 0f, 50f, 50f);
		public Rectangle intersectingRectangle = new Rectangle(25f, 25f, 50f, 50f);
		public Rectangle nonIntersectingRectangle = new Rectangle(100f, 100f, 50f, 50f);

		public Rectangle containingRectangle = new Rectangle(25f, 25f, 10f, 10f);
		public Rectangle nonContainingRectangle = new Rectangle(100f, 100f, 50f, 50f);
		
		public LineSegment intersectingLineSegment = new LineSegment(-25f, -25f, 100f, 100f);
		public LineSegment nonIntersectingLineSegment = new LineSegment(-200f, -25f, -300f, 100f);
		
		public Triangle intersectingTriangle = new Triangle(25f, 0f, 50f, 25f, -50f, 25f);
		public Triangle nonIntersectingTriangle = new Triangle(-250f, 0f, 200f, 25f, -300f, 25f);
		
		public Circle intersectingCircle = new Circle(75f, 75f, 50f);
		public Circle nonIntersectingCircle = new Circle(1000f, 1000f, 50f);
    }
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testContainsXY(TestState state) {
		state.rectangle.contains(25f, 25f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotContainsXY(TestState state) {
		state.rectangle.contains(100f, 100f);
	}

	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testContainsRectangle(TestState state) {
		state.rectangle.contains(state.containingRectangle);
	}

	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotContainsRectangle(TestState state) {
		state.rectangle.contains(state.nonContainingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_Rotate")
	public void testRotate(TestState state) {
		state.rectangle.rotate(45f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateContains(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.contains(0f, 25f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testNotRotateContains(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.contains(100f, 100f);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testIntersectsRectangle(TestState state) {
		state.rectangle.intersects(state.intersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotIntersectsRectangle(TestState state) {
		state.rectangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateIntersectsRectangle(TestState state) {
		state.rectangle.setRotation(45f);
		state.intersectingRectangle.setRotation(90f);
		state.rectangle.intersects(state.intersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateNotIntersectsRectangle(TestState state) {
		state.rectangle.setRotation(45f);
		state.nonIntersectingRectangle.setRotation(90f);
		state.rectangle.intersects(state.nonIntersectingRectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testIntersectsLineSegment(TestState state) {
		state.rectangle.intersects(state.intersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotIntersectsLineSegment(TestState state) {
		state.rectangle.intersects(state.nonIntersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateIntersectsLineSegment(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.intersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateNotIntersectsLineSegment(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.nonIntersectingLineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testIntersectsTriangle(TestState state) {
		state.rectangle.intersects(state.intersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotIntersectsTriangle(TestState state) {
		state.rectangle.intersects(state.nonIntersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateIntersectsTriangle(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.intersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateNotIntersectsTriangle(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.nonIntersectingTriangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testIntersectsCircle(TestState state) {
		state.rectangle.intersects(state.intersectingCircle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle")
	public void testNotIntersectsCircle(TestState state) {
		state.rectangle.intersects(state.nonIntersectingCircle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateIntersectsCircle(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.intersectingCircle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("Rectangle_SetRotation")
	public void testRotateNotIntersectsCircle(TestState state) {
		state.rectangle.setRotation(45f);
		state.rectangle.intersects(state.nonIntersectingCircle);
	}
}
