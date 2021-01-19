using Microsoft.Xna.Framework.Input;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Core.Util;
using Org.Mini2Dx.Gdx;
using Color = Org.Mini2Dx.Core.Graphics.Color;

namespace monogame.Graphics
{
    public class MonoGameCustomCursor : CustomCursor, InputProcessor
    {
        private const string DEBUG_TAG = "MonoGameCustomCursor";
        private MouseCursor upCursor, downCursor;
        
        public MonoGameCustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) : base()
        {
            base._init_(upPixmap, downPixmap, xHotspot, yHotspot);

            if(Mini2DxGame.instance.getConfig().IsMouseVisible.HasValue &&
                Mini2DxGame.instance.getConfig().IsMouseVisible.Value)
            {
                upCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics_.newTexture(upPixmap)).texture2D, xHotspot, yHotspot);
                downCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics_.newTexture(downPixmap)).texture2D, xHotspot, yHotspot);
                Mouse.SetCursor(upCursor);
            }
        }


        bool InputProcessor.keyDown(int obj0)
        {
            return false;
        }

        bool InputProcessor.keyUp(int obj0)
        {
            return false;
        }

        bool InputProcessor.keyTyped(char obj0)
        {
            return false;
        }

        bool InputProcessor.touchDown(int obj0, int obj1, int obj2, int obj3)
        {
            if (Mini2DxGame.instance.getConfig().IsMouseVisible.HasValue &&
                Mini2DxGame.instance.getConfig().IsMouseVisible.Value)
            {
                if (isVisible())
                {
                    Mouse.SetCursor(downCursor);
                }  
            }
            return false;
        }

        bool InputProcessor.touchUp(int obj0, int obj1, int obj2, int obj3)
        {
            if (Mini2DxGame.instance.getConfig().IsMouseVisible.HasValue &&
                Mini2DxGame.instance.getConfig().IsMouseVisible.Value)
            {
                if(isVisible())
                {
                    Mouse.SetCursor(upCursor);
                }
            }
            return false;
        }

        bool InputProcessor.touchDragged(int obj0, int obj1, int obj2)
        {
            return false;
        }

        bool InputProcessor.mouseMoved(int obj0, int obj1)
        {
            return false;
        }

        bool InputProcessor.scrolled(float scrollX, float scrollY)
        {
            return false;
        }
    }
}