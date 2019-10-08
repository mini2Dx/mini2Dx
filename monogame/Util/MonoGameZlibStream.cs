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
using Org.Mini2Dx.Core.Util;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace monogame.Util
{
    public class MonoGameZlibStream : global::Java.Lang.Object, ZlibStream
    {
        private MonoGame.Utilities.ZlibStream zlibStream;
        private byte[] buffer = null;

        public MonoGameZlibStream(sbyte [] sdata)
        {
            byte[] data = new byte[sdata.Length];
            for(int i = 0; i < data.Length; i++)
            {
                data[i] = (byte) sdata[i];
            }
            zlibStream = new MonoGame.Utilities.ZlibStream(new MemoryStream(data), MonoGame.Utilities.CompressionMode.Decompress);
        }

        public void read(sbyte[] arg0)
        {
            if(buffer == null || buffer.Length != arg0.Length)
            {
                buffer = new byte[arg0.Length];
            }
            zlibStream.Read(buffer, 0, buffer.Length);

            for(int i = 0; i < arg0.Length; i++)
            {
                arg0[i] = (sbyte) buffer[i];
            }
        }

        public void dispose()
        {
            zlibStream.Close();
        }
    }
}
