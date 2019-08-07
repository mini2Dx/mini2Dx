using System;
using Microsoft.Xna.Framework;

namespace monogame.Graphics
{
    public class MonoGameReadOnlyColor : MonoGameColor
    {
        private const string EXCEPTION_MESSAGE = "This color is readonly, if you wish to change the values call copy()";
        
        public MonoGameReadOnlyColor(byte r, byte g, byte b, byte a) : base(r, g, b, a)
        {
        }

        public MonoGameReadOnlyColor(float r, float g, float b, float a) : base(r, g, b, a)
        {
        }

        public MonoGameReadOnlyColor(int r, int g, int b, int a) : base(r, g, b, a)
        {
        }

        public MonoGameReadOnlyColor(uint rgba8888) : base(rgba8888)
        {
        }

        public MonoGameReadOnlyColor(Color color) : base(color)
        {
        }

        public override org.mini2Dx.core.graphics.Color add(byte r, byte g, byte b, byte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color add(org.mini2Dx.core.graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color add(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color lerp(byte r, byte g, byte b, byte a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color lerp(org.mini2Dx.core.graphics.Color color, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color lerp(float r, float g, float b, float a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color multiply(byte r, byte g, byte b, byte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color multiply(org.mini2Dx.core.graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color multiply(float multiplier)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color multiply(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color set(org.mini2Dx.core.graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color set(byte r, byte g, byte b, byte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color set(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color subtract(org.mini2Dx.core.graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color subtract(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override org.mini2Dx.core.graphics.Color subtract(byte r, byte g, byte b, byte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA(byte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB(byte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG(byte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR(byte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }
    }
}