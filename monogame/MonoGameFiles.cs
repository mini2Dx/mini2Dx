﻿/*******************************************************************************
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
using monogame.Files;
using org.mini2Dx.core.files;

namespace monogame
{
    public class MonoGameFiles : org.mini2Dx.core.Files
    {
        public static readonly string InternalFilePrefix = "Content" + Path.DirectorySeparatorChar;
        public FileHandle external(string path)
        {
            throw new NotImplementedException();
        }

        public FileHandle @internal(string path)
        {
            return new MonoGameFileHandle(InternalFilePrefix + path, FileType.INTERNAL);
        }

        public bool isExternalStorageAvailable()
        {
            return false;
        }

        public bool isLocalStorageAvailable()
        {
            return false;
        }

        public FileHandle local(string path)
        {
            throw new NotImplementedException();
        }
    }
}