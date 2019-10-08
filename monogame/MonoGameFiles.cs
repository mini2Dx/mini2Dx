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
        private readonly string _internalFilePrefix, _externalFilePrefix = ((string) Mdx.gameIdentifier_) + Path.DirectorySeparatorChar;
        internal readonly MonoGameContentManager _contentManager;

        public MonoGameFiles(ContentManager contentManager)
        {
            _internalFilePrefix = contentManager.RootDirectory + Path.DirectorySeparatorChar;
            if (Mdx.platform_.isDesktop())
            {
                _externalFilePrefix = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + Path.DirectorySeparatorChar + _externalFilePrefix;
            }
            else
            {
                throw new NotSupportedException("Files aren't yet supported on this platform");
            }
            _contentManager = new MonoGameContentManager(contentManager.ServiceProvider, contentManager.RootDirectory);
        }

        public FileHandle @internal(Java.Lang.String path)
        {
            return new MonoGameFileHandle(_internalFilePrefix, path, FileType.INTERNAL_);
        }

        public bool isExternalStorageAvailable()
        {
            return true;
        }

        public FileHandle external(Java.Lang.String path)
        {
            return new MonoGameFileHandle(_externalFilePrefix, path, FileType.EXTERNAL_);
        }

        public bool isLocalStorageAvailable()
        {
            return false;
        }

        public FileHandle local(Java.Lang.String path)
        {
            throw new NotImplementedException();
        }
    }
}
