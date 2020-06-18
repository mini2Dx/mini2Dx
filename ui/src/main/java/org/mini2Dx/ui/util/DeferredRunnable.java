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
package org.mini2Dx.ui.util;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.gdx.utils.Queue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for pooling deferred {@link Runnable} instances
 */
public class DeferredRunnable implements Comparable<DeferredRunnable> {
	private static final Queue<DeferredRunnable> POOL = new Queue<DeferredRunnable>();
	private static final AtomicInteger DEFER_ID_ALLOCATOR = new AtomicInteger();

	private Runnable runnable;
	private float timer;
	private int deferId;
	private boolean cancelled = false, completed = false;

	/**
	 * Attempts to run the deferred {@link Runnable}
	 * 
	 * @return True if it was run, false if the timer deferred time hasn't
	 *         elapsed
	 */
	public boolean run() {
		if(cancelled) {
			if(!completed) {
				synchronized (POOL) {
					POOL.addLast(this);
				}
			}
			return true;
		}
		
		timer -= (1f / 60f);
		if (timer > 0f) {
			return false;
		}
		runnable.run();
		synchronized (POOL) {
			POOL.addLast(this);
		}
		completed = true;
		return true;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void cancel() {
		cancelled = true;
	}

	public int getDeferId() {
		return deferId;
	}

	/**
	 * Allocates a new {@link DeferredRunnable} instance from the instance pool
	 * and assigns the duration and {@link Runnable} to it
	 * 
	 * @param runnable The {@link Runnable} to defer
	 * @param duration The amount of time to defer execution by
	 * @return A {@link DeferredRunnable} instance
	 */
	public static DeferredRunnable allocate(Runnable runnable, float duration) {
		DeferredRunnable result = null;
		synchronized (POOL) {
			if(POOL.size > 0) {
				result = POOL.removeFirst();
			}
		}
		if (result == null) {
			result = new DeferredRunnable();
		}
		result.runnable = runnable;
		result.timer = duration;
		result.deferId = DEFER_ID_ALLOCATOR.incrementAndGet();
		result.cancelled = false;
		result.completed = false;
		return result;
	}

	@Override
	public int compareTo(DeferredRunnable o) {
		final int result = Float.compare(o.timer, timer);
		if(result == 0) {
			return Integer.compare(o.deferId, deferId);
		}
		return result;
	}
}
