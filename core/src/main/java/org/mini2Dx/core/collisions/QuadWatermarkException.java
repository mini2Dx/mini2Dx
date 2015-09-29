/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.collisions;

/**
 *
 */
public class QuadWatermarkException extends RuntimeException {
	private static final long serialVersionUID = 3266210496163675808L;

	public QuadWatermarkException(int elementsPerQuad, int mergeWatermark) {
		super("Quad merge watermark must be lower than element limit. [Elements per quad: " + elementsPerQuad
				+ "][Merge watermark: " + mergeWatermark + "]");
	}
}
