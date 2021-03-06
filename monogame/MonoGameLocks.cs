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
using monogame.Locks;
using Org.Mini2Dx.Lockprovider;

namespace monogame
{
    public class MonoGameLocks : global::Java.Lang.Object, global::Org.Mini2Dx.Lockprovider.Locks
    {
        public ReadWriteLock newReadWriteLock_DBA9C975()
        {
            return new MonoGameReadWriteLock();
        }

        public ReentrantLock newReentrantLock_70EC328B()
        {
            return new MonoGameReentrantLock();
        }
    }
}
