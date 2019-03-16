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
package org.mini2Dx.core.di.bean;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * An implementation of {@link Bean} based on the
 * <a href="http://en.wikipedia.org/wiki/Prototype_pattern">prototype pattern</a>
 */
public class PrototypeBean extends Bean implements Runnable {
	private static final int MAXIMUM_PREPARED_PROTOTYPES = 3;

	private Object bean;
	private BlockingQueue<Object> prototypes;
	private ExecutorService executorService;

	public PrototypeBean(Object bean, ExecutorService executorService) {
		this.bean = bean;
		this.executorService = executorService;
		prototypes = new ArrayBlockingQueue<Object>(MAXIMUM_PREPARED_PROTOTYPES);
	}

	@Override
	public Object getInstance() {
		Object result = null;
		try {
			result = prototypes.take();
			executorService.submit(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static Object duplicate(Object object)
			throws MdxException {
		return Mdx.di.beanUtils().cloneBean(object);
	}

	@Override
	public void run() {
		try {
			while (prototypes.size() < MAXIMUM_PREPARED_PROTOTYPES) {
				prototypes.offer(PrototypeBean.duplicate(bean));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
