/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.core.data;

import com.badlogic.gdx.files.FileHandle;

/**
 *
 * @author Thomas Cashman
 */
public interface Data {

	public <T> T read(Class<T> clazz, String filename);
	
	public <T> void write(T object, String filename);
}
