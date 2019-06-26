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

import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.TiledMapLoader.TiledMapParameter;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An {@link AssetLoader} implementation for loading {@link TiledMap} instances
 */
public class TiledMapLoader extends AsynchronousAssetLoader<TiledMap, TiledMapParameter> {
	private static final TiledMapParameter DEFAULT_PARAMETERS = new TiledMapParameter();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final ObjectMap<String, TiledMapData> tiledMapData = new ObjectMap<String, TiledMapData>();
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
		if(parameter.cacheLayers) {
			nextTiledMap = null;
			return;
		}
		loadNextTiledMap(fileName, file, parameter);
	}

	@Override
	public TiledMap loadSync(AssetManager manager, String fileName, FileHandle file, TiledMapParameter parameter) {
		if(parameter == null) {
			parameter = DEFAULT_PARAMETERS;
		}
		final TiledMap result = loadNextTiledMap(fileName, file, parameter);
		result.loadTilesetTextures(manager);
		this.nextTiledMap = null;
		return result;
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

	private TiledMap loadNextTiledMap(String fileName, FileHandle file, TiledMapParameter parameter) {
		if(nextTiledMap == null) {
			nextTiledMap = new TiledMap(getTiledMapData(fileName, file), false);
		}
		return nextTiledMap;
	}
	
	private TiledMapData getTiledMapData(String fileName, FileHandle file) {
		lock.readLock().lock();
		final TiledMapData result;
		if(!tiledMapData.containsKey(fileName)) {
			lock.readLock().unlock();
			lock.writeLock().lock();
			result = new TiledMapData(tiledParser, file);
			tiledMapData.put(fileName, result);
			lock.writeLock().unlock();
		} else {
			result = tiledMapData.get(fileName);
			lock.readLock().unlock();
		}
		return result;
	}
	
	static public class TiledMapParameter extends AssetLoaderParameters<TiledMap> {
		public boolean loadTilesets = true;
		public boolean cacheLayers = false;
	}
}
