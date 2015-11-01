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
package org.mini2Dx.ui.theme;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.ui.theme.UiThemeLoader.UiThemeParameter;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * An {@link AssetLoader} for {@link UiTheme}s
 */
public class UiThemeLoader extends AsynchronousAssetLoader<UiTheme, UiThemeParameter> {
	private volatile UiTheme theme;
	
	public UiThemeLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, UiThemeParameter parameter) {
		try {
			if(theme == null) {
				theme = Mdx.json.fromJson(file, UiTheme.class);
			}
			theme.validate();
		} catch (SerializationException e) {
			throw new MdxException(e.getMessage(), e);
		}
	}

	@Override
	public UiTheme loadSync(AssetManager manager, String fileName, FileHandle file, UiThemeParameter parameter) {
		UiTheme theme = this.theme;
		this.theme = null;
		theme.prepareAssets(manager);
		return theme;
	}
	
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, UiThemeParameter parameter) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		try {
			if(theme == null) {
				theme = Mdx.json.fromJson(file, UiTheme.class);
			}
			theme.loadDependencies(dependencies);
		} catch (SerializationException e) {
			throw new MdxException(e.getMessage(), e);
		}
		return dependencies;
	}
	
	static public class UiThemeParameter extends AssetLoaderParameters<UiTheme> {
	}
}
