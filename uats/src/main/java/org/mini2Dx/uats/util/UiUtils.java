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
package org.mini2Dx.uats.util;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.CustomCursor;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xboxOne.XboxOneGamePad;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.animation.TextAnimation;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.gamepad.GamePadUiInput;
import org.mini2Dx.ui.gamepad.PS4UiInput;
import org.mini2Dx.ui.gamepad.Xbox360UiInput;
import org.mini2Dx.ui.gamepad.XboxOneUiInput;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.navigation.UiNavigation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class UiUtils {
	private static Map<String, GamePadListener> MAPPED_CONTROLLERS = new ConcurrentHashMap<String, GamePadListener>();
	private static Map<String, GamePadUiInput<?>> MAPPED_CONTROLLER_INPUT = new ConcurrentHashMap<String, GamePadUiInput<?>>();
	private static CustomCursor customCursor;

	public static CustomCursor getCustomCursor(FileHandleResolver fileHandleResolver) {
		if(customCursor == null) {
			customCursor = Mdx.graphics.newCustomCursor(Mdx.graphics.newPixmap(fileHandleResolver.resolve("default-mdx-cursor-up.png")),
					Mdx.graphics.newPixmap(fileHandleResolver.resolve("default-mdx-cursor-down.png")), 0, 0);
		}
		return customCursor;
	}
	
	public static GamePadUiInput<?> setUpControllerInput(GamePad gamePad, UiContainer uiContainer) {
		if(MAPPED_CONTROLLER_INPUT.containsKey(uiContainer.getId())) {
			return MAPPED_CONTROLLER_INPUT.get(uiContainer.getId());
		}
		switch(gamePad.getGamePadType()) {
		case PS4:
			PS4GamePad ps4GamePad = null;
			if(MAPPED_CONTROLLERS.containsKey(gamePad.getInstanceId())) {
				ps4GamePad = (PS4GamePad) MAPPED_CONTROLLERS.get(gamePad.getInstanceId());
			} else {
				ps4GamePad = Mdx.input.newPS4GamePad(gamePad);
				ps4GamePad.setLeftStickDeadZone(new RadialDeadZone());
				ps4GamePad.setRightStickDeadZone(new RadialDeadZone());
				MAPPED_CONTROLLERS.put(gamePad.getInstanceId(), ps4GamePad);
			}
			PS4UiInput ps4UiInput = new PS4UiInput(uiContainer);
			ps4UiInput.setNavigateWithDPad(true);
			ps4GamePad.addListener(ps4UiInput);

			MAPPED_CONTROLLER_INPUT.put(uiContainer.getId(), ps4UiInput);
			Mdx.log.info(UiUtils.class.getSimpleName(), "Set up PS4 controller UI input");
			return ps4UiInput;
		case XBOX_360:
			Xbox360GamePad xbox360GamePad = null;
			if(MAPPED_CONTROLLERS.containsKey(gamePad.getInstanceId())) {
				xbox360GamePad = (Xbox360GamePad) MAPPED_CONTROLLERS.get(gamePad.getInstanceId());
			} else {
				xbox360GamePad = Mdx.input.newXbox360GamePad(gamePad);
				xbox360GamePad.setLeftStickDeadZone(new RadialDeadZone());
				xbox360GamePad.setRightStickDeadZone(new RadialDeadZone());
				MAPPED_CONTROLLERS.put(gamePad.getInstanceId(), xbox360GamePad);
			}
			Xbox360UiInput xbox360UiInput = new Xbox360UiInput(uiContainer);
			xbox360UiInput.setNavigateWithDPad(true);
			xbox360GamePad.addListener(xbox360UiInput);
			
			MAPPED_CONTROLLER_INPUT.put(uiContainer.getId(), xbox360UiInput);
			Mdx.log.info(UiUtils.class.getSimpleName(), "Set up Xbox 360 controller UI input");
			return xbox360UiInput;
		case XBOX_ONE:
			XboxOneGamePad xboxOneGamePad = null;
			if(MAPPED_CONTROLLERS.containsKey(gamePad.getInstanceId())) {
				xboxOneGamePad = (XboxOneGamePad) MAPPED_CONTROLLERS.get(gamePad.getInstanceId());
			} else {
				xboxOneGamePad = Mdx.input.newXboxOneGamePad(gamePad);
				xboxOneGamePad.setLeftStickDeadZone(new RadialDeadZone());
				xboxOneGamePad.setRightStickDeadZone(new RadialDeadZone());
				MAPPED_CONTROLLERS.put(gamePad.getInstanceId(), xboxOneGamePad);
			}
			
			XboxOneUiInput xboxOneUiInput = new XboxOneUiInput(uiContainer);
			xboxOneUiInput.setNavigateWithDPad(true);
			xboxOneGamePad.addListener(xboxOneUiInput);
			
			MAPPED_CONTROLLER_INPUT.put(uiContainer.getId(), xboxOneUiInput);
			Mdx.log.info(UiUtils.class.getSimpleName(), "Set up Xbox One controller UI input");
			return xboxOneUiInput;
		case UNKNOWN:
		default:
			break;
		}
		return null;
	}

	public static Label createHeader(String text) {
		return createLabel(text, "header", Colors.BLACK_P1());
	}
	
	public static Label createHeader(String text, TextAnimation textAnimation) {
		return createLabel(text, "header", Colors.BLACK_P1(), textAnimation);
	}
	
	public static Label createHeader(String text, TextAnimation textAnimation, boolean debug) {
		return createLabel(text, "header", Colors.BLACK_P1(), textAnimation, debug);
	}
	
	public static Label createLabel(String text) {
		return createLabel(text, "default", Colors.BLACK_P1());
	}
	
	public static Label createLabel(String text, boolean debug) {
		return createLabel(text, "default", Colors.BLACK_P1(), null, debug);
	}
	
	public static Label createLabel(String text, TextAnimation textAnimation) {
		return createLabel(text, "default", Colors.BLACK_P1(), textAnimation);
	}
	
	public static Label createLabel(String text, TextAnimation textAnimation, boolean debug) {
		return createLabel(text, "default", Colors.BLACK_P1(), textAnimation, debug);
	}

	private static Label createLabel(String text, String styleId, Color color) {
		return createLabel(text, styleId, color, null);
	}
	
	private static Label createLabel(String text, String styleId, Color color, TextAnimation textAnimation) {
		return createLabel(text, styleId, color, textAnimation, false);
	}
	
	private static Label createLabel(String text, String styleId, Color color, TextAnimation textAnimation, boolean debug) {
		final String id;
		if(text == null || text.trim().isEmpty()) {
			id = null;
		} else {
			id = "Label: " + text;
		}
		Label label = new Label(id);
		label.setText(text);
		label.setStyleId(styleId);
		label.setColor(color);
		label.setVisibility(Visibility.VISIBLE);
		label.setTextAnimation(textAnimation);
		label.setDebugEnabled(debug);
		return label;
	}

	public static TextButton createButton(UiNavigation navigation, String text, ActionListener listener) {
		return createButton(navigation, text, false, listener);
	}
	
	public static TextButton createButton(UiNavigation navigation, String text, boolean debug, ActionListener listener) {
		final String id;
		if(text == null || text.isEmpty()) {
			id = null;
		} else {
			id = "TextButton: " + text;
		}
		TextButton button = new TextButton(id);
		button.setFlexLayout("flex-column:xs-12c");
		button.setText(text);
		if(listener != null) {
			button.addActionListener(listener);
		}
		button.setDebugEnabled(debug);
		button.setVisibility(Visibility.VISIBLE);
		
		if(navigation != null) {
			navigation.add(button);
		}
		return button;
	}
	
	public static TextBox createTextBox(UiNavigation navigation, String id, ActionListener listener) {
		TextBox textBox = new TextBox(id);
		textBox.setFlexLayout("flex-column:xs-12c");
		textBox.addActionListener(listener);
		textBox.setVisibility(Visibility.VISIBLE);
		
		if(navigation != null) {
			navigation.add(textBox);
		}
		return textBox;
	}
	
	public static Select<String> createSelect(UiNavigation navigation, String id, ActionListener listener) {
		Select<String> select = new Select<String>(id);
		select.setFlexLayout("flex-column:xs-12c");
		select.addActionListener(listener);
		select.setVisibility(Visibility.VISIBLE);
		
		if(navigation != null) {
			navigation.add(select);
		}
		return select;
	}
	
	public static Checkbox createCheckbox(UiNavigation navigation, String id, ActionListener listener) {
		Checkbox checkbox = new Checkbox(id);
		checkbox.addActionListener(listener);
		checkbox.setVisibility(Visibility.VISIBLE);
		
		if(navigation != null) {
			navigation.add(checkbox);
		}
		return checkbox;
	}
	
	public static RadioButton createRadioButton(UiNavigation navigation, String id, ActionListener listener) {
		RadioButton radioButton = new RadioButton(id);
		radioButton.addActionListener(listener);
		radioButton.setVisibility(Visibility.VISIBLE);
		
		if(navigation != null) {
			navigation.add(radioButton);
		}
		return radioButton;
	}
}
