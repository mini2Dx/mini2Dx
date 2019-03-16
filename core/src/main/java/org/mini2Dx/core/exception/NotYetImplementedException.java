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

/**
 * Thrown when the a mini2Dx function is not implemented on the current platform
 */
public class NotYetImplementedException extends MdxException {
    private static final long serialVersionUID = 3910057641370555308L;

    public NotYetImplementedException() {
        this("This operation is not yet implemented for the current platform. "
                        + "If you would like to contribute an implementation, "
                        + "please send a pull request to the mini2Dx repository at https://github.com/mini2Dx/mini2Dx");
    }
    
    public NotYetImplementedException(String message) {
    	super(message);
    }
}
