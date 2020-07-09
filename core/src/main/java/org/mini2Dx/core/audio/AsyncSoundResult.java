/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.core.audio;

import org.mini2Dx.core.Audio;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.executor.AsyncResult;
import org.mini2Dx.core.files.FileHandle;

import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class AsyncSoundResult implements AsyncResult<Sound> {
    protected final FileHandle handle;
    private final AsyncResult<Object> result;
    private Sound cached;

    public AsyncSoundResult(FileHandle file) {
        handle = file;
        result = Mdx.executor.submit(asyncReadFile(file));
    }

    /**
     * Returns if the sound has been loaded.
     *
     * @return False if the sound is still loading
     */
    @Override
    public boolean isFinished() {
        return result.isFinished();
    }

    /**
     * Returns the {@link Sound} object. If the platform doesn't support async sound loading (see {@link Audio#newAsyncSound(FileHandle)})
     * the sound object is cached for future access.
     *
     * @return the {@link Sound} object
     */
    @Override
    public Sound getResult() {
        if (!isFinished()){
            return null;
        }
        if (cached != null) {
            return cached;
        }
        Sound s = makeSound(result);
        if (s != null){
            cached = s;
            return s;
        }
        try {
            Mdx.log.info("WARNING", "Async sound loading not supported on this platform, falling back to loading on main thread...");
            cached = Mdx.audio.newSound(handle);
            return cached;
        } catch (IOException e) {
            throw new MdxException("Failed to load sound on game thread: ", e);
        }
    }

    /**
     * Returns a {@link Callable} which does the I/O required to load the sound.
     * @param file The {@link FileHandle} to load the audio from
     * @return a {@link Callable} doing the required I/O.
     */
    protected abstract Callable<Object> asyncReadFile(FileHandle file);

    /**
     * Instantiates a {@link Sound} object on the current thread.
     * @param result an {@link AsyncResult} containing the results of {{@link #asyncReadFile(FileHandle)}}
     * @return the new {@link Sound} object
     */
    protected abstract Sound makeSound(AsyncResult<Object> result);
}
