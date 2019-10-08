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
using Microsoft.Xna.Framework.Audio;
using monogame.Files;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Files;

namespace monogame.Audio
{
    public class MonoGameSound : global::Java.Lang.Object, Sound
    {
        public static readonly List<SoundEffectInstance> instances;
        
        private readonly SoundEffect _sound;
        private readonly List<long> _thisInstancesId;

        static MonoGameSound()
        {
            instances = new List<SoundEffectInstance>();
        }

        public MonoGameSound(FileHandle fileHandle)
        {
            _sound = ((MonoGameFileHandle) fileHandle).loadFromContentManager<SoundEffect>();
            _thisInstancesId = new List<long>();
        }
        
        public void dispose()
        {
            for (int i = 0; i < _thisInstancesId.Count; i++)
            {
                if (_thisInstancesId[i] != -1)
                {
                    instances[(int) _thisInstancesId[i]].Dispose();
                }
            }
            _sound.Dispose();
        }

        public long play()
        {
            return play(1);
        }

        public long play(float volume)
        {
            return play(volume, 1, 0);
        }

        private static float convertPitch(float pitch)
        {
            pitch -= 1;
            return pitch < 0 ? pitch * 2 : pitch;
        }

        private bool addInstance(long id)
        {
            for (int i = 0; i < _thisInstancesId.Count; i++)
            {
                if (_thisInstancesId[i] == -1)
                {
                    _thisInstancesId[i] = id;
                    return true;
                }
            }

            return false;
        }

        public long play(float volume, float pitch, float pan)
        {
            long index = instances.Count;
            if (!addInstance(index))
            {
                _thisInstancesId.Add(index);
            }
            var instance = _sound.CreateInstance();
            instances.Add(instance);
            instance.Volume = volume;
            instance.Pan = pan;
            instance.Pitch = convertPitch(pitch);
            instance.Play();
            return index;
        }

        public long loop()
        {
            return loop(1);
        }

        public long loop(float volume)
        {
            return loop(volume, 1, 1);
        }

        public long loop(float volume, float pitch, float pan)
        {
            long index = play(volume, pitch, pan);
            setLooping(index, true);
            return index;
        }

        public void stop()
        {
            for (int i = 0; i < _thisInstancesId.Count; i++)
            {
                stop(_thisInstancesId[i]);
            }
        }

        public void pause()
        {
            for (int i = 0; i < _thisInstancesId.Count; i++)
            {
                pause(_thisInstancesId[i]);
            }
        }

        public void resume()
        {
            for (int i = 0; i < _thisInstancesId.Count; i++)
            {
                resume(_thisInstancesId[i]);
            }
        }

        public void stop(long soundId)
        {
            if (soundId != -1)
            {
                var soundEffectInstance = instances[(int) soundId];
                if (soundEffectInstance != null)
                {
                    soundEffectInstance.Stop();
                    soundEffectInstance.Dispose();
                    instances[(int) soundId] = null;
                    MonoGameAudio.soundCompleted(soundId);
                }
                _thisInstancesId[(int) soundId] = -1;
            }
        }

        public static void dispose(long soundId)
        {
            var soundEffectInstance = instances[(int) soundId];
            if (soundEffectInstance != null)
            {
                soundEffectInstance.Stop();
                soundEffectInstance.Dispose();
            }
            instances[(int) soundId] = null;
        }

        public void pause(long soundId)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].Pause();
            }
        }

        public void resume(long soundId)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].Resume();
            }
        }

        public void setLooping(long soundId, bool looping)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].IsLooped = true;
            }
        }

        public void setPitch(long soundId, float pitch)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].Pitch = convertPitch(pitch);
            }
        }

        public void setVolume(long soundId, float volume)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].Volume = volume;
            }
        }

        public void setPan(long soundId, float pan, float volume)
        {
            if (soundId != -1)
            {
                if (instances[(int) soundId] == null)
                {
                    _thisInstancesId[(int) soundId] = -1;
                    return;
                }
                instances[(int) soundId].Volume = volume;
                instances[(int) soundId].Pan = pan;
            }
        }
    }
}