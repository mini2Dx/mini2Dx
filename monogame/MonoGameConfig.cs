﻿/*******************************************************************************
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
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace monogame
{
    public class MonoGameConfig
    {
        public bool? AllowUserResizing = null;
        public bool? IsMouseVisible = null;
        /// <summary>
        /// Needs to be true if CapToFPS is set, else false
        /// </summary>
        public bool IsFixedTimeStep = true;
        /// <summary>
        /// Lock frame rate
        /// </summary>
        public int? CapToFPS = null;
        public int? PreferredBackBufferWidth = null;
        public int? PreferredBackBufferHeight = null;
        public bool? IsFullScreen = null;
        public Org.Mini2Dx.Core.Platform OverridePlatform = null;
        public string OverrideSaveDataPathPrefix = null;
    }
}
