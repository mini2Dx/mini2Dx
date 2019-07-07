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
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.InputMultiplexer;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATApplication;
import org.mini2Dx.uats.util.UATSelectionScreen;
import org.mini2Dx.uats.util.UiUtils;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.effect.SlideDirection;
import org.mini2Dx.ui.effect.SlideIn;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.gamepad.GamePadUiInput;
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.PixelLayoutUtils;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.navigation.VerticalUiNavigation;
import org.mini2Dx.ui.style.UiTheme;

/**
 * User acceptance test for pixel UI layout
 */
public class PixelUiUAT extends BasicGameScreen implements GameResizeListener {
	private final AssetManager assetManager;
	private final FileHandleResolver fileHandleResolver;

	private UiContainer uiContainer;
	private GamePadUiInput<?> controllerInput;

	private Container topLeftContainer, centerContainer, bottomRightContainer;

	private TabView tabView;
	private Select<String> select;
	private TextBox textBox;
	private Checkbox checkbox;
	private RadioButton radioButton;
	private Slider slider;
	private Label header, textBoxResult, checkboxResult, radioButtonResult, sliderResult;
	private TextButton returnButton;

	private int nextScreenId = -1;

	public PixelUiUAT(AssetManager assetManager, FileHandleResolver fileHandleResolver) {
		super();
		this.assetManager = assetManager;
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
		uiContainer.setNavigationMode(UATApplication.NAVIGATION_MODE);
		gc.addResizeListener(this);

		if(Mdx.input.getGamePads().size > 0) {
			try {
				System.out.println(uiContainer.getId());
				controllerInput = UiUtils.setUpControllerInput(Mdx.input.getGamePads().get(0), uiContainer);
				if(controllerInput != null) {
					controllerInput.disable();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if(Mdx.input.isKeyJustPressed(Input.Keys.B)) {
			centerContainer.setXY(MathUtils.random(0f, uiContainer.getWidth() * 0.5f),
					MathUtils.random(0f, uiContainer.getHeight() * 0.5f));
		}

		uiContainer.update(delta);
		if (nextScreenId > -1) {
			screenManager.enterGameScreen(nextScreenId, new FadeOutTransition(), new FadeInTransition());
			nextScreenId = -1;
		}

		if(Mdx.input.isKeyJustPressed(Input.Keys.R)) {
			centerContainer.setXY(MathUtils.random(0f, uiContainer.getWidth() * 0.5f),
					MathUtils.random(0f, uiContainer.getHeight() * 0.5f));
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.WHITE());
		g.setColor(Colors.BLACK());
		g.drawString("Layout complete: " + PixelLayoutUtils.isOperationsComplete(), 150f, 32f);
		g.drawString("UI Container Dirty Bit: " + centerContainer.isRenderNodeDirty(), 150f, 48f);
		uiContainer.render(g);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(PixelUiUAT.class);
	}

	@Override
	public void onResize(int width, int height) {
		uiContainer.set(width, height);
	}

	@Override
	public void preTransitionIn(Transition transitionIn) {
		nextScreenId = -1;
		if (!UiContainer.isThemeApplied()) {
			UiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
		}
		if(topLeftContainer == null) {
			initialiseUi();
		}
		centerContainer.applyEffect(new SlideIn(SlideDirection.UP, 0.5f));

		final InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(UiUtils.getCustomCursor(fileHandleResolver));
		inputMultiplexer.addProcessor(uiContainer);
		Mdx.input.setInputProcessor(inputMultiplexer);
	}

	private void initialiseUi() {
		topLeftContainer = new Container("top-left-frame");
		topLeftContainer.setStyleId("no-background");

		header = UiUtils.createHeader("UI UAT");
		header.set(10f, 10f, 50f, 50f);
		header.setVisibility(Visibility.HIDDEN);
		//header.setDebugEnabled(true);

		final Div topLeftHeaderDiv = Div.withElements("top-left-header", header);
		topLeftContainer.add(topLeftHeaderDiv);

		topLeftContainer.deferUntilUpdate(new Runnable() {
			@Override
			public void run() {
				header.setVisibility(Visibility.VISIBLE);
			}
		}, 5f);

		Button backRowButton = UiUtils.createButton(null, "", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActionBegin(ActionEvent event) {
				// TODO Auto-generated method stub

			}
		});
		backRowButton.set(0f, 0f, 50f, 20f);

		Div backDiv = Div.withElements("behind-header", backRowButton);
		backDiv.setZIndex(-1);
		topLeftContainer.add(backDiv);

		topLeftContainer.shrinkToContents(true);
		topLeftContainer.snapTo(uiContainer, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
		topLeftContainer.setVisibility(Visibility.VISIBLE);
		uiContainer.add(topLeftContainer);

		VerticalUiNavigation tab1Navigation = new VerticalUiNavigation();
		textBox = UiUtils.createTextBox(tab1Navigation, "textbox", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				textBoxResult.setText(textBox.getValue());
			}

			@Override
			public void onActionBegin(ActionEvent event) {
			}
		});
		textBox.setFlexLayout(null);
		textBox.set(0f, 0f, uiContainer.getWidth() * 0.75f, 25f);

		select = UiUtils.createSelect(tab1Navigation, "select", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				System.out.println("Selected value: " + select.getSelectedOption().getValue());
			}

			@Override
			public void onActionBegin(ActionEvent event) {
			}
		});
		select.setFlexLayout(null);
		select.set(0f, 0f, uiContainer.getWidth() * 0.75f, 25f);

