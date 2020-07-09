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
using Java.Io;
using Microsoft.Xna.Framework.Content;
using monogame.Util;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Files;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using File = System.IO.File;
using IOException = Java.Io.IOException;

namespace monogame.Files
{
    public class MonoGameFileHandle : global::Java.Lang.Object, FileHandle
    {
        private readonly FileType _fileType;
        private bool _isDirectory;
        private string _prefix;
        private readonly string _filename;
        private int _totalBytes;
        private DirectoryInfo _directoryInfo;
        private FileInfo _fileInfo;
        private readonly string _originalPath;

        public MonoGameFileHandle(string prefix, string path, FileType fileType)
        {
            _fileType = fileType;
            _prefix = prefix;
            _originalPath = path;
            path = prefix + path;

            if(Mdx.platform_.isDesktop())
            {
                _isDirectory = (File.Exists(path) || Directory.Exists(path)) && (File.GetAttributes(path) & FileAttributes.Directory) != 0;
                if (_isDirectory)
                {
                    _directoryInfo = new DirectoryInfo(path);
                    _filename = _directoryInfo.Name;
                    _totalBytes = -1;
                }
                else
                {
                    _fileInfo = new FileInfo(path);
                    _filename = _fileInfo.Name;
                    _totalBytes = _fileInfo.Exists ? (int) _fileInfo.Length : 0;
                }
            }
            else if(Mdx.platform_.isConsole())
            {
                _isDirectory = path.EndsWith("/");

                if (_isDirectory)
                {
                    _directoryInfo = new DirectoryInfo(path);
                    _totalBytes = -1;

                    string dirWithoutTrailingSlash = path.Substring(0, path.Length - 1);
                    _filename = dirWithoutTrailingSlash.Substring(dirWithoutTrailingSlash.LastIndexOf('/'));
                }
                else
                {
                    _fileInfo = new FileInfo(path);
                    _filename = path.Substring(path.LastIndexOf('/') + 1);
                    _totalBytes = _fileType.Equals(FileType.INTERNAL_) ? -1 : (_fileInfo.Exists ? (int)_fileInfo.Length : 0);
                }
            }
            else
            {
                throw new PlatformNotSupportedException();
            }
        }

        public virtual T loadFromContentManager<T>()
        {
            if (_fileType != FileType.INTERNAL_)
            {
                throw new NotSupportedException("You can load from contentManager only INTERNAL files");
            }

            ContentManager contentManager = ((MonoGameFiles)Mdx.files_)._contentManager;
            string resolvedPath = path();
            return contentManager.Load<T>(resolvedPath);
        }

        public Java.Lang.String pathWithPrefix()
        {
            return _prefix + _filename;
        }
        
        public Java.Lang.String path()
        {
            return _originalPath;
        }

        public Java.Lang.String normalize()
        {
            string path = this.path();
            while (path.Contains(".."))
            {
                path = Regex.Replace(path, "[^\\/]+\\/\\.\\.\\/", "");
            }
            while (path.Contains("./"))
            {
                path = Regex.Replace(path, "\\.\\/", "");
            }
            return path;
        }

        public FileHandle normalizedHandle()
        {
            if(type().Equals(FileType.INTERNAL_))
            {
                return Mdx.files_.@internal(normalize());
            }
            else if (type().Equals(FileType.EXTERNAL_))
            {
                return Mdx.files_.external(normalize());
            }
            else
            {
                return Mdx.files_.local(normalize());
            }
        }

        public Java.Lang.String name()
        {
            return _filename;
        }

        public Java.Lang.String extension()
        {
            string name = (string)this.name();
            return _isDirectory ? "" : name.Remove(0, name.LastIndexOf('.')+1);
        }

        public Java.Lang.String nameWithoutExtension()
        {
            string name = (string) this.name();
            var dotIndex = name.LastIndexOf('.');
            if (dotIndex == -1 && !_isDirectory)
            {
                return name;
            }
            return _isDirectory ? _filename : name.Substring(0, dotIndex);
        }

        public Java.Lang.String pathWithoutExtension()
        {
            string path = (string)this.path();
            var dotIndex = path.LastIndexOf('.');
            if (dotIndex == -1 && !_isDirectory)
            {
                return path;
            }
            return _isDirectory ? _filename : path.Substring(0, dotIndex);
        }

        public FileType type()
        {
            return _fileType;
        }

