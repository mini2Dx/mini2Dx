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
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using org.mini2Dx.core;
using org.mini2Dx.core.files;

namespace monogame
{
    public class MonoGamePlayerData : org.mini2Dx.core.PlayerData
    {

        private static string joinPath(string[] filePath)
        {
            return string.Join(Path.DirectorySeparatorChar.ToString(), filePath);
        }

        protected override FileHandle resolve(string[] filePath)
        {
            return Mdx.files.external(joinPath(filePath));
        }

        protected override FileHandle resolveTmp(string[] filePath)
        {
            return Mdx.files.external(joinPath(filePath) + ".tmp");
        }

        protected override void ensureDataDirectoryExists()
        {
            Mdx.files.external("").mkdirs();
        }

        public override void wipe()
        {
            Mdx.files.external("").emptyDirectory(false);
        }
    }
}
