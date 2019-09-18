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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.LibgdxTextureRegionWrapper;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlasRegion;

public class LibgdxTextureAtlasRegion extends LibgdxTextureRegion implements TextureAtlasRegion {

    public final String name, texturePath;
    public final boolean rotate;
    public final int x, y;
    public final int width, height;
    public final int originalWidth, originalHeight;
    public final int offsetX, offsetY;
    public final int index;

    public LibgdxTextureAtlasRegion(Texture texture, String name, int index, int x, int y, int width, int height,
                                    boolean rotate, int originalWidth, int originalHeight, int offsetX, int offsetY) {
        super(new LibgdxTextureRegion(new LibgdxTextureRegionWrapper((LibgdxTexture) texture)), x, y, width, height);
        this.texturePath = "";
        this.name = name;
        this.index = index;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotate = rotate;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public LibgdxTextureAtlasRegion(String texturePath, String name, int index, int x, int y, int width, int height,
                                    boolean rotate, int originalWidth, int originalHeight, int offsetX, int offsetY) {
        super(new LibgdxTextureRegion(new LibgdxTextureRegionWrapper()));
        this.texturePath = texturePath;
        this.name = name;
        this.index = index;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotate = rotate;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public float getRotatedPackedWidth() {
        if (rotate) {
            return getRegionHeight();
        } else {
            return getRegionWidth();
        }
    }

    public float getRotatedPackedHeight() {
        if (rotate) {
            return getRegionWidth();
        } else {
            return getRegionHeight();
        }
    }

    public String getName() {
        return name;
    }

    public String getTexturePath() {
        return texturePath;
    }

    @Override
    public void setTexture(Texture texture) {
        super.setTexture(texture);
        super.setRegion(x, y + height, width, -height);
    }

    public int getIndex() {
        return index;
    }

    public float getPackedWidth() {
        return width;
    }

    public float getPackedHeight() {
        return height;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }
}
