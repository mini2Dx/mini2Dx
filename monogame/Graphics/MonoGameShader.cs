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
using org.mini2Dx.core;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx.math;
using System;

namespace monogame.Graphics
{
    public class MonoGameShader : org.mini2Dx.core.graphics.Shader
    {
        private Microsoft.Xna.Framework.Vector2 tmpVector2 = new Microsoft.Xna.Framework.Vector2();
        private Microsoft.Xna.Framework.Vector3 tmpVector3 = new Microsoft.Xna.Framework.Vector3();
        private Microsoft.Xna.Framework.Vector4 tmpVector4 = new Microsoft.Xna.Framework.Vector4();
        private Microsoft.Xna.Framework.Matrix tmpMatrix = new Microsoft.Xna.Framework.Matrix();

        internal Effect shader;
        
        public MonoGameShader(string name)
        {
            if(!name.EndsWith(".fx"))
            {
                name += ".fx";
            }
            shader = ((MonoGameFiles) Mdx.files)._contentManager.Load<Effect>(name);
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

        public bool hasParameter(string str)
        {
            try
            {
                return shader.Parameters[str] != null;
            }
            catch
            {
            }
            return false;
        }

        public void setParameter(string str, org.mini2Dx.core.graphics.Texture t)
        {
            setParameter(str, 0, t);
        }

        public void setParameter(string str, int i, org.mini2Dx.core.graphics.Texture t)
        {
            MonoGameTexture texture = t as MonoGameTexture;
            shader.Parameters[str].SetValue(texture.texture2D);
        }

        public void setParameterf(string str, float f)
        {
            shader.Parameters[str].SetValue(f);
        }

        public void setParameterf(string str, float f1, float f2)
        {
            tmpVector2.X = f1;
            tmpVector2.Y = f2;
            shader.Parameters[str].SetValue(tmpVector2);
        }

        public void setParameterf(string str, float f1, float f2, float f3)
        {
            tmpVector3.X = f1;
            tmpVector3.Y = f2;
            tmpVector3.Z = f3;
            shader.Parameters[str].SetValue(tmpVector3);
        }

        public void setParameterf(string str, float f1, float f2, float f3, float f4)
        {
            tmpVector4.X = f1;
            tmpVector4.Y = f2;
            tmpVector4.Z = f3;
            tmpVector4.W = f4;
            shader.Parameters[str].SetValue(tmpVector4);
        }

        public void setParameterf(string str, Vector2 v)
        {
            tmpVector2.X = v.x;
            tmpVector2.Y = v.y;
            shader.Parameters[str].SetValue(tmpVector2);
        }

        public void setParameterf(string str, Vector3 v)
        {
            tmpVector3.X = v.x;
            tmpVector3.Y = v.y;
            tmpVector3.Z = v.z;
            shader.Parameters[str].SetValue(tmpVector3);
        }

        public void setParameteri(string str, int i)
        {
            shader.Parameters[str].SetValue(i);
        }

        public void setParameterMatrix(string str, Matrix4 m)
        {
            setParameterMatrix(str, m, false);
        }

        public void setParameterMatrix(string str, Matrix4 m, bool b)
        {
            setTempMatrix(m);
            if (b)
            {
                shader.Parameters[str].SetValueTranspose(tmpMatrix);
            }
            else
            {
                shader.Parameters[str].SetValue(tmpMatrix);
            }
        }

        private void setTempMatrix(Matrix4 m)
        {
            tmpMatrix.M11 = m.val[Matrix4.M00];
            tmpMatrix.M12 = m.val[Matrix4.M01];
            tmpMatrix.M13 = m.val[Matrix4.M02];
            tmpMatrix.M14 = m.val[Matrix4.M03];
            tmpMatrix.M21 = m.val[Matrix4.M10];
            tmpMatrix.M22 = m.val[Matrix4.M11];
            tmpMatrix.M23 = m.val[Matrix4.M12];
            tmpMatrix.M24 = m.val[Matrix4.M13];
            tmpMatrix.M31 = m.val[Matrix4.M20];
            tmpMatrix.M32 = m.val[Matrix4.M21];
            tmpMatrix.M33 = m.val[Matrix4.M22];
            tmpMatrix.M34 = m.val[Matrix4.M23];
            tmpMatrix.M41 = m.val[Matrix4.M30];
            tmpMatrix.M42 = m.val[Matrix4.M31];
            tmpMatrix.M43 = m.val[Matrix4.M32];
            tmpMatrix.M44 = m.val[Matrix4.M33];
        }

        public string getLog()
        {
            return string.Empty;
        }

        public bool isCompiled()
        {
            return true;
        }

        public ShaderType getShaderType()
        {
            return ShaderType.HLSL;
        }
    }
}