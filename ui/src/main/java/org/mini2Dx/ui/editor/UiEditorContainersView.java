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

import java.io.File;
import java.io.FileWriter;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.ui.editor.modals.EditorNewFileDialog;
import org.mini2Dx.ui.editor.modals.EditorNewFileListener;
import org.mini2Dx.ui.editor.modals.EditorOpenFileDialog;
import org.mini2Dx.ui.editor.modals.EditorOpenFileListener;
import org.mini2Dx.ui.element.AlignedContainer;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.style.UiTheme;

/**
 *
 */
public class UiEditorContainersView extends AlignedContainer implements ActionListener, EditorNewFileListener, EditorOpenFileListener {
	private final UiEditor uiEditor;
	private final UiTheme inGameUiTheme;
	
	private EditorInGameView inGameView;
	private UiHierarchyEditor hierarchyEditor;
	private UiElementEditor elementEditor;
	private Label currentFilenameLabel;
	private TextButton newButton, openButton, saveButton;
	
	private String currentFilename = null;
	private Container currentFileData = null;

	public UiEditorContainersView(UiEditor uiEditor, UiTheme inGameUiTheme) {
		super("uiEditorContainersView");
		this.uiEditor = uiEditor;
		this.inGameUiTheme = inGameUiTheme;
		
		setStyleId("no-background-no-padding");
		setUpUserInterface();
	}
	
	public void onResize(int width, int height) {
		inGameView.setMinHeight(height - 1);
		inGameView.setMaxHeight(height);
	}
	
	@Override
	public void onActionBegin(ActionEvent event) {
	}

	@Override
	public void onActionEnd(ActionEvent event) {
		if(event.getSource().getId().equals(newButton.getId())) {
			uiEditor.newFile(this);
		} else if(event.getSource().getId().equals(openButton.getId())) {
			uiEditor.openFile(this);
		} else if(event.getSource().getId().equals(saveButton.getId())) {
			saveData();
		}
	}
	
	private boolean saveData() {
		if(currentFilename == null) {
			return false;
		}
		try {
			FileWriter fileWriter = new FileWriter(new File(uiEditor.getDirectory(), currentFilename));
			Mdx.xml.toXml(currentFileData, fileWriter);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void setUpUserInterface() {
		inGameView = new EditorInGameView(inGameUiTheme);
		inGameView.setHorizontalLayout("xs-8c");
		inGameView.setVisibility(Visibility.VISIBLE);
		add(inGameView);

		newButton = new TextButton(getId() + "-newButton");
		newButton.setHorizontalLayout("xs-4c");
		newButton.setVisibility(Visibility.VISIBLE);
		newButton.setText("New");
		newButton.addActionListener(this);

		openButton = new TextButton(getId() + "-openButton");
		openButton.setHorizontalLayout("xs-4c");
		openButton.setVisibility(Visibility.VISIBLE);
		openButton.setText("Open");
		openButton.addActionListener(this);

		saveButton = new TextButton(getId() + "-saveButton");
		saveButton.setHorizontalLayout("xs-4c");
		saveButton.setVisibility(Visibility.VISIBLE);
		saveButton.setText("Save");
		saveButton.addActionListener(this);

		Row filesMenuRow = Row.withElements(getId() + "-filesMenuRow", newButton, openButton, saveButton);

		currentFilenameLabel = new Label("currentFilename");
		currentFilenameLabel.setVisibility(Visibility.VISIBLE);
		currentFilenameLabel.setText("No file selected.");
		currentFilenameLabel.setResponsive(true);

		Row filenameRow = Row.withElements(currentFilenameLabel);

		Label hierarchyLabel = new Label();
		hierarchyLabel.setVisibility(Visibility.VISIBLE);
		hierarchyLabel.setText("UI Elements Hierarchy");
		hierarchyLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
		hierarchyLabel.setResponsive(true);

		Row hierarchyLabelRow = Row.withElements(hierarchyLabel);

		UiHierarchyEditor hierarchyEditor = new UiHierarchyEditor();
		hierarchyEditor.setVisibility(Visibility.HIDDEN);

		UiElementEditor elementEditor = new UiElementEditor();
		elementEditor.setVisibility(Visibility.HIDDEN);

		Column controlsColumn = Column.withElements(getId() + "-controlsColumn", filesMenuRow, filenameRow, hierarchyLabelRow, hierarchyEditor, elementEditor);
		controlsColumn.setStyleId("frame");
		controlsColumn.setHorizontalLayout("xs-4c");
		add(controlsColumn);
	}

	@Override
	public void onOpenFile(EditorOpenFileDialog dialog) {
		
	}

	@Override
	public void onCancelOpenFile() {
		
	}

	@Override
	public void onNewFile(EditorNewFileDialog dialog) {
		currentFilename = dialog.getSelectedFilename();
		currentFileData = dialog.createFileData();
		saveData();
	}

	@Override
	public void onCancelNewFile() {
		
	}
}
