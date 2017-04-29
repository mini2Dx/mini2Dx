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

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.UiTheme;

/**
 * An {@link UiEditor} view that shows how elements will look in-game
 */
public class EditorInGameView extends ScrollBox {
	private final UiTheme inGameUiTheme;

	private ScreenSize screenSize = ScreenSize.XS;
	private float scale = 1f;

	/**
	 * Constructor. Generates a unique ID for this {@link EditorInGameView}
	 * 
	 * @param inGameUiTheme
	 *            The {@link UiTheme} used in-game
	 */
	public EditorInGameView(UiTheme inGameUiTheme) {
		this(null, inGameUiTheme);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link EditorInGameView}
	 * @param inGameUiTheme
	 *            The {@link UiTheme} used in-game
	 */
	public EditorInGameView(@ConstructorArg(clazz = String.class, name = "id") String id, UiTheme inGameUiTheme) {
		super(id);
		this.inGameUiTheme = inGameUiTheme;
	}

	public UiTheme getInGameUiTheme() {
		return inGameUiTheme;
	}

	public ScreenSize getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(ScreenSize screenSize) {
		this.screenSize = screenSize;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
