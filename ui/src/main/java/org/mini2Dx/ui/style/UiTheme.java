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
package org.mini2Dx.ui.style;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ruleset.ButtonStyleRuleset;
import org.mini2Dx.ui.style.ruleset.ColumnStyleRuleset;
import org.mini2Dx.ui.style.ruleset.ContainerStyleRuleset;
import org.mini2Dx.ui.style.ruleset.DefaultStyleRuleset;
import org.mini2Dx.ui.style.ruleset.LabelStyleRuleset;
import org.mini2Dx.ui.style.ruleset.ProgressBarStyleRuleset;
import org.mini2Dx.ui.style.ruleset.ScrollBoxStyleRuleset;
import org.mini2Dx.ui.style.ruleset.SelectStyleRuleset;
import org.mini2Dx.ui.style.ruleset.TabStyleRuleset;
import org.mini2Dx.ui.style.ruleset.TextBoxStyleRuleset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Represents a user interface theme applied to a {@link UiContainer}
 */
public class UiTheme {
	private static final String LOGGING_TAG = UiTheme.class.getSimpleName();
	
	public static final String DEFAULT_THEME_FILENAME = "default-mdx-theme.json";
	public static final String DEFAULT_STYLE_ID = "default";

	@Field
	private String id;
	@Field
	private String atlas;
	@Field
	private Map<String, ButtonStyleRuleset> buttons;
	@Field
	private Map<String, ColumnStyleRuleset> columns;
	@Field
	private Map<String, ContainerStyleRuleset> containers;
	@Field
	private Map<String, UiFont> fonts;
	@Field
	private Map<String, DefaultStyleRuleset> images;
	@Field
	private Map<String, LabelStyleRuleset> labels;
	@Field
	private Map<String, ProgressBarStyleRuleset> progressBars;
	@Field
	private Map<String, SelectStyleRuleset> selects;
	@Field
	private Map<String, ScrollBoxStyleRuleset> scrollBoxes;
	@Field
	private Map<String, TabStyleRuleset> tabs;
	@Field
	private Map<String, TextBoxStyleRuleset> textboxes;
	
	private TextureAtlas textureAtlas;
	private boolean headless;

	public void validate() {
		if (!buttons.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for buttons");
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
		if (!tabs.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for tabs");
		}
		if (!textboxes.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for textboxes");
		}

		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.validate(this);
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
		for (StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset : scrollBoxes.values()) {
			scrollBoxRuleset.validate(this);
		}
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.validate(this);
		}
		for (StyleRuleset<TabStyleRule> tabRuleset : tabs.values()) {
			tabRuleset.validate(this);
		}
		for (StyleRuleset<TextBoxStyleRule> textboxRuleset : textboxes.values()) {
			textboxRuleset.validate(this);
		}
	}

