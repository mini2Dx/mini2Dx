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
using monogame.Files;
using IOException = java.io.IOException;

namespace monogame.Util
{
    //adapted from http://hg.openjdk.java.net/jdk10/jdk10/jdk/file/777356696811/src/java.base/share/classes/java/io/InputStream.java
    public class MonoGameInputStream : java.io.InputStream
    {
        private readonly Stream _stream;
        private const int MaxSkipBufferSize = 2048;
        
        public MonoGameInputStream(MonoGameFileHandle fileHandle)
        {
            if (!fileHandle.exists() || fileHandle.isDirectory())
            {
                throw new IOException();
            }

            var fileInfo = new FileInfo(fileHandle.pathWithPrefix());
            _stream = fileInfo.OpenRead();
        }

        public MonoGameInputStream(Stream stream)
        {
            _stream = stream;
        }
        
        public override int read()
        {
            return _stream.ReadByte();
        }

        public override int read(byte[] b, int off, int len)
        {
            if (len == 0)
            {
                return 0;
            }

            var c = read();
            if (c == -1)
            {
                return -1;
            }

            b[off] = (byte) c;
            var i = 1;
            try
            {
                for (; i < len; i++)
                {
                    c = read();
                    if (c == -1)
                    {
                        break;
                    }

                    b[off + i] = (byte) c;
                }
            }
            catch (IOException)
            {
            }
            return i;
        }

        public override int read(byte[] b)
        {
            return read(b, 0, b.Length);
        }

        public override long skip(long n)
        {
            var remaining = n;
            if (n <= 0)
            {
                return 0;
            }

            var size = (int) Math.Min(MaxSkipBufferSize, remaining);
            var skipBuffer = new byte[size];
            while (remaining > 0)
            {
                var nr = read(skipBuffer, 0, (int) Math.Min(size, remaining));
                if (nr < 0)
                {
                    break;
                }

                remaining -= nr;
            }
            return n - remaining;
        }

        public override int available()
        {
            return 0;
        }

        public override void close()
        {
            _stream.Close();
        }

        public override void mark(int readLimit)
        {
        }

        public override void reset()
        {
            throw new IOException("mark/reset aren't supported");
        }

        public override bool markSupported()
        {
            return false;
        }
    }
}