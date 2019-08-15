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
 * A common interface for objects that are aware of quad trees and as such can use {@link QuadTreeSearchDirection#UPWARDS} search.
 */
public interface QuadTreeAware {

    /**
     * Sets the {@link QuadTree} where this object is located. Automatically called by {@link QuadTree#add} methods.
     *
     * NOTE: Internal use only.
     *
     * @param quadTree The QuadTree where this object is located.
     */
    public void setQuad(QuadTree quadTree);

    /**
     * Returns the {@link QuadTree} where this object is located. You can call this method in order to get the QuadTree on which you can do {@link QuadTreeSearchDirection#UPWARDS} search.
     * @return QuadTree where this object is located.
     */
    public QuadTree getQuad();
}
