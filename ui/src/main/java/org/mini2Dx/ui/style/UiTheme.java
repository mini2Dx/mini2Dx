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
import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ruleset.ButtonStyleRuleset;
import org.mini2Dx.ui.style.ruleset.ContainerStyleRuleset;
import org.mini2Dx.ui.style.ruleset.DefaultStyleRuleset;
import org.mini2Dx.ui.style.ruleset.LabelStyleRuleset;
import org.mini2Dx.ui.style.ruleset.SelectStyleRuleset;
import org.mini2Dx.ui.style.ruleset.TextBoxStyleRuleset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class UiTheme {
	private static final String LOGGING_TAG = UiTheme.class.getSimpleName();
	
	public static final String DEFAULT_THEME_FILENAME = "default-mdx-theme.json";
	public static final String DEFAULT_STYLE_ID = "default";

	@Field
	private String id;
	@Field
	private Map<String, ButtonStyleRuleset> buttons;
	@Field
	private Map<String, DefaultStyleRuleset> columns;
	@Field
	private Map<String, ContainerStyleRuleset> containers;
	@Field
	private Map<String, UiFont> fonts;
	@Field
	private Map<String, DefaultStyleRuleset> images;
	@Field
	private Map<String, LabelStyleRuleset> labels;
	@Field
	private Map<String, SelectStyleRuleset> selects;
	@Field
	private Map<String, TextBoxStyleRuleset> textboxes;

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
		if (!selects.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for selects");
		}
		if (!textboxes.containsKey(DEFAULT_STYLE_ID)) {
			throw new MdxException("No style with id 'default' for textboxes");
		}

		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.validate(this);
		}
		for (StyleRuleset<StyleRule> columnRuleset : columns.values()) {
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
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.validate(this);
		}
		for (StyleRuleset<TextBoxStyleRule> textboxRuleset : textboxes.values()) {
			textboxRuleset.validate(this);
		}
	}

	public void loadDependencies(Array<AssetDescriptor> dependencies) {
		for (String id : buttons.keySet()) {
			StyleRuleset<ButtonStyleRule> buttonRuleset = buttons.get(id);
			buttonRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Button Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : columns.keySet()) {
			StyleRuleset<StyleRule> columnRuleset = columns.get(id);
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
		for (String id : selects.keySet()) {
			StyleRuleset<SelectStyleRule> selectRuleset = selects.get(id);
			selectRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", Select Ruleset: " + id + "] Dependencies loaded");
		}
		for (String id : textboxes.keySet()) {
			StyleRuleset<TextBoxStyleRule> textboxRuleset = textboxes.get(id);
			textboxRuleset.loadDependencies(this, dependencies);
			Gdx.app.log(LOGGING_TAG, "[Theme: " + this.id + ", TextBox Ruleset: " + id + "] Dependencies loaded");
		}
	}

	public void prepareAssets(FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		for (UiFont font : fonts.values()) {
			font.prepareAssets(fileHandleResolver);
		}
		for (StyleRuleset<ButtonStyleRule> buttonRuleset : buttons.values()) {
			buttonRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<StyleRule> columnRuleset : columns.values()) {
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
		for (StyleRuleset<SelectStyleRule> selectRuleset : selects.values()) {
			selectRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
		for (StyleRuleset<TextBoxStyleRule> textboxRuleset : textboxes.values()) {
			textboxRuleset.prepareAssets(this, fileHandleResolver, assetManager);
		}
	}

	public ButtonStyleRule getStyleRule(Button button, ScreenSize screenSize) {
		StyleRuleset<ButtonStyleRule> ruleset = buttons.get(button.getStyleId());
		if (ruleset == null) {
			ruleset = buttons.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public StyleRule getStyleRule(Column column, ScreenSize screenSize) {
		return getStyleRule(column, screenSize, columns);
	}

	public ContainerStyleRule getStyleRule(Container container, ScreenSize screenSize) {
		StyleRuleset<ContainerStyleRule> ruleset = containers.get(container.getStyleId());
		if (ruleset == null) {
			ruleset = containers.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public LabelStyleRule getStyleRule(Label label, ScreenSize screenSize) {
		StyleRuleset<LabelStyleRule> ruleset = labels.get(label.getStyleId());
		if (ruleset == null) {
			ruleset = labels.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public StyleRule getStyleRule(Image image, ScreenSize screenSize) {
		return getStyleRule(image, screenSize, images);
	}

	public SelectStyleRule getStyleRule(Select<?> select, ScreenSize screenSize) {
		StyleRuleset<SelectStyleRule> ruleset = selects.get(select.getStyleId());
		if (ruleset == null) {
			ruleset = selects.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	public TextBoxStyleRule getStyleRule(TextBox textbox, ScreenSize screenSize) {
		StyleRuleset<TextBoxStyleRule> ruleset = textboxes.get(textbox.getStyleId());
		if (ruleset == null) {
			ruleset = textboxes.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}

	private StyleRule getStyleRule(UiElement element, ScreenSize screenSize,
			Map<String, DefaultStyleRuleset> rules) {
		StyleRuleset<?> ruleset = rules.get(element.getStyleId());
		if (ruleset == null) {
			ruleset = rules.get(DEFAULT_STYLE_ID);
		}
		return ruleset.getStyleRule(screenSize);
	}
	
	public UiFont getFont(String id) {
		return fonts.get(id);
	}
	
	public void putButtonStyleRuleset(String rulesetId, ButtonStyleRuleset ruleset) {
		if(buttons == null) {
			buttons = new HashMap<String, ButtonStyleRuleset>();
		}
		buttons.put(rulesetId, ruleset);
	}
	
	public void putColumnStyleRuleset(String rulesetId, DefaultStyleRuleset ruleset) {
		if(columns == null) {
			columns = new HashMap<String, DefaultStyleRuleset>();
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
	
	public void putSelectStyleRuleset(String rulesetId, SelectStyleRuleset ruleset) {
		if(selects == null) {
			selects = new HashMap<String, SelectStyleRuleset>();
		}
		selects.put(rulesetId, ruleset);
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
}
