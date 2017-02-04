/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.core.di.exception;

/**
 *
 */
public class PostInjectException extends RuntimeException {
	private static final long serialVersionUID = -3527429226914949481L;

	public PostInjectException() {
		super("Cannot invoke @PostInject on methods with parameters");
	}
}
