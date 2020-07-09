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
            private FileHandle _handle;
            
            public AsyncSoundCallable(FileHandle handle)
            {
                _handle = handle;
            }
            
            public object call()
            {
                return new MonoGameSound(_handle);
            }
        }

        public MonoGameAsyncSoundResult(FileHandle handle)
        {
            _init_(handle);
        }

        public override Callable asyncReadFile(FileHandle arg0)
        {
            return new AsyncSoundCallable(handle_);
        }

        public override Sound makeSound(AsyncResult arg0)
        {
            return (Sound) arg0.getResult();
        }
    }
}