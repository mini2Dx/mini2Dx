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
import org.mini2Dx.gdx.utils.Queue;

/**
 * An implementation of {@link Bean} based on the
 * <a href="http://en.wikipedia.org/wiki/Prototype_pattern">prototype pattern</a>
 */
public class PrototypeBean extends Bean implements Runnable {
	private static final int MAXIMUM_PREPARED_PROTOTYPES = 3;

	private Object bean;
	private Queue<Object> prototypes;

	public PrototypeBean(Object bean) {
		this.bean = bean;
		prototypes = new Queue<Object>(MAXIMUM_PREPARED_PROTOTYPES);
		Mdx.executor.submit(this);
	}

	@Override
	public Object getInstance() {
		Object result = null;
		try {
			synchronized(prototypes) {
				while(prototypes.size == 0) {
					prototypes.wait();
				}
				result = prototypes.removeFirst();
			}
			Mdx.executor.submit(this);
		} catch (Exception e) {
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
			synchronized(prototypes) {
				while (prototypes.size < MAXIMUM_PREPARED_PROTOTYPES) {
					prototypes.addLast(PrototypeBean.duplicate(bean));
				}
				prototypes.notifyAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
