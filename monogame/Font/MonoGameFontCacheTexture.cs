using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Gdx.Math;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rectangle = Microsoft.Xna.Framework.Rectangle;
using Vector2 = Microsoft.Xna.Framework.Vector2;

namespace monogame.Font
{
    public class MonoGameFontCacheTexture
    {
        private const int TEXTURE_WIDTH = 1024;
        private const int TEXTURE_HEIGHT = 1024;
        private const int REGION_WIDTH = 32;
        private const int REGION_HEIGHT = 16;
        private const int REGION_ARRAY_WIDTH = TEXTURE_WIDTH / REGION_WIDTH;
        private const int REGION_ARRAY_HEIGHT = TEXTURE_HEIGHT / REGION_HEIGHT;

        private Dictionary<int, MonoGameTextureRegion> _caches = new Dictionary<int, MonoGameTextureRegion>();
        private List<MonoGameTextureRegion> _gc = new List<MonoGameTextureRegion>();
        private short[,] _regions = new short[REGION_ARRAY_WIDTH, REGION_ARRAY_HEIGHT];
        private MonoGameTexture _texture;
        private GraphicsDevice _graphicsDevice;
        private SpriteBatch _spriteBatch;

        public MonoGameFontCacheTexture()
        {
            _graphicsDevice = ((MonoGameGraphics)Mdx.graphicsContext_)._graphicsDevice;
            _spriteBatch = new SpriteBatch(_graphicsDevice);
            _texture = new MonoGameTexture(new RenderTarget2D(_graphicsDevice, TEXTURE_WIDTH, TEXTURE_HEIGHT, false, SurfaceFormat.Color, _graphicsDevice.PresentationParameters.DepthStencilFormat, 0, RenderTargetUsage.PreserveContents));

            for(int x = 0; x < REGION_ARRAY_WIDTH; x++)
            {
                for(int y = 0; y < REGION_ARRAY_HEIGHT; y++)
                {
                    _regions[x, y] = -1;
                }
            }
        }

        public int allocateId()
        {
            int result = _caches.Count;
            _caches.Add(result, null);
            return result;
        }

        public void begin()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_texture.texture2D);
            _spriteBatch.Begin();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext_).updateClip();
        }

        public void end()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_texture.texture2D);
            _spriteBatch.End();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext_).updateClip();
        }

        public void clear()
        {
            end();
            _spriteBatch.GraphicsDevice.Clear(Microsoft.Xna.Framework.Color.Transparent);
            begin();

            for (int x = 0; x < REGION_ARRAY_WIDTH; x++)
            {
                for (int y = 0; y < REGION_ARRAY_HEIGHT; y++)
                {
                    _regions[x, y] = -1;
                }
            }
        }

        public void setText(MonoGameGameFontCache fontCache)
        {
            if(_caches.ContainsKey(fontCache.cacheId) && _caches[fontCache.cacheId] != null)
            {
                _gc.Add(_caches[fontCache.cacheId]);
                _caches.Remove(fontCache.cacheId);
            }

            Rectangle rect = determineRequiredArea(fontCache);
            allocateTextureRegionXY(rect);

            for (int i = 0; i < fontCache._previousDrawingOperations.Count; i++)
            {
                MonoGameGameFontCacheDrawingOperation operation = fontCache._previousDrawingOperations[i];
                fontCache._gameFont.draw(_spriteBatch, operation.text, operation.targetWidth, operation.horizontalAlign, operation.wrap, new Vector2(rect.X + operation.x, rect.Y + operation.y), operation.color * operation.alpha);
            }

            _caches.Add(fontCache.cacheId, new MonoGameTextureRegion(_texture, rect.X, rect.Y, rect.Width, rect.Height));
        }

        public void draw(Org.Mini2Dx.Core._Graphics g, MonoGameGameFontCache fontCache, Vector2 position)
        {
            int cacheId = fontCache.cacheId;
            if(fontCache._previousDrawingOperations.Count == 0)
            {
                return;
            }
            if (!_caches.ContainsKey(cacheId) || _caches[cacheId] == null)
            {
                setText(fontCache);
            }

            MonoGameTextureRegion textureRegion = _caches[cacheId];

            end();
            g.drawTextureRegion(textureRegion, position.X, position.Y);
            begin();
        }

        public void clear(int cacheId)
        {
            if (!_caches.ContainsKey(cacheId))
            {
                return;
            }
            _caches.Remove(cacheId);
        }

        private int GetX(int flatIndex)
        {
            return flatIndex % REGION_ARRAY_WIDTH;
        }

        private int GetY(int flatIndex)
        {
            return flatIndex / REGION_ARRAY_WIDTH;
        }

        private Rectangle determineRequiredArea(MonoGameGameFontCache cache)
        {
            int maxX = 0;
            int maxY = 0;

            for(int i = 0; i < cache._previousDrawingOperations.Count; i++)
            {
                int currentMaxX = MathUtils.round(cache._previousDrawingOperations[i].x +
                    cache._previousDrawingOperations[i].targetWidth);
                int currentMaxY = MathUtils.round(cache._previousDrawingOperations[i].y +
                    cache._previousDrawingOperations[i].targetHeight);

                maxX = Math.Max(maxX, currentMaxX);
                maxY = Math.Max(maxY, currentMaxY);
            }

            return new Rectangle(0, 0, maxX, maxY);
        }

        private void allocateTextureRegionXY(Rectangle area)
        {

        }
    }
}
