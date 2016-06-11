/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.uats.util;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.BlendingUAT;
import org.mini2Dx.uats.ClippingUAT;
import org.mini2Dx.uats.ControllerMapping;
import org.mini2Dx.uats.ControllerUAT;
import org.mini2Dx.uats.GeometryUAT;
import org.mini2Dx.uats.GraphicsUAT;
import org.mini2Dx.uats.IsometricTiledMapUAT;
import org.mini2Dx.uats.OrthogonalTiledMapNoCachingUAT;
import org.mini2Dx.uats.OrthogonalTiledMapWithCachingUAT;
import org.mini2Dx.uats.ParticleEffectsUAT;
import org.mini2Dx.uats.UiUAT;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.animation.TypingTextAnimation;
import org.mini2Dx.ui.effect.SlideIn;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author Thomas Cashman
 */
public class UATSelectionScreen extends BasicGameScreen implements ScreenSizeListener {
	public static final int SCREEN_ID = 1;

	private final AssetManager assetManager;

	private UiContainer uiContainer;
	private AlignedModal uatsDialog;
	private int nextScreenId = -1;

	public UATSelectionScreen(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
		uiContainer.addScreenSizeListener(this);
		initialiseUi();
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		uiContainer.update(delta);
		if (nextScreenId > -1) {
			screenManager.enterGameScreen(nextScreenId, new FadeOutTransition(), new FadeInTransition());
			nextScreenId = -1;
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		uiContainer.interpolate(alpha);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.clearBlendFunction();
		g.clearShaderProgram();
		g.removeClip();
		g.setBackgroundColor(Color.WHITE);
		g.setColor(Color.BLUE);

		uiContainer.render(g);
	}

	@Override
	public void preTransitionIn(Transition transitionIn) {
		nextScreenId = -1;
		if (!uiContainer.isThemeApplied()) {
			uiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
		}
		Gdx.input.setInputProcessor(uiContainer);
		uatsDialog.applyEffect(new SlideIn());
	}

	@Override
	public void postTransitionIn(Transition transitionIn) {
		
	}

	@Override
	public void preTransitionOut(Transition transitionOut) {
	}

	@Override
	public void postTransitionOut(Transition transitionOut) {
		uatsDialog.setVisibility(Visibility.HIDDEN);
	}

	@Override
	public int getId() {
		return SCREEN_ID;
	}

	private void initialiseUi() {
		uatsDialog = new AlignedModal("uats-dialog");
		uatsDialog.setHorizontalAlignment(HorizontalAlignment.CENTER);
		uatsDialog.setVerticalAlignment(VerticalAlignment.TOP);
		uatsDialog.setLayout(new LayoutRuleset("xs-12 sm-10 md-8 lg-6 sm-offset-1 md-offset-2 lg-offset-3"));

		uatsDialog.add(Row.withElements("row-os", UiUtils.createLabel("Detected OS: " + Mdx.os)));
		uatsDialog.add(Row.withElements("row-header", UiUtils.createHeader("User Acceptance Tests", new TypingTextAnimation())));
		uatsDialog.add(Row.withElements("row-blending", UiUtils.createButton("Blending", false, new ActionListener() {
			@Override
			public void onActionBegin(Actionable source) {
			}

			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = ScreenIds.getScreenId(BlendingUAT.class);
			}
		})));
		uatsDialog.add(Row.withElements("row-clip", UiUtils.createButton("Graphics.clip()", false, new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(ClippingUAT.class);
					}
				})));
		uatsDialog.add(Row.withElements("row-geometry", UiUtils.createButton("Geometry", new ActionListener() {
			@Override
			public void onActionBegin(Actionable source) {
			}

			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = ScreenIds.getScreenId(GeometryUAT.class);
			}
		})));
		uatsDialog.add(Row.withElements("row-graphics", UiUtils.createButton("Graphics", new ActionListener() {
			@Override
			public void onActionBegin(Actionable source) {
			}

			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = ScreenIds.getScreenId(GraphicsUAT.class);
			}
		})));
		uatsDialog.add(Row.withElements("row-orthogonal-tiledmap",
				UiUtils.createButton("Orthogonal TiledMap (No Caching)", new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(OrthogonalTiledMapNoCachingUAT.class);
					}
				})));
		uatsDialog.add(Row.withElements("row-orthogonal-tiledmap-caching",
				UiUtils.createButton("Orthogonal TiledMap (With Caching)", new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(OrthogonalTiledMapWithCachingUAT.class);
					}
				})));
		uatsDialog.add(Row.withElements("row-isometric-tiledmap",
				UiUtils.createButton("Isometric TiledMap (No Caching)", new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(IsometricTiledMapUAT.class);
					}
				})));
		uatsDialog.add(
				Row.withElements("row-particle-effects", UiUtils.createButton("Particle Effects", new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(ParticleEffectsUAT.class);
					}
				})));
		uatsDialog.add(Row.withElements("row-controllers", UiUtils.createButton("Controllers", new ActionListener() {
			@Override
			public void onActionBegin(Actionable source) {
			}

			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = ScreenIds.getScreenId(ControllerUAT.class);
			}
		})));
		uatsDialog.add(Row.withElements("row-ui", UiUtils.createButton("UI", new ActionListener() {
			@Override
			public void onActionBegin(Actionable source) {
			}

			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = ScreenIds.getScreenId(UiUAT.class);
			}
		})));
		uatsDialog.add(Row.withElements("row-utilities", UiUtils.createHeader("Utilities")));
		uatsDialog.add(
				Row.withElements("row-controller-mapping", UiUtils.createButton("Controller Mapping", new ActionListener() {
					@Override
					public void onActionBegin(Actionable source) {
					}

					@Override
					public void onActionEnd(Actionable source) {
						nextScreenId = ScreenIds.getScreenId(ControllerMapping.class);
					}
				})));
		uatsDialog.setVisibility(Visibility.VISIBLE);

		uiContainer.add(uatsDialog);
	}

	@Override
	public void onScreenSizeChanged(ScreenSize screenSize) {
		System.out.println("Current Screen Size: " + screenSize);
	}
}
