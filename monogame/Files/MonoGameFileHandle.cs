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
using System.Text;
using java.io;
using monogame.Util;
using org.mini2Dx.core.files;
using File = System.IO.File;
using IOException = java.io.IOException;

namespace monogame.Files
{
    public class MonoGameFileHandle : FileHandle
    {
        private readonly FileType _fileType;
        private readonly FileInfo _fileInfo;
        private DirectoryInfo _directoryInfo;
        private bool _isDirectory;
        
        public MonoGameFileHandle(string path, FileType fileType)
        {
            _fileType = fileType;
            _isDirectory = (File.GetAttributes(path) & FileAttributes.Directory) != 0;
            if (_isDirectory)
            {
                _directoryInfo = new DirectoryInfo(path);
            }
            else
            {
                _fileInfo = new FileInfo(path);
            }
        }

        public string fullPath()
        {
            return (isDirectory() ? (object) _directoryInfo : _fileInfo).ToString();
        }
        
        public string path()
        {
            return _fileType == FileType.INTERNAL ? fullPath().Remove(0, MonoGameFiles.InternalFilePrefix.Length) : fullPath();
        }

        public string normalize()
        {
            throw new NotImplementedException(); //You can't normalize a path in C#, see dotnet/corefx#30701 and dotnet/corefx#4208.
        }

        public string name()
        {
            return _isDirectory ? _directoryInfo.Name : _fileInfo.Name;
        }

        public string extension()
        {
            return _isDirectory ? "" : name().Remove(0, name().LastIndexOf('.')+1);
        }

        public string nameWithoutExtension()
        {
            return _isDirectory ? _directoryInfo.Name : name().Substring(0, name().LastIndexOf('.'));
        }

        public string pathWithoutExtension()
        {
            return _isDirectory ? _directoryInfo.ToString() : path().Substring(0, path().LastIndexOf('.'));
        }

        public FileType type()
        {
            return _fileType;
        }

        public InputStream read()
        {
            return new MonoGameInputStream(this);
        }

        public BufferedInputStream read(int i)
        {
            return new BufferedInputStream(read(), i);
        }

        public Reader reader()
        {
            return new InputStreamReader(read());
        }

        public Reader reader(string encoding)
        {
            return new InputStreamReader(read(), encoding);
        }

        public BufferedReader reader(int bufferSize)
        {
            return new BufferedReader(reader(), bufferSize);
        }

        public BufferedReader reader(int bufferSize, string encoding)
        {
            return new BufferedReader(reader(encoding), bufferSize);
        }

        public string readString()
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read from a directory");
            }

