using System;
using Microsoft.Xna.Framework;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;

namespace monogame.Graphics
{
    public class MonoGameReadOnlyColor : MonoGameColor
    {
        private const string EXCEPTION_MESSAGE = "This color is readonly, if you wish to change the values call copy()";
        
        public MonoGameReadOnlyColor(sbyte r, sbyte g, sbyte b, sbyte a) : base(r, g, b, a)
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

        public MonoGameReadOnlyColor(Microsoft.Xna.Framework.Color color) : base(color)
        {
        }

        public override Org.Mini2Dx.Core.Graphics.Color add(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color add(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color add(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp(sbyte r, sbyte g, sbyte b, sbyte a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp(Org.Mini2Dx.Core.Graphics.Color color, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp(float r, float g, float b, float a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply(float multiplier)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }
    }
}