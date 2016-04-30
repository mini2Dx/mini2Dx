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
package org.mini2Dx.core.collisions;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

import com.badlogic.gdx.math.MathUtils;

/**
 * Performance tests for {@link RegionQuadTree}
 */
@Threads(value=1)
public class RegionQuadTreePerformanceTest {
	private static final int ELEMENTS_PER_QUAD = 4;
	
	@State(Scope.Thread)
	public static class TestState {
		public RegionQuadTree<CollisionBox> emptyQuadTree = new RegionQuadTree<CollisionBox>(ELEMENTS_PER_QUAD, 0f, 0f, 100f, 100f);
		public RegionQuadTree<CollisionBox> basicQuadTree = new RegionQuadTree<CollisionBox>(ELEMENTS_PER_QUAD, 0f, 0f, 100f, 100f);
		public RegionQuadTree<CollisionBox> complexQuadTree = new RegionQuadTree<CollisionBox>(ELEMENTS_PER_QUAD, 0f, 0f, 100f, 100f);
		
		public LineSegment lineSegment = new LineSegment(25f, 25f, 75f, 75f);
		public Rectangle rectangle = new Rectangle(25f, 25f, 50f, 50f);
		
		public List<CollisionBox> basicCollisions = new ArrayList<CollisionBox>();
		public List<CollisionBox> complexCollisions = new ArrayList<CollisionBox>();
		
		{
			basicCollisions.add(new CollisionBox(0f, 0f, 20f, 20f));
			basicCollisions.add(new CollisionBox(80f, 80f, 20f, 20f));
			basicCollisions.add(new CollisionBox(80f, 0f, 20f, 20f));
			basicCollisions.add(new CollisionBox(0f, 80f, 20f, 20f));
			basicCollisions.add(new CollisionBox(40f, 40f, 20f, 20f));
			
			//Create a semi-random complex quad tree
			//CENTER
			for(int i = 0; i < (ELEMENTS_PER_QUAD * 2) + 1; i++) {
				complexCollisions.add(new CollisionBox(MathUtils.random(30f, 60f), MathUtils.random(30f, 60f), 20f, 20f));
			}
			//TOP-LEFT
			for(int i = 0; i < (ELEMENTS_PER_QUAD * 2) + 1; i++) {
				complexCollisions.add(new CollisionBox(MathUtils.random(0f, 30f), MathUtils.random(0f, 30f), 20f, 20f));
			}
			//TOP-RIGHT
			for(int i = 0; i < (ELEMENTS_PER_QUAD * 2) + 1; i++) {
				complexCollisions.add(new CollisionBox(MathUtils.random(60f, 80f), MathUtils.random(0f, 30f), 20f, 20f));
			}
			//BOTTOM-LEFT
			for(int i = 0; i < (ELEMENTS_PER_QUAD * 2) + 1; i++) {
				complexCollisions.add(new CollisionBox(MathUtils.random(0f, 30f), MathUtils.random(60f, 80f), 20f, 20f));
			}
			//BOTTOM-RIGHT
			for(int i = 0; i < (ELEMENTS_PER_QUAD * 2) + 1; i++) {
				complexCollisions.add(new CollisionBox(MathUtils.random(60f, 80f), MathUtils.random(60f, 80f), 20f, 20f));
			}
		}
		
		@Setup(Level.Iteration)
		public void setUp() {			
			basicQuadTree.addAll(basicCollisions);
			complexQuadTree.addAll(complexCollisions);
		}
		
		@TearDown(Level.Iteration)
		public void cleanup() {
			emptyQuadTree.clear();
			basicQuadTree.clear();
			complexQuadTree.clear();
		}
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Empty_Add")
	public void testAddToEmptyRegionQuadTree(TestState state) {
		state.emptyQuadTree.add(new CollisionBox(10f, 10f, 25f, 25f));
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Basic_Add")
	public void testAddToBasicRegionQuadTree(TestState state) {
		state.basicQuadTree.add(new CollisionBox(10f, 10f, 25f, 25f));
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Complex_Add")
	public void testAddToComplexRegionQuadTree(TestState state) {
		state.complexQuadTree.add(new CollisionBox(10f, 10f, 25f, 25f));
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Empty")
	public void testEmptyRegionQuadTreeIntersectingLineSegment(TestState state) {
		state.emptyQuadTree.getElementsIntersectingLineSegment(state.lineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Basic")
	public void testBasicRegionQuadTreeIntersectingLineSegment(TestState state) {
		state.basicQuadTree.getElementsIntersectingLineSegment(state.lineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Complex")
	public void testComplexRegionQuadTreeIntersectingLineSegment(TestState state) {
		state.complexQuadTree.getElementsIntersectingLineSegment(state.lineSegment);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Empty")
	public void testEmptyRegionQuadTreeIntersectingRectangle(TestState state) {
		state.emptyQuadTree.getElementsWithinArea(state.rectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Basic")
	public void testBasicRegionQuadTreeIntersectingRectangle(TestState state) {
		state.basicQuadTree.getElementsWithinArea(state.rectangle);
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("RegionQuadTree_Complex")
	public void testComplexRegionQuadTreeIntersectingRectangle(TestState state) {
		state.complexQuadTree.getElementsWithinArea(state.rectangle);
	}
}
