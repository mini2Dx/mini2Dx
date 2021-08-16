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
using IOException = Java.Io.IOException;

namespace monogame.Util
{
    //adapted from http://hg.openjdk.java.net/jdk10/jdk10/jdk/file/777356696811/src/java.base/share/classes/java/io/InputStream.java
    public class MonoGameInputStream : Java.Io.InputStream
    {
        private readonly Stream _stream;
        private const int MaxSkipBufferSize = 2048;
        
        public MonoGameInputStream(MonoGameFileHandle fileHandle)
        {
            base._init_();
            if (!fileHandle.exists_FBE0B2A4() || fileHandle.isDirectory_FBE0B2A4())
            {
                throw new IOException();
            }

            var fileInfo = new FileInfo(fileHandle.originalPath());
            _stream = fileInfo.OpenRead();
        }

        public MonoGameInputStream(Stream stream)
        {
            _stream = stream;
        }
        
        public override int read_0EE0D08D()
        {
            return _stream.ReadByte();
        }

        public override int read_A3D15292(sbyte[] b, int off, int len)
        {
            if (len == 0)
            {
                return 0;
            }

            var c = read_0EE0D08D();
            if (c == -1)
            {
                return -1;
            }

            b[off] = (sbyte) c;
            var i = 1;
            try
            {
                for (; i < len; i++)
                {
                    c = read_0EE0D08D();
                    if (c == -1)
                    {
                        break;
                    }

                    b[off + i] = (sbyte) c;
                }
            }
            catch (IOException)
            {
            }
            return i;
        }

        public override int read_FDE2BDAA(sbyte[] b)
        {
            return read_A3D15292(b, 0, b.Length);
        }

        public override long skip_5BE5DC4A(long n)
        {
            var remaining = n;
            if (n <= 0)
            {
                return 0;
            }

            var size = (int) Math.Min(MaxSkipBufferSize, remaining);
            var skipBuffer = new sbyte[size];
            while (remaining > 0)
            {
                var nr = read_A3D15292(skipBuffer, 0, (int) Math.Min(size, remaining));
                if (nr < 0)
                {
                    break;
                }

                remaining -= nr;
            }
            return n - remaining;
        }

        public override int available_0EE0D08D()
        {
            return 0;
        }

        public override void close_EFE09FC0()
        {
            _stream.Close();
        }

        public override void mark_3518BA33(int readLimit)
        {
        }

        public override void reset_EFE09FC0()
        {
            IOException exception = new IOException();
            exception._init_56DB2ED6("mark/reset aren't supported");
            throw exception;
        }

        public override bool markSupported_FBE0B2A4()
        {
            return false;
        }
    }
}