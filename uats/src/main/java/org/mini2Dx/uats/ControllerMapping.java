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
package org.mini2Dx.uats;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.uats.util.ScreenIds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Utility for mapping controller input values
 */
public class ControllerMapping extends BasicGameScreen implements ControllerListener {
	private static final String LOGGING_TAG = ControllerMapping.class.getSimpleName();
	
	private String controllerName = "";
	private Map<Integer, String> buttonMessages = new HashMap<Integer, String>();
	private Map<Integer, String> axisMessages = new HashMap<Integer, String>();
	private Map<Integer, String> povMessages = new HashMap<Integer, String>();
	private Map<Integer, String> xSliderMessages = new HashMap<Integer, String>();
	private Map<Integer, String> ySliderMessages = new HashMap<Integer, String>();
	private Map<Integer, String> accelerometerMessages = new HashMap<Integer, String>();
	
	@Override
	public void initialise(GameContainer gc) {
		Controllers.addListener(this);
		for(Controller controller : Controllers.getControllers()) {
			Gdx.app.log(LOGGING_TAG, "Detected: " + controller.getName());
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Color.WHITE);
        g.setColor(Color.BLUE);
		
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
		return ScreenIds.getScreenId(ControllerMapping.class);
	}

	@Override
	public void connected(Controller controller) {
		controllerName = controller.getName();
	}

	@Override
	public void disconnected(Controller controller) {
		controllerName = controller.getName();
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		controllerName = controller.getName();
		buttonMessages.put(buttonCode, "DOWN");
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		buttonMessages.put(buttonCode, "UP");
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		axisMessages.put(axisCode, String.valueOf(value));
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		povMessages.put(povCode, value.toString());
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		xSliderMessages.put(sliderCode, String.valueOf(value));
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		ySliderMessages.put(sliderCode, String.valueOf(value));
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		accelerometerMessages.put(accelerometerCode, value.toString());
		return false;
	}

}
