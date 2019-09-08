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

import org.mini2Dx.gdx.utils.OrderedSet;

/**
 * Tracks {@link Interpolatable} objects and auto-interpolates them each frame
 */
public class InterpolationTracker {
	public static int INITIAL_SIZE = 512;
	private static OrderedSet<Interpolatable> INTERPOLATABLES = null;

	private static void init() {
		if(INTERPOLATABLES != null) {
			return;
		}
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

		return INTERPOLATABLES.contains(interpolatable);
	}

	public static synchronized void register(Interpolatable interpolatable) {
		init();

		INTERPOLATABLES.add(interpolatable);
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
}
