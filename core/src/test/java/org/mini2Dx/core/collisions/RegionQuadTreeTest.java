/**
 * Copyright (c) 2015 See AUTHORS file
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
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;

import junit.framework.Assert;

/**
 * Unit tests for {@link RegionQuadTree}
 */
public class RegionQuadTreeTest {
	private RegionQuadTree<CollisionBox> rootQuad;
	private CollisionBox box1, box2, box3, box4;
	
	@Before
	public void setup() {
		rootQuad = new RegionQuadTree<CollisionBox>(2, 0, 0, 128, 128);
		
		box1 = new CollisionBox(1, 1, 32, 32);
		box2 = new CollisionBox(95, 1, 32, 32);
		box3 = new CollisionBox(1, 95, 32, 32);
		box4 = new CollisionBox(95, 95, 32, 32);
	}
	
	@Test
	public void testAdd() {
		int totalElements = 100;
		Random random = new Random();
		long startTime = System.nanoTime();
		for(int i = 0; i < totalElements; i++) {
			CollisionBox rect = new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f);
			Assert.assertEquals(true, rootQuad.add(rect));
			Assert.assertEquals(i + 1, rootQuad.getTotalElements());
		}
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements individually to " + RegionQuadTree.class.getSimpleName());
	}
	
	@Test
	public void testAddAll() {
		int totalElements = 100;
		Random random = new Random();
		List<CollisionBox> rects = new ArrayList<CollisionBox>();
		long startTime = System.nanoTime();
		for(int i = 0; i < totalElements; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f));
		}
		rects.add(new CollisionBox(-4f, -4f, 32f, 32f));
		
		rootQuad.addAll(rects);
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements in bulk to " + RegionQuadTree.class.getSimpleName());
		Assert.assertEquals(rects.size(), rootQuad.getTotalElements());
	}
	
	@Test
	public void testRemove() {
		Random random = new Random();
		List<CollisionBox> CollisionBoxs = new ArrayList<CollisionBox>();
		for(int i = 0; i < 1000; i++) {
			CollisionBoxs.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		
		for(int i = 0; i < CollisionBoxs.size(); i++) {
			rootQuad.add(CollisionBoxs.get(i));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
		
		for(int i = CollisionBoxs.size() - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
			rootQuad.remove(CollisionBoxs.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size());
		}
	}
	
	@Test
	public void testRemoveAll() {
		Random random = new Random();
		List<CollisionBox> rects = new ArrayList<CollisionBox>();
		for(int i = 0; i < 100; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		rootQuad.addAll(rects);
		Assert.assertEquals(rects.size(), rootQuad.getTotalElements());
		rootQuad.removeAll(rects);
		Assert.assertEquals(0, rootQuad.getTotalElements());
	}
	
	@Test
	public void testSubdivide() {
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		
		rootQuad.add(box2);
		Assert.assertEquals(2, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		
		rootQuad.add(box3);
		Assert.assertEquals(3, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(box4);
		Assert.assertEquals(4, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new CollisionBox(24, 24, 2, 2));
		Assert.assertEquals(5, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(6, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(7, rootQuad.getElements().size());
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}
	
	@Test
	public void testMerge() {
		rootQuad = new RegionQuadTree<CollisionBox>(4, 3, 0, 0, 128, 128);
		Assert.assertEquals(true, rootQuad.add(box1));
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		Assert.assertEquals(true, rootQuad.add(box2));
		Assert.assertEquals(true, rootQuad.add(box3));
		Assert.assertEquals(true, rootQuad.add(box4));
		Assert.assertEquals(true, rootQuad.add(new CollisionBox(24, 24, 2, 2)));
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		Assert.assertEquals(true, rootQuad.remove(box4));
		Assert.assertEquals(true, rootQuad.remove(box3));
		Assert.assertEquals(true, rootQuad.remove(box2));
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		Assert.assertEquals(2, rootQuad.getTotalElements());
		Assert.assertEquals(true, rootQuad.getElements().contains(box1));
	}
	
	@Test
	public void testGetTotalElements() {
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getTotalElements());
		rootQuad.add(box2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(box3);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.remove(box2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(box4);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.add(box2);
		Assert.assertEquals(4, rootQuad.getTotalElements());
		rootQuad.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(5, rootQuad.getTotalElements());
		rootQuad.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(6, rootQuad.getTotalElements());
	}
	
	@Test
	public void testGetElementsWithinRegion() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);
		
		List<CollisionBox> CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(0, CollisionBoxs.size());
		
		CollisionBox CollisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox CollisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox CollisionBox7 = new CollisionBox(12, 48, 8, 8);
		
		rootQuad.add(CollisionBox5);
		rootQuad.add(CollisionBox6);
		rootQuad.add(CollisionBox7);
		
		CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(0, 0, 128, 128));
		Assert.assertEquals(rootQuad.getElements().size(), CollisionBoxs.size());
		
		CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(36, 36, 32, 32));
		Assert.assertEquals(1, CollisionBoxs.size());
		Assert.assertEquals(CollisionBox6, CollisionBoxs.get(0));
		
		CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(0, 0, 64, 64));
		Assert.assertEquals(4, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox5));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox6));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox7));
		
		CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(16, 16, 24, 24));
		Assert.assertEquals(2, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox5));
		
		CollisionBoxs = rootQuad.getElementsWithinRegion(new CollisionBox(12, 40, 48, 8));
		Assert.assertEquals(2, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox6));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox7));
	}
	
	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);
		
		List<CollisionBox> CollisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(2, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(box4));
		
		CollisionBox CollisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox CollisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox CollisionBox7 = new CollisionBox(12, 48, 8, 8);
		
		rootQuad.add(CollisionBox5);
		rootQuad.add(CollisionBox6);
		rootQuad.add(CollisionBox7);
		
		CollisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(4, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(box4));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox5));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox6));
	}
	
	@Test
	public void testGetElementsContainingPoint() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);
		
		List<CollisionBox> collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 16));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));
		
		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 16));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box2));
		
		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 112));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box3));
		
		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 112));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box4));
	}
}
