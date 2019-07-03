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
using System.IO;
using java.lang;
using monogame.Files;

namespace monogame.Util
{
    //adapted from http://hg.openjdk.java.net/jdk10/jdk10/jdk/file/777356696811/src/java.base/share/classes/java/io/OutputStream.java
    public class MonoGameOutputStream : java.io.OutputStream
    {
        
        private readonly Stream _stream;
        
        public MonoGameOutputStream(MonoGameFileHandle fileHandle, bool append)
        {
            if (!fileHandle.exists() || fileHandle.isDirectory()) throw new IOException();
            var fileInfo = new FileInfo(fileHandle.fullPath());
            _stream = append ? fileInfo.Open(FileMode.Append, FileAccess.Write) : fileInfo.Create();
        }

        public MonoGameOutputStream(Stream stream)
        {
            _stream = stream;
        }
        
        public override void write(int b)
        {
            _stream.WriteByte((byte) b);
        }

        public override void write(byte[] b, int off, int len)
        {
            if (b == null)
                throw new NullPointerException();
            if ((off < 0) || (off > b.Length) || (len < 0) || ((off + len) > b.Length) || ((off + len) < 0))
                throw new IndexOutOfBoundsException();
            if (len == 0)
                return;
            for (var i = 0; i < len; i++)
            {
                write(b[off + i]);
            }
        }

        public override void write(byte[] b)
        {
            write(b,0, b.Length);
        }

        public override void close()
        {
            _stream.Close();
        }

        public override void flush()
        {
            _stream.Flush();
        }
    }
}