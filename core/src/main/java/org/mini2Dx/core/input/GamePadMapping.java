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
package org.mini2Dx.core.input;

/**
 * Base interface for {@link GamePad} mappings
 */
public interface GamePadMapping<T extends MappedGamePadListener> extends GamePadListener {

	public void addListener(T listener);

	public void addListener(int index, T listener);

	public void removeListener(int index);

	public void removeListener(T listener);

	public void clearListeners();

	public int getTotalListeners();
}
