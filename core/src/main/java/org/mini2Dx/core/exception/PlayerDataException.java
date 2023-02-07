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
package org.mini2Dx.core.exception;

import org.mini2Dx.core.PlayerData;

/**
 * Thrown when an error occurs during {@link PlayerData} operations
 */
public class PlayerDataException extends RuntimeException {
    private static final long serialVersionUID = 1489776657559713023L;

    public PlayerDataException(String message) {
        super(message);
    }

    public PlayerDataException(Exception exception) {
        super(exception.getMessage(), exception);
    }
}
