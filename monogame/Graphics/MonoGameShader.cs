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

namespace monogame.Graphics
{
    public class MonoGameShader : org.mini2Dx.core.graphics.Shader
    {
        internal Effect shader;
        
        public MonoGameShader(string name)
        {
            shader = ((MonoGameFiles) Mdx.files)._contentManager.Load<Effect>(name);
        }
        
        public void dispose()
        {
            shader.Dispose();
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