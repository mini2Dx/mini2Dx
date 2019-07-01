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

import org.mini2Dx.gdx.utils.Array;

/**
 * Checks an array of {@link FileHandleResolver}s and returns the first {@link FileHandle} found
 */
public class FallbackFileHandleResolver implements FileHandleResolver {
	private final Array<FileHandleResolver> resolvers = new Array<FileHandleResolver>();

	public FallbackFileHandleResolver(FileHandleResolver... resolvers) {
		this.resolvers.addAll(resolvers);
	}

	@Override
	public FileHandle resolve(String filePath) {
		for(int i = 0; i < resolvers.size; i++) {
			try {
				final FileHandle result = resolvers.get(i).resolve(filePath);
				if(result != null) {
					return result;
				}
			} catch (Exception e) {}
		}
		return null;
	}
}
