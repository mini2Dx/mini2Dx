/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.render;

/**
 * Interface for {@link RenderNode}s that can handle text input
 */
public interface TextInputableRenderNode extends ActionableRenderNode {
	
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button);

	public void characterReceived(char c);

	public void pasteReceived(String pastedText);

	public void backspace();
	
	public boolean enter();
	
	public void moveCursorRight();
	
	public void moveCursorLeft();
	
	public void cut();
	
	public void copy();
	
	public void paste();
	
	public boolean isReceivingInput();
}
