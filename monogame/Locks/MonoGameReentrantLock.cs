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
using Org.Mini2Dx.Lockprovider;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace monogame.Locks
{
    public class MonoGameReentrantLock : global::Java.Lang.Object, ReentrantLock
    {
        object @object = new object();

        public bool isHeldByCurrentThread_FBE0B2A4()
        {
            return Monitor.IsEntered(@object);
        }

        public void lock_EFE09FC0()
        {
            bool locked = false;
            while (!locked)
            {
                try
                {
                    Monitor.Enter(@object, ref locked);
                }
                catch (global::System.Threading.ThreadInterruptedException) { }
            }
        }

        public bool tryLock_FBE0B2A4()
        {
            try
            {
                return Monitor.TryEnter(@object);
            }
            catch
            {
                return false;
            }
        }

        public void unlock_EFE09FC0()
        {
            try
            {
                Monitor.Exit(@object);
            }
            catch (global::System.Exception) { }
        }
    }
}
