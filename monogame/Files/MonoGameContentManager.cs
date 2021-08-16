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
using System.IO;
using System.Threading;
using Microsoft.Xna.Framework.Content;

namespace monogame.Files
{
    public class MonoGameContentManager : ContentManager
    {
        ReaderWriterLockSlim @lock = new ReaderWriterLockSlim(LockRecursionPolicy.SupportsRecursion);
        private bool disposed = false;

        public MonoGameContentManager(IServiceProvider serviceProvider, string rootDirectory) : base(serviceProvider, rootDirectory)
        {
        }

        public override T Load<T>(string assetName)
        {
            if (string.IsNullOrEmpty(assetName))
            {
                throw new ArgumentNullException("assetName");
            }
            if (disposed)
            {
                throw new ObjectDisposedException("ContentManager");
            }

            T result = default(T);

            // On some platforms, name and slash direction matter.
            // We store the asset by a /-seperating key rather than how the
            // path to the file was passed to us to avoid
            // loading "content/asset1.xnb" and "content\\ASSET1.xnb" as if they were two 
            // different files. This matches stock XNA behavior.
            // The dictionary will ignore case differences
            var key = assetName.Replace('\\', '/');

            // Check for a previously loaded asset first
            object asset = null;
            @lock.EnterReadLock();
            try
            {
                if (LoadedAssets.TryGetValue(key, out asset))
                {
                    if (asset is T)
                    {
                        return (T)asset;
                    }
                }
            }
            finally
            {
                @lock.ExitReadLock();
            }

            // Load the asset.
            result = ReadAsset<T>(assetName, null);

            @lock.EnterWriteLock();
            try
            {
                LoadedAssets[key] = result;
            }
            finally
            {
                @lock.ExitWriteLock();
            }
            return result;
        }

        public new Stream OpenStream(string assetName)
        {
            mini2DxFileContent fileContent = Load<mini2DxFileContent>(assetName);
            return new MemoryStream(fileContent.content);
        }

        protected override void Dispose(bool disposing)
        {
            base.Dispose(disposing);
            disposed = true;
        }
    }
}
