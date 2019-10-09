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

namespace monogame.Files
{
    public class MonoGameContentManager : ContentManager
    {
        public MonoGameContentManager(IServiceProvider serviceProvider, string rootDirectory) : base(serviceProvider, rootDirectory)
        {
        }

        public new Stream OpenStream(string assetName)
        {
            ContentManager contentManager = this;
            mini2DxFileContent fileContent = contentManager.Load<mini2DxFileContent>(assetName);
            return new MemoryStream(fileContent.content);
        }
    }
}
