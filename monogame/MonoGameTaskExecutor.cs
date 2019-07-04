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

using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using java.lang;
using java.util.concurrent;
using org.mini2Dx.core.executor;

namespace monogame
{
    public class MonoGameTaskExecutor : org.mini2Dx.core.TaskExecutor
    {
        private class MonoGameAsyncFuture : org.mini2Dx.core.executor.AsyncFuture
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

        private class MonoGameAsyncResult : MonoGameAsyncFuture, org.mini2Dx.core.executor.AsyncResult
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
        private readonly LinkedList<FrameSpreadTask> spreadTasks = new LinkedList<FrameSpreadTask>();
        private int maxFrameTasksPerFrame = DefaultMaxFrameTasksPerFrame;


        public void dispose()
        {
        }

        public void update(float f)
        {
            var taskCount = 0;

            var node = spreadTasks.First;

            while (node != null)
            {
                if (node.Value.updateTask())
                {
                    node = node.Next;
                    spreadTasks.Remove(node.Previous);
                }
                else
                {
                    node = node.Next;
                }
                taskCount++;

                if (taskCount >= maxFrameTasksPerFrame)
                {
                    break;
                }
            }
        }

        public void execute(Runnable r)
        {
            submit(r);
        }

        public AsyncFuture submit(Runnable r)
        {
            return new MonoGameAsyncFuture(Task.Factory.StartNew(r.run));
        }

        public AsyncResult submit(Callable c)
        {
            return new MonoGameAsyncResult(Task.Factory.StartNew(c.call));
        }

        public void submit(FrameSpreadTask fst)
        {
            spreadTasks.AddLast(fst);
        }

        public void setMaxFrameTasksPerFrame(int i)
        {
            maxFrameTasksPerFrame = i;
        }
    }
}
