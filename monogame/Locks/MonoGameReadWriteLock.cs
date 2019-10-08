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
using Org.Mini2Dx.Core.Lock;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace monogame.Locks
{
    public class MonoGameReadWriteLock : global::Java.Lang.Object, ReadWriteLock
    {
        ReaderWriterLockSlim @lock = new ReaderWriterLockSlim();

        public void lockRead()
        {
            @lock.EnterReadLock();
        }

        public void lockWrite()
        {
            @lock.EnterWriteLock();
        }

        public bool tryLockRead()
        {
            return @lock.TryEnterReadLock(0);
        }

        public bool tryLockWrite()
        {
            return @lock.TryEnterWriteLock(0);
        }

        public void unlockRead()
        {
            @lock.ExitReadLock();
        }

        public void unlockWrite()
        {
            @lock.ExitWriteLock();
        }
    }
}
