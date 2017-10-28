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
package org.mini2Dx.tiled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.tiled.TiledMapLoader.TiledMapParameter;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * An {@link AssetLoader} implementation for loading {@link TiledMap} instances
 */
public class TiledMapLoader extends AsynchronousAssetLoader<TiledMap, TiledMapParameter> {
	private static final TiledMapParameter DEFAULT_PARAMETERS = new TiledMapParameter();
	
	private final Map<String, TiledMapData> tiledMapData = new ConcurrentHashMap<String, TiledMapData>();
	private final TiledParser tiledParser = new TiledParser();
	
	private TiledMap nextTiledMap;
	
	public TiledMapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TiledMapParameter parameter) {
		if(parameter == null) {
			parameter = DEFAULT_PARAMETERS;
		}
		nextTiledMap = new TiledMap(getTiledMapData(fileName, file), false, parameter.cacheLayers);
	}

	@Override
	public TiledMap loadSync(AssetManager manager, String fileName, FileHandle file, TiledMapParameter parameter) {
		if(parameter == null) {
			parameter = DEFAULT_PARAMETERS;
		}
		nextTiledMap.loadTilesetTextures(manager);
		return nextTiledMap;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TiledMapParameter parameter) {
		if(parameter == null) {
			parameter = DEFAULT_PARAMETERS;
		}
		if(!parameter.loadTilesets) {
			return null;
		}
		return getTiledMapData(fileName, file).getDependencies();
	}
	
	private TiledMapData getTiledMapData(String fileName, FileHandle file) {
		if(!tiledMapData.containsKey(fileName)) {
			tiledMapData.put(fileName, new TiledMapData(tiledParser, file));
		}
		return tiledMapData.get(fileName);
	}
	
	static public class TiledMapParameter extends AssetLoaderParameters<TiledMap> {
		boolean loadTilesets = true;
		boolean cacheLayers = false;
	}
}
