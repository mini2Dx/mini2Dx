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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ruleset.*;

/**
 * Represents a user interface theme applied to a {@link UiContainer}
 */
public class UiTheme {
	private static final String LOGGING_TAG = UiTheme.class.getSimpleName();
	
	public static final String DEFAULT_THEME_FILENAME = "default-mdx-theme.json";
	public static final String DEFAULT_THEME_ATLAS = "default-mdx-theme.atlas";
	public static final String DEFAULT_STYLE_ID = "default";

	@Field
	private String id;
	@Field
	private String atlas;
	@Field
	private ObjectMap<String, ButtonStyleRuleset> buttons;
	@Field
	private ObjectMap<String, CheckboxStyleRuleset> checkboxes;
	@Field
	private ObjectMap<String, ColumnStyleRuleset> columns;
	@Field
	private ObjectMap<String, ContainerStyleRuleset> containers;
	@Field
	private ObjectMap<String, UiFont> fonts;
	@Field
	private ObjectMap<String, DefaultStyleRuleset> images;
	@Field
	private ObjectMap<String, LabelStyleRuleset> labels;
	@Field
	private ObjectMap<String, ProgressBarStyleRuleset> progressBars;
	@Field
	private ObjectMap<String, RadioButtonStyleRuleset> radioButtons;
	@Field
	private ObjectMap<String, SelectStyleRuleset> selects;
	@Field
	private ObjectMap<String, ScrollBoxStyleRuleset> scrollBoxes;
	@Field
	private ObjectMap<String, SliderStyleRuleset> sliders;
	@Field
	private ObjectMap<String, TabStyleRuleset> tabs;
	@Field
	private ObjectMap<String, TextBoxStyleRuleset> textboxes;
	
	private TextureAtlas textureAtlas;
	private boolean headless;

