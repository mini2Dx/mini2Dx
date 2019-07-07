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
package org.mini2Dx.core.graphics.pipeline;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;

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
