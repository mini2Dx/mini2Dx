/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.uats.desktop;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.UiTheme;
import org.mini2Dx.ui.style.ruleset.ButtonStyleRuleset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

/**
 *
 */
public class UiThemeWriter {

	public static void main(String [] args) {
		Gdx.files = new LwjglFiles();
		
		UiTheme theme = new UiTheme();
		theme.setId(UiTheme.DEFAULT_STYLE_ID);
		theme.putFont("default", "example.ttf");
		
		ButtonStyleRuleset buttonRuleset = new ButtonStyleRuleset();
		ButtonStyleRule styleRule = new ButtonStyleRule();
		styleRule.setAction("");
		styleRule.setDisabled("");
		styleRule.setFont("");
		styleRule.setHover("");
		styleRule.setNormal("");
		styleRule.setTextColor("");
		
		buttonRuleset.putStyleRule(ScreenSize.XS, styleRule);
		theme.putButtonStyleRuleset(UiTheme.DEFAULT_STYLE_ID, buttonRuleset);
		
		try {
			Mdx.json.toJson(Gdx.files.absolute("/tmp/theme.json"), theme);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}
}
