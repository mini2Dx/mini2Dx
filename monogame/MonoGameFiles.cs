using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using org.mini2Dx.core.files;

namespace monogame
{
    class MonoGameFiles : org.mini2Dx.core.Files
    {
        public FileHandle external(string str)
        {
            throw new NotImplementedException();
        }

        public FileHandle @internal(string str)
        {
            throw new NotImplementedException();
        }

        public bool isExternalStorageAvailable()
        {
            throw new NotImplementedException();
        }

        public bool isLocalStorageAvailable()
        {
            throw new NotImplementedException();
        }

        public FileHandle local(string str)
        {
            throw new NotImplementedException();
        }
    }
}
