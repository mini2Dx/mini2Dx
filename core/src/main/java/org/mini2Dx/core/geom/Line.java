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

import org.mini2Dx.core.Geometry;
import org.mini2Dx.gdx.math.Intersector;

public class Line extends LineSegment {

    public Line() {
        this(0, 0, 0, 0);
    }

    public Line(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    public Line(Geometry geometry) {
       super(geometry);
    }

    @Override
    public boolean contains(float x, float y) {
        return Intersector.distanceLinePoint(pointA.x, pointA.y, pointB.x, pointB.y, x, y) == 0;
    }
}
