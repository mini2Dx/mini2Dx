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
package org.mini2Dx.core.util;

import org.mini2Dx.gdx.math.MathUtils;

import java.util.BitSet;

/**
 * Utility class for fast sorting of objects to be rendered
 *
 * Works by combining an grid with a bitset.
 *
 * For per-pixel games, it is recommended to decide on a grid cell size (e.g. 8 x 8) and divide your screen size by this size.
 *
 * For tile-based games, your entities should already be conforming to a grid.
 *
 * For best performance, pre-calculate everything according to the grid.
 *
 * @param <T>
 */
public class FastXYSorter<T extends Comparable<T>> {
    private final int width, height, size;
    private final FastXYSorterBucket<T>[] buckets;
    private final BitSet bitSet;

    private int originX, originY;
    private int cursor;

    boolean moveCursor = false;

    /**
     * Constructor
     * @param screenWidth The screen width in pixels
     * @param screenHeight The screen height in pixels
     * @param cellWidth The cell width in pixels to divide the screen width by
     * @param cellHeight The cell height in pixels to divide the screen height by
     * @param expectedElementsPerCell The expected amount of entities per cell
     */
    public FastXYSorter(int screenWidth, int screenHeight, int cellWidth, int cellHeight, int expectedElementsPerCell) {
        this(screenWidth / cellWidth, screenHeight / cellHeight, expectedElementsPerCell);
    }

    /**
     * Constructor
     * @param gridWidth The width of the grid
     * @param gridHeight The height of the grid
     * @param expectedElementsPerCell The expected amount of entities per cell
     */
    public FastXYSorter(int gridWidth, int gridHeight, int expectedElementsPerCell) {
        this.width = gridWidth;
        this.height = gridHeight;
        this.size = gridWidth * gridHeight;

        buckets = new FastXYSorterBucket[size];
        bitSet = new BitSet(size);

        for(int y = 0; y < gridHeight; y++) {
            for(int x = 0; x < gridWidth; x++) {
                buckets[(y * gridWidth) + x] = new FastXYSorterBucket<>(expectedElementsPerCell);
            }
        }
    }

    /**
     * Useful for setting an origin that is subtracted from values added to the sorter (e.g. camera position)
     * @param originX The X value to subtract
     * @param originY The Y value to subtract
     */
    public void setOrigin(int originX, int originY) {
        this.originX = originX;
        this.originY = originY;
    }

    /**
     *
     * @param screenX The pixel X coordinate of the object
     * @param screenY The pixel Y coordinate of the object
     * @param cellWidth The width of the cells in pixels
     * @param cellHeight The height of the cells in pixels
     * @param obj The object to be added
     */
    public void add(int screenX, int screenY, int cellWidth, int cellHeight, T obj) {
        add(screenX / cellWidth, screenY / cellHeight, obj);
    }

    /**
     * Add an object to be sorted
     * @param gridX The grid X coordinate of the object
     * @param gridY The grid Y coordinate of the object
     * @param obj The object to be added
     */
    public void add(int gridX, int gridY, T obj) {
        final int targetX = MathUtils.clamp(gridX - originX, 0, width);
        final int targetY = MathUtils.clamp(gridY - originY, 0, height);
        final int index = (targetY * width) + targetX;
        buckets[index].add(obj);
        bitSet.set(index);
    }

    /**
     * Must be called after all calls to {@link #add(int, int, Comparable)} are done
     */
    public void begin() {
        cursor = bitSet.nextSetBit(0);
        moveCursor = false;
    }

    /**
     * Returns the next object to render
     * @return Null if no more objects
     */
    public T poll() {
        if(moveCursor) {
            moveCursor = false;
            bitSet.clear(cursor);
            cursor = bitSet.nextSetBit(cursor);
        }
        while(cursor > -1) {
            final FastXYSorterBucket<T> bucket = buckets[cursor];
            final T result = bucket.poll(this);
            if(result != null) {
                return result;
            }
            bitSet.clear(cursor);
            cursor = bitSet.nextSetBit(cursor);
        }
        return null;
    }
}
