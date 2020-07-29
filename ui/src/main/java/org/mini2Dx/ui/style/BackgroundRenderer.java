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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;

public abstract class BackgroundRenderer {
	protected final String imagePath;

	public BackgroundRenderer(String imagePath) {
		super();
		this.imagePath = imagePath;
	}

	public abstract void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager);

	public abstract void render(Graphics g, float x, float y, float width, float height);

	public static BackgroundRenderer parse(String value) {
		if(value == null || value.isEmpty()) {
			return new NoopBackgroundRenderer();
		}
		final String [] components = value.split(" ");
		if(components.length > 2) {
			throw new MdxException("Invalid background value: " + value);
		}
		final String imagePath = components[0];
		if(imagePath.isEmpty()) {
			throw new MdxException("Invalid image path in background: " + value);
		}
		final String [] typeComponents = (components.length > 1 ? components[1].toLowerCase() : "static").split(":");
		final String backgroundType = typeComponents[0];

		switch(backgroundType) {
		case "none":
			return new NoopBackgroundRenderer();
		case "ninepatch": {
			if(typeComponents.length == 1 || typeComponents[1].isEmpty()) {
				throw new MdxException(value + " is missing ninepatch args (left,right,top,bottom), e.g. " + value + ":4,4,2,2");
			}
			final String [] args = typeComponents[1].split(",");
			if(args.length != 4) {
				throw new MdxException(value + " has insufficient ninepatch args. Must be 4 args total");
			}
			return new NinePatchBackgroundRenderer(imagePath, Integer.parseInt(args[0].trim()),
					Integer.parseInt(args[1].trim()), Integer.parseInt(args[2].trim()),
					Integer.parseInt(args[3].trim()));
		}
		case "tiling":
			return new TilingBackgroundRenderer(imagePath);
		default:
		case "static":
			return new StaticBackgroundRenderer(imagePath);
		}
	}
}
