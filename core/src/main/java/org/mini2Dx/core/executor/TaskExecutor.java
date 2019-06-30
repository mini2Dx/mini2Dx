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
package org.mini2Dx.core.executor;

import org.mini2Dx.gdx.utils.Disposable;

import java.util.concurrent.Callable;

/**
 * Interface for platform-specific threading implementations
 */
public interface TaskExecutor extends Disposable {

	/**
	 * Updates {@link FrameTask} instances each frame.
	 * Note: This is called by mini2Dx and should not be called manually.
	 * @param delta The game delta time
	 */
	public void update(float delta);

	/**
	 * Queues a {@link Runnable} instance to be run on a separate thread
	 * @param runnable The task to run on a separate thread
	 */
	public void execute(Runnable runnable);

	/**
	 * Submits a {@link Callable} instance to be executed on a separate thread
	 * @param callable The task to run on a separate thread
	 * @param <T> The return type
	 * @return A {@link AsyncResult} instance that can be checked for task completion
	 */
	public <T> AsyncResult<T> submit(Callable<T> callable);

	/**
	 * Submits a {@link FrameTask} to be completed over several frames
	 * @param task The {@link FrameTask} to execute
	 */
	public void submit(FrameTask task);

	/**
	 * Sets the maximum number of {@link FrameTask} instances to update per frame
	 * @param max The maximum number greater than 0
	 */
	public void setMaxFrameTasksPerFrame(int max);
}
