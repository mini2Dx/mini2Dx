using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using org.mini2Dx.core.audio;
using org.mini2Dx.core.files;

namespace monogame
{
    class MonoGameAudio : org.mini2Dx.core.Audio
    {
        public Music newMusic(FileHandle fileHandle)
        {
            throw new NotImplementedException();
        }

        public void addMusicCompletionListener(MusicCompletionListener completionListener)
        {
            throw new NotImplementedException();
        }

        public void removeMusicCompletionListener(MusicCompletionListener completionListener)
        {
            throw new NotImplementedException();
        }

        public void addSoundCompletionListener(SoundCompletionListener completionListener)
        {
            throw new NotImplementedException();
        }

        public void removeSoundCompletionListener(SoundCompletionListener completionListener)
        {
            throw new NotImplementedException();
        }

        public Sound newSound(FileHandle fileHandle)
        {
            throw new NotImplementedException();
        }
    }
}