	public void validate() {
		if (!buttons.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for buttons");
		}
		if (!checkboxes.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for checkboxes");
		}
		if (!columns.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for columns");
		}
		if (!containers.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for containers");
		}
		if (!images.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for images");
		}
		if (!labels.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for labels");
		}
		if (!progressBars.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for progressBars");
		}
		if (!selects.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for selects");
		}
		if (!scrollBoxes.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for scrollBoxes");
		}
		if (!sliders.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for sliders");
		}
		if (!tabs.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for tabs");
		}
		if (!textboxes.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for textboxes");
		}

		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.validate(this);
		}
		for (StyleRuleset<CheckboxStyleRule> checkboxRuleset : checkboxes.values()) {
			checkboxRuleset.validate(this);
		}
		for (StyleRuleset<ParentStyleRule> columnRuleset : columns.values()) {
			columnRuleset.validate(this);
		}
		for (StyleRuleset<ContainerStyleRule> containerRuleset : containers.values()) {
			containerRuleset.validate(this);
		}
		for (StyleRuleset<StyleRule> imageRuleset : images.values()) {
			imageRuleset.validate(this);
		}
		for (StyleRuleset<LabelStyleRule> labelRuleset : labels.values()) {
			labelRuleset.validate(this);
		}
		for (StyleRuleset<ProgressBarStyleRule> progressBarRuleset : progressBars.values()) {
			progressBarRuleset.validate(this);
		}
		for (StyleRuleset<RadioButtonStyleRule> radioButtonRuleset : radioButtons.values()) {
			radioButtonRuleset.validate(this);
		}
		for (StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset : scrollBoxes.values()) {
			scrollBoxRuleset.validate(this);
		}
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.validate(this);
		}
		for (StyleRuleset<SliderStyleRule> sliderRuleset : sliders.values()) {
			sliderRuleset.validate(this);
		}
		for (StyleRuleset<TabStyleRule> tabRuleset : tabs.values()) {
			tabRuleset.validate(this);
		}
		for (StyleRuleset<TextBoxStyleRule> textboxRuleset : textboxes.values()) {
			textboxRuleset.validate(this);
		}
	}

	public void loadDependencies(Array<AssetDescriptor> dependencies, FileHandleResolver resolver, boolean headless) {
		this.headless = headless;
		if(!headless) {
			dependencies.add(new AssetDescriptor<TextureAtlas>(atlas, TextureAtlas.class));
		}
		
		Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Atlas: " + atlas + "]");
		for (UiFont font : fonts.values()) {
			font.loadDependencies(this, resolver, dependencies);
		}
		for (String id : buttons.keys()) {
			StyleRuleset<ButtonStyleRule> buttonRuleset = buttons.get(id);
			buttonRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Button Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : checkboxes.keys()) {
			StyleRuleset<CheckboxStyleRule> columnRuleset = checkboxes.get(id);
			columnRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Checkbox Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : columns.keys()) {
			StyleRuleset<ParentStyleRule> columnRuleset = columns.get(id);
			columnRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Div Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : containers.keys()) {
			StyleRuleset<ContainerStyleRule> containerRuleset = containers.get(id);
			containerRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Container Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : images.keys()) {
			StyleRuleset<StyleRule> imageRuleset = images.get(id);
			imageRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Image Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : labels.keys()) {
			StyleRuleset<LabelStyleRule> labelRuleset = labels.get(id);
			labelRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Label Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : progressBars.keys()) {
			StyleRuleset<ProgressBarStyleRule> progressBarRuleset = progressBars.get(id);
			progressBarRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Progress Bar Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : radioButtons.keys()) {
			StyleRuleset<RadioButtonStyleRule> scrollBoxRuleset = radioButtons.get(id);
			scrollBoxRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", RadioButton Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : scrollBoxes.keys()) {
			StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset = scrollBoxes.get(id);
			scrollBoxRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", ScrollBox Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : selects.keys()) {
			StyleRuleset<SelectStyleRule> selectRuleset = selects.get(id);
			selectRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Select Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : sliders.keys()) {
			StyleRuleset<SliderStyleRule> sliderRuleset = sliders.get(id);
			sliderRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Slider Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : tabs.keys()) {
			StyleRuleset<TabStyleRule> tabRuleset = tabs.get(id);
			tabRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", Tab Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : textboxes.keys()) {
			StyleRuleset<TextBoxStyleRule> textboxRuleset = textboxes.get(id);
			textboxRuleset.loadDependencies(this, dependencies);
			Mdx.log.info(LOGGING_TAG, "[Theme: " + this.id + ", TextBox Ruleset: " + id + "] Dependencies loaded");
		}
	}

	public void prepareAssets(FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if(!headless) {
			textureAtlas = assetManager.get(atlas, TextureAtlas.class);
		}
		
		for (UiFont font : fonts.values()) {
			font.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<CheckboxStyleRule> checkboxRuleset : checkboxes.values()) {
			checkboxRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<ParentStyleRule> columnRuleset : columns.values()) {
			columnRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<ContainerStyleRule> containerRuleset : containers.values()) {
			containerRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<StyleRule> imageRuleset : images.values()) {
			imageRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<LabelStyleRule> labelRuleset : labels.values()) {
			labelRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<ProgressBarStyleRule> progressBarRuleset : progressBars.values()) {
			progressBarRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<RadioButtonStyleRule> radioButtonRuleset : radioButtons.values()) {
			radioButtonRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset : scrollBoxes.values()) {
			scrollBoxRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<SliderStyleRule> sliderRuleset : sliders.values()) {
			sliderRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<TabStyleRule> tabRuleset : tabs.values()) {
			tabRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<TextBoxStyleRule> textboxRuleset : textboxes.values()) {
			textboxRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
	}
	
	public boolean containsButtonStyleRuleset(String id) {
		return buttons.containsKey(id);
	}
	
	public boolean containsCheckboxStyleRuleset(String id) {
		return checkboxes.containsKey(id);
	}
	
	public boolean containsColumnStyleRuleset(String id) {
		return columns.containsKey(id);
	}
	
	public boolean containsImageStyleRuleset(String id) {
		return images.containsKey(id);
	}
	
	public boolean containsLabelStyleRuleset(String id) {
		return labels.containsKey(id);
	}
	
	public StyleRule getStyleRule(AnimatedImage image, ScreenSize screenSize) {
		return getStyleRule(image, screenSize, images);
	}

	public ButtonStyleRule getStyleRule(Button button, ScreenSize screenSize) {
		return getButtonStyleRule(button.getStyleId(), screenSize);
	}
	
	public CheckboxStyleRule getStyleRule(Checkbox checkbox, ScreenSize screenSize) {
		return getCheckboxStyleRule(checkbox.getStyleId(), screenSize);
	}

	public ParentStyleRule getStyleRule(Div div, ScreenSize screenSize) {
		return getColumnStyleRule(div.getStyleId(), screenSize);
	}

	public ContainerStyleRule getStyleRule(Container container, ScreenSize screenSize) {
		return getContainerStyleRule(container.getStyleId(), screenSize);
	}

	public LabelStyleRule getStyleRule(Label label, ScreenSize screenSize) {
		return getLabelStyleRule(label.getStyleId(), screenSize);
	}

	public StyleRule getStyleRule(Image image, ScreenSize screenSize) {
		return getStyleRule(image, screenSize, images);
	}
	
	public ProgressBarStyleRule getStyleRule(ProgressBar progressBar, ScreenSize screenSize) {
		return getProgressBarStyleRule(progressBar.getStyleId(), screenSize);
	}
	
	public RadioButtonStyleRule getStyleRule(RadioButton radioButton, ScreenSize screenSize) {
		return getRadioButtonStyleRule(radioButton.getStyleId(), screenSize);
	}
	
	public ScrollBoxStyleRule getStyleRule(ScrollBox scrollBox, ScreenSize screenSize) {
		return getScrollBoxStyleRule(scrollBox.getStyleId(), screenSize);
	}

	public SelectStyleRule getStyleRule(Select<?> select, ScreenSize screenSize) {
		return getSelectStyleRule(select.getStyleId(), screenSize);
	}
	
	public SliderStyleRule getStyleRule(Slider slider, ScreenSize screenSize) {
		return getSliderStyleRule(slider.getStyleId(), screenSize);
	}
	
	public TabStyleRule getStyleRule(TabView tabView, ScreenSize screenSize) {
		return getTabStyleRule(tabView.getStyleId(), screenSize);
	}

	public TextBoxStyleRule getStyleRule(TextBox textbox, ScreenSize screenSize) {
		return getTextBoxStyleRule(textbox.getStyleId(), screenSize);
	}

	private StyleRule getStyleRule(UiElement element, ScreenSize screenSize,
			ObjectMap<String, DefaultStyleRuleset> rules) {
		StyleRuleset<?> ruleset = rules.get(element.getStyleId());
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + element.getStyleId());
			ruleset = rules.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public UiFont getFont(String id) {
		return fonts.get(id);
	}
	
	public ButtonStyleRule getButtonStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ButtonStyleRule> ruleset = buttons.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = buttons.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public CheckboxStyleRule getCheckboxStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<CheckboxStyleRule> ruleset = checkboxes.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = checkboxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ParentStyleRule getColumnStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ParentStyleRule> ruleset = columns.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = columns.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ContainerStyleRule getContainerStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ContainerStyleRule> ruleset = containers.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = containers.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public StyleRule getImageStyleRule(String styleId, ScreenSize screenSize) {
		DefaultStyleRuleset ruleset = images.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = images.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public LabelStyleRule getLabelStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<LabelStyleRule> ruleset = labels.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = labels.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ProgressBarStyleRule getProgressBarStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ProgressBarStyleRule> ruleset = progressBars.get(styleId);
		if(ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = progressBars.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public RadioButtonStyleRule getRadioButtonStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<RadioButtonStyleRule> ruleset = radioButtons.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = radioButtons.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ScrollBoxStyleRule getScrollBoxStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ScrollBoxStyleRule> ruleset = scrollBoxes.get(styleId);
		if(ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = scrollBoxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public SelectStyleRule getSelectStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<SelectStyleRule> ruleset = selects.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = selects.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public SliderStyleRule getSliderStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<SliderStyleRule> ruleset = sliders.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = sliders.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public TabStyleRule getTabStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<TabStyleRule> ruleset = tabs.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = tabs.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public TextBoxStyleRule getTextBoxStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<TextBoxStyleRule> ruleset = textboxes.get(styleId);
		if (ruleset == null) {
			Mdx.log.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = textboxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public void putButtonStyleRuleset(String rulesetId, ButtonStyleRuleset ruleset) {
		if(buttons == null) {
			buttons = new ObjectMap<String, ButtonStyleRuleset>();
		}
		buttons.put(rulesetId, ruleset);
	}
	
	public void putColumnStyleRuleset(String rulesetId, ColumnStyleRuleset ruleset) {
		if(columns == null) {
			columns = new ObjectMap<String, ColumnStyleRuleset>();
		}
		columns.put(rulesetId, ruleset);
	}
	
	public void putContainerStyleRuleset(String rulesetId, ContainerStyleRuleset ruleset) {
		if(containers == null) {
			containers = new ObjectMap<String, ContainerStyleRuleset>();
		}
		containers.put(rulesetId, ruleset);
	}
	
	public void putImageStyleRuleset(String rulesetId,DefaultStyleRuleset ruleset) {
		if(images == null) {
			images = new ObjectMap<String, DefaultStyleRuleset>();
		}
		images.put(rulesetId, ruleset);
	}
	
	public void putLabelStyleRuleset(String rulesetId, LabelStyleRuleset ruleset) {
		if(labels == null) {
			labels = new ObjectMap<String, LabelStyleRuleset>();
		}
		labels.put(rulesetId, ruleset);
	}
	
	public void putProgressBarStyleRuleset(String rulesetId, ProgressBarStyleRuleset ruleset) {
		if(progressBars == null) {
			progressBars = new ObjectMap<String, ProgressBarStyleRuleset>();
		}
		progressBars.put(rulesetId, ruleset);
	}
	
	public void putScrollBoxStyleRuleset(String rulesetId, ScrollBoxStyleRuleset ruleset) {
		if(scrollBoxes == null) {
			scrollBoxes = new ObjectMap<String, ScrollBoxStyleRuleset>();
		}
		scrollBoxes.put(rulesetId, ruleset);
	}
	
	public void putSelectStyleRuleset(String rulesetId, SelectStyleRuleset ruleset) {
		if(selects == null) {
			selects = new ObjectMap<String, SelectStyleRuleset>();
		}
		selects.put(rulesetId, ruleset);
	}
	
	public void putTabStyleRuleset(String rulesetId, TabStyleRuleset ruleset) {
		if(tabs == null) {
			tabs = new ObjectMap<String, TabStyleRuleset>();
		}
		tabs.put(rulesetId, ruleset);
	}
	
	public void putTextBoxStyleRuleset(String rulesetId, TextBoxStyleRuleset ruleset) {
		if(textboxes == null) {
			textboxes = new ObjectMap<String, TextBoxStyleRuleset>();
		}
		textboxes.put(rulesetId, ruleset);
	}
	
	public void putFont(String id, String path) {
		if(fonts == null) {
			fonts = new ObjectMap<String, UiFont>();
		}
		UiFont font = new UiFont();
		font.setPath(path);
		fonts.put(id, font);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public boolean isHeadless() {
		return headless;
	}

	public ObjectMap<String, ButtonStyleRuleset> getButtonRulesets() {
		return buttons;
	}

	public ObjectMap<String, CheckboxStyleRuleset> getCheckboxRulesets() {
		return checkboxes;
	}

	public ObjectMap<String, ColumnStyleRuleset> getColumnRulesets() {
		return columns;
	}

	public ObjectMap<String, ContainerStyleRuleset> getContainerRulesets() {
		return containers;
	}

	public ObjectMap<String, UiFont> getFonts() {
		return fonts;
	}

	public ObjectMap<String, DefaultStyleRuleset> getImageRulesets() {
		return images;
	}

	public ObjectMap<String, LabelStyleRuleset> getLabelRulesets() {
		return labels;
	}

	public ObjectMap<String, ProgressBarStyleRuleset> getProgressBarRulesets() {
		return progressBars;
	}

	public ObjectMap<String, RadioButtonStyleRuleset> getRadioButtonRulesets() {
		return radioButtons;
	}

	public ObjectMap<String, SelectStyleRuleset> getSelectRulesets() {
		return selects;
	}

	public ObjectMap<String, ScrollBoxStyleRuleset> getScrollBoxRulesets() {
		return scrollBoxes;
	}

	public ObjectMap<String, SliderStyleRuleset> getSliderRulesets() {
		return sliders;
	}

	public ObjectMap<String, TabStyleRuleset> getTabRulesets() {
		return tabs;
	}

	public ObjectMap<String, TextBoxStyleRuleset> getTextBoxRulesets() {
		return textboxes;
	}
}
