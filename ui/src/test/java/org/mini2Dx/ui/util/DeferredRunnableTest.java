/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.ScreenSizeScaleMode;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeferredRunnableTest {
    private final Mockery mockery = new Mockery();

    private AssetManager assetManager;
    private UiContainer uiContainer;
    private Graphics graphics;

    @Before
    public void setUp() {
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
                atLeast(1).of(graphics).getDeltaTime();
                will(returnValue(0.16f));
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
                atLeast(1).of(graphics).getDeltaTime();
                will(returnValue(0.16f));
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
