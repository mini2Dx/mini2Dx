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
package org.mini2Dx.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.junit.*;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.Platform;
import org.mini2Dx.core.assets.AssetManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.mini2Dx.ui.ScreenSizeScaleMode;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeferredRunnableTest {
    static {
        Mdx.locks = new JvmLocks();
    }

    private final Mockery mockery = new Mockery();

    private AssetManager assetManager;
    private UiContainer uiContainer;
    private Graphics graphics;

    @Before
    public void setUp() {
        Mdx.platform = Platform.WINDOWS;

        mockery.setImposteriser(ClassImposteriser.INSTANCE);

        assetManager = mockery.mock(AssetManager.class);
        graphics = mockery.mock(Graphics.class);
        uiContainer = mockery.mock(UiContainer.class);
        Gdx.graphics = graphics;
    }

    @After
    public void teardown() {
        mockery.assertIsSatisfied();
    }

    @Test
    public void testDeferredRunnableCompare() {
        final DeferredRunnable runnable1 = DeferredRunnable.allocate(null, 1.0f);
        final DeferredRunnable runnable2 = DeferredRunnable.allocate(null, 1.0f);
        Assert.assertEquals(1, runnable1.compareTo(runnable2));
        Assert.assertEquals(-1, runnable2.compareTo(runnable1));

        final DeferredRunnable runnable3 = DeferredRunnable.allocate(null, 2.0f);
        final DeferredRunnable runnable4 = DeferredRunnable.allocate(null, 1.0f);
        Assert.assertEquals(-1, runnable3.compareTo(runnable4));
        Assert.assertEquals(1, runnable4.compareTo(runnable3));

        final DeferredRunnable runnable5 = DeferredRunnable.allocate(null, 1.0f);
        final DeferredRunnable runnable6 = DeferredRunnable.allocate(null, 2.0f);
        Assert.assertEquals(1, runnable5.compareTo(runnable6));
        Assert.assertEquals(-1, runnable6.compareTo(runnable5));
    }

    @Test
    public void testDeferredUntilUpdateRunnableProcessOrder() {
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(uiContainer).getZIndex();
                will(returnValue(0));
                atLeast(1).of(uiContainer).getWidth();
                will(returnValue(800f));
                atLeast(1).of(uiContainer).getHeight();
                will(returnValue(600f));
                atLeast(1).of(uiContainer).getFlexLayout();
                will(returnValue(null));
                atLeast(1).of(uiContainer).getScreenSizeScaleMode();
                will(returnValue(ScreenSizeScaleMode.NO_SCALING));
            }
        });

        final DummyUiElement element = new DummyUiElement();
        final UiContainerRenderTree renderTree = new UiContainerRenderTree(uiContainer, assetManager);

        final AtomicBoolean flag1 = new AtomicBoolean(false);
        final AtomicBoolean flag2 = new AtomicBoolean(false);
        final AtomicBoolean flag3 = new AtomicBoolean(false);

        element.deferUntilUpdate(new Runnable() {
            @Override
            public void run() {
                flag1.set(true);
                element.deferUntilUpdate(new Runnable() {
                    @Override
                    public void run() {
                        flag2.set(true);
                    }
                });
            }
        });
        element.deferUntilUpdate(new Runnable() {
            @Override
            public void run() {
                flag3.set(true);
            }
        });

        Assert.assertEquals(false, flag1.get());
        Assert.assertEquals(false, flag2.get());
        Assert.assertEquals(false, flag3.get());

        element.syncWithUpdate(renderTree);
        renderTree.processUpdateDeferred();

        Assert.assertEquals(true, flag1.get());
        Assert.assertEquals(false, flag2.get());
        Assert.assertEquals(true, flag3.get());

        element.syncWithUpdate(renderTree);
        renderTree.processUpdateDeferred();

        Assert.assertEquals(true, flag1.get());
        Assert.assertEquals(true, flag2.get());
        Assert.assertEquals(true, flag3.get());
    }

    @Test
    public void testDeferredUntilLayoutRunnableProcessOrder() {
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(uiContainer).getZIndex();
                will(returnValue(0));
                atLeast(1).of(uiContainer).getWidth();
                will(returnValue(800f));
                atLeast(1).of(uiContainer).getHeight();
                will(returnValue(600f));
                atLeast(1).of(uiContainer).getFlexLayout();
                will(returnValue(null));
                atLeast(1).of(uiContainer).getScreenSizeScaleMode();
                will(returnValue(ScreenSizeScaleMode.NO_SCALING));
            }
        });

        final DummyUiElement element = new DummyUiElement();
        final UiContainerRenderTree renderTree = new UiContainerRenderTree(uiContainer, assetManager);

        final AtomicBoolean flag1 = new AtomicBoolean(false);
        final AtomicBoolean flag2 = new AtomicBoolean(false);
        final AtomicBoolean flag3 = new AtomicBoolean(false);

        element.deferUntilLayout(new Runnable() {
            @Override
            public void run() {
                flag1.set(true);
                element.deferUntilLayout(new Runnable() {
                    @Override
                    public void run() {
                        flag2.set(true);
                    }
                });
            }
        });
        element.deferUntilLayout(new Runnable() {
            @Override
            public void run() {
                flag3.set(true);
            }
        });

        Assert.assertEquals(false, flag1.get());
        Assert.assertEquals(false, flag2.get());
        Assert.assertEquals(false, flag3.get());

        element.syncWithLayout(renderTree);
        renderTree.processLayoutDeferred();

        Assert.assertEquals(true, flag1.get());
        Assert.assertEquals(false, flag2.get());
        Assert.assertEquals(true, flag3.get());

        element.syncWithLayout(renderTree);
        renderTree.processLayoutDeferred();

        Assert.assertEquals(true, flag1.get());
        Assert.assertEquals(true, flag2.get());
        Assert.assertEquals(true, flag3.get());
    }
}
