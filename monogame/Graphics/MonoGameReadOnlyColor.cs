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

        public override Org.Mini2Dx.Core.Graphics.Color add_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color add_F18CABCA(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color add_DF74E9CF(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp_8C006841(sbyte r, sbyte g, sbyte b, sbyte a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp_50042A18(Org.Mini2Dx.Core.Graphics.Color color, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color lerp_2E63AB81(float r, float g, float b, float a, float t)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply_F18CABCA(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply_56581B81(float multiplier)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color multiply_DF74E9CF(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set_F18CABCA(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color set_DF74E9CF(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract_F18CABCA(Org.Mini2Dx.Core.Graphics.Color c)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract_DF74E9CF(float r, float g, float b, float a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override Org.Mini2Dx.Core.Graphics.Color subtract_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA_638B22FE(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setA_97413DCA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB_638B22FE(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setB_97413DCA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG_638B22FE(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setG_97413DCA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR_638B22FE(sbyte b)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }

        public override void setR_97413DCA(float f)
        {
            throw new NotSupportedException(EXCEPTION_MESSAGE);
        }
    }
}