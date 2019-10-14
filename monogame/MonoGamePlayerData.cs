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
        private readonly string dataDirectoryPrefix;

        public MonoGamePlayerData() : base()
        {
            base._init_();

            if(Mini2DxGame.instance.getConfig().OverrideSaveDataPathPrefix != null)
            {
                dataDirectoryPrefix = Mini2DxGame.instance.getConfig().OverrideSaveDataPathPrefix;
            }
            else
            {
                string currentPath = Environment.CurrentDirectory;
                if(!currentPath.EndsWith("/"))
                {
                    currentPath += "/";
                }
                currentPath += "data/";
                dataDirectoryPrefix = currentPath;
            }
        }

        private static string joinPath(string prefix, Java.Lang.String[] filePath)
        {
            string[] paths = new string[filePath.Length + 1];
            paths[0] = prefix;
            for (int i = 0; i < paths.Length; i++)
            {
                paths[i + 1] = filePath[i];
            }
            return string.Join(Path.DirectorySeparatorChar.ToString(), paths);
        }

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
            return Mdx.files_.external(joinPath(dataDirectoryPrefix, filePath));
        }

        public override FileHandle resolveTmp(Java.Lang.String[] filePath)
        {
            return Mdx.files_.external(joinPath(dataDirectoryPrefix, filePath) + ".tmp");
        }

        public override void ensureDataDirectoryExists()
        {
            if(Mdx.platform_.isConsole())
            {
                return;
            }
            Mdx.files_.external(dataDirectoryPrefix).mkdirs();
        }

        public override void wipe()
        {
            Mdx.files_.external(dataDirectoryPrefix).emptyDirectory(false);
        }
    }
}
