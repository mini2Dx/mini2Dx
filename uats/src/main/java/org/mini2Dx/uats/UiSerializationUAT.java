/**
 * Copyright 2018 Thomas Cashman
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.AlignedModal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class UiSerializationUAT extends BasicGameScreen {
	private static final String LOGGING_TAG = UiSerializationUAT.class.getSimpleName();
	
	private final AssetManager assetManager;
	
	private UiContainer uiContainer;
	private AlignedModal modal;
	
	private boolean success = false;
	private String failureMessage = "";

	public UiSerializationUAT(AssetManager assetManager) {
		super();
		this.assetManager = assetManager;
	}

	@Override
	public void initialise(GameContainer gc) {
		uiContainer = new UiContainer(gc, assetManager);
		
		try {
			modal = Mdx.xml.fromXml(Gdx.files.internal("ui.xml").reader(), AlignedModal.class);
		} catch (SerializationException e) {
			Gdx.app.error(LOGGING_TAG, e.getMessage(), e);
			modal = null;
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		success = isSuccess();
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if(success) {
			g.drawString("Success", 32f, 32f);
		} else {
			g.drawString("Failed", 32f, 32f);
			
			if(failureMessage == null) {
				return;
			}
			g.drawString(failureMessage, 32f, 64f);
		}
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(UiSerializationUAT.class);
	}

	private boolean isSuccess() {
		if(modal == null) {
			failureMessage = "'modal' is null";
			return false;
		}
		if(modal.getElementById("row") == null) {
			failureMessage = "'row' is null";
			return false;
		}
		if(modal.getElementById("column1") == null) {
			failureMessage = "'column1' is null";
			return false;
		}
		if(modal.getElementById("image") == null) {
			failureMessage = "'image' is null";
			return false;
		}
		if(modal.getElementById("label") == null) {
			failureMessage = "'label' is null";
			return false;
		}
		if(modal.getElementById("textButton") == null) {
			failureMessage = "'textButton' is null";
			return false;
		}
		if(modal.getElementById("imageButton") == null) {
			failureMessage = "'imageButton' is null";
			return false;
		}
		if(modal.getElementById("checkbox") == null) {
			failureMessage = "'checkbox' is null";
			return false;
		}
		if(modal.getElementById("progeessBar") == null) {
			failureMessage = "'progeessBar' is null";
			return false;
		}
		if(modal.getElementById("radioButton") == null) {
			failureMessage = "'radioButton' is null";
			return false;
		}
		if(modal.getElementById("scrollBox") == null) {
			failureMessage = "'scrollBox' is null";
			return false;
		}
		if(modal.getElementById("select") == null) {
			failureMessage = "'select' is null";
			return false;
		}
		if(modal.getElementById("slider") == null) {
			failureMessage = "'slider' is null";
			return false;
		}
		if(modal.getElementById("tabView") == null) {
			failureMessage = "'tabView' is null";
			return false;
		}
		if(modal.getElementById("textBox") == null) {
			failureMessage = "'textBox' is null";
			return false;
		}
		return true;
	}
}
