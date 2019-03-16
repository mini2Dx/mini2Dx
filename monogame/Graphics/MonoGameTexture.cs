using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Xna.Framework.Graphics;
using org.mini2Dx.core.graphics;

namespace monogame.Graphics
{
    class MonoGameTexture : org.mini2Dx.core.graphics.Texture
    {
        public readonly Texture2D texture2D;

        public MonoGameTexture(Texture2D texture2D)
        {
            this.texture2D = texture2D;
        }

        public void dispose()
        {
            texture2D.Dispose();
        }

        public void draw(Pixmap p, int i1, int i2)
        {
            
        }

        public int getHeight()
        {
            return texture2D.Height;
        }

        public int getWidth()
        {
            return texture2D.Width;
        }

        public bool isManaged()
        {
            return true;
        }
    }
}
