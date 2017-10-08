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

import java.lang.reflect.Constructor;

import org.mini2Dx.ui.element.AbsoluteContainer;
import org.mini2Dx.ui.element.AbsoluteModal;
import org.mini2Dx.ui.element.AlignedContainer;
import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.element.TextButton;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.listener.ActionListener;

/**
 *
 */
public class EditorNewFileDialog extends AlignedModal implements ActionListener {
	private final TextBox filename, rootElementId;
	private final Select<Class<?>> containerType;
	private final TextButton createButton, cancelButton;
	
	private EditorNewFileListener listener;

	public EditorNewFileDialog() {
		super("editorNewFileDialog");
		
		setHorizontalLayout("xs-6c");
		
		Label newFileLabel = new Label(getId() + "-newFileLabel");
		newFileLabel.setStyleId("header");
		newFileLabel.setText("NEW FILE");
		newFileLabel.setHorizontalAlignment(HorizontalAlignment.LEFT);
		newFileLabel.setResponsive(true);
		newFileLabel.setVisibility(Visibility.VISIBLE);
		add(newFileLabel);
		
		Label filenameLabel = new Label(getId() + "-filenameLabel");
		filenameLabel.setText("Filename");
		filenameLabel.setHorizontalAlignment(HorizontalAlignment.LEFT);
		filenameLabel.setResponsive(true);
		filenameLabel.setVisibility(Visibility.VISIBLE);
		
		Column filenameColumn = Column.withElements(filenameLabel);
		filenameColumn.setHorizontalLayout("xs-4c");
		
		filename = new TextBox(getId() + "-filename");
		filename.setHorizontalLayout("xs-8c");
		filename.setVisibility(Visibility.VISIBLE);
		filename.addActionListener(this);
		
		Row filenameRow = Row.withElements(filenameColumn, filename);
		add(filenameRow);
		
		Label containerTypeLabel = new Label(getId() + "-containerTypeLabel");
		containerTypeLabel.setText("Container Type");
		containerTypeLabel.setHorizontalAlignment(HorizontalAlignment.LEFT);
		containerTypeLabel.setResponsive(true);
		containerTypeLabel.setVisibility(Visibility.VISIBLE);
		
		Column containerTypeColumn = Column.withElements(containerTypeLabel);
		containerTypeColumn.setHorizontalLayout("xs-4c");
		
		containerType = new Select<Class<?>>(getId() + "-containerType");
		containerType.setHorizontalLayout("xs-8c");
		containerType.setVisibility(Visibility.VISIBLE);
		addContainerOption(AlignedContainer.class);
		addContainerOption(AlignedModal.class);
		addContainerOption(AbsoluteContainer.class);
		addContainerOption(AbsoluteModal.class);
		containerType.setSelectedIndex(0);
		containerType.addActionListener(this);
		
		Row containerTypeRow = Row.withElements(containerTypeColumn, containerType);
		add(containerTypeRow);
		
		Label rootElementIdLabel = new Label(getId() + "-rootElementIdLabel");
		rootElementIdLabel.setText("Root Element ID");
		rootElementIdLabel.setHorizontalAlignment(HorizontalAlignment.LEFT);
		rootElementIdLabel.setResponsive(true);
		rootElementIdLabel.setVisibility(Visibility.VISIBLE);
		
		Column rootElementIdColumn = Column.withElements(rootElementIdLabel);
		rootElementIdColumn.setHorizontalLayout("xs-4c");
		
		rootElementId = new TextBox(getId() + "-rootElementId");
		rootElementId.setHorizontalLayout("xs-8c");
		rootElementId.setVisibility(Visibility.VISIBLE);
		rootElementId.addActionListener(this);
		
		Row rootElementRow = Row.withElements(rootElementIdColumn, rootElementId);
		add(rootElementRow);
		
		createButton = new TextButton(getId() + "-createButton");
		createButton.setText("Create");
		createButton.setVisibility(Visibility.VISIBLE);
		createButton.setEnabled(false);
		createButton.addActionListener(this);
		
		cancelButton = new TextButton(getId() + "-cancelButton");
		cancelButton.setText("Cancel");
		cancelButton.setVisibility(Visibility.VISIBLE);
		cancelButton.addActionListener(this);
		
		Row actionButtonsRow = Row.withElements(createButton, cancelButton);
		add(actionButtonsRow);
	}
	
	private void resetUi() {
		filename.setValue("");
		rootElementId.setValue("");
		createButton.setEnabled(false);
	}
	
	public String getSelectedFilename() {
		return filename.getValue();
	}
	
	public Container createFileData() {
		try {
			Constructor<?> constructor = containerType.getSelectedValue().getConstructor(String.class);
			return (Container) constructor.newInstance(rootElementId.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onActionBegin(ActionEvent event) {}

	@Override
	public void onActionEnd(ActionEvent event) {
		if(event.getSource().getId().equals(createButton.getId())) {
			setVisibility(Visibility.HIDDEN);
			listener.onNewFile(this);
		} else if(event.getSource().getId().equals(cancelButton.getId())) {
			setVisibility(Visibility.HIDDEN);
			listener.onCancelNewFile();
		} else if(event.getSource().getId().equals(filename.getId()) ||
				event.getSource().getId().equals(rootElementId.getId()) ||
				event.getSource().getId().equals(containerType.getId())) {
			createButton.setEnabled(false);
			
			if(filename.getValue() == null) {
				return;
			}
			if(filename.getValue().isEmpty()) {
				return;
			}
			if(rootElementId.getValue() == null) {
				return;
			}
			if(rootElementId.getValue().isEmpty()) {
				return;
			}
			createButton.setEnabled(true);
		}
	}
	
	private <T extends Container> void addContainerOption(Class<T> clazz) {
		containerType.addOption(clazz.getSimpleName(), clazz);
	}

	public TextButton getCreateButton() {
		return createButton;
	}

	public TextButton getCancelButton() {
		return cancelButton;
	}
	
	public void show(EditorNewFileListener listener) {
		this.listener = listener;
		setVisibility(Visibility.VISIBLE);
		resetUi();
	}
}
