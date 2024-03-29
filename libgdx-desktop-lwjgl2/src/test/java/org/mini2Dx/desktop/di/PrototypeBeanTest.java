/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.desktop.di;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.DependencyInjection;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.TaskExecutor;
import org.mini2Dx.core.di.bean.PrototypeBean;
import org.mini2Dx.core.di.dummy.*;
import org.mini2Dx.core.executor.AsyncFuture;
import org.mini2Dx.core.executor.AsyncResult;
import org.mini2Dx.core.executor.FrameSpreadTask;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.desktop.DesktopComponentScanner;
import org.mini2Dx.libgdx.game.GameWrapper;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

/**
 * Unit tests for {@link PrototypeBean}
 */
public class PrototypeBeanTest {
	private TestPrototypeBean prototype;
	private ExecutorService executorService;
	private PrototypeBean bean;

	@Before
	public void setup() {
		executorService = Executors.newFixedThreadPool(1);

		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
		Mdx.locks = new JvmLocks();
		Mdx.reflect = new JvmReflection();
		Mdx.platform = GameWrapper.getPlatform();
		Mdx.di = new DependencyInjection(new DesktopComponentScanner());
		Mdx.executor = new TaskExecutor() {
			@Override
			public void dispose() {

			}

			@Override
			public TaskExecutor newExecutor(int threads) {
				return this;
			}

			@Override
			public void update(float delta) {

			}

			@Override
			public void execute(Runnable runnable) {
				executorService.submit(runnable);
			}

			@Override
			public AsyncFuture submit(Runnable runnable) {
				executorService.submit(runnable);
				return null;
			}

			@Override
			public <T> AsyncResult<T> submit(Callable<T> callable) {
				executorService.submit(callable);
				return null;
			}

			@Override
			public void submit(FrameSpreadTask task) {

			}

			@Override
			public void setMaxFrameTasksPerFrame(int max) {

			}

			@Override
			public int getTotalQueuedAsyncTasks() {
				return 0;
			}

			@Override
			public int getTotalQueuedFrameSpreadTasks() {
				return 0;
			}
		};


		prototype = new TestPrototypeBean();
		prototype.setIntField(100);

		bean = new PrototypeBean(prototype);
		executorService.submit(bean);
	}

	@After
	public void teardown() {
		executorService.shutdown();
	}

	@Test(timeout = 10000L)
	public void testGetInstance() {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Object result = bean.getInstance();
			Assert.assertEquals(true, result != null);
			Assert.assertEquals(false, result.equals(prototype));
		}
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime));
	}
}
