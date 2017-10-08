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

import org.mini2Dx.core.assets.FallbackFileHandleResolver;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiThemeLoader;
import org.mini2Dx.ui.editor.modals.EditorNewFileDialog;
import org.mini2Dx.ui.editor.modals.EditorNewFileListener;
import org.mini2Dx.ui.editor.modals.EditorOpenFileDialog;
import org.mini2Dx.ui.editor.modals.EditorOpenFileListener;
import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
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
	
	private AlignedModal initialModal;
	private TextButton editThemeButton, editContainersButton;
	
	private UiEditorThemeView themeEditorView;
	private UiEditorContainersView containersEditorView;
	private EditorNewFileDialog newFileDialog;
	private EditorOpenFileDialog openFileDialog;

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
	
	@Override
	public void onResize(int width, int height) {
		uiContainer.set(width, height);
		themeEditorView.onResize(width, height);
	}

	private void setUpUserInterface() {
		UiTheme uiTheme = assetManager.get(gameThemePath, UiTheme.class);
		
		initialModal = new AlignedModal("editorInitialModal");
		initialModal.setVisibility(Visibility.VISIBLE);
		initialModal.setHorizontalLayout("xs-6c");
		uiContainer.add(initialModal);
		
		Label themeLabel = new Label(initialModal.getId() + "-themeLabel");
		themeLabel.setStyleId("header");
		themeLabel.setVisibility(Visibility.VISIBLE);
		themeLabel.setText(uiTheme.getId());
		themeLabel.setResponsive(true);
		initialModal.add(themeLabel);
		
		Label themePathLabel = new Label(initialModal.getId() + "-themePathLabel");
		themePathLabel.setVisibility(Visibility.VISIBLE);
		themePathLabel.setText(gameThemePath);
		themePathLabel.setResponsive(true);
		initialModal.add(themePathLabel);
		
		editThemeButton = new TextButton(initialModal.getId() + "-editThemeButton");
		editThemeButton.setVisibility(Visibility.VISIBLE);
		editThemeButton.setText("Edit Theme");
		editThemeButton.addActionListener(this);
		initialModal.add(editThemeButton);
		
		editContainersButton = new TextButton(initialModal.getId() + "-editContainersButton");
		editContainersButton.setVisibility(Visibility.VISIBLE);
		editContainersButton.setText("Edit Containers");
		editContainersButton.addActionListener(this);
		initialModal.add(editContainersButton);
		
		themeEditorView = new UiEditorThemeView(this, uiTheme);
		themeEditorView.setVisibility(Visibility.HIDDEN);
		uiContainer.add(themeEditorView);
		
		containersEditorView = new UiEditorContainersView(this, uiTheme);
		containersEditorView.setVisibility(Visibility.HIDDEN);
		uiContainer.add(containersEditorView);
		
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
	
	public void goToInitialModal() {
		initialModal.setVisibility(Visibility.VISIBLE);
		themeEditorView.setVisibility(Visibility.HIDDEN);
		containersEditorView.setVisibility(Visibility.HIDDEN);
	}
	
	public void newFile(EditorNewFileListener listener) {
		newFileDialog.show(listener);
	}
	
	public void openFile(EditorOpenFileListener listener) {
		openFileDialog.show(listener);
	}

	@Override
	public void onActionBegin(ActionEvent event) {
		
	}

	@Override
	public void onActionEnd(ActionEvent event) {
		if(event.getSource().getId().equals(editThemeButton.getId())) {
			initialModal.setVisibility(Visibility.HIDDEN);
			themeEditorView.setVisibility(Visibility.VISIBLE);
		} else if(event.getSource().getId().equals(editContainersButton.getId())) {
			initialModal.setVisibility(Visibility.HIDDEN);
			containersEditorView.setVisibility(Visibility.VISIBLE);
		}
	}

	public String getDirectory() {
		return directory;
	}
}
