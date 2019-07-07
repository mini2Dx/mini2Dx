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
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Div;

import org.mini2Dx.ui.element.Container;

import java.io.IOException;

/**
 *
 */
public class UiSerializationUAT extends BasicGameScreen {
	private static final String LOGGING_TAG = UiSerializationUAT.class.getSimpleName();
	
	private final AssetManager assetManager;
	
	private UiContainer uiContainer;
	private Container container;
	
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
			container = Mdx.xml.fromXml(Mdx.files.internal("ui.xml").reader(), Container.class);
		} catch (SerializationException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			container = null;
		} catch (IOException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			container = null;
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
		if(container == null) {
			failureMessage = "'container' is null";
			return false;
		}
		if(container.getElementById("flexRow") == null) {
			failureMessage = "'flexRow' is null";
			return false;
		}
		if(container.getElementById("column1") == null) {
			failureMessage = "'column1' is null";
			return false;
		}
		final Div div = (Div) container.getElementById("column1");
		
		if(container.getElementById("image") == null) {
			failureMessage = "'image' is null";
			
			if(div.getTotalChildren() == 0) {
				failureMessage += ". Element was not deserialized";
			} else if(!div.get(0).getId().equals("image")) {
				failureMessage += ". Id was stored as " + div.get(0).getId();
			}
			return false;
		}
		if(container.getElementById("label") == null) {
			failureMessage = "'label' is null";
			return false;
		}
		if(container.getElementById("textButton") == null) {
			failureMessage = "'textButton' is null";
			return false;
		}
		if(container.getElementById("imageButton") == null) {
			failureMessage = "'imageButton' is null";
			return false;
		}
		if(container.getElementById("checkbox") == null) {
			failureMessage = "'checkbox' is null";
			return false;
		}
		if(container.getElementById("progeessBar") == null) {
			failureMessage = "'progeessBar' is null";
			return false;
		}
		if(container.getElementById("radioButton") == null) {
			failureMessage = "'radioButton' is null";
			return false;
		}
		if(container.getElementById("scrollBox") == null) {
			failureMessage = "'scrollBox' is null";
			return false;
		}
		if(container.getElementById("select") == null) {
			failureMessage = "'select' is null";
			return false;
		}
		if(container.getElementById("slider") == null) {
			failureMessage = "'slider' is null";
			return false;
		}
		if(container.getElementById("tabView") == null) {
			failureMessage = "'tabView' is null";
			return false;
		}
		if(container.getElementById("textBox") == null) {
			failureMessage = "'textBox' is null";
			return false;
		}
		return true;
	}
}
