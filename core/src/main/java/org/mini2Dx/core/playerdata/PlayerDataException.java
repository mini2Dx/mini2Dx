/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.playerdata;

import org.mini2Dx.core.serialization.SerializationException;

/**
 *
 * @author Thomas Cashman
 */
public class PlayerDataException extends Exception {
	private static final long serialVersionUID = 1489776657559713023L;

	public PlayerDataException(String message) {
		super(message);
	}
	
	public PlayerDataException(SerializationException exception) {
		super(exception.getMessage(), exception);
	}
}
