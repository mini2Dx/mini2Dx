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
using Java.Util.Concurrent;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Executor;
using Org.Mini2Dx.Core.Files;

namespace monogame.Audio
{
    public sealed class MonoGameAsyncSoundResult : AsyncSoundResult
    {
        private class AsyncSoundCallable : Callable
        {
            FileHandle fileHandle;

            public AsyncSoundCallable(FileHandle fileHandle)
            {
                this.fileHandle = fileHandle;
            }

            public object call_6069C574()
            {
                return new MonoGameSound(fileHandle);
            }
        }

        public MonoGameAsyncSoundResult(FileHandle handle)
        {
            _init_88D76E6E(handle);
        }

        public override Callable asyncReadFile_8463974D(FileHandle arg0)
        {
            return new AsyncSoundCallable(arg0);
        }

        public override Sound makeSound_C7580A69(AsyncResult arg0)
        {
            return (MonoGameSound) arg0.getResult_6069C574();
        }
    }
}