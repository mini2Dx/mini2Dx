/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.core.controller.deadzone;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Unit tests for {@link NoopDeadZone}
 */
public class NoopDeadZoneTest {
	private final NoopDeadZone noopDeadZone = new NoopDeadZone();
	
	@Test
	public void testInsideDeadZone() {
		noopDeadZone.setDeadZone(0.25f);
		noopDeadZone.updateX(0.1f);
		noopDeadZone.updateY(0.1f);
		Assert.assertEquals(0.1f, noopDeadZone.getX());
		Assert.assertEquals(0.1f, noopDeadZone.getY());
	}
	
	@Test
	public void testXInsideDeadZone() {
		noopDeadZone.setDeadZone(0.25f);
		noopDeadZone.updateX(0.1f);
		noopDeadZone.updateY(0.3f);
		Assert.assertEquals(0.1f, noopDeadZone.getX());
		Assert.assertEquals(0.3f, noopDeadZone.getY());
	}
	
	@Test
	public void testYInsideDeadZone() {
		noopDeadZone.setDeadZone(0.25f);
		noopDeadZone.updateX(0.3f);
		noopDeadZone.updateY(0.1f);
		Assert.assertEquals(0.3f, noopDeadZone.getX());
		Assert.assertEquals(0.1f, noopDeadZone.getY());
	}
	
	@Test
	public void testOutsideDeadZone() {
		noopDeadZone.setDeadZone(0.25f);
		noopDeadZone.updateX(0.3f);
		noopDeadZone.updateY(0.3f);
		Assert.assertEquals(0.3f, noopDeadZone.getX());
		Assert.assertEquals(0.3f, noopDeadZone.getY());
	}
}
