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
package org.mini2Dx.uats.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.style.ruleset.ButtonStyleRuleset;

/**
 *
 */
public class UiThemeWriter {

	public static void main(String [] args) {
		Gdx.files = new LwjglFiles();
		Mdx.reflect = new JvmReflection();
		Mdx.files = new LibgdxFiles();
		
		UiTheme theme = new UiTheme();
		theme.setId(UiTheme.DEFAULT_STYLE_ID);
		theme.putFont("default", "example.ttf");
		
		ButtonStyleRuleset buttonRuleset = new ButtonStyleRuleset();
		ButtonStyleRule styleRule = new ButtonStyleRule();
		styleRule.setActionBackground("");
		styleRule.setDisabledBackground("");
		styleRule.setHoverBackground("");
		styleRule.setBackground("");
		
		buttonRuleset.putStyleRule(ScreenSize.XS, styleRule);
		theme.putButtonStyleRuleset(UiTheme.DEFAULT_STYLE_ID, buttonRuleset);
		
		try {
			Mdx.json.toJson(Mdx.files.external("/tmp/theme.json"), theme);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}
}
