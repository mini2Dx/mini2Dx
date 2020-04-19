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
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.CollisionPoint;
import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;

/**
 * A {@link CollisionPoint} which implements {@link QuadTreeAware} in order to allow {@link org.mini2Dx.core.collision.QuadTreeSearchDirection#UPWARDS} search.
 */
public class QuadTreeAwareCollisionPoint extends CollisionPoint implements QuadTreeAware {

    private QuadTree tree;

    public QuadTreeAwareCollisionPoint(CollisionPoint point){
        super(point.x, point.y);
    }

    public QuadTreeAwareCollisionPoint(float x, float y) {
        super(x, y);
    }

    public QuadTreeAwareCollisionPoint(int id, float x, float y) {
        super(id, x, y);
    }

    public QuadTreeAwareCollisionPoint(int id) {
        super(id);
    }

    public QuadTreeAwareCollisionPoint() {
        super();
    }

    public QuadTreeAwareCollisionPoint(int id, Collisions collisions) {
        super(id, collisions);
    }

    @Override
    protected void release() {
        collisions.release(this);
    }

    @Override
    public QuadTree getQuad() {
        return tree;
    }

    @Override
    public void setQuad(QuadTree quadTree) {
        tree = quadTree;
    }
}
