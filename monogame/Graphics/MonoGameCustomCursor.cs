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
                upCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics_.newTexture_5A25B7D4(upPixmap)).texture2D, xHotspot, yHotspot);
                downCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics_.newTexture_5A25B7D4(downPixmap)).texture2D, xHotspot, yHotspot);
                Mouse.SetCursor(upCursor);
            }
        }


        bool InputProcessor.keyDown_4118CD17(int obj0)
        {
            return false;
        }

        bool InputProcessor.keyUp_4118CD17(int obj0)
        {
            return false;
        }

        bool InputProcessor.keyTyped_0E996675(char obj0)
        {
            return false;
        }

        bool InputProcessor.touchDown_A890D1B4(int obj0, int obj1, int obj2, int obj3)
        {
            if (Mini2DxGame.instance.getConfig().IsMouseVisible.HasValue &&
                Mini2DxGame.instance.getConfig().IsMouseVisible.Value)
            {
                if (isVisible_FBE0B2A4())
                {
                    Mouse.SetCursor(downCursor);
                }  
            }
            return false;
        }

        bool InputProcessor.touchUp_A890D1B4(int obj0, int obj1, int obj2, int obj3)
        {
            if (Mini2DxGame.instance.getConfig().IsMouseVisible.HasValue &&
                Mini2DxGame.instance.getConfig().IsMouseVisible.Value)
            {
                if(isVisible_FBE0B2A4())
                {
                    Mouse.SetCursor(upCursor);
                }
            }
            return false;
        }

        bool InputProcessor.touchDragged_F8B7DE3F(int obj0, int obj1, int obj2)
        {
            return false;
        }

        bool InputProcessor.mouseMoved_1E4D20DC(int obj0, int obj1)
        {
            return false;
        }

        bool InputProcessor.scrolled_1548FAA4(float scrollX, float scrollY)
        {
            return false;
        }
    }
}