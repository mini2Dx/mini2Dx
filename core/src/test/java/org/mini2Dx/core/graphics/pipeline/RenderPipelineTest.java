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
package org.mini2Dx.core.graphics.pipeline;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;

/**
 * Unit tests for {@link RenderPipeline}
 */
public class RenderPipelineTest {
	private Mockery mockery;
	private RenderPipeline pipeline;
	private RenderOperation operation1, operation2;
	private GameContainer gc;
	private Graphics g;
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		operation1 = mockery.mock(RenderOperation.class, "operation1");
		operation2 = mockery.mock(RenderOperation.class, "operation2");
		gc = mockery.mock(GameContainer.class);
		g = mockery.mock(Graphics.class);
		
		pipeline = new RenderPipeline();
		pipeline.add(operation1);
		pipeline.add(operation2);
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testPipelineUpdate() {
		final float delta = 1.0f;
		
		mockery.checking(new Expectations() {
			{
				oneOf(operation1).update(gc, delta);
				oneOf(operation2).update(gc, delta);
			}
		});
		
		pipeline.update(gc, delta);
	}
	
	@Test
	public void testPipelineInterpolate() {
		final float alpha = 1.0f;
		
		mockery.checking(new Expectations() {
			{
				oneOf(operation1).interpolate(gc, alpha);
				oneOf(operation2).interpolate(gc, alpha);
			}
		});
		
		pipeline.interpolate(gc, alpha);
	}
	
	@Test
	public void testDefaultPipeline() {
		final Sequence invokeSequence = mockery.sequence("sequence-name");
		
		mockery.checking(new Expectations() {
			{
				oneOf(operation1).apply(gc, g);
				inSequence(invokeSequence);
				oneOf(operation2).apply(gc, g);
				inSequence(invokeSequence);
				oneOf(operation2).unapply(gc, g);
				inSequence(invokeSequence);
				oneOf(operation1).unapply(gc, g);
				inSequence(invokeSequence);
			}
		});
		
		pipeline.render(gc, g);
	}
	
	@Test
	public void testOneWayPipeline() {
		pipeline.setOneWay(true);
		
		mockery.checking(new Expectations() {
			{
				oneOf(operation1).apply(gc, g);
				oneOf(operation2).apply(gc, g);
				never(operation2).unapply(gc, g);
				never(operation1).unapply(gc, g);
			}
		});
		
		pipeline.render(gc, g);
	}
}