	public void loadDependencies(Array<AssetDescriptor> dependencies, boolean headless) {
		this.headless = headless;
		if(!headless) {
			dependencies.add(new AssetDescriptor<TextureAtlas>(atlas, TextureAtlas.class));
		}
		
		Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Atlas: " + atlas + "]");
		for (String id : buttons.keySet()) {
			StyleRuleset<ButtonStyleRule> buttonRuleset = buttons.get(id);
			buttonRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Button Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : columns.keySet()) {
			StyleRuleset<ParentStyleRule> columnRuleset = columns.get(id);
			columnRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Column Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : containers.keySet()) {
			StyleRuleset<ContainerStyleRule> containerRuleset = containers.get(id);
			containerRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Container Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : images.keySet()) {
			StyleRuleset<StyleRule> imageRuleset = images.get(id);
			imageRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Image Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : labels.keySet()) {
			StyleRuleset<LabelStyleRule> labelRuleset = labels.get(id);
			labelRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Label Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : progressBars.keySet()) {
			StyleRuleset<ProgressBarStyleRule> progressBarRuleset = progressBars.get(id);
			progressBarRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Progress Bar Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : scrollBoxes.keySet()) {
			StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset = scrollBoxes.get(id);
			scrollBoxRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", ScrollBox Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : selects.keySet()) {
			StyleRuleset<SelectStyleRule> selectRuleset = selects.get(id);
			selectRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Select Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : tabs.keySet()) {
			StyleRuleset<TabStyleRule> tabRuleset = tabs.get(id);
			tabRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Tab Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : textboxes.keySet()) {
			StyleRuleset<TextBoxStyleRule> textboxRuleset = textboxes.get(id);
			textboxRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", TextBox Ruleset: " + id + "] Dependencies loaded");
		}
	}

	public void prepareAssets(FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if(!headless) {
			textureAtlas = assetManager.get(atlas, TextureAtlas.class);
		}
		
		for (UiFont font : fonts.values()) {
			font.prepareAssets(this, fileHandleResolver);
		}
		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.prepareAssets(this, fileHandleResolver, assetManager);
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
		for (StyleRuleset<ScrollBoxStyleRule> scrollBoxRuleset : scrollBoxes.values()) {
			scrollBoxRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.prepareAssets(this, fileHandleResolver, assetManager);
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
	
	public boolean containsColumnStyleRuleset(String id) {
		return columns.containsKey(id);
	}
	
	public boolean containsImageStyleRuleset(String id) {
		return images.containsKey(id);
	}
	
	public boolean containsLabelStyleRuleset(String id) {
		return labels.containsKey(id);
	}

	public ButtonStyleRule getStyleRule(Button button, ScreenSize screenSize) {
		return getButtonStyleRule(button.getStyleId(), screenSize);
	}

	public ParentStyleRule getStyleRule(Column column, ScreenSize screenSize) {
		return getColumnStyleRule(column.getStyleId(), screenSize);
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
	
	public ScrollBoxStyleRule getStyleRule(ScrollBox scrollBox, ScreenSize screenSize) {
		return getScrollBoxStyleRule(scrollBox.getStyleId(), screenSize);
	}

	public SelectStyleRule getStyleRule(Select<?> select, ScreenSize screenSize) {
		return getSelectStyleRule(select.getStyleId(), screenSize);
	}
	
	public TabStyleRule getStyleRule(TabView tabView, ScreenSize screenSize) {
		return getTabStyleRule(tabView.getStyleId(), screenSize);
	}

	public TextBoxStyleRule getStyleRule(TextBox textbox, ScreenSize screenSize) {
		return getTextBoxStyleRule(textbox.getStyleId(), screenSize);
	}

	private StyleRule getStyleRule(UiElement element, ScreenSize screenSize,
			Map<String, DefaultStyleRuleset> rules) {
		StyleRuleset<?> ruleset = rules.get(element.getStyleId());
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + element.getStyleId());
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
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = buttons.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ParentStyleRule getColumnStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ParentStyleRule> ruleset = columns.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = columns.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ContainerStyleRule getContainerStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ContainerStyleRule> ruleset = containers.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = containers.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public LabelStyleRule getLabelStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<LabelStyleRule> ruleset = labels.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = labels.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ProgressBarStyleRule getProgressBarStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ProgressBarStyleRule> ruleset = progressBars.get(styleId);
		if(ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = progressBars.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public ScrollBoxStyleRule getScrollBoxStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<ScrollBoxStyleRule> ruleset = scrollBoxes.get(styleId);
		if(ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = scrollBoxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public SelectStyleRule getSelectStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<SelectStyleRule> ruleset = selects.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = selects.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public TabStyleRule getTabStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<TabStyleRule> ruleset = tabs.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = tabs.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public TextBoxStyleRule getTextBoxStyleRule(String styleId, ScreenSize screenSize) {
		StyleRuleset<TextBoxStyleRule> ruleset = textboxes.get(styleId);
		if (ruleset == null) {
			Gdx.app.error(LOGGING_TAG, "No style found with ID " + styleId);
			ruleset = textboxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public void putButtonStyleRuleset(String rulesetId, ButtonStyleRuleset ruleset) {
		if(buttons == null) {
			buttons = new HashMap<String, ButtonStyleRuleset>();
		}
		buttons.put(rulesetId, ruleset);
	}
	
	public void putColumnStyleRuleset(String rulesetId, ColumnStyleRuleset ruleset) {
		if(columns == null) {
			columns = new HashMap<String, ColumnStyleRuleset>();
		}
		columns.put(rulesetId, ruleset);
	}
	
	public void putContainerStyleRuleset(String rulesetId, ContainerStyleRuleset ruleset) {
		if(containers == null) {
			containers = new HashMap<String, ContainerStyleRuleset>();
		}
		containers.put(rulesetId, ruleset);
	}
	
	public void putImageStyleRuleset(String rulesetId,DefaultStyleRuleset ruleset) {
		if(images == null) {
			images = new HashMap<String, DefaultStyleRuleset>();
		}
		images.put(rulesetId, ruleset);
	}
	
	public void putLabelStyleRuleset(String rulesetId, LabelStyleRuleset ruleset) {
		if(labels == null) {
			labels = new HashMap<String, LabelStyleRuleset>();
		}
		labels.put(rulesetId, ruleset);
	}
	
	public void putProgressBarStyleRuleset(String rulesetId, ProgressBarStyleRuleset ruleset) {
		if(progressBars == null) {
			progressBars = new HashMap<String, ProgressBarStyleRuleset>();
		}
		progressBars.put(rulesetId, ruleset);
	}
	
	public void putScrollBoxStyleRuleset(String rulesetId, ScrollBoxStyleRuleset ruleset) {
		if(scrollBoxes == null) {
			scrollBoxes = new HashMap<String, ScrollBoxStyleRuleset>();
		}
		scrollBoxes.put(rulesetId, ruleset);
	}
	
	public void putSelectStyleRuleset(String rulesetId, SelectStyleRuleset ruleset) {
		if(selects == null) {
			selects = new HashMap<String, SelectStyleRuleset>();
		}
		selects.put(rulesetId, ruleset);
	}
	
	public void putTabStyleRuleset(String rulesetId, TabStyleRuleset ruleset) {
		if(tabs == null) {
			tabs = new HashMap<String, TabStyleRuleset>();
		}
		tabs.put(rulesetId, ruleset);
	}
	
	public void putTextBoxStyleRuleset(String rulesetId, TextBoxStyleRuleset ruleset) {
		if(textboxes == null) {
			textboxes = new HashMap<String, TextBoxStyleRuleset>();
		}
		textboxes.put(rulesetId, ruleset);
	}
	
	public void putFont(String id, String path) {
		if(fonts == null) {
			fonts = new HashMap<String, UiFont>();
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
}
