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
package org.mini2Dx.uats.controller;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.controller.PS4Controller;
import org.mini2Dx.core.controller.button.PS4Button;
import org.mini2Dx.core.controller.ps4.PS4ControllerListener;
import org.mini2Dx.core.graphics.Graphics;

/**
 * Shows input from {@link PS4Controller}
 */
public class PS4ControllerDebugger implements PS4ControllerListener, ControllerDebugger {
	private Map<PS4Button, String> buttonMessages = new HashMap<PS4Button, String>();
	private float leftStickXAxis, leftStickYAxis;
	private float rightStickXAxis, rightStickYAxis;
	private float l2, r2;
	private boolean connected = true;
	
	@Override
	public void render(Graphics g) {
		g.drawString("PS4 Controller", 32f, 24f);
		if(connected) {
			g.drawString("Connected", 32f, 48f);
		} else {
			g.drawString("Disconnected", 32f, 48f);
		}
		g.drawString("Left Stick X: " + leftStickXAxis, 32f, 72f);
		g.drawString("Left Stick Y: " + leftStickYAxis, 32f, 96f);
		g.drawString("Right Stick X: " + rightStickXAxis, 32f, 120f);
		g.drawString("Right Stick Y: " + rightStickYAxis, 32f, 144f);
		g.drawString("L2: " + l2, 32f, 168f);
		g.drawString("R2: " + r2, 32f, 192f);
		
		float y = 72f;
		for(PS4Button button : buttonMessages.keySet()) {
			g.drawString(button.name() + ": " + buttonMessages.get(button), 256f, y);
			y += 24f;
		}
	}
	
	@Override
	public void disconnected(PS4Controller controller) {
		connected = false;
	}
	
	@Override
	public boolean buttonDown(PS4Controller controller, PS4Button button) {
		buttonMessages.put(button, "DOWN");
		return false;
	}
	
	@Override
	public boolean buttonUp(PS4Controller controller, PS4Button button) {
		buttonMessages.put(button, "UP");
		return false;
	}
	
	@Override
	public boolean leftStickXMoved(PS4Controller controller, float value) {
		leftStickXAxis = value;
		return false;
	}

	@Override
	public boolean leftStickYMoved(PS4Controller controller, float value) {
		leftStickYAxis = value;
		return false;
	}

	@Override
	public boolean rightStickXMoved(PS4Controller controller, float value) {
		rightStickXAxis = value;
		return false;
	}

	@Override
	public boolean rightStickYMoved(PS4Controller controller, float value) {
		rightStickYAxis = value;
		return false;
	}
	
	@Override
	public boolean l2Moved(PS4Controller controller, float value) {
		l2 = value;
		return false;
	}
	
	@Override
	public boolean r2Moved(PS4Controller controller, float value) {
		r2 = value;
		return false;
	}
}
