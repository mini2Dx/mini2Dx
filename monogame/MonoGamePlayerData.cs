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
                if(!currentPath.EndsWith("" + Path.DirectorySeparatorChar))
                {
                    currentPath += Path.DirectorySeparatorChar;
                }
                currentPath += "data" + Path.DirectorySeparatorChar;
                dataDirectoryPrefix = currentPath;

                if (!Mdx.files_.external_1F3F44D2(dataDirectoryPrefix).exists_FBE0B2A4())
                {
                    Mdx.files_.external_1F3F44D2(dataDirectoryPrefix).mkdirs_EFE09FC0();
                }
            }
        }

        private static string joinPath(string prefix, Java.Lang.String[] filePath)
        {
            if (filePath.Length == 0)
            {
                return prefix;
            }
            string[] paths = new string[filePath.Length + 1];
            paths[0] = prefix;
            if(prefix.EndsWith("" + Path.DirectorySeparatorChar))
            {
                paths[0] = paths[0].Substring(0, paths[0].Length - 1);
            }
            for (int i = 0; i < filePath.Length; i++)
            {
                paths[i + 1] = filePath[i];
            }
            return string.Join(Path.DirectorySeparatorChar.ToString(), paths);
        }

        private static string joinPath(Java.Lang.String[] filePath)
        {
            if (filePath.Length == 0)
            {
                return "";
            }
            string[] paths = new string[filePath.Length];
            for(int i = 0; i < paths.Length; i++)
            {
                paths[i] = filePath[i];
            }
            return string.Join(Path.DirectorySeparatorChar.ToString(), paths);
        }

        public override FileHandle resolve_85AFE6AF(Java.Lang.String[] filePath)
        {
            return Mdx.files_.external_1F3F44D2(joinPath(dataDirectoryPrefix, filePath));
        }

        public override FileHandle resolveTmp_85AFE6AF(Java.Lang.String[] filePath)
        {
            return Mdx.files_.external_1F3F44D2(joinPath(dataDirectoryPrefix, filePath) + ".tmp");
        }

        public override void ensureDataDirectoryExists_EFE09FC0()
        {
            if(Mdx.platform_.isConsole_FBE0B2A4())
            {
                return;
            }
            Mdx.files_.external_1F3F44D2(dataDirectoryPrefix).mkdirs_EFE09FC0();
        }

        public override void wipe_EFE09FC0()
        {
            Mdx.files_.external_1F3F44D2(dataDirectoryPrefix).emptyDirectory_AA5A2C66(false);
        }
    }
}
