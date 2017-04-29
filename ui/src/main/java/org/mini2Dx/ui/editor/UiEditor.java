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
import org.mini2Dx.core.assets.FallbackFileHandleResolver;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiThemeLoader;
import org.mini2Dx.ui.element.AlignedContainer;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;

/**
 *
 */
public class UiEditor extends BasicGame implements GameResizeListener, ActionListener {
	private final String directory, gameThemePath;
	private final AssetManager assetManager;

	private final UiContainer uiContainer;

	private EditorInGameView inGameView;
	private UiHierarchyEditor hierarchyEditor;
	private UiElementEditor elementEditor;
	private Label currentFilenameLabel;
	private TextButton newButton, openButton, saveButton;
	
	private EditorNewFileDialog newFileDialog;
	private EditorOpenFileDialog openFileDialog;
	
	private String currentFilename = null;
	private Container currentFileData = null;

	public UiEditor(String directory, String gameThemePath) {
		super();
		this.directory = directory;
		this.gameThemePath = gameThemePath;

		FileHandleResolver fileHandleResolver = new FallbackFileHandleResolver(new ClasspathFileHandleResolver(),
				new InternalFileHandleResolver());
		assetManager = new AssetManager(fileHandleResolver);
		assetManager.setLoader(UiTheme.class, new UiThemeLoader(fileHandleResolver));
		assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);
		assetManager.load(gameThemePath, UiTheme.class);
		
		uiContainer = new UiContainer(this, assetManager);
	}

	@Override
	public void initialise() {
		Gdx.input.setInputProcessor(uiContainer);
		
		assetManager.finishLoading();
		uiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
		setUpUserInterface();
		
		onResize(getWidth(), getHeight());
	}

	@Override
	public void update(float delta) {
		uiContainer.update(delta);
	}

	@Override
	public void interpolate(float alpha) {
		uiContainer.interpolate(alpha);
	}

	@Override
	public void render(Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		uiContainer.render(g);
	}

	private void setUpUserInterface() {
		AlignedContainer outerContainer = new AlignedContainer();
		outerContainer.setHorizontalAlignment(HorizontalAlignment.LEFT);
		outerContainer.setVerticalAlignment(VerticalAlignment.TOP);
		outerContainer.setVisibility(Visibility.VISIBLE);
		outerContainer.setStyleId("no-background-no-padding");

		inGameView = new EditorInGameView(assetManager.get(gameThemePath, UiTheme.class));
		inGameView.setLayout("xs-8c");
		inGameView.setVisibility(Visibility.VISIBLE);
		outerContainer.add(inGameView);

		newButton = new TextButton("newButton");
		newButton.setLayout("xs-4c");
		newButton.setVisibility(Visibility.VISIBLE);
		newButton.setText("New");
		newButton.addActionListener(this);

		openButton = new TextButton("openButton");
		openButton.setLayout("xs-4c");
		openButton.setVisibility(Visibility.VISIBLE);
		openButton.setText("Open");
		openButton.addActionListener(this);

		saveButton = new TextButton("saveButton");
		saveButton.setLayout("xs-4c");
		saveButton.setVisibility(Visibility.VISIBLE);
		saveButton.setText("Save");
		saveButton.addActionListener(this);

		Row filesMenuRow = Row.withElements("filesMenuRow", newButton, openButton, saveButton);

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

		Column controlsColumn = Column.withElements("controlsColumn", filesMenuRow, filenameRow, hierarchyLabelRow, hierarchyEditor, elementEditor);
		controlsColumn.setStyleId("frame");
		controlsColumn.setLayout("xs-4c");
		outerContainer.add(controlsColumn);
		
		uiContainer.add(outerContainer);
		
		openFileDialog = new EditorOpenFileDialog(directory);
		openFileDialog.setVisibility(Visibility.HIDDEN);
		openFileDialog.getOpenButton().addActionListener(this);
		openFileDialog.getCancelButton().addActionListener(this);
		uiContainer.add(openFileDialog);
		
		newFileDialog = new EditorNewFileDialog();
		newFileDialog.setVisibility(Visibility.HIDDEN);
		newFileDialog.getCreateButton().addActionListener(this);
		newFileDialog.getCancelButton().addActionListener(this);
		uiContainer.add(newFileDialog);
	}

	@Override
	public void onResize(int width, int height) {
		uiContainer.set(width, height);
		inGameView.setMinHeight(height - 1);
		inGameView.setMaxHeight(height);
	}

	@Override
	public void onActionBegin(ActionEvent event) {
		
	}

	@Override
	public void onActionEnd(ActionEvent event) {
		if(event.getSource().getId().equals(newButton.getId())) {
			newFileDialog.resetUi();
			newFileDialog.setVisibility(Visibility.VISIBLE);
		} else if(event.getSource().getId().equals(newFileDialog.getCreateButton().getId())) {
			newFileDialog.setVisibility(Visibility.HIDDEN);
			
			currentFilename = newFileDialog.getSelectedFilename();
			currentFileData = newFileDialog.createFileData();
			saveData();
		} else if(event.getSource().getId().equals(newFileDialog.getCancelButton().getId())) {
			newFileDialog.setVisibility(Visibility.HIDDEN);
		}
		
		if(event.getSource().getId().equals(openButton.getId())) {
			openFileDialog.resetUi();
			openFileDialog.setVisibility(Visibility.VISIBLE);
		} else if(event.getSource().getId().equals(openFileDialog.getOpenButton().getId())) {
			currentFilename = openFileDialog.getSelectedFilename();
		} else if(event.getSource().getId().equals(openFileDialog.getCancelButton().getId())) {
			openFileDialog.setVisibility(Visibility.HIDDEN);
		}
		
		if(event.getSource().getId().equals(saveButton.getId())) {
			saveData();
		}
	}
	
	private boolean saveData() {
		if(currentFilename == null) {
			return false;
		}
		try {
			FileWriter fileWriter = new FileWriter(new File(directory, currentFilename));
			Mdx.xml.toXml(currentFileData, fileWriter);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
