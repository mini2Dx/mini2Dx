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
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using Microsoft.Xna.Framework.Audio;
using monogame.Files;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Files;

namespace monogame.Audio
{
    public class MonoGameSound : global::Java.Lang.Object, Sound
    {
        private readonly SoundEffect _sound;
        private readonly List<long> _thisInstancesIds;

        public MonoGameSound(FileHandle fileHandle)
        {
            _sound = ((MonoGameFileHandle) fileHandle).loadFromContentManager<SoundEffect>();
            _thisInstancesIds = new List<long>();
        }
        
        //todo implement a proper solution for disposing SoundEffects
        public void dispose_EFE09FC0()
        {
            for (int i = _thisInstancesIds.Count - 1; i >= 0; i--)
            {
                if (_thisInstancesIds[i] != -1 && MonoGameSoundInstance.InstancesById.ContainsKey(_thisInstancesIds[i]))
                {
                    MonoGameSoundInstance.InstancesById[_thisInstancesIds[i]].Dispose();
                }
            }
            //_sound.Dispose();
        }

        public long play_0BE0CBD4()
        {
            return play_9B414416(1);
        }

        public long play_9B414416(float volume)
        {
            return play_4956CC16(volume, 1, 0);
        }

        private static float convertPitch(float pitch)
        {
            pitch -= 1;
            return pitch < 0 ? pitch * 2 : pitch;
        }

        public long play_4956CC16(float volume, float pitch, float pan)
        {
            var soundEffectInstance = _sound.CreateInstance();
            var soundInstance = MonoGameSoundInstance.Allocate(this, soundEffectInstance);

            soundEffectInstance.Volume = volume;
            soundEffectInstance.Pan = pan;
            soundEffectInstance.Pitch = convertPitch(pitch);
            soundEffectInstance.Play();
            return soundInstance.Id;
        }

        public long loop_0BE0CBD4()
        {
            return loop_9B414416(1);
        }

        public long loop_9B414416(float volume)
        {
            return loop_4956CC16(volume, 1, 1);
        }

        public long loop_4956CC16(float volume, float pitch, float pan)
        {
            long index = play_4956CC16(volume, pitch, pan);
            setLooping_98E3C020(index, true);
            return index;
        }

        public void stop_EFE09FC0()
        {
            for (int i = 0; i < _thisInstancesIds.Count; i++)
            {
                stop_5FE5E296(_thisInstancesIds[i]);
            }
        }

        public void pause_EFE09FC0()
        {
            for (int i = 0; i < _thisInstancesIds.Count; i++)
            {
                pause_5FE5E296(_thisInstancesIds[i]);
            }
        }

        public void resume_EFE09FC0()
        {
            for (int i = 0; i < _thisInstancesIds.Count; i++)
            {
                resume_5FE5E296(_thisInstancesIds[i]);
            }
        }

        public void stopTracking(long soundId)
        {
            _thisInstancesIds.Remove(soundId);
        }

        public void stop_5FE5E296(long soundId)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;

            if (soundEffectInstance != null)
            {
                soundEffectInstance.Stop();
                soundEffectInstance.Dispose();
                MonoGameAudio.soundCompleted(soundId);
            }
            soundInstance.Dispose();
        }

        public void pause_5FE5E296(long soundId)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.Pause();
        }

        public void resume_5FE5E296(long soundId)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.Resume();
        }

        public void setLooping_98E3C020(long soundId, bool looping)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.IsLooped = true;
        }

        public void setPitch_F9247704(long soundId, float pitch)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.Pitch = convertPitch(pitch);
        }

        public void setVolume_F9247704(long soundId, float volume)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.Volume = volume;
        }

        public void setPan_3604DC16(long soundId, float pan, float volume)
        {
            if (soundId == -1)
            {
                return;
            }
            if (!MonoGameSoundInstance.InstancesById.ContainsKey(soundId))
            {
                return;
            }
            var soundInstance = MonoGameSoundInstance.InstancesById[soundId];
            var soundEffectInstance = soundInstance.SoundEffectInstance;
            soundEffectInstance.Volume = volume;
            soundEffectInstance.Pan = pan;
        }
    }

    public class MonoGameSoundInstance
    {
        public static readonly List<MonoGameSoundInstance> Instances = new List<MonoGameSoundInstance>();
        public static readonly Dictionary<long, MonoGameSoundInstance> InstancesById = new Dictionary<long, MonoGameSoundInstance>();

        private static List<MonoGameSoundInstance> POOL = new List<MonoGameSoundInstance>();
        private static long ID_ALLOCATOR = 0;

        public MonoGameSound Sound { get; private set; }
        public SoundEffectInstance SoundEffectInstance { get; private set; }
        public long Id { get; private set; }

        private MonoGameSoundInstance(){}

        public void Dispose()
        {
            Monitor.Enter(POOL);
            try
            {
                Instances.Remove(this);
                InstancesById.Remove(Id);

                Sound.stopTracking(Id);
                Sound = null;
                SoundEffectInstance = null;
                POOL.Add(this);
            }
            finally
            {
                Monitor.Exit(POOL);
            }
        }

        public static MonoGameSoundInstance Allocate(MonoGameSound sound, SoundEffectInstance soundEffectInstance)
        {
            Monitor.Enter(POOL);
            try
            {
                MonoGameSoundInstance result = null;
                if (POOL.Count == 0)
                {
                    result = new MonoGameSoundInstance();
                }
                else
                {
                    result = POOL[0];
                    POOL.RemoveAt(0);
                }
                result.Id = ID_ALLOCATOR++;
                result.Sound = sound;
                result.SoundEffectInstance = soundEffectInstance;

                if(ID_ALLOCATOR >= long.MaxValue)
                {
                    ID_ALLOCATOR = 0;
                }

                Instances.Add(result);
                InstancesById.Add(result.Id, result);
                return result;
            }
            finally
            {
                Monitor.Exit(POOL);
            }
        }
    }
}