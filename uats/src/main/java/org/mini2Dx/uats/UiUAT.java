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
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;
import org.mini2Dx.uats.util.UiUtils;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.element.Dialog;
import org.mini2Dx.ui.element.Frame;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 * A user acceptance test for the mini2Dx responsive UI framework
 */
public class UiUAT extends BasicGameScreen {
	private final AssetManager assetManager;
	
	private UiContainer uiContainer;
	private Frame topLeftFrame, bottomRightFrame;
	private Dialog dialog;
	
	private Select<String> select;
	private TextBox textBox;
	private Label textBoxResult;
	
	private int nextScreenId = -1;

	public UiUAT(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
		initialiseUi();
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		uiContainer.update(delta);
		if (nextScreenId > -1) {
			screenManager.enterGameScreen(nextScreenId, new FadeOutTransition(), new FadeInTransition());
			nextScreenId = -1;
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		uiContainer.interpolate(alpha);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		uiContainer.render(g);
	}
	
	@Override
	public void preTransitionIn(Transition transitionIn) {
		nextScreenId = -1;
    	if(!uiContainer.isThemeApplied()) {
    		uiContainer.applyTheme(UiTheme.DEFAULT_THEME_FILE);
    	}
    	Gdx.input.setInputProcessor(uiContainer);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(UiUAT.class);
	}

	private void initialiseUi() {
		topLeftFrame = new Frame();
		topLeftFrame.setXRules("xs-0");
		topLeftFrame.setYRules("top");
		topLeftFrame.setWidthRules("xs-12 sm-6 md-4 lg-3");
		topLeftFrame.addRow(Row.withElements(UiUtils.createHeader("UI UAT")));
		topLeftFrame.setVisible(true);
		uiContainer.add(topLeftFrame);
		
		textBox = UiUtils.createTextBox("xs-0", "xs-6", new ActionListener() {
			
			@Override
			public void onActionEnd(Actionable source) {
				textBoxResult.setText(textBox.getText());
			}
			
			@Override
			public void onActionBegin(Actionable source) {
			}
		});
		textBoxResult = UiUtils.createLabel("", "xs-6", "xs-6");
		select = UiUtils.createSelect("xs-3", "xs-6", new ActionListener() {
			
			@Override
			public void onActionEnd(Actionable source) {
				System.out.println("Selected value: " + select.getSelectedItem().getValue());
			}
			
			@Override
			public void onActionBegin(Actionable source) {
			}
		});
		select.addOption("Item 1", "1");
		select.addOption("Item 2", "2");
		select.addOption("Item 3", "3");
		
		dialog = new Dialog();
		dialog.setXRules("auto");
		dialog.setYRules("auto");
		dialog.setWidthRules("xs-12 md-8 lg-6");
		dialog.addRow(Row.withElements(textBox, textBoxResult));
		dialog.addRow(Row.withElements(select));
		dialog.addRow(Row.withElements(UiUtils.createButton("Return to UAT Selection Screen", "xs-0 md-4 xl-6", "xs-12 md-8 xl-6", new ActionListener() {
			
			@Override
			public void onActionBegin(Actionable source) {}
			
			@Override
			public void onActionEnd(Actionable source) {
				nextScreenId = UATSelectionScreen.SCREEN_ID;
			}
		})));
		
		dialog.setControllerHint(0, textBox);
		dialog.setControllerHint(1, select);
		dialog.setVisible(true);
		uiContainer.add(dialog);
		uiContainer.setActiveDialog(dialog);
		
		bottomRightFrame = new Frame();
		bottomRightFrame.setXRules("xs-0 sm-6 md-8 lg-9");
		bottomRightFrame.setYRules("bottom");
		bottomRightFrame.setWidthRules("xs-12 sm-6 md-4 lg-3");
		bottomRightFrame.setVisible(true);
		bottomRightFrame.addRow(Row.withElements(UiUtils.createHeader("Detected OS: " + Mdx.os)));
		uiContainer.add(bottomRightFrame);
	}
}
