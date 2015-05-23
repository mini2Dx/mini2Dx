/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.util;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit tests for {@link OsDetector}
 * @author Thomas Cashman
 */
public class OsDetectorTest {

	@Test
	public void testDetectDesktop() {
		switch(OsDetector.getOs()) {
		case ANDROID:
			Assert.fail("Detected Android OS for desktop-based JVM");
			break;
		case IOS:
			Assert.fail("Detected iOS for desktop-based JVM");
			break;
		case WINDOWS:
		case MAC:
		case UNIX:
			System.out.println("Detected " + OsDetector.getOs());
			break;
		case UNKNOWN:
			Assert.fail("Could not detect OS for desktop-based JVM");
			break;
		}
	}
}
