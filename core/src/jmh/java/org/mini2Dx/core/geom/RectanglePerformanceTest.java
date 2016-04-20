/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
