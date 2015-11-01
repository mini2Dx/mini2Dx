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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.element.CheckBox;
import org.mini2Dx.ui.element.Frame;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.TextBox;

/**
 *
 */
public class UiRenderer {	
	private final ButtonRenderer buttonRenderer = new ButtonRenderer();
	private final CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
	private final FrameRenderer frameRenderer = new FrameRenderer();
	private final ImageRenderer imageRenderer = new ImageRenderer();
	private final LabelRenderer labelRenderer = new LabelRenderer();
	private final TextBoxRenderer textBoxRenderer = new TextBoxRenderer();
	
	private final UiContainer uiContainer;
	private Graphics graphics;
	
	public UiRenderer(UiContainer uiContainer) {
		this.uiContainer = uiContainer;
	}

	public void render(Button button) {
		buttonRenderer.render(uiContainer, button, graphics);
	}
	
	public void render(CheckBox checkBox) {
		checkBoxRenderer.render(uiContainer, checkBox, graphics);
	}
	
	public void render(Frame frame) {
		frameRenderer.render(uiContainer, frame, graphics);
	}

	public void render(Image image) {
		imageRenderer.render(uiContainer, image, graphics);
	}
	
	public void render(Label label) {
		labelRenderer.render(uiContainer, label, graphics);
	}
	
	public void render(TextBox textBox) {
		textBoxRenderer.render(uiContainer, textBox, graphics);
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}
}
