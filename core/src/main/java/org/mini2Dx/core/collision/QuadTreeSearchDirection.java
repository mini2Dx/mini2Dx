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
package org.mini2Dx.core.collision;

/**
 * Defines the search direction for a {@link QuadTree} getElements operation.
 */
public enum QuadTreeSearchDirection {
    /**
     * New search method introduced in 2.0, starting form a {@link QuadTree} object it searches for objects inside it
     * and then upwards in the hierarchy.
     */
    UPWARDS,
    /**
     * The default search method
     */
    DOWNWARDS
}
