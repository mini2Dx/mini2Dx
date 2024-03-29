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
package org.mini2Dx.libgdx;

import org.mini2Dx.core.TaskExecutor;
import org.mini2Dx.core.collections.concurrent.ConcurrentArray;
import org.mini2Dx.core.executor.AsyncFuture;
import org.mini2Dx.core.executor.AsyncResult;
import org.mini2Dx.core.executor.FrameSpreadTask;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.libgdx.executor.LibgdxAsyncResult;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LibgdxTaskExecutor implements TaskExecutor {
	private static final String LOGGING_TAG = LibgdxTaskExecutor.class.getSimpleName();
	private static final int DEFAULT_MAX_FRAME_TASKS_PER_FRAME = 32;

	private final ExecutorService executorService;
	private final AtomicInteger threadIdGenerator = new AtomicInteger(0);
	private final ConcurrentArray<FrameSpreadTask> spreadTasks = new ConcurrentArray<FrameSpreadTask>(false, 16);

	private int maxFrameTasksPerFrame;

	public LibgdxTaskExecutor(int threads) {
		executorService = Executors.newFixedThreadPool(threads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread result = new Thread(r);
				result.setName(LOGGING_TAG + "-" + threadIdGenerator.getAndIncrement());
				return result;
			}
		});
		maxFrameTasksPerFrame = DEFAULT_MAX_FRAME_TASKS_PER_FRAME;
	}

	@Override
	public TaskExecutor newExecutor(int threads) {
		return new LibgdxTaskExecutor(threads);
	}

	@Override
	public void update(float delta) {
		int taskCount = 0;
		for(int i = 0; i < spreadTasks.size(); i++) {
			if(spreadTasks.get(i).updateTask()) {
				spreadTasks.removeIndex(i);
				i--;
			}
			taskCount++;

			if(taskCount >= maxFrameTasksPerFrame) {
				break;
			}
		}
	}

	@Override
	public void execute(Runnable runnable) {
		executorService.execute(runnable);
	}

	@Override
	public AsyncFuture submit(Runnable runnable) {
		final Future future = executorService.submit(runnable);
		return new AsyncFuture() {
			@Override
			public boolean isFinished() {
				return future.isDone() || future.isCancelled();
			}
		};
	}

	@Override
	public <T> AsyncResult<T> submit(Callable<T> callable) {
		return new LibgdxAsyncResult<T>(executorService.submit(callable));
	}

	@Override
	public void submit(FrameSpreadTask task) {
		spreadTasks.add(task);
	}

	@Override
	public void setMaxFrameTasksPerFrame(int max) {
		this.maxFrameTasksPerFrame = max;
	}

	@Override
	public int getTotalQueuedAsyncTasks() {
		if(executorService instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executorService).getQueue().size();
		}
		if(executorService instanceof ForkJoinPool) {
			return ((ForkJoinPool) executorService).getQueuedSubmissionCount();
		}
		if(executorService instanceof ScheduledThreadPoolExecutor) {
			return ((ScheduledThreadPoolExecutor) executorService).getQueue().size();
		}
		return 0;
	}

	@Override
	public int getTotalQueuedFrameSpreadTasks() {
		return spreadTasks.size();
	}

	@Override
	public void dispose() {
		executorService.shutdown();
	}
}
