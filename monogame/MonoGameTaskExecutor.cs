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
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using Java.Lang;
using Java.Util.Concurrent;
using Org.Mini2Dx.Core.Executor;

namespace monogame
{
    public class MonoGameTaskExecutor : global::Java.Lang.Object, Org.Mini2Dx.Core.TaskExecutor
    {
        internal class MonoGameAsyncFuture : Org.Mini2Dx.Core.Executor.AsyncFuture
        {
            private Task _task;

            internal MonoGameAsyncFuture(Task task)
            {
                _task = task;
            }

            public bool isFinished()
            {
                return _task.IsCanceled || _task.IsCompleted || _task.IsFaulted;
            }
        }

        internal class MonoGameAsyncResult : MonoGameAsyncFuture, Org.Mini2Dx.Core.Executor.AsyncResult
        {
            private TaskAwaiter<object> taskAwaiter;
            internal MonoGameAsyncResult(Task<object> task) : base(task)
            {
                taskAwaiter = task.GetAwaiter();
            }

            public object getResult()
            {
                return taskAwaiter.IsCompleted ? taskAwaiter.GetResult() : null;
            }
        }

        private const int DefaultMaxFrameTasksPerFrame = 32;
        private readonly List<FrameSpreadTask> spreadTasks = new List<FrameSpreadTask>();
        private readonly List<Task<object>> asyncTasks = new List<Task<object>>();
        private int maxFrameTasksPerFrame = DefaultMaxFrameTasksPerFrame;

        public void dispose()
        {
        }

        public void update(float delta)
        {
            var taskCount = 0;

            for(int i = 0; i < spreadTasks.Count; i++)
            {
                if(spreadTasks[i].updateTask())
                {
                    spreadTasks.RemoveAt(i);
                    i--;
                }
                taskCount++;

                if (taskCount >= maxFrameTasksPerFrame)
                {
                    break;
                }
            }

            lock (asyncTasks)
            {
                for(int i = asyncTasks.Count - 1; i >= 0; i--)
                {
                    if(asyncTasks[i].IsCompleted)
                    {
                        asyncTasks.RemoveAt(i);
                    }
                }
            }
        }

        public void execute(Runnable r)
        {
            submit(r);
        }

        public AsyncFuture submit(Runnable r)
        {
            Task<object> task = Task.Factory.StartNew<object>(() =>
            {
                r.run();
                return null;
            });
            lock(asyncTasks)
            {
                asyncTasks.Add(task);
            }
            return new MonoGameAsyncFuture(task);
        }

        public AsyncResult submit(Callable c)
        {
            Task<object> task = Task.Factory.StartNew((Func<object>)c.call);
            lock(asyncTasks)
            {
                asyncTasks.Add(task);
            }
            return new MonoGameAsyncResult(task);
        }

        public void submit(FrameSpreadTask fst)
        {
            spreadTasks.Add(fst);
        }

        public void setMaxFrameTasksPerFrame(int i)
        {
            maxFrameTasksPerFrame = i;
        }

        public int getTotalQueuedAsyncTasks()
        {
            int result = 0;
            lock(asyncTasks)
            {
                result = asyncTasks.Count;
            }
            return result;
        }

        public int getTotalQueuedFrameSpreadTasks()
        {
            return spreadTasks.Count;
        }
    }
}