		textBoxResult = UiUtils.createLabel("");
		checkboxResult = UiUtils.createLabel("Radio Button::responsive = false");
		checkbox = UiUtils.createCheckbox(tab1Navigation, "checkbox", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				if(checkbox.isChecked()) {
					radioButton.setResponsive(true);
				} else {
					radioButton.setResponsive(false);
				}
				checkboxResult.setText("Radio Button::responsive = " + checkbox.isChecked());
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});

		radioButtonResult = UiUtils.createLabel("");
		radioButton = UiUtils.createRadioButton(tab1Navigation, "radioButton", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				switch(radioButton.getSelectedOption()) {
				case "FlexRow Layout":
					radioButton.setFlexDirection(FlexDirection.ROW);
					break;
				case "FlexRow-Reverse Layout":
					radioButton.setFlexDirection(FlexDirection.ROW_REVERSE);
					break;
				case "Div Layout":
					radioButton.setFlexDirection(FlexDirection.COLUMN);
					break;
				case "Div-Reverse Layout":
					radioButton.setFlexDirection(FlexDirection.COLUMN_REVERSE);
					break;
				}
				radioButtonResult.setText(radioButton.getSelectedOption());
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		radioButton.addOption("FlexRow Layout");
		radioButton.addOption("FlexRow-Reverse Layout");
		radioButton.addOption("Column Layout");
		radioButton.addOption("Column-Reverse Layout");

		slider = new Slider();
		slider.setVisibility(Visibility.VISIBLE);
		slider.addActionListener(new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				sliderResult.setText(String.valueOf(slider.getValue()));
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		sliderResult = UiUtils.createLabel("0.0");

		returnButton = UiUtils.createButton(tab1Navigation, "Return to UAT Selection Screen", new ActionListener() {

			@Override
			public void onActionBegin(ActionEvent event) {}

			@Override
			public void onActionEnd(ActionEvent event) {
				nextScreenId = UATSelectionScreen.SCREEN_ID;
			}
		});
		returnButton.setFlexLayout(null);
		returnButton.set(0f, 0f, uiContainer.getWidth() * 0.75f, 20f);
		returnButton.shrinkToContents(true);

		select.addOption("Item 1", "1");
		select.addOption("Item 2", "2");
		select.addOption("Item 3", "3");

		centerContainer = new Container("main-centerContainer");
		centerContainer.set(0f, 0f, uiContainer.getWidth(), uiContainer.getHeight() * 0.6f);

		tabView = new TabView("tabView");
		tabView.set(0f, 0f, centerContainer.getContentWidth(), centerContainer.getContentHeight());
		tabView.setVisibility(Visibility.VISIBLE);

		Tab tab1 = new Tab("tab1", "Tab 1");
		final FlexRow textBoxRow = FlexRow.withElements("row-textbox", textBox, textBoxResult);
		final FlexRow selectRow = FlexRow.withElements("row-select", select);
		final FlexRow checkboxRow = FlexRow.withElements("row-checkbox", checkbox, checkboxResult);
		final FlexRow radioButtonRow = FlexRow.withElements("row-radioButton", radioButton, radioButtonResult);
		final FlexRow sliderRow = FlexRow.withElements("row-slider", slider, sliderResult);
		final FlexRow returnRow = FlexRow.withElements("row-return-button", returnButton);

		textBoxResult.alignBelow(textBox, HorizontalAlignment.LEFT);

		selectRow.alignBelow(textBoxRow, HorizontalAlignment.LEFT);
		checkboxRow.alignBelow(selectRow, HorizontalAlignment.LEFT);
		radioButtonRow.alignBelow(checkboxRow, HorizontalAlignment.LEFT);
		sliderRow.alignBelow(radioButtonRow, HorizontalAlignment.LEFT);
		returnRow.alignBelow(sliderRow, HorizontalAlignment.LEFT);

		tab1.add(textBoxRow);
		tab1.add(selectRow);
		tab1.add(checkboxRow);
		tab1.add(radioButtonRow);
		tab1.add(sliderRow);
		tab1.add(returnRow);
		tab1.setNavigation(tab1Navigation);
		tabView.add(tab1);

		Tab tab2 = new Tab("tab2", "Tab 2");

		ProgressBar progressBar = new ProgressBar();
		progressBar.set(0f, 0f, 200f, 20f);
		progressBar.setWidthToContentWidthOf(tab2);
		progressBar.setValue(0.4f);
		progressBar.setVisibility(Visibility.VISIBLE);

		final FlexRow progressBarRow = FlexRow.withElements(progressBar);
		tab2.add(progressBarRow);

		Button reAddElementsButton =  UiUtils.createButton(null, "Re-add tabview", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				centerContainer.remove(tabView);
				centerContainer.add(tabView);
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});

		final FlexRow readdButtonRow = FlexRow.withElements(reAddElementsButton);
		readdButtonRow.alignBelow(progressBarRow, HorizontalAlignment.LEFT);
		tab2.add(readdButtonRow);

		Button slideInButton = UiUtils.createButton(null, "Slide In", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				centerContainer.applyEffect(new SlideIn(SlideDirection.UP, 0.5f));
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});

		final FlexRow slideInRow = FlexRow.withElements(slideInButton);
		slideInRow.alignBelow(readdButtonRow, HorizontalAlignment.LEFT);
		tab2.add(slideInRow);

		final ImageButton imageButton = new ImageButton();
		imageButton.setStyleId("noop");
		imageButton.setFlexLayout("flex-column:xs-12c");
		imageButton.setVisibility(Visibility.VISIBLE);
		imageButton.setAtlas(UiTheme.DEFAULT_THEME_ATLAS);
		imageButton.setNormalTexturePath("button_default_normal");
		imageButton.setHoverTexturePath("button_default_hover");
		imageButton.setActionTexturePath("button_default_action");
		tab2.setHotkey(Input.Keys.W, imageButton);

		final FlexRow imageButtonRow = FlexRow.withElements(imageButton);
		imageButtonRow.alignBelow(slideInRow, HorizontalAlignment.LEFT);
		tab2.add(imageButtonRow);

		tabView.add(tab2);

		Tab tab3 = new Tab("tab3", "Tab 3");
		Button hiddenButton = UiUtils.createButton(null, "Hidden", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		hiddenButton.setVisibility(Visibility.HIDDEN);
		tab3.add(FlexRow.withElements(hiddenButton));

		final ScrollBox scrollBox = new ScrollBox("scrollBox");
		scrollBox.setWidthToContentWidthOf(tab3);
		scrollBox.setVisibility(Visibility.VISIBLE);
		scrollBox.setMaxHeight(150f);

		FlexRow previousRow = null;
		for(int i = 0; i < 30; i++) {
			if(i % 2 == 0) {
				Label label = UiUtils.createLabel("Label " + i);
				FlexRow row = FlexRow.withElements(label);
				if(previousRow != null) {
					row.alignBelow(previousRow, HorizontalAlignment.LEFT);
				}

				row.setVisibility(Visibility.VISIBLE);
				scrollBox.add(row);
				previousRow = row;
			} else {
				Button button = UiUtils.createButton(null, "Test", null);
				FlexRow row = FlexRow.withElements(button);
				if(previousRow != null) {
					row.alignBelow(previousRow, HorizontalAlignment.LEFT);
				}

				row.setVisibility(Visibility.VISIBLE);
				scrollBox.add(row);
				previousRow = row;
			}
		}
		scrollBox.resizeScrollContentHeightToContents();
		final FlexRow scrollBoxRow = FlexRow.withElements(scrollBox);
		tab3.add(scrollBoxRow);

		Button scrollToTopButton = UiUtils.createButton(null, "Scroll to top (immediate)", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				scrollBox.scrollToTop(true);
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		final FlexRow scrollToTopRow = FlexRow.withElements(scrollToTopButton);
		scrollToTopRow.alignBelow(scrollBoxRow, HorizontalAlignment.LEFT);
		tab3.add(scrollToTopRow);

		Button scrollToBottomButton = UiUtils.createButton(null, "Scroll to bottom (immediate)", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				scrollBox.scrollToBottom(true);
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		final FlexRow scrollToBottomRow = FlexRow.withElements(scrollToBottomButton);
		scrollToBottomRow.alignBelow(scrollToTopRow, HorizontalAlignment.LEFT);
		tab3.add(scrollToBottomRow);

		scrollToTopButton = UiUtils.createButton(null, "Scroll to top (smooth)", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				scrollBox.scrollToTop(false);
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		final FlexRow smoothScrollToTopRow = FlexRow.withElements(scrollToTopButton);
		smoothScrollToTopRow.alignBelow(scrollToBottomRow, HorizontalAlignment.LEFT);
		tab3.add(smoothScrollToTopRow);

		scrollToBottomButton = UiUtils.createButton(null, "Scroll to bottom (smooth)", new ActionListener() {

			@Override
			public void onActionEnd(ActionEvent event) {
				scrollBox.scrollToBottom(false);
			}

			@Override
			public void onActionBegin(ActionEvent event) {}
		});
		final FlexRow smoothScrollToBottomRow = FlexRow.withElements(scrollToBottomButton);
		smoothScrollToBottomRow.alignBelow(smoothScrollToTopRow, HorizontalAlignment.LEFT);
		tab3.add(smoothScrollToBottomRow);

		tabView.add(tab3);

		tabView.setNextTabHotkey(Input.Keys.E);
		tabView.setPreviousTabHotkey(Input.Keys.Q);

		centerContainer.add(tabView);
		centerContainer.setVisibility(Visibility.VISIBLE);
		centerContainer.setNavigation(tabView.getNavigation());
		centerContainer.shrinkToContents(true);
		centerContainer.snapTo(uiContainer, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		uiContainer.add(centerContainer);
		uiContainer.setActiveNavigation(tabView);

		bottomRightContainer = new Container("bottom-right-frame");
		bottomRightContainer.set(0f, uiContainer.getHeight() - 25f, uiContainer.getWidth() * 0.33f, 50f);
		bottomRightContainer.setVisibility(Visibility.VISIBLE);
		Div bottomFrameDiv = Div.withElements("row-os", UiUtils.createHeader("OVERFLOW CLIPPED"));
		bottomFrameDiv.setHeight(12f);
		bottomFrameDiv.setOverflowClipped(true);
		bottomRightContainer.add(bottomFrameDiv);
		uiContainer.add(bottomRightContainer);
		bottomRightContainer.snapTo(uiContainer, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
	}
}
