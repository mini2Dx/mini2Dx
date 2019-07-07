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
package org.mini2Dx.uats.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.mini2Dx.core.screen.GameScreen;

/**
 * Auto-generate IDs for each {@link GameScreen} in the UAT project
 */
public class ScreenIds {
	private static AtomicInteger counter = new AtomicInteger(2);
	private static Map<String, Integer> screenIds = new ConcurrentHashMap<String, Integer>();
	
	public static int getScreenId(Class<?> clazz) {
		String key = clazz.getName();
		if(!screenIds.containsKey(key)) {
			screenIds.put(key, counter.getAndIncrement());
		}
		return screenIds.get(clazz.getName());
	}
}
