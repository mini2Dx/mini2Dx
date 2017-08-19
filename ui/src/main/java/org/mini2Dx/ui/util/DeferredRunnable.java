/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;

/**
 * Utility class for pooling deferred {@link Runnable} instances
 */
public class DeferredRunnable implements Comparable<DeferredRunnable> {
	private static final Queue<DeferredRunnable> POOL = new ConcurrentLinkedQueue<DeferredRunnable>();

	private Runnable runnable;
	private float timer;
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
				POOL.offer(this);
			}
			return true;
		}
		
		timer -= Gdx.graphics.getDeltaTime();
		if (timer > 0f) {
			return false;
		}
		runnable.run();
		POOL.offer(this);
		completed = true;
		return true;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void cancel() {
		cancelled = true;
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
		DeferredRunnable result = POOL.poll();
		if (result == null) {
			result = new DeferredRunnable();
		}
		result.runnable = runnable;
		result.timer = duration;
		result.cancelled = false;
		result.completed = false;
		return result;
	}

	@Override
	public int compareTo(DeferredRunnable o) {
		return Float.compare(o.timer, timer);
	}
}
