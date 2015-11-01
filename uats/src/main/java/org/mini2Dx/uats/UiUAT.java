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

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.assets.AssetManager;

/**
 * A user acceptance test for the mini2Dx responsive UI framework
 */
public class UiUAT extends BasicGameScreen {
	private final AssetManager assetManager;
	
	private UiContainer uiContainer;

	public UiUAT(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void preTransitionIn(Transition transitionIn) {
		if(!uiContainer.isThemeApplied()) {
    		uiContainer.applyTheme(UiTheme.DEFAULT_THEME_FILE);
    	}
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(UiUAT.class);
	}

}
