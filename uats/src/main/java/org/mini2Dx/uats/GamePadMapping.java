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
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.PovState;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.math.Vector3;
import org.mini2Dx.uats.util.ScreenIds;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility for mapping controller input values
 */
public class GamePadMapping extends BasicGameScreen implements GamePadListener {
	private static final String LOGGING_TAG = GamePadMapping.class.getSimpleName();
	
	private String controllerName = "";
	private Map<Integer, String> buttonMessages = new HashMap<Integer, String>();
	private Map<Integer, String> axisMessages = new HashMap<Integer, String>();
	private Map<Integer, String> povMessages = new HashMap<Integer, String>();
	private Map<Integer, String> xSliderMessages = new HashMap<Integer, String>();
	private Map<Integer, String> ySliderMessages = new HashMap<Integer, String>();
	private Map<Integer, String> accelerometerMessages = new HashMap<Integer, String>();
	
	@Override
	public void initialise(GameContainer gc) {
		for(GamePad gamePad : Mdx.input.getGamePads()) {
			Mdx.log.info(LOGGING_TAG, "Detected: " + gamePad.getInstanceId());
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.WHITE());
        g.setColor(Colors.BLUE());
		
		float y = 24f;
		float x = 32f;
		g.drawString("Controller Name: " + controllerName, 32f, y);
		y = incrementY(gc, y);
		
		for(int buttonCode : buttonMessages.keySet()) {
			g.drawString("Button " + buttonCode + ": " + buttonMessages.get(buttonCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
		
		for(int axisCode : axisMessages.keySet()) {
			g.drawString("Axis " + axisCode + ": " + axisMessages.get(axisCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
		
		for(int povCode : povMessages.keySet()) {
			g.drawString("POV " + povCode + ": " + povMessages.get(povCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
		
		for(int xSliderCode : xSliderMessages.keySet()) {
			g.drawString("xSlider " + xSliderCode + ": " + xSliderMessages.get(xSliderCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
		
		for(int ySliderCode : ySliderMessages.keySet()) {
			g.drawString("ySlider " + ySliderCode + ": " + ySliderMessages.get(ySliderCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
		
		for(int accelerometerCode : accelerometerMessages.keySet()) {
			g.drawString("Accelerometer " + accelerometerCode + ": " + accelerometerMessages.get(accelerometerCode), x, y);
			y = incrementY(gc, y);
			x = determineX(gc, x, y);
		}
	}
	
	private float incrementY(GameContainer gc, float y) {
		if(y >= gc.getHeight() - 48f) {
			y = 24f;
		}
		return y + 24f;
	}
	
	private float determineX(GameContainer gc, float x, float y) {
		if(y <= 24f) {
			return x + 256f;
		}
		return x;
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(GamePadMapping.class);
	}

	@Override
	public void onConnect(GamePad gamePad) {
		controllerName = gamePad.getInstanceId();
	}

	@Override
	public void onDisconnect(GamePad gamePad) {
		controllerName = gamePad.getInstanceId();
	}

	@Override
	public void onButtonDown(GamePad gamePad, int buttonCode) {
		controllerName = gamePad.getInstanceId();
		buttonMessages.put(buttonCode, "DOWN");
	}

	@Override
	public void onButtonUp(GamePad gamePad, int buttonCode) {
		controllerName = gamePad.getInstanceId();
		buttonMessages.put(buttonCode, "UP");
	}

	@Override
	public void onPovChanged(GamePad gamePad, int povCode, PovState povState) {
		controllerName = gamePad.getInstanceId();
		povMessages.put(povCode, povState.toString());
	}

	@Override
	public void onAxisChanged(GamePad gamePad, int axisCode, float axisValue) {
		controllerName = gamePad.getInstanceId();
		axisMessages.put(axisCode, String.valueOf(axisValue));
	}

	@Override
	public void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value) {
		controllerName = gamePad.getInstanceId();
		accelerometerMessages.put(accelerometerCode, value.toString());
	}
}
