/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.element.AlignedContainer;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.Slider;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.CheckboxStyleRule;
import org.mini2Dx.ui.style.ContainerStyleRule;
import org.mini2Dx.ui.style.LabelStyleRule;
import org.mini2Dx.ui.style.ParentStyleRule;
import org.mini2Dx.ui.style.ProgressBarStyleRule;
import org.mini2Dx.ui.style.RadioButtonStyleRule;
import org.mini2Dx.ui.style.ScrollBoxStyleRule;
import org.mini2Dx.ui.style.SelectStyleRule;
import org.mini2Dx.ui.style.SliderStyleRule;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.TabStyleRule;
import org.mini2Dx.ui.style.TextBoxStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public class UiEditorThemeView extends AlignedContainer implements ActionListener {
	private static final int HEADER_HEIGHT = 40;
	private final UiEditor uiEditor;
	private final UiTheme inGameUiTheme;

	private ScrollBox elementTypeScollBox, rulesetsScrollBox;
	private TextButton returnButton;
	private final List<TextButton> elementTypes = new ArrayList<TextButton>();
	private final List<TextButton> rulesets = new ArrayList<TextButton>();
	private StyleRulesetEditor<?, ?> rulesetEditor;

	private TextButton currentElementType = null;
	private TextButton currentRuleset = null;

	public UiEditorThemeView(UiEditor uiEditor, UiTheme inGameUiTheme) {
		super("uiEditorThemeView");
		this.uiEditor = uiEditor;
		this.inGameUiTheme = inGameUiTheme;

		setStyleId("no-background-no-padding");
		setUpUserInterface();
	}

	public void onResize(int width, int height) {
		elementTypeScollBox.setMinHeight(height - HEADER_HEIGHT - 1);
		elementTypeScollBox.setMaxHeight(height - HEADER_HEIGHT);
		rulesetsScrollBox.setMinHeight(height - HEADER_HEIGHT - 1);
		rulesetsScrollBox.setMaxHeight(height - HEADER_HEIGHT);
		
		if(rulesetEditor != null) {
			rulesetEditor.onResize(width, height);
		}
	}

	@Override
	public void onActionBegin(ActionEvent event) {
	}

	@Override
	public void onActionEnd(ActionEvent event) {
		if(event.getSource().getId().equals(returnButton.getId())) {
			uiEditor.goToInitialModal();
			return;
		}
		
		for (int i = 0; i < elementTypes.size(); i++) {
			TextButton button = elementTypes.get(i);
			if (!button.getId().equals(event.getSource().getId())) {
				continue;
			}
			if (currentElementType != null) {
				currentElementType.setEnabled(true);
			}
			currentElementType = button;
			currentElementType.setEnabled(false);
			listRulesets();
			return;
		}

		for (int i = 0; i < rulesets.size(); i++) {
			TextButton button = rulesets.get(i);
			if (!button.getId().equals(event.getSource().getId())) {
				continue;
			}
			if (currentRuleset != null) {
				currentRuleset.setEnabled(true);
			}
			currentRuleset = button;
			currentRuleset.setEnabled(false);
			try {
				initialiseEditor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private void initialiseEditor() throws Exception {
		if (rulesetEditor != null) {
			remove(rulesetEditor);
		}

		switch (currentElementType.getText()) {
		case ELEMENT_TYPE_BUTTON: {
			rulesetEditor = new StyleRulesetEditor<ButtonStyleRule, TextButton>(this, ButtonStyleRule.class,
					TextButton.class, currentRuleset.getText(),
					inGameUiTheme.getButtonRulesets().get(currentRuleset.getText()));
			TextButton element = new TextButton(rulesetEditor.getId() + "-element");
			element.setText("Example");
			element.setVisibility(Visibility.VISIBLE);
			rulesetEditor.setElement(element);
			break;
		}
		case ELEMENT_TYPE_CHECKBOX: {
			rulesetEditor = new StyleRulesetEditor<CheckboxStyleRule, Checkbox>(this, CheckboxStyleRule.class,
					Checkbox.class, currentRuleset.getText(),
					inGameUiTheme.getCheckboxRulesets().get(currentRuleset.getText()));
			Checkbox element = new Checkbox(rulesetEditor.getId() + "-element");
			element.setVisibility(Visibility.VISIBLE);
			rulesetEditor.setElement(element);
			break;
		}
		case ELEMENT_TYPE_COLUMN: {
			rulesetEditor = new StyleRulesetEditor<ParentStyleRule, Column>(this, ParentStyleRule.class, Column.class,
					currentRuleset.getText(), inGameUiTheme.getColumnRulesets().get(currentRuleset.getText()));
			Label label = new Label();
			label.setVisibility(Visibility.VISIBLE);
			label.setText("Example content");
			label.setResponsive(true);
			rulesetEditor.setElement(Column.withElements(label));
			break;
		}
		case ELEMENT_TYPE_CONTAINER:
			rulesetEditor = new StyleRulesetEditor<ContainerStyleRule, Container>(this, ContainerStyleRule.class,
					Container.class, currentRuleset.getText(),
					inGameUiTheme.getContainerRulesets().get(currentRuleset.getText()));
			AlignedContainer alignedContainer = new AlignedContainer();
			alignedContainer.setVisibility(Visibility.VISIBLE);
			generateExampleContentB(alignedContainer);
			rulesetEditor.setElement(alignedContainer);
			break;
		case ELEMENT_TYPE_IMAGE:
			rulesetEditor = new StyleRulesetEditor<StyleRule, Image>(this, StyleRule.class, Image.class,
					currentRuleset.getText(), inGameUiTheme.getImageRulesets().get(currentRuleset.getText()));
			Image image = new Image();
			image.setVisibility(Visibility.VISIBLE);
			image.setAtlas(UiTheme.DEFAULT_THEME_ATLAS);
			image.setTexturePath("button_danger_action");
			rulesetEditor.setElement(image);
			break;
		case ELEMENT_TYPE_LABEL:
			rulesetEditor = new StyleRulesetEditor<LabelStyleRule, Label>(this, LabelStyleRule.class, Label.class,
					currentRuleset.getText(), inGameUiTheme.getLabelRulesets().get(currentRuleset.getText()));
			Label label = new Label();
			label.setVisibility(Visibility.VISIBLE);
			label.setText("Example Label");
			label.setResponsive(true);
			rulesetEditor.setElement(label);
			break;
		case ELEMENT_TYPE_PROGRESS_BAR:
			rulesetEditor = new StyleRulesetEditor<ProgressBarStyleRule, ProgressBar>(this, ProgressBarStyleRule.class,
					ProgressBar.class, currentRuleset.getText(),
					inGameUiTheme.getProgressBarRulesets().get(currentRuleset.getText()));
			ProgressBar progressBar = new ProgressBar();
			progressBar.setVisibility(Visibility.VISIBLE);
			progressBar.setValue(0.5f);
			rulesetEditor.setElement(progressBar);
			break;
		case ELEMENT_TYPE_RADIO_BUTTON:
			rulesetEditor = new StyleRulesetEditor<RadioButtonStyleRule, RadioButton>(this, RadioButtonStyleRule.class,
					RadioButton.class, currentRuleset.getText(),
					inGameUiTheme.getRadioButtonRulesets().get(currentRuleset.getText()));
			RadioButton radioButton = new RadioButton();
			radioButton.setVisibility(Visibility.VISIBLE);
			radioButton.addOption("Option 1");
			radioButton.addOption("Option 2");
			radioButton.addOption("Option 3");
			rulesetEditor.setElement(radioButton);
			break;
		case ELEMENT_TYPE_SELECT:
			rulesetEditor = new StyleRulesetEditor<SelectStyleRule, Select>(this, SelectStyleRule.class, Select.class,
					currentRuleset.getText(), inGameUiTheme.getSelectRulesets().get(currentRuleset.getText()));
			Select<String> select = new Select<>();
			select.setVisibility(Visibility.VISIBLE);
			select.addOption("Option 1", "Value 1");
			select.addOption("Option 2", "Value 2");
			select.addOption("Option 3", "Value 3");
			rulesetEditor.setElement(select);
			break;
		case ELEMENT_TYPE_SCROLLBOX:
			rulesetEditor = new StyleRulesetEditor<ScrollBoxStyleRule, ScrollBox>(this, ScrollBoxStyleRule.class,
					ScrollBox.class, currentRuleset.getText(),
					inGameUiTheme.getScrollBoxRulesets().get(currentRuleset.getText()));
			ScrollBox scrollBox = new ScrollBox();
			scrollBox.setVisibility(Visibility.VISIBLE);
			scrollBox.setMinHeight(49);
			scrollBox.setMaxHeight(50);
			generateExampleContentA(scrollBox);
			rulesetEditor.setElement(scrollBox);
			break;
		case ELEMENT_TYPE_SLIDER:
			rulesetEditor = new StyleRulesetEditor<SliderStyleRule, Slider>(this, SliderStyleRule.class, Slider.class,
					currentRuleset.getText(), inGameUiTheme.getSliderRulesets().get(currentRuleset.getText()));
			Slider slider = new Slider();
			slider.setVisibility(Visibility.VISIBLE);
			slider.setValue(0.5f);
			rulesetEditor.setElement(slider);
			break;
		case ELEMENT_TYPE_TAB:
			rulesetEditor = new StyleRulesetEditor<TabStyleRule, TabView>(this, TabStyleRule.class, TabView.class,
					currentRuleset.getText(), inGameUiTheme.getTabRulesets().get(currentRuleset.getText()));
			TabView tabView = new TabView();
			tabView.setVisibility(Visibility.VISIBLE);
			
			Tab tab1 = new Tab();
			tab1.setVisibility(Visibility.VISIBLE);
			generateExampleContentA(tab1);
			
			Tab tab2 = new Tab();
			tab2.setVisibility(Visibility.VISIBLE);
			generateExampleContentB(tab2);
			
			tabView.add(tab1);
			tabView.add(tab2);
			rulesetEditor.setElement(tabView);
			break;
		case ELEMENT_TYPE_TEXTBOX:
			rulesetEditor = new StyleRulesetEditor<TextBoxStyleRule, TextBox>(this, TextBoxStyleRule.class,
					TextBox.class, currentRuleset.getText(),
					inGameUiTheme.getTextBoxRulesets().get(currentRuleset.getText()));
			TextBox textBox = new TextBox();
			textBox.setVisibility(Visibility.VISIBLE);
			rulesetEditor.setElement(textBox);
			break;
		}

		rulesetEditor.setHorizontalLayout("xs-6c");
		rulesetEditor.onResize(0, MathUtils.round(elementTypeScollBox.getMaxHeight()));
		add(rulesetEditor);
	}

	private void listRulesets() {
		rulesetsScrollBox.removeAll();
		rulesets.clear();

		switch (currentElementType.getText()) {
		case ELEMENT_TYPE_BUTTON:
			for (String ruleset : inGameUiTheme.getButtonRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_CHECKBOX:
			for (String ruleset : inGameUiTheme.getCheckboxRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_COLUMN:
			for (String ruleset : inGameUiTheme.getColumnRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_CONTAINER:
			for (String ruleset : inGameUiTheme.getContainerRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_LABEL:
			for (String ruleset : inGameUiTheme.getLabelRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_IMAGE:
			for (String ruleset : inGameUiTheme.getImageRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_PROGRESS_BAR:
			for (String ruleset : inGameUiTheme.getProgressBarRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_RADIO_BUTTON:
			for (String ruleset : inGameUiTheme.getRadioButtonRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_SELECT:
			for (String ruleset : inGameUiTheme.getSelectRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_SCROLLBOX:
			for (String ruleset : inGameUiTheme.getScrollBoxRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_SLIDER:
			for (String ruleset : inGameUiTheme.getSliderRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_TAB:
			for (String ruleset : inGameUiTheme.getTabRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		case ELEMENT_TYPE_TEXTBOX:
			for (String ruleset : inGameUiTheme.getTextBoxRulesets().keySet()) {
				createRulesetButton(ruleset);
			}
			break;
		}
	}

	private void setUpUserInterface() {
		returnButton = new TextButton(getId() + "-returnButton");
		returnButton.setText("Save and return to main menu");
		returnButton.setVisibility(Visibility.VISIBLE);
		returnButton.setHorizontalLayout("xs-12c md-6c lg-4c");
		returnButton.addActionListener(this);
		add(Row.withElements(returnButton));
		
		Label elementTypeLabel = new Label();
		elementTypeLabel.setVisibility(Visibility.VISIBLE);
		elementTypeLabel.setText("Element Type");
		elementTypeLabel.setResponsive(true);

		elementTypeScollBox = new ScrollBox(getId() + "-elementTypeScollBox");
		elementTypeScollBox.setVisibility(Visibility.VISIBLE);

		Column elementTypeColumn = Column.withElements(elementTypeLabel, elementTypeScollBox);
		elementTypeColumn.setHorizontalLayout("xs-3c");
		add(elementTypeColumn);

		Label rulesetLabel = new Label();
		rulesetLabel.setVisibility(Visibility.VISIBLE);
		rulesetLabel.setText("Ruleset");
		rulesetLabel.setResponsive(true);

		rulesetsScrollBox = new ScrollBox(getId() + "-rulesetsScrollBox");
		rulesetsScrollBox.setVisibility(Visibility.VISIBLE);

		Column rulesetColumn = Column.withElements(rulesetLabel, rulesetsScrollBox);
		rulesetColumn.setHorizontalLayout("xs-3c");
		add(rulesetColumn);

		for (int i = 0; i < ELEMENT_TYPES.length; i++) {
			createElementTypeButton(ELEMENT_TYPES[i]);
		}
	}

	private void createElementTypeButton(String label) {
		TextButton result = new TextButton(getId() + "-elementType-" + label);
		result.setVisibility(Visibility.VISIBLE);
		result.setTextAlignment(HorizontalAlignment.CENTER);
		result.setText(label);
		result.addActionListener(this);
		elementTypes.add(result);
		elementTypeScollBox.add(result);
	}

	private void createRulesetButton(String rulesetName) {
		TextButton result = new TextButton(getId() + "-ruleset-" + rulesetName);
		result.setVisibility(Visibility.VISIBLE);
		result.setTextAlignment(HorizontalAlignment.CENTER);
		result.setText(rulesetName);
		result.addActionListener(this);
		rulesets.add(result);
		rulesetsScrollBox.add(result);
	}
	
	private void generateExampleContentA(ParentUiElement parent) {
		Label label = new Label();
		label.setVisibility(Visibility.VISIBLE);
		label.setText("Example Label");
		label.setResponsive(true);
		
		TextButton textButton = new TextButton();
		textButton.setVisibility(Visibility.VISIBLE);
		textButton.setText("Example Button");
		
		parent.add(Row.withElements(label, textButton));
	}
	
	private void generateExampleContentB(ParentUiElement parent) {
		Label label = new Label();
		label.setVisibility(Visibility.VISIBLE);
		label.setText("Field");
		label.setResponsive(true);
		
		Column labelColumn = Column.withElements(label);
		labelColumn.setHorizontalLayout("xs-4c");
		
		TextBox textBox = new TextBox();
		textBox.setVisibility(Visibility.VISIBLE);
		textBox.setValue("value");
		textBox.setHorizontalLayout("xs-8c");
		
		parent.add(Row.withElements(labelColumn, textBox));
	}

	public UiTheme getInGameUiTheme() {
		return inGameUiTheme;
	}

	private static final String ELEMENT_TYPE_BUTTON = "Buttons";
	private static final String ELEMENT_TYPE_CHECKBOX = "Checkboxes";
	private static final String ELEMENT_TYPE_COLUMN = "Columns / Rows";
	private static final String ELEMENT_TYPE_CONTAINER = "Containers";
	private static final String ELEMENT_TYPE_IMAGE = "Images";
	private static final String ELEMENT_TYPE_LABEL = "Labels";
	private static final String ELEMENT_TYPE_PROGRESS_BAR = "Progress Bars";
	private static final String ELEMENT_TYPE_RADIO_BUTTON = "Radio Buttons";
	private static final String ELEMENT_TYPE_SELECT = "Selects";
	private static final String ELEMENT_TYPE_SCROLLBOX = "ScrollBoxes";
	private static final String ELEMENT_TYPE_SLIDER = "Sliders";
	private static final String ELEMENT_TYPE_TAB = "Tab Views";
	private static final String ELEMENT_TYPE_TEXTBOX = "Textboxes";

	private static final String[] ELEMENT_TYPES = new String[] { ELEMENT_TYPE_BUTTON, ELEMENT_TYPE_CHECKBOX,
			ELEMENT_TYPE_COLUMN, ELEMENT_TYPE_CONTAINER, ELEMENT_TYPE_IMAGE, ELEMENT_TYPE_LABEL,
			ELEMENT_TYPE_PROGRESS_BAR, ELEMENT_TYPE_RADIO_BUTTON, ELEMENT_TYPE_SELECT, ELEMENT_TYPE_SCROLLBOX,
			ELEMENT_TYPE_SLIDER, ELEMENT_TYPE_TAB, ELEMENT_TYPE_TEXTBOX };
}
