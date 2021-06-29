using Java.Lang;
using System;
using System.Collections.Generic;
using System.Text;

namespace monogame.Util
{
    public class MonoGameByteOutputStream : Java.Io.OutputStream
    {
        //Defaults to 4MB
        public static int DEFAULT_CAPACITY = 4194304;

        protected sbyte[] buffer;
        protected int bufferIndex;

        public MonoGameByteOutputStream() : this(DEFAULT_CAPACITY)
        { 
        }

        public MonoGameByteOutputStream(int capacity)
        {
            buffer = new sbyte[capacity];
        }

        private void EnsureWriteable(int amount)
        {
            if (bufferIndex + amount < buffer.Length)
            {
                return;
            }
            sbyte[] newBuffer = new sbyte[buffer.Length * 2];
            System.Buffer.BlockCopy(buffer, 0, newBuffer, 0, buffer.Length);
            buffer = newBuffer;
        }

        public override void write_3518BA33(int b)
        {
            EnsureWriteable(1);
            buffer[bufferIndex] = (sbyte)b;
            bufferIndex++;
        }

        public override void write_B6D1707B(sbyte[] b, int off, int len)
        {
            if (b == null)
            {
                NullPointerException nullPointerException = new NullPointerException();
                nullPointerException._init_("Byte array cannot be null");
                throw nullPointerException;
            }
            if ((off < 0) || (off > b.Length) || (len < 0) || ((off + len) > b.Length) || ((off + len) < 0))
            {
                IndexOutOfBoundsException indexOutOfBoundsException = new IndexOutOfBoundsException();
                indexOutOfBoundsException._init_("Out of bounds. Offset: " + off + ", Length: " + len);
            }
            if (len == 0)
                return;

            EnsureWriteable(len);
            System.Buffer.BlockCopy(b, off, buffer, bufferIndex, len);
            bufferIndex += len;
        }

        public override void write_00E2C263(sbyte[] b)
        {
            write_B6D1707B(b, 0, b.Length);
        }

        public override void flush_EFE09FC0()
        {
        }

        public sbyte [] ToSByteArray()
        {
            sbyte[] result = new sbyte[bufferIndex];
            System.Buffer.BlockCopy(buffer, 0, result, 0, bufferIndex);
            return buffer;
        }
    }
}
