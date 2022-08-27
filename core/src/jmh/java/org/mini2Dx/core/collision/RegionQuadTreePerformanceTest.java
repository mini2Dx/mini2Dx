package org.mini2Dx.core.collision;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.Circle;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Triangle;
import org.mini2Dx.core.util.FastXYSorterTest;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.RandomXS128;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.lockprovider.jvm.JvmLocks;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

public class RegionQuadTreePerformanceTest {
    private static final float X = 0f;
    private static final float Y = 0f;
    private static final float WIDTH = 512f;
    private static final float HEIGHT = 512f;
    private static final int ELEMENTS_PER_QUAD = 16;
    private static final float MINIMUM_QUAD_SIZE = 8f;
    private static final float MAXIMUM_QUAD_SIZE = 64f;

    private static final int TOTAL_ELEMENTS = 4096;

    static {
        Mdx.locks = new JvmLocks();
    }

    @State(Scope.Thread)
    public static class TestState {
        public RegionQuadTreeOld<CollisionBox> oldImpl = new RegionQuadTreeOld<>(ELEMENTS_PER_QUAD, X, Y, WIDTH, HEIGHT);
        public RegionQuadTree<CollisionBox> newImpl = new RegionQuadTree<>(ELEMENTS_PER_QUAD, X, Y, WIDTH, HEIGHT);

        public Array<CollisionBox> toAdd = new Array<>(TOTAL_ELEMENTS);

        public RegionQuadTreeOld<CollisionBox> oldImplPopulated = new RegionQuadTreeOld<>(ELEMENTS_PER_QUAD, X, Y, WIDTH, HEIGHT);
        public RegionQuadTree<CollisionBox> newImplPopulated = new RegionQuadTree<>(ELEMENTS_PER_QUAD, X, Y, WIDTH, HEIGHT);

        public Array<CollisionBox> searchResults = new Array<>(TOTAL_ELEMENTS);

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            oldImpl.clear();
            newImpl.clear();
            searchResults.clear();
            toAdd.clear();

            MathUtils.random = new RandomXS128(13442574);
            for(int i = 0; i < TOTAL_ELEMENTS; i++) {
                float size = MathUtils.random(MINIMUM_QUAD_SIZE, MAXIMUM_QUAD_SIZE);
                float x = MathUtils.random(X, X + WIDTH - size - 1f);
                float y = MathUtils.random(Y, Y + HEIGHT - size - 1f);
                toAdd.add(new CollisionBox(i, x, y, size, size));

                oldImplPopulated.add(new CollisionBox(i, x, y, size, size));
                newImplPopulated.add(new CollisionBox(i, x, y, size, size));
            }
        }
    }

//    @Benchmark
//    @BenchmarkMode(value= Mode.AverageTime)
//    public void testAddOldImpl(RegionQuadTreePerformanceTest.TestState state) {
//        for(int i = 0; i < TOTAL_ELEMENTS; i++) {
//            state.oldImpl.add(state.toAdd.get(i));
//        }
//    }
//
//    @Benchmark
//    @BenchmarkMode(value= Mode.AverageTime)
//    public void testAddNewImpl(RegionQuadTreePerformanceTest.TestState state) {
//        for(int i = 0; i < TOTAL_ELEMENTS; i++) {
//            state.newImpl.add(state.toAdd.get(i));
//        }
//    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchOldImplTinyArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 4f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.oldImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchNewImplTinyArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 4f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.newImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchOldImplSmallArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 32f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.oldImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchNewImplSmallArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 32f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.newImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchOldImplMediumArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 64f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.oldImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchNewImplMediumArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 64f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.newImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchOldImplLargeArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 256f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.oldImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testSearchNewImplLargeArea(RegionQuadTreePerformanceTest.TestState state) {
        final Rectangle searchArea = new Rectangle();
        final float searchSize = 256f;
        for(int y = 0; y < HEIGHT; y += searchSize * 0.5f) {
            for(int x = 0; x < WIDTH; x += searchSize * 0.5f) {
                searchArea.set(x, y, 32f, 32f);
                state.newImplPopulated.getElementsWithinArea(state.searchResults, searchArea);
                state.searchResults.clear();
            }
        }
    }
}
