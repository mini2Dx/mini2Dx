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

import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;

/**
 * A {@link CollisionBox} which implements {@link QuadTreeAware} in order to allow {@link org.mini2Dx.core.collision.QuadTreeSearchDirection#UPWARDS} search.
 */
public class QuadTreeAwareCollisionBox extends CollisionBox implements QuadTreeAware {

    private QuadTree tree;

    public QuadTreeAwareCollisionBox(CollisionBox box){
        super(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    public QuadTreeAwareCollisionBox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public QuadTreeAwareCollisionBox(int id, float x, float y, float width, float height) {
        super(id, x, y, width, height);
    }

    public QuadTreeAwareCollisionBox(int id) {
        super(id);
    }

    public QuadTreeAwareCollisionBox() {
        super();
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
