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
package org.mini2Dx.core.collision;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates IDs for collision objects (e.g. {@link CollisionBox}, {@link CollisionPoint}, etc.)
 */
public class CollisionIdSequence {
	private static final AtomicInteger ID_SEQUENCE = new AtomicInteger();

	public static int nextId() {
		return ID_SEQUENCE.incrementAndGet();
	}

	public static int offset(int value) {
		ID_SEQUENCE.set(value);
		return value;
	}
}
