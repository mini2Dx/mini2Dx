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

import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.utils.Queue;

/**
 * Provides pooled geometry classes
 */
public class Geometry {
    /**
     * Default pool size. Modify this value before launching the game to increase the default pool sizes.
     */
    public static int DEFAULT_POOL_SIZE = 8;

    final Queue<Circle> circles = new Queue<Circle>();
    final Queue<EquilateralTriangle> equilateralTriangles = new Queue<EquilateralTriangle>();
    final Queue<Line> lines = new Queue<Line>();
    final Queue<LineSegment> lineSegments = new Queue<LineSegment>();
    final Queue<Point> points = new Queue<Point>();
    final Queue<Polygon> polygons = new Queue<Polygon>();
    final Queue<Rectangle> rectangles = new Queue<Rectangle>();
    final Queue<RegularHexagon> regularHexagons = new Queue<RegularHexagon>();
    final Queue<RegularPentagon> regularPentagons = new Queue<RegularPentagon>();
    final Queue<Triangle> triangles = new Queue<Triangle>();

    private boolean initialised = false;

    public Geometry() {
        super();
        init();
    }

    /**
     * Initialises the pool
     */
    public void init() {
        if(initialised) {
            return;
        }
        for(int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            circles.addLast(new Circle(this));
            equilateralTriangles.addLast(new EquilateralTriangle(this));
            lines.addLast(new Line(this));
            lineSegments.addLast(new LineSegment(this));
            points.addLast(new Point(this));
            rectangles.addLast(new Rectangle(this));
            regularHexagons.addLast(new RegularHexagon(this));
            regularPentagons.addLast(new RegularPentagon(this));
        }
        initialised = true;
    }

    /**
     * Allocates a {@link Circle} from the pool
     * @return A {@link Circle} instance
     */
    public Circle circle() {
        if(circles.size == 0) {
            return new Circle(this);
        }
        return circles.removeFirst();
    }

    /**
     * Releases a {@link Circle} instance back to the pool
     * @param circle The {@link Circle} instance
     */
    public void release(Circle circle) {
        circles.addLast(circle);
    }

    /**
     * Allocates a {@link EquilateralTriangle} from the pool
     * @return A {@link EquilateralTriangle} instance
     */
    public EquilateralTriangle equilateralTriangle() {
        if(equilateralTriangles.size == 0) {
            return new EquilateralTriangle(this);
        }
        return equilateralTriangles.removeFirst();
    }

    /**
     * Releases a {@link EquilateralTriangle} instance back to the pool
     * @param equilateralTriangle The {@link EquilateralTriangle} instance
     */
    public void release(EquilateralTriangle equilateralTriangle) {
        equilateralTriangles.addLast(equilateralTriangle);
    }

    /**
     * Allocates a {@link Line} from the pool
     * @return A {@link Line} instance
     */
    public Line line() {
        if(lines.size == 0) {
            return new Line(this);
        }
        return lines.removeFirst();
    }

    /**
     * Releases a {@link Line} instance back to the pool
     * @param line The {@link Line} instance
     */
    public void release(Line line) {
        lines.addLast(line);
    }

    /**
     * Allocates a {@link LineSegment} from the pool
     * @return A {@link LineSegment} instance
     */
    public LineSegment lineSegment() {
        if(lineSegments.size == 0) {
            return new LineSegment(this);
        }
        return lineSegments.removeFirst();
    }

    /**
     * Releases a {@link LineSegment} instance back to the pool
     * @param lineSegment The {@link LineSegment} instance
     */
    public void release(LineSegment lineSegment) {
        lineSegments.addLast(lineSegment);
    }

    /**
     * Allocates a {@link Point} from the pool
     * @return A {@link Point} instance
     */
    public Point point() {
        if(points.size == 0) {
            return new Point(this);
        }
        return points.removeFirst();
    }

    /**
     * Releases a {@link Point} instance back to the pool
     * @param point The {@link Point} instance
     */
    public void release(Point point) {
        points.addLast(point);
    }

    /**
     * Allocates a {@link Polygon} from the pool
     * @return A {@link Polygon} instance
     */
    public Polygon polygon() {
        if(polygons.size == 0) {
            return polygon(new float[]{0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f});
        }
        return polygons.removeFirst();
    }

    /**
     * Allocates a {@link Polygon} from the pool
     * @param vertices The vertices to set
     * @return A {@link Polygon} instance
     */
    public Polygon polygon(float [] vertices) {
        if(polygons.size == 0) {
            return new Polygon(this, vertices);
        }
        final Polygon result = polygons.removeFirst();
        result.setVertices(vertices);
        return result;
    }

    /**
     * Releases a {@link Polygon} instance back to the pool
     * @param polygon The {@link Polygon} instance
     */
    public void release(Polygon polygon) {
        polygons.addLast(polygon);
    }

    /**
     * Allocates a {@link Rectangle} from the pool
     * @return A {@link Rectangle} instance
     */
    public Rectangle rectangle() {
        if(rectangles.size == 0) {
            return new Rectangle(this);
        }
        return rectangles.removeFirst();
    }

    /**
     * Releases a {@link Rectangle} instance back to the pool
     * @param rectangle The {@link Rectangle} instance
     */
    public void release(Rectangle rectangle) {
        rectangles.addLast(rectangle);
    }

    /**
     * Allocates a {@link RegularHexagon} from the pool
     * @return A {@link RegularHexagon} instance
     */
    public RegularHexagon regularHexagon() {
        if(regularHexagons.size == 0) {
            return new RegularHexagon(this);
        }
        return regularHexagons.removeFirst();
    }

    /**
     * Releases a {@link RegularHexagon} instance back to the pool
     * @param regularHexagon The {@link RegularHexagon} instance
     */
    public void release(RegularHexagon regularHexagon) {
        regularHexagons.addLast(regularHexagon);
    }

    /**
     * Allocates a {@link RegularPentagon} from the pool
     * @return A {@link RegularPentagon} instance
     */
    public RegularPentagon regularPentagon() {
        if(regularPentagons.size == 0) {
            return new RegularPentagon(this);
        }
        return regularPentagons.removeFirst();
    }

    /**
     * Releases a {@link RegularPentagon} instance back to the pool
     * @param regularPentagon The {@link RegularPentagon} instance
     */
    public void release(RegularPentagon regularPentagon) {
        regularPentagons.addLast(regularPentagon);
    }

    /**
     * Allocates a {@link Triangle} from the pool
     * @return A {@link Triangle} instance
     */
    public Triangle triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        if(triangles.size == 0) {
            return new Triangle(this, x1, y1, x2, y2, x3, y3);
        }
        final Triangle result = triangles.removeFirst();
        result.setPosition(x1, y1, x2, y2, x3, y3);
        return result;
    }

    /**
     * Releases a {@link Triangle} instance back to the pool
     * @param triangle The {@link Triangle} instance
     */
    public void release(Triangle triangle) {
        triangles.addLast(triangle);
    }
}
