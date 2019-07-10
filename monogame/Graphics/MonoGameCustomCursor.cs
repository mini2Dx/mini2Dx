using Microsoft.Xna.Framework.Input;
using org.mini2Dx.core;
using org.mini2Dx.core.graphics;

namespace monogame.Graphics
{
    public class MonoGameCustomCursor : CustomCursor
    {
        private const string DEBUG_TAG = "MonoGameCustomCursor";
        private MouseCursor upCursor, downCursor;
        
        public MonoGameCustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) : base(upPixmap, downPixmap, xHotspot, yHotspot)
        {
            upCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics.newTexture(upPixmap)).texture2D, xHotspot, yHotspot);
            downCursor = MouseCursor.FromTexture2D(((MonoGameTexture)Mdx.graphics.newTexture(downPixmap)).texture2D, xHotspot, yHotspot);
            Mouse.SetCursor(upCursor);
        }

        public override bool keyDown(int obj0)
        {
            return false;
        }

        public override bool keyUp(int obj0)
        {
            return false;
        }

        public override bool keyTyped(char obj0)
        {
            return false;
        }

        public override bool touchDown(int obj0, int obj1, int obj2, int obj3)
        {
            Mouse.SetCursor(downCursor);
            Mdx.log.debug(DEBUG_TAG, "touchDown");
            return false;
        }

        public override bool touchUp(int obj0, int obj1, int obj2, int obj3)
        {
            Mouse.SetCursor(upCursor);
            Mdx.log.debug(DEBUG_TAG, "touchUp");
            return false;
        }

        public override bool touchDragged(int obj0, int obj1, int obj2)
        {
            return false;
        }

        public override bool mouseMoved(int obj0, int obj1)
        {
            return false;
        }

        public override bool scrolled(int obj0)
        {
            return false;
        }
    }
}