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
package org.mini2Dx.ui.event;

import org.mini2Dx.gdx.utils.Queue;

/**
 *
 */
public class ActionEventPool {
	private static final Queue<ActionEvent> pool = new Queue<ActionEvent>();
	
	public static ActionEvent allocate() {
		if(pool.size == 0) {
			return new ActionEvent();
		}
		return pool.removeFirst();
	}
	
	public static void release(ActionEvent event) {
		pool.addLast(event);
	}
}
