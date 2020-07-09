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
package org.mini2Dx.libgdx.audio;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.audio.AsyncSoundResult;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.executor.AsyncResult;
import org.mini2Dx.core.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class LibgdxAsyncSoundResult extends AsyncSoundResult {

    public LibgdxAsyncSoundResult(FileHandle file){
        super(file);
    }

    @Override
    protected Callable<Object> asyncReadFile(FileHandle file) {
        return new Callable<Object>() {
            @Override
            public InputStream call() throws Exception {
                return new ByteArrayInputStream(file.readBytes());
            }
        };
    }

    @Override
    protected Sound makeSound(AsyncResult result) {
        try {
            return (Sound) Gdx.audio.getClass().getMethod("newSound", InputStream.class, String.class).invoke(Gdx.audio, result.getResult(), handle.path());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }
}
