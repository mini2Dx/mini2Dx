/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.tiled;

import com.badlogic.gdx.math.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class TileLayerTest {
	private static final int LAYER_WIDTH = 100;
	private static final int LAYER_HEIGHT = 50;

	@Test
	public void testIsMostlyEmptyTiles() {
		final TileLayer mostlyFullLayer = TileLayer.create(LAYER_WIDTH, LAYER_HEIGHT);
		final TileLayer mostlyEmptyLayer = TileLayer.create(LAYER_WIDTH, LAYER_HEIGHT);

		fillLayer(mostlyFullLayer, getEmptyLayerThreshold());
		fillLayer(mostlyEmptyLayer, (LAYER_WIDTH * LAYER_HEIGHT) - getEmptyLayerThreshold());

		Assert.assertTrue(mostlyEmptyLayer.isMostlyEmptyTiles());
		Assert.assertFalse(mostlyFullLayer.isMostlyEmptyTiles());
	}

	private void fillLayer(TileLayer layer, int totalTiles) {
		int count = 0;
		for(int y = 0; y < LAYER_HEIGHT; y++) {
			for(int x = 0; x < LAYER_WIDTH; x++) {
				if(count >= totalTiles) {
					return;
				}

				layer.setTileId(x, y, 1);
				count++;
			}
		}
	}

	private int getEmptyLayerThreshold() {
		return MathUtils.round((LAYER_WIDTH * LAYER_HEIGHT) * TiledMap.FAST_RENDER_EMPTY_LAYERS_THRESHOLD);
	}
}
