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
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.executor.AsyncFuture;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.InputMultiplexer;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATApplication;
import org.mini2Dx.uats.util.UiUtils;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.element.Slider;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.gamepad.GamePadUiInput;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.ui.xml.spi.CorePopulator;
import org.mini2Dx.ui.xml.spi.ParentUiElementPopulator;

import static org.mini2Dx.ui.element.Visibility.VISIBLE;
import static org.mini2Dx.ui.layout.HorizontalAlignment.CENTER;

/**
 * User acceptance test for the mini2Dx UI xml
 */
public class XmlUiUAT extends BasicGameScreen implements GameResizeListener {
    private final AssetManager assetManager;
    private final FileHandleResolver fileHandleResolver;

    private UiContainer uiContainer;
    private GamePadUiInput<?> controllerInput;
    private UiXmlLoader uiXmlLoader;

    private int nextScreenId = -1;

    public XmlUiUAT(AssetManager assetManager, FileHandleResolver fileHandleResolver) {
        super();
        this.assetManager = assetManager;
        this.fileHandleResolver = fileHandleResolver;
        this.uiXmlLoader = new UiXmlLoader(fileHandleResolver);

        this.uiXmlLoader.addTagHandler("custom:player-label", this::newPlayerLabel,
                new CorePopulator(), new ParentUiElementPopulator(), this::playerLabelPopulator);
    }

    @Override
    public void initialise(GameContainer gc) {
        assetManager.load("texture-region-uat.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        uiContainer = new UiContainer(gc, assetManager);
        uiContainer.setNavigationMode(UATApplication.NAVIGATION_MODE);
        gc.addResizeListener(this);

        uiContainer.add(uiXmlLoader.load("example.ui.xml"));

        final int maxAge = 200;
        final Label age = (Label) uiContainer.getElementById("age");
        final Slider ageModifier = (Slider) uiContainer.getElementById("ageModifier");

        ageModifier.setValue(0.25f);
        age.setText(String.valueOf((int) (maxAge * ageModifier.getValue())));

        ageModifier.addActionListener(new ActionListener() {
            @Override
            public void onActionBegin(ActionEvent event) {
            }

            @Override
            public void onActionEnd(ActionEvent event) {
                age.setText(String.valueOf((int) (maxAge * ageModifier.getValue())));
            }
        });


        final ProgressBar createProgress = (ProgressBar) uiContainer.getElementById("create-character-progress");
        final TextButton createCharacter = (TextButton) uiContainer.getElementById("create-character");
        createCharacter.addActionListener(new ActionListener() {
            @Override
            public void onActionBegin(ActionEvent event) {

            }

            @Override
            public void onActionEnd(ActionEvent event) {
                createCharacter.setEnabled(false);
                createProgress.setVisibility(VISIBLE);

                AsyncFuture future = Mdx.executor.submit(() -> {
                    for (int i = 0; i < 100; i += 10) {
                        createProgress.setValue(createProgress.getValue() + 0.1f);
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {

                        }
                    }

                    createCharacter.setEnabled(true);
                    createProgress.setVisibility(Visibility.HIDDEN);
                    createProgress.setValue(0);
                });


            }
        });

        if (Mdx.input.getGamePads().size > 0) {
            try {
                System.out.println(uiContainer.getId());
                controllerInput = UiUtils.setUpControllerInput(Mdx.input.getGamePads().get(0), uiContainer);
                if (controllerInput != null) {
                    controllerInput.disable();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResize(int width, int height) {
        uiContainer.set(width, height);
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
    public void render(GameContainer gc, Graphics g) {
        g.setBackgroundColor(Colors.WHITE());
        g.setColor(Colors.BLACK());

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
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
        if (controllerInput != null) {
            controllerInput.enable();
        }
    }

    @Override
    public void preTransitionOut(Transition transitionOut) {
        if (controllerInput != null) {
            controllerInput.disable();
        }
    }

    @Override
    public int getId() {
        return ScreenIds.getScreenId(XmlUiUAT.class);
    }


    private UiElement newPlayerLabel(XmlReader.Element element) {
        return new PlayerLabel();
    }

    private boolean playerLabelPopulator(XmlReader.Element element, UiElement uiElement) {
        PlayerLabel playerLabel = (PlayerLabel) uiElement;
        playerLabel.avatar.setTexturePath(element.getAttribute("texture-path"));
        playerLabel.avatar.setAtlas(element.getAttribute("atlas", null));
        playerLabel.name.setText(element.getAttribute("name"));
        return false;
    }

    private static class PlayerLabel extends Container {
        private final Label name;
        private final Image avatar;

        public PlayerLabel() {
            name = new Label();
            name.setVisibility(VISIBLE);
            name.setHorizontalAlignment(CENTER);

            avatar = new Image();
            avatar.setVisibility(VISIBLE);

            add(avatar);
            add(name);
            setFlexLayout("flex-row:xs-2c");
        }
    }
}