            return File.ReadAllText(fullPath());
        }

        public string readString(string encoding)
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read from a directory");
            }

            return File.ReadAllText(fullPath(), Encoding.GetEncoding(encoding));
        }

        public byte[] readBytes()
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read from a directory");
            }

            var fileBytes = new byte[_fileInfo.Length];

            readBytes(fileBytes, 0, (int) _fileInfo.Length);

            return fileBytes;
        }

        public int readBytes(byte[] fileBytes, int offset, int size)
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read from a directory");
            }

            var readBytesNumber = 0;
            using (var byteStream = _fileInfo.OpenRead())
            {
                for (var i = offset; i < offset + size; i++)
                {
                    var readByte = byteStream.ReadByte();
                    if (readByte == -1)
                    {
                        break;
                    }

                    fileBytes[i] = (byte) readByte;
                    readBytesNumber++;
                }
            }

            return readBytesNumber;
        }

        public OutputStream write(bool append)
        {
            return new MonoGameOutputStream(this, append);
        }

        public OutputStream write(bool append, int bufferSize)
        {
            return new BufferedOutputStream(write(append), bufferSize);
        }

        public Writer writer(bool append)
        {
            return new OutputStreamWriter(write(append));
        }

        public Writer writer(bool append, string encoding)
        {
            return new OutputStreamWriter(write(append), encoding);
        }

        public void writeString(string str, bool append)
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read write to directory");
            }

            if (type() == FileType.INTERNAL)
            {
                throw new IOException("Can't write in an INTERNAL file");
            }

            if (append)
            {
                File.AppendAllText(fullPath(), str);
            }
            else
            {
                File.WriteAllText(fullPath(), str);
            }
        }

        public void writeString(string str, bool append, string encoding)
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read write to directory");
            }

            if (type() == FileType.INTERNAL)
            {
                throw new IOException("Can't write in an INTERNAL file");
            }

            if (append)
            {
                File.AppendAllText(fullPath(), str, Encoding.GetEncoding(encoding));
            }
            else
            {
                File.WriteAllText(fullPath(), str, Encoding.GetEncoding(encoding));
            }
        }

        public void writeBytes(byte[] bytes, bool append)
        {
            writeBytes(bytes, 0, bytes.Length, append);
        }

        public void writeBytes(byte[] bytes, int offset, int size, bool append)
        {
            if (_isDirectory)
            {
                throw new IOException("Can't read write to directory");
            }

            if (type() == FileType.INTERNAL)
            {
                throw new IOException("Can't write in an INTERNAL file");
            }

            var fileStream = new FileStream(fullPath(), append ? FileMode.Append : FileMode.OpenOrCreate, FileAccess.Write);
            fileStream.Seek(offset, SeekOrigin.Begin);
            using (var streamWriter = new StreamWriter(fileStream))
            {
                for (var i = offset; i < offset + size; i++)
                {
                    streamWriter.Write(bytes[i]);
                }
            }
        }

        public FileHandle[] list()
        {
            return list("");
        }

        public FileHandle[] list(string suffix)
        {
            if (!_isDirectory || _fileType == FileType.INTERNAL)
                return new FileHandle[0];
            var matchingChilds = new List<FileHandle>();
            var childFiles = _directoryInfo.GetFiles();
            var childDirs = _directoryInfo.GetDirectories();

            //Not using foreach or LINQ query because they could be as much as 10x slower.
            
            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childFiles.Length; i++)
            {
                var child = childFiles[i];
                if (child.Name.EndsWith(suffix))
                {
                    matchingChilds.Add(new MonoGameFileHandle(child.ToString(), _fileType));
                }
            }

            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childDirs.Length; i++)
            {
                var child = childDirs[i];
                if (child.Name.EndsWith(suffix))
                {
                    matchingChilds.Add(new MonoGameFileHandle(child.ToString(), _fileType));
                }
            }

            return matchingChilds.ToArray();
        }

        public bool isDirectory()
        {
            return _isDirectory;
        }

        public FileHandle child(string name)
        {
            return new MonoGameFileHandle(Path.Combine(fullPath(),  name), _fileType);
        }

        public FileHandle sibling(string name)
        {
            return parent().child(name);
        }

        public FileHandle parent()
        {
            return new MonoGameFileHandle((_isDirectory ? _directoryInfo.Parent : _fileInfo.Directory).ToString(), _fileType);
        }

        public void mkdirs()
        {
            if (_fileType == FileType.INTERNAL)
            {
                throw new IOException("You can't mkdirs() an INTERNAL file");
            }

            _directoryInfo = Directory.CreateDirectory(_fileInfo.ToString());
            _isDirectory = true;
        }

        public bool exists()
        {
            return _isDirectory ? _directoryInfo.Exists : _fileInfo.Exists;
        }

        public bool delete()
        {
            if (_fileType == FileType.INTERNAL)
            {
                throw new IOException("You can't delete() an INTERNAL file");
            }

            if (_isDirectory)
            {
                if (_directoryInfo.GetDirectories().Length + _directoryInfo.GetFiles().Length != 0)
                {
                    return false;
                }

                _directoryInfo.Delete();
            }
            else
            {
                _fileInfo.Delete();
            }

            return true;
        }

        public bool deleteDirectory()
        {
            if (_fileType == FileType.INTERNAL)
            {
                throw new IOException("You can't deleteDirectory() an INTERNAL file");
            }

            if (_isDirectory)
            {
                try
                {
                    _directoryInfo.Delete(true);
                }
                catch (Exception)
                {
                    return false;
                }
            }
            else
            {
                try
                {
                    _fileInfo.Delete();
                }
                catch (Exception)
                {
                    return false;
                }
            }

            return true;
        }

        public void emptyDirectory()
        {
            if (_fileType == FileType.INTERNAL)
            {
                throw new IOException("You can't emptyDirectory() an INTERNAL file");
            }

            emptyDirectory(false);
        }

        public void emptyDirectory(bool preserveTree)
        {
            if (_fileType == FileType.INTERNAL)
            {
                throw new IOException("You can't emptyDirectory() an INTERNAL file");
            }

            foreach (var child in list())
            {
                if (!preserveTree)
                {
                    child.deleteDirectory();
                }
                else
                {
                    if (child.isDirectory())
                    {
                        child.emptyDirectory(false);
                    }
                    else
                    {
                        child.delete();
                    }
                }
            }
        }

        //https://stackoverflow.com/a/690980
        private static void CopyAll(DirectoryInfo source, DirectoryInfo target)
        {
            Directory.CreateDirectory(target.FullName);
            
            var files = source.GetFiles();
            for (var i = 0; i < files.Length; i++)
            {
                var fileInfo = files[i];
                fileInfo.CopyTo(Path.Combine(target.FullName, fileInfo.Name), true);
            }

            var subdirectories = source.GetDirectories();
            for (var i = 0; i < subdirectories.Length; i++)
            {
                var subdirectoryInfo = subdirectories[i];
                var nextTargetSubDir = target.CreateSubdirectory(subdirectoryInfo.Name);
                CopyAll(subdirectoryInfo, nextTargetSubDir);
            }
        }

        public void copyTo(FileHandle dest)
        {
            if (dest.type() == FileType.INTERNAL)
            {
                throw new IOException();
            }

            if (_isDirectory)
            {
                if (dest.exists() && !dest.isDirectory())
                {
                    throw new IOException();
                }

                CopyAll(_directoryInfo, ((MonoGameFileHandle)dest)._directoryInfo);
            }
            else
            {
                if (dest.exists())
                {
                    if (dest.isDirectory())
                    {
                        
                        _fileInfo.CopyTo(((MonoGameFileHandle)dest.child(name())).fullPath(), true);
                    }
                    else
                    {
                        _fileInfo.CopyTo(((MonoGameFileHandle) dest).fullPath(), true);
                    }
                }
                else
                {
                    dest.parent().mkdirs();
                    
                    _fileInfo.CopyTo(((MonoGameFileHandle) dest)._fileInfo.ToString(), true);
                }
            }
        }

        public void moveTo(FileHandle dest)
        {
            if (_fileType == FileType.INTERNAL || dest.type() == FileType.INTERNAL)
            {
                throw new IOException("You can't moveTo() an INTERNAL file");
            }

            if (dest.exists())
            {
                dest.deleteDirectory();
            }

            if (_isDirectory)
            {
                _directoryInfo.MoveTo(dest.ToString());
            }
            else
            {
                _fileInfo.MoveTo(dest.ToString());
            }
        }

        public long length()
        {
            return _isDirectory ? 0 : _fileInfo.Length;
        }

        public long lastModified()
        {
            if (_fileType == FileType.INTERNAL || !exists())
            {
                return 0;
            }

            return _isDirectory ? DateTimeToTotalMs(_directoryInfo.LastWriteTimeUtc) : DateTimeToTotalMs(_fileInfo.LastWriteTimeUtc);
        }

        public FileHandle[] list(FilenameFilter filter)
        {
            var matchingChilds = new List<FileHandle>();

            var childs = list();

            //Not using foreach or LINQ query because they could be as much as 10x slower.
            
            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childs.Length; i++)
            {
                if (filter.accept(new java.io.File(fullPath()), childs[i].name()))
                {
                    matchingChilds.Add(childs[i]);
                }
            }

            return matchingChilds.ToArray();
        }

        public FileHandle[] list(FileFilter filter)
        {
            var matchingChilds = new List<FileHandle>();

            var childs = list();

            //Not using foreach or LINQ query because they could be as much as 10x slower.
            
            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childs.Length; i++)
            {
                if (filter.accept(new java.io.File(((MonoGameFileHandle) childs[i]).fullPath())))
                {
                    matchingChilds.Add(childs[i]);
                }
            }

            return matchingChilds.ToArray();
        }

        public void write(InputStream inputStream, bool append)
        {
            var outputStream = write(append);
            var read = inputStream.read();
            while (read != -1)
            {
                outputStream.write(read);
                read = inputStream.read();
            }
        }

        private static long DateTimeToTotalMs(DateTime dateTime)
        {
            return dateTime.Subtract(new DateTime(1970, 1, 1)).Ticks / TimeSpan.TicksPerMillisecond;
        }
    }
}