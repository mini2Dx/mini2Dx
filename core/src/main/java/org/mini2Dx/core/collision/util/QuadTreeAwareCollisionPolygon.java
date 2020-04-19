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

import org.mini2Dx.core.collision.CollisionPolygon;
import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;
import org.mini2Dx.gdx.math.Vector2;

/**
 * A {@link CollisionPolygon} which implements {@link QuadTreeAware} in order to allow {@link org.mini2Dx.core.collision.QuadTreeSearchDirection#UPWARDS} search.
 */
public class QuadTreeAwareCollisionPolygon extends CollisionPolygon implements QuadTreeAware {
    QuadTree tree;

    public QuadTreeAwareCollisionPolygon(float[] vertices) {
        super(vertices);
    }

    public QuadTreeAwareCollisionPolygon(Vector2[] vectors) {
        super(vectors);
    }

    public QuadTreeAwareCollisionPolygon(int id, float[] vertices) {
        super(id, vertices);
    }

    public QuadTreeAwareCollisionPolygon(int id, Vector2[] vectors) {
        super(id, vectors);
    }

    public QuadTreeAwareCollisionPolygon(CollisionPolygon polygon){
        super(polygon.getVertices());
    }

    public QuadTreeAwareCollisionPolygon(int id, Collisions collisions, float[] vertices) {
        super(id, collisions, vertices);
    }

    public QuadTreeAwareCollisionPolygon(int id, Collisions collisions, Vector2[] vectors) {
        super(id, collisions, vectors);
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
