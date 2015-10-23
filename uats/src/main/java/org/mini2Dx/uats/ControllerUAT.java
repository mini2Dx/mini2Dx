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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.controller.MdxController;
import org.mini2Dx.core.exception.ControllerPlatformException;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.uats.controller.ControllerDebugger;
import org.mini2Dx.uats.controller.Xbox360ControllerDebugger;
import org.mini2Dx.uats.controller.XboxOneControllerDebugger;
import org.mini2Dx.uats.util.ScreenIds;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

/**
 * UAT for debugging {@link MdxController}s
 */
public class ControllerUAT extends BasicGameScreen {
	private ControllerDebugger controllerDebugger;

	@Override
	public void initialise(GameContainer gc) {
		for(Controller controller : Controllers.getControllers()) {
			switch(Mdx.controllers.getControllerType(controller)) {
			case XBOX_360:
				Xbox360ControllerDebugger xbox360Debugger = new Xbox360ControllerDebugger();
				try {
					Mdx.controllers.xbox360(controller).addListener(xbox360Debugger);
					this.controllerDebugger = xbox360Debugger;
				} catch (ControllerPlatformException e) {
					e.printStackTrace();
				}
				return;
			case XBOX_ONE:
				XboxOneControllerDebugger xboxOneDebugger = new XboxOneControllerDebugger();
				try {
					Mdx.controllers.xboxOne(controller).addListener(xboxOneDebugger);
					this.controllerDebugger = xboxOneDebugger;
				} catch (ControllerPlatformException e) {
					e.printStackTrace();
				}
				return;
			case UNKNOWN:
			default:
				controllerDebugger = new ControllerDebugger() {
					@Override
					public void render(Graphics g) {
						g.drawString("Unknown controller", 32f, 32f);
					}
				};
				break;
			}
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
		if(controllerDebugger == null) {
			return;
		}
		controllerDebugger.render(g);
	}

	@Override
    public int getId() {
    	return ScreenIds.getScreenId(ControllerUAT.class);
    }
}
