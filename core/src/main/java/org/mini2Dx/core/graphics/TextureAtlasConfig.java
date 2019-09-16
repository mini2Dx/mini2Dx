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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;
import java.util.HashMap;

public class TextureAtlasConfig {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public HashMap<String, Texture> textures = new HashMap<>();
    public Array<TextureAtlasRegion> atlasRegions = new Array<>();

    public TextureAtlasConfig(FileHandle packFile, FileHandle imagesDir){
        String[] lines;
        try {
            lines = packFile.readAllLines();
        } catch (IOException e) {
            throw new MdxException(e.toString());
        }
        int i = 1;
        while (i < lines.length){
            String texturePath = imagesDir.child(lines[i]).path();
            textures.put(texturePath, null);
            i+=5;
            while (i < lines.length && !lines[i].isEmpty()){
                int index, x, y, width, height, originalWidth, originalHeight, offsetX, offsetY;
                String name = lines[i];
                boolean rotate = Boolean.parseBoolean(lines[i + 1].split(":")[1].trim());
                int[] xyTuple = readTuple(lines[i + 2]);
                x = xyTuple[0];
                y = xyTuple[1];
                int[] whTuple = readTuple(lines[i + 3]);
                width = whTuple[0];
                height = whTuple[1];
                int[] originalWhTuple = readTuple(lines[i + 4]);
                originalWidth = originalWhTuple[0];
                originalHeight = originalWhTuple[1];
                int[] offsetTuple = readTuple(lines[i + 5]);
                offsetX = offsetTuple[0];
                offsetY = offsetTuple[1];
                index = Integer.parseInt(lines[i + 6].split(":")[1].trim());

                atlasRegions.add(Mdx.graphics.newTextureAtlasRegion(texturePath, name, index, x, y, width, height, rotate, originalWidth, originalHeight, offsetX, offsetY));
                i += 7;
            }
            i++;
        }
    }

    private static int[] readTuple(String s){
        String[] tuple = s.split(":")[1].split(",");
        return new int[]{Integer.parseInt(tuple[0].trim()), Integer.parseInt(tuple[1].trim())};
    }

    public TextureAtlasConfig(FileHandle packFile){
        this(packFile, packFile.parent());
    }

    public String[] getDependencies(){
        return textures.keySet().toArray(EMPTY_STRING_ARRAY);
    }

}