        public InputStream read()
        {
            if (_fileType == FileType.INTERNAL_)
            {
                return new MonoGameInputStream(((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path()));
            }
            return new MonoGameInputStream(this);
        }

        public BufferedInputStream read(int i)
        {
            BufferedInputStream inputStream = new BufferedInputStream();
            inputStream._init_(read(), i);
            return inputStream;
        }

        public Reader reader()
        {
            InputStreamReader inputStreamReader = new InputStreamReader();
            inputStreamReader._init_(read());
            return inputStreamReader;
        }

        public Reader reader(Java.Lang.String encoding)
        {
            InputStreamReader inputStreamReader = new InputStreamReader();
            inputStreamReader._init_(read(), encoding);
            return inputStreamReader;
        }

        public BufferedReader reader(int bufferSize)
        {
            BufferedReader bufferedReader = new BufferedReader();
            bufferedReader._init_(reader(), bufferSize);
            return bufferedReader;
        }

        public BufferedReader reader(int bufferSize, Java.Lang.String encoding)
        {
            BufferedReader bufferedReader = new BufferedReader();
            bufferedReader._init_(reader(encoding), bufferSize);
            return bufferedReader;
        }

        public Java.Lang.String readString()
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read string from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                using(StreamReader reader = new StreamReader(((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path())))
                {
                    return reader.ReadToEnd();
                }
            }

            return File.ReadAllText(pathWithPrefix());
        }

        public Java.Lang.String readString(Java.Lang.String encoding)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read string from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                return readString();
            }

