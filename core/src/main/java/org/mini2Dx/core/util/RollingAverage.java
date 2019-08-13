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
package org.mini2Dx.core.util;

import org.mini2Dx.core.collections.LongQueue;

/**
 * Utility class for tracking a rolling average
 */
public class RollingAverage {
	private final LongQueue queue = new LongQueue();
	private int size;
	private double average;

	public RollingAverage(int size) {
		super();
		this.size = size;
	}

	public void mark(long value) {
		if (queue.size < size) {
			queue.addLast(value);
			long sum = 0;
			for (int i = 0; i < queue.size; i++) {
				sum += queue.get(i);
			}
			average = (double) sum / queue.size;
		} else {
			long head = queue.removeFirst();
			double minus = (double) head / size;
			queue.addLast(value);
			double add = (double) value / size;
			average = average + add - minus;
		}
	}

	public double getAverage() {
		return average;
	}
}
