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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Files;

namespace monogame
{
    public class MonoGamePlayerData : Org.Mini2Dx.Core.PlayerData
    {

        private static string joinPath(Java.Lang.String[] filePath)
        {
            string[] paths = new string[filePath.Length];
            for(int i = 0; i < paths.Length; i++)
            {
                paths[i] = filePath[i];
            }
            return string.Join(Path.DirectorySeparatorChar.ToString(), paths);
        }

        public override FileHandle resolve(Java.Lang.String[] filePath)
        {
            return Mdx.files_.external(joinPath(filePath));
        }

        public override FileHandle resolveTmp(Java.Lang.String[] filePath)
        {
            return Mdx.files_.external(joinPath(filePath) + ".tmp");
        }

        public override void ensureDataDirectoryExists()
        {
            Mdx.files_.external("").mkdirs();
        }

        public override void wipe()
        {
            Mdx.files_.external("").emptyDirectory(false);
        }
    }
}
