/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.util;

import org.junit.Assert;
import org.junit.Test;

public class FastXYSorterTest {
	private final int WIDTH = 10;
	private final int HEIGHT = 12;
	private final int EXPECTED_ELEMENTS_PER_CELL = 4;

	@Test
	public void testNegativeOutOfBounds() {
		final String expected = "Hello";

		final FastXYSorter<String> sorter = new FastXYSorter<>(WIDTH, HEIGHT, EXPECTED_ELEMENTS_PER_CELL);
		sorter.add(-1, -1, expected);

		sorter.begin();
		Assert.assertEquals(expected, sorter.poll());

		sorter.add(0, 0, expected);

		sorter.begin();
		Assert.assertEquals(expected, sorter.poll());
	}

	@Test
	public void testPositiveOutOfBounds() {
		final String expected = "Hello";

		final FastXYSorter<String> sorter = new FastXYSorter<>(WIDTH, HEIGHT, EXPECTED_ELEMENTS_PER_CELL);
		sorter.add(WIDTH, HEIGHT, expected);

		sorter.begin();
		Assert.assertEquals(expected, sorter.poll());

		sorter.add(WIDTH + 1, HEIGHT + 1, expected);

		sorter.begin();
		Assert.assertEquals(expected, sorter.poll());

		sorter.add(WIDTH - 1, HEIGHT - 1, expected);

		sorter.begin();
		Assert.assertEquals(expected, sorter.poll());
	}
}
