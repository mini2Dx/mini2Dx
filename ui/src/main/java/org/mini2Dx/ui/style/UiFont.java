/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.style;

import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 *
 */
public class UiFont {
	@Field
	private String path;
	
	private FreeTypeFontGenerator fontGenerator;
	
	public void prepareAssets(FileHandleResolver fileHandleResolver) {
		fontGenerator = new FreeTypeFontGenerator(fileHandleResolver.resolve(path));
	}
	
	public void dispose() {
		fontGenerator.dispose();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public FreeTypeFontGenerator getFontGenerator() {
		return fontGenerator;
	}
}
