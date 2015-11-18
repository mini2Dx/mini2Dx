/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.theme.ButtonStyle;
import org.mini2Dx.ui.theme.CheckBoxStyle;
import org.mini2Dx.ui.theme.FrameStyle;
import org.mini2Dx.ui.theme.LabelStyle;
import org.mini2Dx.ui.theme.TextBoxStyle;
import org.mini2Dx.ui.theme.UiFont;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public class UiThemeWriter {

	public static void main(String [] args) {
		UiTheme theme = new UiTheme();
		theme.setColumns(12);
		theme.setName("mdx-default-theme");
		
		UiFont uiFont = new UiFont();
		uiFont.setPath("");
		theme.putFont(UiTheme.DEFAULT_STYLE_ID, uiFont);
		
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.setActionImage("");
		buttonStyle.setDisabledImage("");
		buttonStyle.setHoverImage("");
		buttonStyle.setNormalImage("");
		theme.putButtonStyle(ScreenSize.XS, UiTheme.DEFAULT_STYLE_ID, buttonStyle);
		
		CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
		checkBoxStyle.setNormalCheckIcon("");
		checkBoxStyle.setDisabledImage("");
		checkBoxStyle.setHoverImage("");
		checkBoxStyle.setNormalImage("");
		theme.putCheckBoxStyle(ScreenSize.XS, UiTheme.DEFAULT_STYLE_ID, checkBoxStyle);
		
		FrameStyle frameStyle = new FrameStyle();
		frameStyle.setBackgroundImage("");
		frameStyle.setScrollBarImage("");
		theme.putFrameStyle(ScreenSize.XS, UiTheme.DEFAULT_STYLE_ID, frameStyle);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.setFont("");
		theme.putLabelStyle(ScreenSize.XS, UiTheme.DEFAULT_STYLE_ID, labelStyle);
		
		TextBoxStyle textBoxStyle = new TextBoxStyle();
		textBoxStyle.setActionImage("");
		textBoxStyle.setDisabledImage("");
		textBoxStyle.setHoverImage("");
		textBoxStyle.setNormalImage("");
		textBoxStyle.setLabelStyle("");
		theme.putTextBoxStyle(ScreenSize.XS, UiTheme.DEFAULT_STYLE_ID, textBoxStyle);
		
		try {
			String json = Mdx.json.toJson(theme);
			Files.write(Paths.get("./", UiTheme.DEFAULT_THEME_FILE), json.getBytes(), StandardOpenOption.CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
