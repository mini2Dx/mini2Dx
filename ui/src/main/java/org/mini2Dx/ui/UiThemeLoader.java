/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.ui.UiThemeLoader.UiThemeParameter;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class UiThemeLoader extends AsynchronousAssetLoader<UiTheme, UiThemeParameter> {
	private volatile UiTheme theme;
	private final FileHandleResolver fileHandleResolver;
	
	public UiThemeLoader(FileHandleResolver resolver) {
		super(resolver);
		this.fileHandleResolver = resolver;
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
		theme.prepareAssets(fileHandleResolver, manager);
		return theme;
	}
	
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, UiThemeParameter parameter) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		try {
			if(theme == null) {
				theme = Mdx.json.fromJson(file, UiTheme.class);
			}
		} catch (SerializationException e) {
			throw new MdxException(e.getMessage(), e);
		}
		theme.loadDependencies(dependencies);
		return dependencies;
	}
	
	static public class UiThemeParameter extends AssetLoaderParameters<UiTheme> {
	}
}
