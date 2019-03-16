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
package org.mini2Dx.core.files;

public enum FileType {
	/**
	 * Desktop: Classpath<br>
	 * Android: Asset directory<br>
	 * iOS:<br>
	 * Console: ROM resource<br>
	 */
	INTERNAL,
	/**
	 * Desktop: Anywhere on filesystem<br>
	 * Android: Asset directory<br>
	 * iOS:<br>
	 * Console: Player storage<br>
	 */
	EXTERNAL,
	/**
	 * Desktop: Folder relative to executing JAR<br>
	 * Android: Private app files directory<br>
	 * iOS:<br>
	 * Console: Game data directory<br>
	 */
	LOCAL
}
