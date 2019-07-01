/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
		if(value.isEmpty()) {
			throw new MdxException("Missing background value");
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
