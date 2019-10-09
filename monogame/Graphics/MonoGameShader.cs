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

using Microsoft.Xna.Framework.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using System;
using Org.Mini2Dx.Gdx.Math;
using Org.Mini2Dx.Core.Graphics;
using Microsoft.Xna.Framework.Content;

namespace monogame.Graphics
{
    public class MonoGameShader : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.Shader
    {
        private Microsoft.Xna.Framework.Vector2 tmpVector2 = new Microsoft.Xna.Framework.Vector2();
        private Microsoft.Xna.Framework.Vector3 tmpVector3 = new Microsoft.Xna.Framework.Vector3();
        private Microsoft.Xna.Framework.Vector4 tmpVector4 = new Microsoft.Xna.Framework.Vector4();
        private Microsoft.Xna.Framework.Matrix tmpMatrix = new Microsoft.Xna.Framework.Matrix();

        public Effect shader;
        
        public MonoGameShader(string name)
        {
            if(!name.EndsWith(".fx"))
            {
                name += ".fx";
            }

            ContentManager contentManager = ((MonoGameFiles)Mdx.files_)._contentManager;
            shader = contentManager.Load<Effect>(name);
            shader.CurrentTechnique = shader.Techniques[0];
        }

        public MonoGameShader(Effect effect)
        {
            shader = effect;
            if(shader != null)
            {
                shader.CurrentTechnique = shader.Techniques[0];
            }
        }
        
        public void dispose()
        {
            shader.Dispose();
        }

        public void begin()
        {
        }

        public void end()
        {
        }

        public bool hasParameter(Java.Lang.String str)
        {
            try
            {
                return shader.Parameters[(string) str] != null;
            }
            catch
            {
            }
            return false;
        }

        public void setParameter(Java.Lang.String str, Org.Mini2Dx.Core.Graphics.Texture t)
        {
            setParameter(str, 0, t);
        }

        public void setParameter(Java.Lang.String str, int i, Org.Mini2Dx.Core.Graphics.Texture t)
        {
            MonoGameTexture texture = t as MonoGameTexture;
            shader.Parameters[(string) str].SetValue(texture.texture2D);
        }

        public void setParameterf(Java.Lang.String str, float f)
        {
            shader.Parameters[(string) str].SetValue(f);
        }

        public void setParameterf(Java.Lang.String str, float f1, float f2)
        {
            tmpVector2.X = f1;
            tmpVector2.Y = f2;
            shader.Parameters[(string) str].SetValue(tmpVector2);
        }

        public void setParameterf(Java.Lang.String str, float f1, float f2, float f3)
        {
            tmpVector3.X = f1;
            tmpVector3.Y = f2;
            tmpVector3.Z = f3;
            shader.Parameters[(string) str].SetValue(tmpVector3);
        }

        public void setParameterf(Java.Lang.String str, float f1, float f2, float f3, float f4)
        {
            tmpVector4.X = f1;
            tmpVector4.Y = f2;
            tmpVector4.Z = f3;
            tmpVector4.W = f4;
            shader.Parameters[(string) str].SetValue(tmpVector4);
        }

        public void setParameterf(Java.Lang.String str, Vector2 v)
        {
            tmpVector2.X = v.x_;
            tmpVector2.Y = v.y_;
            shader.Parameters[(string) str].SetValue(tmpVector2);
        }

        public void setParameterf(Java.Lang.String str, Vector3 v)
        {
            tmpVector3.X = v.x_;
            tmpVector3.Y = v.y_;
            tmpVector3.Z = v.z_;
            shader.Parameters[(string) str].SetValue(tmpVector3);
        }

        public void setParameteri(Java.Lang.String str, int i)
        {
            shader.Parameters[(string) str].SetValue(i);
        }

        public void setParameterMatrix(Java.Lang.String str, Matrix4 m)
        {
            setParameterMatrix(str, m, false);
        }

        public void setParameterMatrix(Java.Lang.String str, Matrix4 m, bool b)
        {
            setTempMatrix(m);
            if (b)
            {
                shader.Parameters[(string) str].SetValueTranspose(tmpMatrix);
            }
            else
            {
                shader.Parameters[(string) str].SetValue(tmpMatrix);
            }
        }

        private void setTempMatrix(Matrix4 m)
        {
            tmpMatrix.M11 = m.val_[Matrix4.M00_];
            tmpMatrix.M12 = m.val_[Matrix4.M01_];
            tmpMatrix.M13 = m.val_[Matrix4.M02_];
            tmpMatrix.M14 = m.val_[Matrix4.M03_];
            tmpMatrix.M21 = m.val_[Matrix4.M10_];
            tmpMatrix.M22 = m.val_[Matrix4.M11_];
            tmpMatrix.M23 = m.val_[Matrix4.M12_];
            tmpMatrix.M24 = m.val_[Matrix4.M13_];
            tmpMatrix.M31 = m.val_[Matrix4.M20_];
            tmpMatrix.M32 = m.val_[Matrix4.M21_];
            tmpMatrix.M33 = m.val_[Matrix4.M22_];
            tmpMatrix.M34 = m.val_[Matrix4.M23_];
            tmpMatrix.M41 = m.val_[Matrix4.M30_];
            tmpMatrix.M42 = m.val_[Matrix4.M31_];
            tmpMatrix.M43 = m.val_[Matrix4.M32_];
            tmpMatrix.M44 = m.val_[Matrix4.M33_];
        }

        public Java.Lang.String getLog()
        {
            return string.Empty;
        }

        public bool isCompiled()
        {
            return true;
        }

        public ShaderType getShaderType()
        {
            return ShaderType.HLSL_;
        }
    }
}