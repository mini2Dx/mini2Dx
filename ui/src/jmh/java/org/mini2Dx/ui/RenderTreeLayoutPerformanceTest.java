/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Div;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Visibility;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Performance tests for UI render tree layout operations
 */
@Threads(value=1)
public class RenderTreeLayoutPerformanceTest {
	private static final int CHILDREN_PER_NODE = 5;

	@State(Scope.Thread)
	public static class TestState extends BasicGame {
		private FileHandleResolver fileHandleResolver = new ClasspathFileHandleResolver();
		//private AssetManager assetManager = new AssetManager(fileHandleResolver);
		private List<Container> containers = new ArrayList<Container>();
		
		//private HeadlessMini2DxGame game;
		private UiContainer uiContainer;
		
//		{
//			assetManager.setLoader(UiTheme.class, new UiThemeLoader(fileHandleResolver, true));
//
//			HeadlessMini2DxConfig config = new HeadlessMini2DxConfig(RenderTreeLayoutPerformanceTest.class.getName());
//			config.runGame = false;
//			game = new HeadlessMini2DxGame(this, config);
//			uiContainer = new UiContainer(this, assetManager);
//
//			assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);
//			assetManager.finishLoading();
//
//			uiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
//
//			for(HorizontalAlignment hAlignment : HorizontalAlignment.values()) {
//				for(VerticalAlignment vAlignment : VerticalAlignment.values()) {
//					Container modal = new Container();
//					modal.setFlexLayout("flex-column:xs-4c");
//					modal.setVisibility(Visibility.VISIBLE);
//
//					addNestedElements(modal, 0);
//					containers.add(modal);
//				}
//			}
//		}
		
		@Setup(Level.Iteration)
		public void setUp() {
			for(Container container : containers) {
				uiContainer.add(container);
			}
		}
		
		@TearDown(Level.Iteration)
		public void cleanup() {
			for(Container container : containers) {
				uiContainer.remove(container);
			}
			uiContainer.update(GameContainer.MAXIMUM_DELTA);
		}
		
		private void addNestedElements(Div root, int depth) {
			if(depth >= CHILDREN_PER_NODE) {
				return;
			}
			for(int i = 0; i < CHILDREN_PER_NODE; i++) {
				Div div = new Div();
				div.setFlexLayout("flex-div:xs-4c");
				div.setVisibility(Visibility.VISIBLE);
				
				Label label = new Label();
				label.setVisibility(Visibility.VISIBLE);
				label.setText("Label " + i);
				label.setResponsive(true);
				div.add(label);
				
				addNestedElements(div, depth + 1);
			}
		}

		@Override
		public void initialise() {}

		@Override
		public void update(float delta) {}

		@Override
		public void interpolate(float alpha) {}

		@Override
		public void render(Graphics g) {}
	}
	
	@Benchmark
	@BenchmarkMode(value=Mode.AverageTime)
	@Group("UiContainer_InitialLayout")
	public void testInitialLayout(TestState state) {
		state.uiContainer.update(GameContainer.MAXIMUM_DELTA);
	}
}