            return File.ReadAllText(pathWithPrefix(), Encoding.GetEncoding((string) encoding));
        }

        public Java.Lang.String[] readAllLines()
        {
            string content = readString();
            string [] rawResult = content.Replace("\r\n", "\n").Split('\n');
            Java.Lang.String[] result = new Java.Lang.String[rawResult.Length];
            for(int i = 0; i < rawResult.Length; i++)
            {
                result[i] = rawResult[i];
            }
            return result;
        }
        
        public sbyte[] readBytes()
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                using (MemoryStream ms = new MemoryStream())
                {
                    ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path()).CopyTo(ms);
                    byte [] rawResult = ms.ToArray();
                    sbyte[] result = new sbyte[rawResult.Length];
                    for(int i = 0; i < rawResult.Length; i++)
                    {
                        result[i] = (sbyte) rawResult[i];
                    }
                    return result;
                }
            }

            var fileBytes = new sbyte[_totalBytes];
            readBytes(fileBytes, 0, _totalBytes);
            return fileBytes;
        }

        public byte[] readBytesAsByteArray()
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                using (MemoryStream ms = new MemoryStream())
                {
                    ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path()).CopyTo(ms);
                    return ms.ToArray();
                }
            }

            var fileBytes = new byte[_totalBytes];
            readBytes(fileBytes, 0, (int)_totalBytes);
            return fileBytes;
        }

        public int readBytes(byte[] fileBytes, int offset, int size)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            var readBytesNumber = 0;
            Stream byteStream;
            if (_fileType == FileType.INTERNAL_)
            {
                byteStream = ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path());
            }
            else
            {
                byteStream = _fileInfo.OpenRead();
            }

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
            byteStream.Close();

            return readBytesNumber;
        }

        public int readBytes(sbyte[] fileBytes, int offset, int size)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            var readBytesNumber = 0;
            Stream byteStream;
            if (_fileType == FileType.INTERNAL_)
            {
                byteStream = ((MonoGameFiles) Mdx.files_)._contentManager.OpenStream(path());
            }
            else
            {
                byteStream = _fileInfo.OpenRead();
            }

            for (var i = offset; i < offset + size; i++)
            {
                var readByte = byteStream.ReadByte();
                if (readByte == -1)
                {
                    break;
                }

                fileBytes[i] = (sbyte) readByte;
                readBytesNumber++;
            }
            byteStream.Close();

            return readBytesNumber;
        }

        public OutputStream write(bool append)
        {
            return new MonoGameOutputStream(this, append);
        }

        public OutputStream write(bool append, int bufferSize)
        {
            BufferedOutputStream writer = new BufferedOutputStream();
            writer._init_(write(append), bufferSize);
            return writer;
        }

        public Writer writer(bool append)
        {
            OutputStreamWriter writer = new OutputStreamWriter();
            writer._init_(write(append));
            return writer;
        }

        public Writer writer(bool append, Java.Lang.String encoding)
        {
            OutputStreamWriter writer = new OutputStreamWriter();
            writer._init_(write(append), encoding);
            return writer;
        }

        public void writeString(Java.Lang.String str, bool append)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't write string to a directory");
                throw exception;
            }

            if (type() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("Can't write in an INTERNAL file");
                throw exception;
            }

            if (append)
            {
                File.AppendAllText(pathWithPrefix(), str);
            }
            else
            {
                File.WriteAllText(pathWithPrefix(), str);
            }
        }

        public void writeString(Java.Lang.String str, bool append, Java.Lang.String encoding)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't write string to a directory");
                throw exception;
            }

            if (type() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("Can't write in an INTERNAL file");
                throw exception;
            }

            if (append)
            {
                File.AppendAllText(pathWithPrefix(), str, Encoding.GetEncoding((string) encoding));
            }
            else
            {
                File.WriteAllText(pathWithPrefix(), str, Encoding.GetEncoding((string) encoding));
            }
        }

        public void writeBytes(sbyte[] bytes, bool append)
        {
            writeBytes(bytes, 0, bytes.Length, append);
        }

        public void writeBytes(sbyte[] bytes, int offset, int size, bool append)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't write bytes to directory");
            }

            if (type() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("Can't write in an INTERNAL file");
                throw exception;
            }

            var fileStream = new FileStream(pathWithPrefix(), append ? FileMode.Append : FileMode.OpenOrCreate, FileAccess.Write);
            fileStream.Seek(offset, SeekOrigin.Begin);
            using (var streamWriter = new StreamWriter(fileStream))
            {
                for (var i = offset; i < offset + size; i++)
                {
                    streamWriter.Write(bytes[i]);
                }
            }
            _totalBytes = bytes.Length;
        }

        public FileHandle[] list()
        {
            return list("");
        }

        public FileHandle[] list(Java.Lang.String suffix)
        {
            if (!_isDirectory || _fileType == FileType.INTERNAL_)
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
                    var path = child.ToString();
                    if (_fileType == FileType.INTERNAL_)
                    {
                        path = path.Replace(".xnb", "");
                    }
                    matchingChilds.Add(new MonoGameFileHandle(_prefix, path, _fileType));
                }
            }

            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childDirs.Length; i++)
            {
                var child = childDirs[i];
                if (child.Name.EndsWith(suffix))
                {
                    matchingChilds.Add(new MonoGameFileHandle(_prefix, child.ToString(), _fileType));
                }
            }

            return matchingChilds.ToArray();
        }

        internal MonoGameFileHandle firstMatchingChildFile(Java.Lang.String prefix)
        {
            if (!_isDirectory )
                return null;
            var childFiles = _directoryInfo.GetFiles();

            //Not using foreach or LINQ query because they could be as much as 10x slower.

            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childFiles.Length; i++)
            {
                var child = childFiles[i];
                if (child.Name.StartsWith(prefix))
                {
                    string path = this.path();
                    var fileName = child.Name;
                    if (_fileType == FileType.INTERNAL_)
                    {
                        fileName = fileName.Replace(".xnb", "");
                    }
                    return new MonoGameFileHandle(_prefix, (path == string.Empty ? string.Empty : path + Path.DirectorySeparatorChar) + fileName, _fileType);
                }
            }

            return null;
        }

        public bool isDirectory()
        {
            return _isDirectory;
        }

        public FileHandle child(Java.Lang.String name)
        {
            return new MonoGameFileHandle(_prefix, Path.Combine(path(),  name), _fileType);
        }

        public FileHandle sibling(Java.Lang.String name)
        {
            return parent().child(name);
        }

        public FileHandle parent()
        {
            string path = "";
            string thisPath = this.path();
            if (thisPath.Contains("\\") || thisPath.Contains("/"))
            {
                path = (_isDirectory ? _directoryInfo.Parent : _fileInfo.Directory).ToString();
            }
            return new MonoGameFileHandle(_prefix, path, _fileType);
        }

        public void mkdirs()
        {
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't mkdirs() an INTERNAL file");
                throw exception;
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
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't delete() an INTERNAL file");
                throw exception;
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
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't deleteDirectory() an INTERNAL file");
                throw exception;
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
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't emptyDirectory() an INTERNAL file");
                throw exception;
            }

            emptyDirectory(false);
        }

        public void emptyDirectory(bool preserveTree)
        {
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't emptyDirectory() an INTERNAL file");
                throw exception;
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
            if (dest.type() == FileType.INTERNAL_)
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
                        
                        _fileInfo.CopyTo(((MonoGameFileHandle)dest.child(name())).pathWithPrefix(), true);
                    }
                    else
                    {
                        _fileInfo.CopyTo(((MonoGameFileHandle) dest).pathWithPrefix(), true);
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
            if (_fileType == FileType.INTERNAL_ || dest.type() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't moveTo() an INTERNAL file");
                throw exception;
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
            return _isDirectory ? 0 : _totalBytes;
        }

        public long lastModified()
        {
            if (_fileType == FileType.INTERNAL_ || !exists())
            {
                return 0;
            }
            if (Mdx.platform_.isConsole())
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
                Java.Io.File file = new Java.Io.File();
                file._init_(pathWithPrefix());
                if (filter.accept(file, childs[i].name()))
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
                Java.Io.File file = new Java.Io.File();
                file._init_(((MonoGameFileHandle)childs[i]).pathWithPrefix());
                if (filter.accept(file))
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