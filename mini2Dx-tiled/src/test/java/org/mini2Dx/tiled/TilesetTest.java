package org.mini2Dx.tiled;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link Tileset}
 *
 * @author Thomas Cashman
 */
public class TilesetTest {
	private Tileset tileset;
	
	@Before
	public void setup() {
		tileset = new Tileset();
		tileset.setTileWidth(32);
		tileset.setTileHeight(32);
		tileset.setWidth(128);
		tileset.setHeight(128);
		tileset.setFirstGid(1);
	}
	
	@Test
	public void testGetTileX() {
		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(32));
	}
	
	@Test
	public void testGetTileXWithSpacing() {
		tileset.setSpacing(4);
		
		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(4));
		Assert.assertEquals(1, tileset.getTileX(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(31));
		Assert.assertEquals(1, tileset.getTileX(32));
	}
	
	@Test
	public void testGetTileXWithMargin() {
		tileset.setMargin(4);
		
		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(4));
		Assert.assertEquals(1, tileset.getTileX(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(31));
		Assert.assertEquals(1, tileset.getTileX(32));
	}
	
	@Test
	public void testGetTileY() {
		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));
	}
	
	@Test
	public void testGetTileYWithSpacing() {
		tileset.setSpacing(4);
		
		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));
	}
	
	@Test
	public void testGetTileYWithMargin() {
		tileset.setMargin(4);
		
		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));
		tileset.setFirstGid(28);
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));
	}
	
	@Test
	public void testGetWidthInTiles() {
		Assert.assertEquals(4, tileset.getWidthInTiles());
		tileset.setWidth(96);
		Assert.assertEquals(3, tileset.getWidthInTiles());
	}
	
	@Test
	public void testGetWidthInTilesWithSpacing() {
		tileset.setSpacing(4);
		
		Assert.assertEquals(3, tileset.getWidthInTiles());
		tileset.setWidth(96);
		Assert.assertEquals(2, tileset.getWidthInTiles());
	}
	
	@Test
	public void testGetWidthInTilesWithMargin() {
		tileset.setMargin(4);
		
		Assert.assertEquals(3, tileset.getWidthInTiles());
		tileset.setWidth(96);
		Assert.assertEquals(2, tileset.getWidthInTiles());
	}
	
	@Test
	public void testGetWidthInTilesWithSpacingAndMargin() {
		tileset.setSpacing(4);
		tileset.setMargin(16);
		
		Assert.assertEquals(2, tileset.getWidthInTiles());
		tileset.setWidth(96);
		Assert.assertEquals(1, tileset.getWidthInTiles());
	}
	
	@Test
	public void testGetHeightInTiles() {
		Assert.assertEquals(4, tileset.getHeightInTiles());
		tileset.setHeight(96);
		Assert.assertEquals(3, tileset.getHeightInTiles());
	}
	
	@Test
	public void testGetHeightInTilesWithSpacing() {
		tileset.setSpacing(4);
		
		Assert.assertEquals(3, tileset.getHeightInTiles());
		tileset.setHeight(96);
		Assert.assertEquals(2, tileset.getHeightInTiles());
	}
	
	@Test
	public void testGetHeightInTilesWithMargin() {
		tileset.setMargin(4);
		
		Assert.assertEquals(3, tileset.getHeightInTiles());
		tileset.setHeight(96);
		Assert.assertEquals(2, tileset.getHeightInTiles());
	}
	
	@Test
	public void testGetHeightInTilesWithSpacingAndMargin() {
		tileset.setSpacing(4);
		tileset.setMargin(16);
		
		Assert.assertEquals(2, tileset.getHeightInTiles());
		tileset.setHeight(96);
		Assert.assertEquals(1, tileset.getHeightInTiles());
	}
}
