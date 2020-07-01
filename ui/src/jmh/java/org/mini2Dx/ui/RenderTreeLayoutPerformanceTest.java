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
	private static final float MAXIMUM_DELTA = 1f / GameContainer.TARGET_FPS;

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
			uiContainer.update(MAXIMUM_DELTA);
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
		state.uiContainer.update(MAXIMUM_DELTA);
	}
}
