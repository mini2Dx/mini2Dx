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

/**
 * An asynchronous result
 * @param <T> The result type
 */
public interface AsyncResult<T> {

	/**
	 * Returns if the asynchronous task has finished executing and a result is available
	 * @return False if the task is still queued or executing
	 */
	public boolean isFinished();

	/**
	 * Returns the result object
	 * @return Null if no result is available
	 */
	public T getResult();
}
