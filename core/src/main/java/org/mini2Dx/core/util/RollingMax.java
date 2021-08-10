/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.util;

import org.mini2Dx.core.collections.LongQueue;

public class RollingMax {
	private final LongQueue queue = new LongQueue();
	private int size;
	private double max;

	public RollingMax(int size) {
		super();
		this.size = size;
	}

	public void mark(long value) {
		if (queue.size >= size) {
			queue.removeFirst();

		}
		queue.addLast(value);

		long max = Integer.MIN_VALUE;
		for (int i = 0; i < queue.size; i++) {
			if(queue.get(i) > max) {
				max = queue.get(i);
			}
		}
		this.max = max;
	}

	public double getMax() {
		return max;
	}
}
