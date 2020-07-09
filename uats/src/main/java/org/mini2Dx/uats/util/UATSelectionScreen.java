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
package org.mini2Dx.uats.util;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.InputMultiplexer;
import org.mini2Dx.uats.*;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.animation.TypingTextAnimation;
import org.mini2Dx.ui.effect.SlideIn;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.FlexRow;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.gamepad.GamePadUiInput;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.navigation.VerticalUiNavigation;
import org.mini2Dx.ui.style.UiTheme;

/**
 *
 * @author Thomas Cashman
 */
public class UATSelectionScreen extends BasicGameScreen implements ScreenSizeListener {
	public static final int SCREEN_ID = 1;

	private final AssetManager assetManager;
	private final FileHandleResolver fileHandleResolver;

	private UiContainer uiContainer;
	private VerticalUiNavigation uiNavigation;
	private GamePadUiInput<?> controllerInput;
	private Container uatsDialog;
	private InputSource lastInputSource = InputSource.KEYBOARD_MOUSE;
	private int nextScreenId = -1;

	public UATSelectionScreen(AssetManager assetManager, FileHandleResolver fileHandleResolver) {
		this.assetManager = assetManager;
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
		uiContainer.addScreenSizeListener(this);
		uiContainer.setNavigationMode(UATApplication.NAVIGATION_MODE);
		uiContainer.addActionKey(Input.Keys.E);
		initialiseUi();
		
		if(Mdx.input.getGamePads().size > 0) {
			try {
				controllerInput = UiUtils.setUpControllerInput(Mdx.input.getGamePads().get(0), uiContainer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		uiContainer.update(delta);

		if(!lastInputSource.equals(uiContainer.getLastInputSource())) {
			lastInputSource = uiContainer.getLastInputSource();
		}

		if (nextScreenId > -1) {
			screenManager.enterGameScreen(nextScreenId, new FadeOutTransition(), new FadeInTransition());
			nextScreenId = -1;
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.clearBlendFunction();
		g.clearShader();
		g.removeClip();
		g.setBackgroundColor(Colors.WHITE());
		g.setColor(Colors.BLUE());

		uiContainer.render(g);
	}

	@Override
	public void preTransitionIn(Transition transitionIn) {
		nextScreenId = -1;
		if (!UiContainer.isThemeApplied()) {
			UiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
		}

		final InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(UiUtils.getCustomCursor(fileHandleResolver));
		inputMultiplexer.addProcessor(uiContainer);
		Mdx.input.setInputProcessor(inputMultiplexer);

		uatsDialog.applyEffect(new SlideIn());
	}

	@Override
	public void postTransitionIn(Transition transitionIn) {
		uiContainer.setActiveNavigation(uatsDialog);
		uiNavigation.resetCursor(true);
		if(controllerInput != null) {
			controllerInput.enable();
		}
	}

	@Override
	public void preTransitionOut(Transition transitionOut) {
		if(controllerInput != null) {
			controllerInput.disable();
		}
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
		uatsDialog = new Container("uats-dialog");
		uatsDialog.setFlexLayout("flex-column:xs-12c sm-10c md-8c lg-6c sm-offset-1c md-offset-2c lg-offset-3c");
		uiNavigation = new VerticalUiNavigation();
		
		uatsDialog.add(FlexRow.withElements("row-os", UiUtils.createLabel("Detected OS: " + Mdx.platform)));
		uatsDialog.add(FlexRow.withElements("row-header", UiUtils.createHeader("User Acceptance Tests", new TypingTextAnimation())));
		uatsDialog.add(FlexRow.withElements("row-blending", UiUtils.createButton(uiNavigation, "Blending", false, new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(BlendingUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-clip", UiUtils.createButton(uiNavigation, "Graphics.clip()", false, new ActionListener() {
					@Override
					public void onActionBegin(ActionEvent event) {
					}

					@Override
					public void onActionEnd(ActionEvent event) {
						nextScreenId = ScreenIds.getScreenId(ClippingUAT.class);
					}
				})));
		uatsDialog.add(FlexRow.withElements("row-geometry", UiUtils.createButton(uiNavigation, "Geometry", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(GeometryUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-graphics", UiUtils.createButton(uiNavigation, "Graphics", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(GraphicsUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-textureregion", UiUtils.createButton(uiNavigation, "Texture Regions", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(TextureRegionUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-sprite", UiUtils.createButton(uiNavigation, "Sprites", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(SpriteUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-viewports", UiUtils.createButton(uiNavigation, "Viewports", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(ViewportUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-monospace", UiUtils.createButton(uiNavigation, "Monospace Font", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(MonospaceFontUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-audio", UiUtils.createButton(uiNavigation, "Audio", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(AudioUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-async-audio", UiUtils.createButton(uiNavigation, "Async audio", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(AsyncAudioUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-orthogonal-tiledmap",
				UiUtils.createButton(uiNavigation, "Orthogonal TiledMap (No Caching)", new ActionListener() {
					@Override
					public void onActionBegin(ActionEvent event) {
					}

					@Override
					public void onActionEnd(ActionEvent event) {
						nextScreenId = ScreenIds.getScreenId(OrthogonalTiledMapNoCachingUAT.class);
					}
				})));
		uatsDialog.add(FlexRow.withElements("row-isometric-tiledmap",
				UiUtils.createButton(uiNavigation, "Isometric TiledMap (No Caching)", new ActionListener() {
					@Override
					public void onActionBegin(ActionEvent event) {
					}

					@Override
					public void onActionEnd(ActionEvent event) {
						nextScreenId = ScreenIds.getScreenId(IsometricTiledMapUAT.class);
					}
				})));
		uatsDialog.add(FlexRow.withElements("row-isometric-tiledmap",
				UiUtils.createButton(uiNavigation, "Hexagonal TiledMap (No Caching)", new ActionListener() {
					@Override
					public void onActionBegin(ActionEvent event) {
					}

					@Override
					public void onActionEnd(ActionEvent event) {
						nextScreenId = ScreenIds.getScreenId(HexagonalTiledMapUAT.class);
					}
				})));
//		uatsDialog.add(
//				FlexRow.withElements("row-particle-effects", UiUtils.createButton(uiNavigation, "Particle Effects", new ActionListener() {
//					@Override
//					public void onActionBegin(ActionEvent event) {
//					}
//
//					@Override
//					public void onActionEnd(ActionEvent event) {
//						nextScreenId = ScreenIds.getScreenId(ParticleEffectsUAT.class);
//					}
//				})));
		uatsDialog.add(FlexRow.withElements("row-controllers", UiUtils.createButton(uiNavigation, "Controllers", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(GamePadUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-ui-flex", UiUtils.createButton(uiNavigation, "Flex UI Layout", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(FlexUiUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-ui-flex", UiUtils.createButton(uiNavigation, "Xml UI Layout", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(XmlUiUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-ui-pixel", UiUtils.createButton(uiNavigation, "Pixel UI Layout", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(PixelUiUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-platform-utils", UiUtils.createButton(uiNavigation, "Platform Utils", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(PlatformUtilsUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-textureatlas-loader", UiUtils.createButton(uiNavigation, "Texture Atlas Loader", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(TextureAtlasLoaderUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-shader", UiUtils.createButton(uiNavigation, "Shader", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(ShaderUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-shape-clipping", UiUtils.createButton(uiNavigation, "Shape Clipping", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(ShapeClippingUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-tiling-drawable", UiUtils.createButton(uiNavigation, "Tiling Drawable", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(TilingDrawableUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-sprite-cache", UiUtils.createButton(uiNavigation, "Sprite Cache", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(SpriteCacheUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-ui-serialization", UiUtils.createButton(uiNavigation, "UI Serialization", new ActionListener() {
			@Override
			public void onActionBegin(ActionEvent event) {
			}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = ScreenIds.getScreenId(UiSerializationUAT.class);
			}
		})));
		uatsDialog.add(FlexRow.withElements("row-utilities", UiUtils.createHeader("Utilities")));
		uatsDialog.add(
				FlexRow.withElements("row-controller-mapping", UiUtils.createButton(uiNavigation, "Controller Mapping", new ActionListener() {
					@Override
					public void onActionBegin(ActionEvent event) {
					}

					@Override
					public void onActionEnd(ActionEvent event) {
						nextScreenId = ScreenIds.getScreenId(GamePadMapping.class);
					}
				})));
		uatsDialog.setVisibility(Visibility.VISIBLE);
		uatsDialog.setNavigation(uiNavigation);

		uiContainer.add(uatsDialog);

		uatsDialog.snapTo(uiContainer, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	}

	@Override
	public void onResize(int width, int height) {
		uiContainer.set(width, height);
	}
	
	@Override
	public void onScreenSizeChanged(ScreenSize screenSize) {
		System.out.println("Current Screen Size: " + screenSize);
	}
}
