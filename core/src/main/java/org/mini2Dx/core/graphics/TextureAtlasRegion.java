/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.graphics;

public interface TextureAtlasRegion extends TextureRegion {
	/**
	 * Returns the name (path) of the region up to the first underscore (underscores are special instructions to packer)
	 * @return
	 */
	public String getName();

	/**
	 * Returns the name (path) of the texture used by this region
	 * @return
	 */
	public String getTexturePath();

	/**
	 * The number at the end of the original filename
	 * @return -1 if none
	 */
	public int getIndex();

	/**
	 * Width of the image after whitespace was removed for packing
	 * @return
	 */
	public float getPackedWidth();

	/**
	 * Height of the image after whitespace was removed for packing
	 * @return
	 */
	public float getPackedHeight();

	/**
	 * Width of the image before whitespace was removed for packing
	 * @return
	 */
	public float getOriginalWidth();

	/**
	 * Height of the image before whitespace was removed for packing
	 * @return
	 */
	public float getOriginalHeight();

	/**
	 * Offset from the left of the original image to the left of the packed image after whitespace was removed for packing
	 * @return
	 */
	public float getOffsetX();

	/**
	 * Offset from the top of the original image to the top of the packed image after whitespace was removed for packing
	 * @return
	 */
	public float getOffsetY();

	/**
	 * Returns the packed width considering the rotate value, if it is true then it returns the packedHeight, otherwise it
	 * returns the packedWidth.
	 */
	public float getRotatedPackedWidth();

	/**
	 * Returns the packed height considering the rotate value, if it is true then it returns the packedWidth, otherwise it
	 * returns the packedHeight.
	 */
	public float getRotatedPackedHeight();
}
