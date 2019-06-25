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
package org.mini2Dx.core.assets;

public class ReferenceCountedObject {
	private final Object object;

	private int referenceCount;

	public ReferenceCountedObject(Object object) {
		super();
		if(object == null) {
			throw new IllegalArgumentException("Object cannot be null");
		}
		this.object = object;
	}

	public void incrementCount() {
		referenceCount++;
	}

	public void decrementCount() {
		referenceCount--;
	}

	public int getReferenceCount() {
		return referenceCount;
	}

	public void setReferenceCount(int referenceCount) {
		this.referenceCount = referenceCount;
	}

	public <T> T getObject(Class<T> type) {
		return (T) object;
	}
}
