/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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

import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Queue;

public class Quad {
    public static final int CHILD_TOP_LEFT_OFFSET = 0;
    public static final int CHILD_TOP_RIGHT_OFFSET = 1;
    public static final int CHILD_BOTTOM_LEFT_OFFSET = 2;
    public static final int CHILD_BOTTOM_RIGHT_OFFSET = 3;

    public static final int INITIAL_POOL_SIZE = 1024;
    private static final Queue<Quad> POOL = new Queue<>();

    static {
        for(int i = 0; i < INITIAL_POOL_SIZE; i++) {
            POOL.addLast(new Quad());
        }
    }

    public float x, y, maxX, maxY;
    public QuadElementBounds elementBounds;
    public int index = -1;
    public int parentIndex = -1;
    public int elementsIndex = -1;
    public int childIndex = -1;

    public boolean contains(float x, float y) {
        if(x < this.x) {
            return false;
        }
        if(y < this.y) {
            return false;
        }
        if(x > this.maxX) {
            return false;
        }
        return y <= this.maxY;
    }

    public boolean elementsOverlap(Rectangle rectangle) {
        return elementsOverlap(rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

    public boolean elementsOverlap(float minX, float minY, float maxX, float maxY) {
        if(elementBounds == null) {
            return false;
        }
        if(elementBounds.maxX < minX) {
            return false;
        }
        if(elementBounds.maxY < minY) {
            return false;
        }
        if(elementBounds.x > maxX) {
            return false;
        }
        return elementBounds.y <= maxY;
    }

    public boolean elementsOverlapIgnoringEdges(Rectangle rectangle) {
        return elementsOverlapIgnoringEdges(rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

    public boolean elementsOverlapIgnoringEdges(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        if(elementBounds == null) {
            return false;
        }
        if(elementBounds.maxX < rectMinX) {
            return false;
        }
        if(elementBounds.maxY < rectMinY) {
            return false;
        }
        if(elementBounds.x > rectMaxX) {
            return false;
        }
        return elementBounds.y <= rectMaxY;
    }

    public boolean elementsIntersect(LineSegment lineSegment) {
        if(elementBounds == null) {
            return false;
        }
        return elementBounds.intersects(lineSegment);
    }

    public boolean elementsContain(Vector2 v) {
        if(elementBounds == null) {
            return false;
        }
        return elementBounds.contains(v.x, v.y);
    }

    public static Quad allocate() {
        synchronized (POOL) {
            if(POOL.size == 0) {
                return new Quad();
            } else {
                return POOL.removeFirst();
            }
        }
    }

    public void reset() {
        if(elementBounds != null) {
            elementBounds.dispose();
            elementBounds = null;
        }
        index = -1;
        elementsIndex = -1;
        parentIndex = -1;
        childIndex = -1;
    }

    public void dispose() {
        reset();

        synchronized (POOL) {
            POOL.addLast(this);
        }
    }

    public float getWidth() {
        return maxX - x;
    }

    public float getHeight() {
        return maxY - y;
    }

    public float getCenterX() {
        return x + ((maxX - x) * 0.5f);
    }

    public float getCenterY() {
        return x + ((maxY - y) * 0.5f);
    }
}
