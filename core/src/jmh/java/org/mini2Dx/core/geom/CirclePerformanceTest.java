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
