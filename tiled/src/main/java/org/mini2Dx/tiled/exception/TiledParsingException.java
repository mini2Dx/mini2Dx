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
package org.mini2Dx.tiled.exception;

import org.mini2Dx.tiled.TiledMap;

import java.io.IOException;

/**
 * Wraps an {@link IOException} thrown when parsing a {@link TiledMap}
 */
public class TiledParsingException extends TiledException {
	private static final long serialVersionUID = 1862285613748397369L;

	public TiledParsingException(IOException e) {
		super("Unable to parsed Tiled map: " + e.getMessage());
	}
}
