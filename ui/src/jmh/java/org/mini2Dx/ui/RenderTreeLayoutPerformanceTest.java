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

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.headless.HeadlessMini2DxConfig;
import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Modal;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.style.UiTheme;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.headless.HeadlessMini2DxGame;

/**
 * Performance tests for UI render tree layout operations
 */
@Threads(value=1)
public class RenderTreeLayoutPerformanceTest {
	private static final int CHILDREN_PER_NODE = 5;

	@State(Scope.Thread)
	public static class TestState extends BasicGame {
		private FileHandleResolver fileHandleResolver = new ClasspathFileHandleResolver();
		private AssetManager assetManager = new AssetManager(fileHandleResolver);
		private List<Modal> modals = new ArrayList<Modal>();
		
		private HeadlessMini2DxGame game;
		private UiContainer uiContainer;
		
		{
			assetManager.setLoader(UiTheme.class, new UiThemeLoader(fileHandleResolver, true));
			
			HeadlessMini2DxConfig config = new HeadlessMini2DxConfig(RenderTreeLayoutPerformanceTest.class.getName());
			config.runGame = false;
			game = new HeadlessMini2DxGame(this, config);
			uiContainer = new UiContainer(this, assetManager);
			
			assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);
			assetManager.finishLoading();
			
			uiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
			
			for(HorizontalAlignment hAlignment : HorizontalAlignment.values()) {
				for(VerticalAlignment vAlignment : VerticalAlignment.values()) {
					AlignedModal modal = new AlignedModal();
					modal.setLayout("xs-4c");
					modal.setVisibility(Visibility.VISIBLE);
					modal.setHorizontalAlignment(hAlignment);
					modal.setVerticalAlignment(vAlignment);
					
					addNestedElements(modal, 0);
					modals.add(modal);
				}
			}
		}
		
		@Setup(Level.Iteration)
		public void setUp() {
			for(Modal modal : modals) {
				uiContainer.add(modal);
			}
		}
		
		@TearDown(Level.Iteration)
		public void cleanup() {
			for(Modal modal : modals) {
				uiContainer.remove(modal);
			}
			uiContainer.update(GameContainer.MAXIMUM_DELTA);
		}
		
		private void addNestedElements(Column root, int depth) {
			if(depth >= CHILDREN_PER_NODE) {
				return;
			}
			for(int i = 0; i < CHILDREN_PER_NODE; i++) {
				Column column = new Column();
				column.setLayout("xs-4c");
				column.setVisibility(Visibility.VISIBLE);
				
				Label label = new Label();
				label.setVisibility(Visibility.VISIBLE);
				label.setText("Label " + i);
				label.setResponsive(true);
				column.add(label);
				
				addNestedElements(column, depth + 1);
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
