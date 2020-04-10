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
package org.mini2Dx.core;

/**
 * Interface for game/application logging
 */
public interface Logger {
    int LOG_NONE = 0;
    int LOG_DEBUG = 3;
    int LOG_INFO = 2;
    int LOG_ERROR = 1;

    void info(String tag, String message);

    void debug(String tag, String message);

    void error(String tag, String message);

    void error(String tag, String message, Exception e);

    void setLoglevel(int loglevel);
}
