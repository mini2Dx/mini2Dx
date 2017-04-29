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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.ui.editor.render.StyleRulesetEditorRenderNode;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.StyleRuleset;
import org.mini2Dx.ui.style.UiTheme;

/**
 *
 */
public class StyleRulesetEditor<T extends StyleRule, E extends UiElement> extends Column implements ActionListener {
	private final UiEditorThemeView themeEditorView;
	private final String rulesetId;
	private final StyleRuleset<T> originalRuleset;
	private final Row elementRow, editorRow, actionsRow;
	private final ScrollBox editorScrollBox;
	private final TextButton applyButton, resetButton;
	private final Map<String, Field> idsToField = new HashMap<String, Field>();

	private E element;

	public StyleRulesetEditor(UiEditorThemeView themeEditorView, Class<T> styleRuleClass, Class<E> elementClass,
			String rulesetId, StyleRuleset<T> ruleset) {
		super("styleRulesetEditor-" + styleRuleClass.getSimpleName());
		setVisibility(Visibility.VISIBLE);

		this.themeEditorView = themeEditorView;
		this.rulesetId = rulesetId;
		this.originalRuleset = ruleset;

		Label previewLabel = new Label();
		previewLabel.setText("PREVIEW");
		previewLabel.setResponsive(true);
		previewLabel.setVisibility(Visibility.VISIBLE);
		add(Row.withElements(previewLabel));

		elementRow = new Row();
		elementRow.setVisibility(Visibility.VISIBLE);
		add(elementRow);

		Label editorLabel = new Label();
		editorLabel.setText("EDITOR");
		editorLabel.setResponsive(true);
		editorLabel.setVisibility(Visibility.VISIBLE);
		add(Row.withElements(editorLabel));

		editorScrollBox = new ScrollBox();
		editorScrollBox.setVisibility(Visibility.VISIBLE);
		editorRow = new Row();
		editorRow.setVisibility(Visibility.VISIBLE);
		editorScrollBox.add(editorRow);
		add(editorScrollBox);

		applyButton = new TextButton();
		applyButton.setText("Apply");
		applyButton.setLayout("xs-6c");
		applyButton.setVisibility(Visibility.VISIBLE);
		applyButton.addActionListener(this);

		resetButton = new TextButton();
		resetButton.setText("Reset");
		resetButton.setLayout("xs-6c");
		resetButton.setVisibility(Visibility.VISIBLE);
		resetButton.addActionListener(this);

		actionsRow = Row.withElements(applyButton, resetButton);
		add(actionsRow);

		generateEditorUi(styleRuleClass);
	}
	
	public void onResize(int width, int height) {
		int threeQuarterHeight = ((height / 4) * 3);
		editorScrollBox.setMinHeight(threeQuarterHeight - 1);
		editorScrollBox.setMaxHeight(threeQuarterHeight);
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new StyleRulesetEditorRenderNode(parent, this);
	}

	@Override
	public void onActionBegin(ActionEvent event) {
	}

	@Override
	public void onActionEnd(ActionEvent event) {

	}

	private void generateEditorUi(Class<T> styleRuleClass) {
		Class<?> currentClass = styleRuleClass;
		while (!currentClass.equals(Object.class)) {
			for (Field field : currentClass.getDeclaredFields()) {
				field.setAccessible(true);
				
				if (!field.isAnnotationPresent(org.mini2Dx.core.serialization.annotation.Field.class)) {
					continue;
				}

				Label fieldLabel = new Label();
				fieldLabel.setVisibility(Visibility.VISIBLE);
				fieldLabel.setText(field.getName());

				Column fieldLabelColumn = Column.withElements(fieldLabel);
				fieldLabelColumn.setLayout("xs-4c");

				UiElement valueElement = null;

				try {
					if (field.getType().equals(String.class)) {
						TextBox textbox = new TextBox();
						textbox.setLayout("xs-8c");
						textbox.setVisibility(Visibility.VISIBLE);
						Object value = field.get(originalRuleset.getStyleRule(getScreenSize()));
						if(value == null) {
							textbox.setValue("");
						} else {
							textbox.setValue(value.toString());
						}
						valueElement = textbox;
					} else if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
						TextBox textbox = new TextBox();
						textbox.setLayout("xs-8c");
						textbox.setVisibility(Visibility.VISIBLE);
						textbox.setValue(String.valueOf(field.get(originalRuleset.getStyleRule(getScreenSize()))));
						valueElement = textbox;
					} else if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {
						Checkbox checkbox = new Checkbox();
						checkbox.setVisibility(Visibility.VISIBLE);
						if (field.getBoolean(originalRuleset.getStyleRule(getScreenSize()))) {
							checkbox.setChecked(true);
						} else {
							checkbox.setChecked(false);
						}
						Column column = Column.withElements(checkbox);
						column.setLayout("xs-8c");
						valueElement = column;
					} else {
						System.err.println(field.getName() + " " + field.getType());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				Row row = Row.withElements(fieldLabelColumn, valueElement);
				editorRow.add(row);
			}
			currentClass = currentClass.getSuperclass();
		}
	}
	
	public E getElement() {
		return element;
	}

	public <T extends UiElement> void setElement(T element) {
		this.element = (E) element;
		this.element.setStyleId(rulesetId);
		elementRow.removeAll();
		elementRow.add(this.element);
	}
	
	public UiTheme getInGameUiTheme() {
		return themeEditorView.getInGameUiTheme();
	}
	
	public ScreenSize getScreenSize() {
		return ScreenSize.XS;
	}

	public Row getElementRow() {
		return elementRow;
	}
}
