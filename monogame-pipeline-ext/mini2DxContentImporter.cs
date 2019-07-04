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

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Content.Pipeline;
using monogame.Files;

namespace monogame_pipeline_ext
{
    /// <summary>
    /// This class will be instantiated by the XNA Framework Content Pipeline
    /// to import a file from disk into the specified type, TImport.
    ///
    /// This should be part of a Content Pipeline Extension Library project.
    ///
    /// TODO: change the ContentImporter attribute to specify the correct file
    /// extension, display name, and default processor for this importer.
    /// </summary>

    [ContentImporter(".txt", ".json", ".tmx", ".tsx", ".tx", ".atlas", ".fnt", DisplayName = "File Importer - mini2Dx", DefaultProcessor = "mini2DxContentProcessor")]
    public class mini2DxContentImporter : ContentImporter<mini2DxFileContent>
    {
        public override mini2DxFileContent Import(string filename, ContentImporterContext context)
        {
            return new mini2DxFileContent
            {
                content = File.ReadAllBytes(filename)
            };
        }

    }

}
