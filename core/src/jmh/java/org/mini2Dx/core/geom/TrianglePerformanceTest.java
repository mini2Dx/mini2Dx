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
