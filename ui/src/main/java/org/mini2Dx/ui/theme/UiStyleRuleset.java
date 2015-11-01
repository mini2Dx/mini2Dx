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
package org.mini2Dx.ui.theme;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;

/**
 *
 */
public class UiStyleRuleset {
	@Field(optional = true)
	Map<String, ButtonStyle> buttons;
	@Field(optional = true)
	Map<String, CheckBoxStyle> checkboxes;
	@Field(optional = true)
	Map<String, FrameStyle> frames;
	@Field(optional = true)
	Map<String, LabelStyle> labels;
	@Field(optional = true)
	Map<String, TextBoxStyle> textboxes;
	
	public ButtonStyle getButtonStyle(String styleId) {
		if(buttons == null) {
			return null;
		}
		return buttons.get(styleId);
	}
	
	public CheckBoxStyle getCheckBoxStyle(String styleId) {
		if(checkboxes == null) {
			return null;
		}
		return checkboxes.get(styleId);
	}
	
	public FrameStyle getFrameStyle(String styleId) {
		if(frames == null) {
			return null;
		}
		return frames.get(styleId);
	}
	
	public LabelStyle getLabelStyle(String styleId) {
		if(labels == null) {
			return null;
		}
		return labels.get(styleId);
	}
	
	public TextBoxStyle getTextBoxStyle(String styleId) {
		if(textboxes == null) {
			return null;
		}
		return textboxes.get(styleId);
	}
	
	public void putButtonStyle(String styleId, ButtonStyle style) {
		if(buttons == null) {
			buttons = new HashMap<String, ButtonStyle>();
		}
		buttons.put(styleId, style);
	}
	
	public void putCheckBoxStyle(String styleId, CheckBoxStyle style) {
		if(checkboxes == null) {
			checkboxes = new HashMap<String, CheckBoxStyle>();
		}
		checkboxes.put(styleId, style);
	}
	
	public void putFrameStyle(String styleId, FrameStyle style) {
		if(frames == null) {
			frames = new HashMap<String, FrameStyle>();
		}
		frames.put(styleId, style);
	}
	
	public void putLabelStyle(String styleId, LabelStyle style) {
		if(labels == null) {
			labels = new HashMap<String, LabelStyle>();
		}
		labels.put(styleId, style);
	}
	
	public void putTextBoxStyle(String styleId, TextBoxStyle style) {
		if(textboxes == null) {
			textboxes = new HashMap<String, TextBoxStyle>();
		}
		textboxes.put(styleId, style);
	}
}
