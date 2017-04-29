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
package org.mini2Dx.ui.editor.modals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.listener.ActionListener;

/**
 *
 */
public class EditorOpenFileDialog extends AlignedModal implements ActionListener {
	private final String directory;
	private final List<TextButton> fileButtons = new ArrayList<TextButton>();
	private final ScrollBox scrollBox;
	private final TextButton openButton, cancelButton;
	
	private TextButton selectedFilenameButton = null;
	private EditorOpenFileListener listener = null;

	/**
	 * Constructor
	 * @param directory The directory of files to list
	 */
	public EditorOpenFileDialog(String directory) {
		super("editorOpenFileDialog");
		this.directory = directory;
		
		setLayout("xs-6c");
		
		Label openFileLabel = new Label();
		openFileLabel.setStyleId("header");
		openFileLabel.setText("OPEN FILE");
		openFileLabel.setHorizontalAlignment(HorizontalAlignment.LEFT);
		openFileLabel.setResponsive(true);
		openFileLabel.setVisibility(Visibility.VISIBLE);
		add(openFileLabel);
		
		scrollBox = new ScrollBox(getId() + "-scrollBox");
		scrollBox.setVisibility(Visibility.VISIBLE);
		add(scrollBox);
		
		openButton = new TextButton(getId() + "-openButton");
		openButton.setLayout("xs-6c");
		openButton.setVisibility(Visibility.VISIBLE);
		openButton.setText("Open");
		openButton.setEnabled(false);
		openButton.addActionListener(this);
		
		cancelButton = new TextButton(getId() + "-cancelButton");
		cancelButton.setLayout("xs-6c");
		cancelButton.setVisibility(Visibility.VISIBLE);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(this);
		
		Row actionButtonsRow = Row.withElements(openButton, cancelButton);
		add(actionButtonsRow);
		
		resetUi();
	}
	
	public String getSelectedFilename() {
		if(selectedFilenameButton == null) {
			return null;
		}
		return selectedFilenameButton.getText();
	}

	@Override
	public void onActionBegin(ActionEvent event) {}

	@Override
	public void onActionEnd(ActionEvent event) {
		for(TextButton button : fileButtons) {
			if(!button.getId().equals(event.getSource().getId())) {
				continue;
			}
			if(selectedFilenameButton != null) {
				selectedFilenameButton.setEnabled(true);
			}
			selectedFilenameButton = button;
			selectedFilenameButton.setEnabled(false);
			openButton.setEnabled(true);
			return;	
		}
		
		if(event.getSource().getId().equals(openButton.getId())) {
			setVisibility(Visibility.HIDDEN);
			listener.onOpenFile(this);
		} else if(event.getSource().getId().equals(cancelButton.getId())) {
			setVisibility(Visibility.HIDDEN);
			listener.onCancelOpenFile();
		}
	}

	public TextButton getOpenButton() {
		return openButton;
	}

	public TextButton getCancelButton() {
		return cancelButton;
	}
	
	private void resetUi() {
		scrollBox.removeAll();
		fileButtons.clear();
		
		File dir = new File(directory);
		for(File file : dir.listFiles()) {
			if(!file.isFile()) {
				continue;
			}
			if(!file.getName().endsWith(".xml")) {
				continue;
			}
			TextButton fileButton = new TextButton(getId() + "-fileButton-" + file.getName());
			fileButton.setVisibility(Visibility.VISIBLE);
			fileButton.setStyleId("list-item-button");
			fileButton.setText(file.getName());
			fileButton.addActionListener(this);
			fileButtons.add(fileButton);
			scrollBox.add(fileButton);
		}
		
		openButton.setEnabled(false);
	}
	
	public void show(EditorOpenFileListener listener) {
		this.listener = listener;
		setVisibility(Visibility.VISIBLE);
		resetUi();
	}
}
