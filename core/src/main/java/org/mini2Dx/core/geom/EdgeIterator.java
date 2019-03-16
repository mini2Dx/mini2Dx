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
package org.mini2Dx.core.geom;

import org.mini2Dx.core.exception.MdxException;

/**
 * A iterator-type class for iterating over {@link Shape} edges.
 *
 * Note: This class is not thread safe.
 */
public abstract class EdgeIterator {
    private boolean begun = false;

    /**
     * Begin iteration
     */
    public void begin() {
        if(begun) {
            throw new MdxException("Cannot call begin() without first calling end() on previous iteration");
        }
        beginIteration();
        begun = true;
    }

    /**
     * End iteration
     */
    public void end() {
        if(!begun) {
            throw new MdxException("Cannot call end() without first calling begin()");
        }
        endIteration();
        begun = false;
    }

    /**
     * Moves the iterator to the next edge
     */
    public void next() {
        if(!begun) {
            throw new MdxException("Cannot call next() without first calling begin()");
        }
        nextEdge();
    }

    protected abstract void beginIteration();

    protected abstract void endIteration();

    protected abstract void nextEdge();

    /**
     * Returns if there is another edge to iterate over
     * @return True if there is another edge
     */
    public abstract boolean hasNext();

    /**
     * Returns the x coordinate of the first point in the edge
     * @return
     */
    public abstract float getPointAX();

    /**
     * Returns the y coordinate of the first point in the edge
     * @return
     */
    public abstract float getPointAY();

    /**
     * Returns the x coordinate of the second point in the edge
     * @return
     */
    public abstract float getPointBX();

    /**
     * Returns the y coordinate of the second point in the edge
     * @return
     */
    public abstract float getPointBY();

    /**
     * Returns the {@link LineSegment} of the edge
     * @return
     */
    public abstract LineSegment getEdgeLineSegment();
}

