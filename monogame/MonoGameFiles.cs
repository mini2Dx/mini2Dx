/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
using System;
using System.IO;
using Microsoft.Xna.Framework.Content;
using monogame.Files;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Files;

namespace monogame
{
    public class MonoGameFiles : global::Java.Lang.Object, Org.Mini2Dx.Core._Files
    {
        private readonly string _internalFilePrefix;
        internal readonly MonoGameContentManager _contentManager;

        public MonoGameFiles(ContentManager contentManager)
        {
            _internalFilePrefix = contentManager.RootDirectory + Path.DirectorySeparatorChar;
            _contentManager = new MonoGameContentManager(contentManager.ServiceProvider, contentManager.RootDirectory);
        }

        public FileHandle internal_1F3F44D2(Java.Lang.String path)
        {
            return new MonoGameFileHandle(_internalFilePrefix, path, FileType.INTERNAL_);
        }

        public bool isExternalStorageAvailable_FBE0B2A4()
        {
            return true;
        }

        public FileHandle external_1F3F44D2(Java.Lang.String path)
        {
            return new MonoGameFileHandle("", path, FileType.EXTERNAL_);
        }

        public bool isLocalStorageAvailable_FBE0B2A4()
        {
            return false;
        }

        public FileHandle local_1F3F44D2(Java.Lang.String path)
        {
            throw new NotImplementedException();
        }
    }
}
