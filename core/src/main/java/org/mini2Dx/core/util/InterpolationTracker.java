/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.utils.ObjectIntMap;
import org.mini2Dx.gdx.utils.OrderedSet;

/**
 * Tracks {@link Interpolatable} objects and auto-interpolates them each frame
 */
public class InterpolationTracker {
	private static final String LOGGING_TAG = InterpolationTracker.class.getSimpleName();

	public static int INITIAL_SIZE = 512;
	private static OrderedSet<Interpolatable> INTERPOLATABLES = null;
	private static ObjectIntMap<String> CLASS_COUNT = null;

	private static void init() {
		if(INTERPOLATABLES != null) {
			return;
		}
		CLASS_COUNT = new ObjectIntMap<String>();
		INTERPOLATABLES = new OrderedSet<Interpolatable>(INITIAL_SIZE);
	}

	public static void preUpdate() {
		init();

		for(Interpolatable interpolatable : INTERPOLATABLES.orderedItems()) {
			interpolatable.preUpdate();
		}
	}

	public static void interpolate(float alpha) {
		init();

		for(Interpolatable interpolatable : INTERPOLATABLES.orderedItems()) {
			interpolatable.interpolate(alpha);
		}
	}

	public static synchronized boolean isRegistered(Interpolatable interpolatable) {
		init();

		if(INTERPOLATABLES.contains(interpolatable)) {
			final Interpolatable existingKey = INTERPOLATABLES.get(interpolatable);
			return interpolatable == existingKey;
		}
		return false;
	}

	public static synchronized boolean register(Interpolatable interpolatable) {
		init();

		final int size = INTERPOLATABLES.size;
		final Interpolatable existingKey = INTERPOLATABLES.get(interpolatable);

		if(!INTERPOLATABLES.add(interpolatable)) {
			if(Mdx.log != null) {
				Mdx.log.info(LOGGING_TAG, "WARN: " + interpolatable.toString() +
						" was not registered for interpolation. This may be due to duplicate collision IDs.");
			}
			return false;
		} else if(existingKey != interpolatable && size == INTERPOLATABLES.size) {
			if(Mdx.log != null) {
				Mdx.log.info(LOGGING_TAG, "WARN: " + interpolatable.toString() +
						" replaced an existing object with same ID. The previous object may not be interpolated correctly.");
			}
		}
		return true;
	}

	public static synchronized void deregister(Interpolatable interpolatable) {
		init();

		INTERPOLATABLES.remove(interpolatable);
	}

	public static synchronized void deregisterAll() {
		init();
		
		INTERPOLATABLES.clear();
	}

	public static synchronized int getTotalObjects() {
		init();

		return INTERPOLATABLES.size;
	}

	public static synchronized String toDebugString() {
		init();

		CLASS_COUNT.clear();
		for(Interpolatable interpolatable : INTERPOLATABLES.orderedItems()) {
			final String key = interpolatable.getClass().getSimpleName();
			CLASS_COUNT.getAndIncrement(key, 0, 1);
		}

		final StringBuilder result = new StringBuilder();
		result.append(InterpolationTracker.class.getSimpleName() + " {\n");
		result.append("size: ");
		result.append(getTotalObjects());
		result.append(", \nclasses: {\n");
		for(String key : CLASS_COUNT.keys()) {
			result.append(key);
			result.append(':');
			result.append(' ');
			result.append(CLASS_COUNT.get(key, 0));
			result.append('\n');
		}
		result.append("}\n}");
		return result.toString();
	}
}
