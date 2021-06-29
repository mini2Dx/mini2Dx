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
using Java.Nio.Charset;
using Java.Util;
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
        private string _filename;
        private int _totalBytes;
        private DirectoryInfo _directoryInfo;
        private FileInfo _fileInfo;
        private readonly string _originalPath, _resolvedPath;

        private bool _referenceDirty = true;

        public MonoGameFileHandle(string prefix, string path, FileType fileType)
        {
            _fileType = fileType;
            _prefix = prefix;
            _originalPath = path;
            _resolvedPath = path;

            _referenceDirty = true;
            CreateFileInfoReference();
        }

        private void CreateFileInfoReference()
        {
            if(!_referenceDirty)
            {
                return;
            }
            string path = _prefix + _originalPath;
            if (Mdx.platform_.isDesktop_FBE0B2A4())
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
                    if (!_fileType.equals_08BE88A2(FileType.EXTERNAL_))
                    {
                        if (!path.EndsWith(".xnb"))
                        {
                            _fileInfo = new FileInfo(path + ".xnb");
                            _filename = _fileInfo.Name.Replace(".xnb", "");
                        }
                        else
                        {
                            _fileInfo = new FileInfo(path);
                            _filename = _fileInfo.Name.Replace(".xnb", "");
                        }
                    }
                    else
                    {
                        _fileInfo = new FileInfo(path);
                        _filename = _fileInfo.Name;
                    }
                    _totalBytes = _fileInfo.Exists ? (int)_fileInfo.Length : 0;
                }
            }
            else if (Mdx.platform_.isConsole_FBE0B2A4())
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
                    if (!_fileType.equals_08BE88A2(FileType.EXTERNAL_))
                    {
                        if (!path.EndsWith(".xnb"))
                        {
                            _fileInfo = new FileInfo(path + ".xnb");
                            _filename = _fileInfo.Name.Replace(".xnb", "");
                        }
                        else
                        {
                            _fileInfo = new FileInfo(path);
                            _filename = _fileInfo.Name.Replace(".xnb", "");
                        }
                    }
                    else
                    {
                        _fileInfo = new FileInfo(path);
                        _filename = _fileInfo.Name;
                    }
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
            string resolvedPath = path_E605312C();
            return contentManager.Load<T>(resolvedPath);
        }

        public Java.Lang.String originalPath()
        {
            return _originalPath;
        }
        
        public Java.Lang.String path_E605312C()
        {
            return _originalPath.Replace('\\', '/');
        }

        public Java.Lang.String normalize_E605312C()
        {
            string path = this.path_E605312C();
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

        public FileHandle normalizedHandle_D17169DC()
        {
            if(type_28835A82().Equals(FileType.INTERNAL_))
            {
                return Mdx.files_.internal_1F3F44D2(normalize_E605312C());
            }
            else if (type_28835A82().Equals(FileType.EXTERNAL_))
            {
                return Mdx.files_.external_1F3F44D2(normalize_E605312C());
            }
            else
            {
                return Mdx.files_.local_1F3F44D2(normalize_E605312C());
            }
        }

        public Java.Lang.String name_E605312C()
        {
            return _filename;
        }

        public Java.Lang.String extension_E605312C()
        {
            string name = (string)this.name_E605312C();
            return _isDirectory ? "" : name.Remove(0, name.LastIndexOf('.')+1);
        }

        public Java.Lang.String nameWithoutExtension_E605312C()
        {
            string name = (string) this.name_E605312C();
            var dotIndex = name.LastIndexOf('.');
            if (dotIndex == -1 && !_isDirectory)
            {
                return name;
            }
            return _isDirectory ? _filename : name.Substring(0, dotIndex);
        }

        public Java.Lang.String pathWithoutExtension_E605312C()
        {
            string path = (string)this.path_E605312C();
            var dotIndex = path.LastIndexOf('.');
            if (dotIndex == -1 && !_isDirectory)
            {
                return path;
            }
            return _isDirectory ? _filename : path.Substring(0, dotIndex);
        }

        public FileType type_28835A82()
        {
            return _fileType;
        }

        public sbyte[] headBytes_0C2FFF8A(int totalBytes)
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream._init_();
            InputStream inputStream = read_C679A59B();
            for (int i = 0; i < totalBytes; i++)
            {
                outputStream.write_3518BA33(inputStream.read_0EE0D08D());
            }
            inputStream.close_EFE09FC0();
            outputStream.close_EFE09FC0();
            return outputStream.toByteArray_A27A8875();
        }

        public InputStream read_C679A59B()
        {
            if (_fileType == FileType.INTERNAL_)
            {
                return new MonoGameInputStream(((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C()));
            }
            return new MonoGameInputStream(this);
        }

        public BufferedInputStream read_0CD0568B(int i)
        {
            BufferedInputStream inputStream = new BufferedInputStream();
            inputStream._init_(read_C679A59B(), i);
            return inputStream;
        }

        public Reader reader_58C463C2()
        {
            InputStreamReader inputStreamReader = new InputStreamReader();
            inputStreamReader._init_(read_C679A59B());
            return inputStreamReader;
        }

        public Reader reader_6525B0FC(Java.Lang.String encoding)
        {
            InputStreamReader inputStreamReader = new InputStreamReader();
            inputStreamReader._init_(read_C679A59B(), encoding);
            return inputStreamReader;
        }

        public BufferedReader reader_305041F2(int bufferSize)
        {
            BufferedReader bufferedReader = new BufferedReader();
            bufferedReader._init_(reader_58C463C2(), bufferSize);
            return bufferedReader;
        }

        public BufferedReader reader_F766227C(int bufferSize, Java.Lang.String encoding)
        {
            BufferedReader bufferedReader = new BufferedReader();
            bufferedReader._init_(reader_6525B0FC(encoding), bufferSize);
            return bufferedReader;
        }

        public Java.Lang.String head_E605312C()
        {
            return head_CB390DF2(Charset.defaultCharset_89B05436().name_E605312C());
        }

        public Java.Lang.String head_A0318287(int lines)
        {
            return head_8F8A7B3D(lines, Charset.defaultCharset_89B05436().name_E605312C());
        }

        public Java.Lang.String head_CB390DF2(Java.Lang.String encoding)
        {
            return head_8F8A7B3D(10, encoding);
        }

        public Java.Lang.String head_8F8A7B3D(int lines, Java.Lang.String encoding)
        {
            Reader reader = this.reader_6525B0FC(encoding);
            Scanner scanner = new Scanner();
            scanner._init_(reader);
            Java.Lang.StringBuilder result = new Java.Lang.StringBuilder();
            result._init_();

            int line = 0;
            while (line < lines && scanner.hasNextLine_FBE0B2A4())
            {
                result.append_00873BF7(scanner.nextLine_E605312C());
                result.append_08AC13B2('\n');

                line++;
            }
            scanner.close_EFE09FC0();
            reader.close_EFE09FC0();
            return result.toString_E605312C();
        }

        public Java.Lang.String readString_E605312C()
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read string from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                using(StreamReader reader = new StreamReader(((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C())))
                {
                    return reader.ReadToEnd();
                }
            }

            return File.ReadAllText(originalPath());
        }

        public Java.Lang.String readString_CB390DF2(Java.Lang.String encoding)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read string from a directory");
                throw exception;
            }

            if (_fileType == FileType.INTERNAL_)
            {
                return readString_E605312C();
            }

            return File.ReadAllText(originalPath(), Encoding.GetEncoding((string) encoding));
        }

        public Java.Lang.String[] readAllLines_37B5F23D()
        {
            string content = readString_E605312C();
            string [] rawResult = content.Replace("\r\n", "\n").Split('\n');
            Java.Lang.String[] result = new Java.Lang.String[rawResult.Length];
            for(int i = 0; i < rawResult.Length; i++)
            {
                result[i] = rawResult[i];
            }
            return result;
        }
        
        public sbyte[] readBytes_A27A8875()
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
                    ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C()).CopyTo(ms);
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
            readBytes_A3D15292(fileBytes, 0, _totalBytes);
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
                    ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C()).CopyTo(ms);
                    return ms.ToArray();
                }
            }

            var fileBytes = new byte[_totalBytes];
            readBytes_A3D15292(fileBytes, 0, (int)_totalBytes);
            return fileBytes;
        }

        public int readBytes_A3D15292(byte[] fileBytes, int offset, int size)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            CreateFileInfoReference();

            var readBytesNumber = 0;
            Stream byteStream;
            if (_fileType == FileType.INTERNAL_)
            {
                byteStream = ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C());
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

        public int readBytes_A3D15292(sbyte[] fileBytes, int offset, int size)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't read bytes from a directory");
                throw exception;
            }

            CreateFileInfoReference();

            var readBytesNumber = 0;
            Stream byteStream;
            if (_fileType == FileType.INTERNAL_)
            {
                byteStream = ((MonoGameFiles) Mdx.files_)._contentManager.OpenStream(path_E605312C());
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

        public OutputStream write_FF837EC6(bool append)
        {
            return new MonoGameOutputStream(this, append);
        }

        public OutputStream write_55C7CCC1(bool append, int bufferSize)
        {
            BufferedOutputStream writer = new BufferedOutputStream();
            writer._init_(write_FF837EC6(append), bufferSize);
            _referenceDirty = true;
            return writer;
        }

        public Writer writer_AF9E0B52(bool append)
        {
            OutputStreamWriter writer = new OutputStreamWriter();
            writer._init_(write_FF837EC6(append));
            _referenceDirty = true;
            return writer;
        }

        public Writer writer_09961AAC(bool append, Java.Lang.String encoding)
        {
            OutputStreamWriter writer = new OutputStreamWriter();
            writer._init_(write_FF837EC6(append), encoding);
            _referenceDirty = true;
            return writer;
        }

        public void writeString_295343E0(Java.Lang.String str, bool append)
        {
            writeString_C7DD1376(str, append, "UTF-8");
        }

        public void writeString_C7DD1376(Java.Lang.String str, bool append, Java.Lang.String encoding)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't write string to a directory");
                throw exception;
            }

            if (type_28835A82() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("Can't write in an INTERNAL file");
                throw exception;
            }

            if (append)
            {
                File.AppendAllText(originalPath(), str, Encoding.GetEncoding((string) encoding));
            }
            else
            {
                File.WriteAllText(originalPath(), str, Encoding.GetEncoding((string) encoding));
            }
            _referenceDirty = true;
        }

        public void writeBytes_5D7BA431(sbyte[] bytes, bool append)
        {
            writeBytes_ED67A059(bytes, 0, bytes.Length, append);
        }

        public void writeBytes_ED67A059(sbyte[] bytes, int offset, int size, bool append)
        {
            if (_isDirectory)
            {
                IOException exception = new IOException();
                exception._init_("Can't write bytes to directory");
            }

            if (type_28835A82() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("Can't write in an INTERNAL file");
                throw exception;
            }

            var fileStream = new FileStream(originalPath(), append ? FileMode.Append : FileMode.OpenOrCreate, FileAccess.Write);
            fileStream.Seek(offset, SeekOrigin.Begin);
            using (var streamWriter = new StreamWriter(fileStream))
            {
                for (var i = offset; i < offset + size; i++)
                {
                    streamWriter.Write(bytes[i]);
                }
            }
            _totalBytes = bytes.Length;
            _referenceDirty = true;
        }

        public FileHandle[] list_685A3945()
        {
            return list_2A7820BF("");
        }

        public FileHandle[] list_2A7820BF(Java.Lang.String suffix)
        {
            CreateFileInfoReference();

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
                    string path = this.path_E605312C();
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

        public bool isDirectory_FBE0B2A4()
        {
            return _isDirectory;
        }

        public FileHandle child_1F3F44D2(Java.Lang.String name)
        {
            return new MonoGameFileHandle(_prefix, Path.Combine(path_E605312C(),  name).Replace("\\", "/"), _fileType);
        }

        public FileHandle sibling_1F3F44D2(Java.Lang.String name)
        {
            return parent_D17169DC().child_1F3F44D2(name);
        }

        public FileHandle parent_D17169DC()
        {
            CreateFileInfoReference();

            if(IsRoot())
            {
                return this;
            }

            string path = "";
            string thisPath = this.path_E605312C();
            if (thisPath.Contains("\\") || thisPath.Contains("/"))
            {
                if(_fileType == FileType.INTERNAL_)
                {
                    if (thisPath.EndsWith("/"))
                    {
                        string tmpPath = thisPath.Substring(0, thisPath.Length - 1);
                        path = tmpPath.Substring(0, tmpPath.LastIndexOf('/'));
                    }
                    else
                    {
                        path = thisPath.Substring(0, thisPath.LastIndexOf('/'));
                    }
                }
                else if(_isDirectory)
                {
                    path = _directoryInfo.Parent.ToString();
                }
                else
                {
                    path = _fileInfo.Directory.ToString();
                }
            }
            return new MonoGameFileHandle(_prefix, path, _fileType);
        }

        public void mkdirs_EFE09FC0()
        {
            if (IsRoot())
            {
                return;
            }
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't mkdirs() an INTERNAL file");
                throw exception;
            }

            if(_fileInfo == null)
            {
                _directoryInfo = Directory.CreateDirectory(_resolvedPath);
            }
            else
            {
                _directoryInfo = Directory.CreateDirectory(_fileInfo.ToString());
            }
            _isDirectory = true;
        }

        public bool exists_FBE0B2A4()
        {
            CreateFileInfoReference();
            return _isDirectory ? _directoryInfo.Exists : _fileInfo.Exists;
        }

        public bool delete_FBE0B2A4()
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

        public bool deleteDirectory_FBE0B2A4()
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

        public void emptyDirectory_EFE09FC0()
        {
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't emptyDirectory() an INTERNAL file");
                throw exception;
            }

            emptyDirectory_AA5A2C66(false);
        }

        public void emptyDirectory_AA5A2C66(bool preserveTree)
        {
            if (_fileType == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't emptyDirectory() an INTERNAL file");
                throw exception;
            }

            foreach (var child in list_685A3945())
            {
                if (!preserveTree)
                {
                    child.deleteDirectory_FBE0B2A4();
                }
                else
                {
                    if (child.isDirectory_FBE0B2A4())
                    {
                        child.emptyDirectory_AA5A2C66(false);
                    }
                    else
                    {
                        child.delete_FBE0B2A4();
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

        public void copyTo_88D76E6E(FileHandle dest)
        {
            if (dest.type_28835A82() == FileType.INTERNAL_)
            {
                throw new IOException();
            }

            if (_isDirectory)
            {
                if (dest.exists_FBE0B2A4() && !dest.isDirectory_FBE0B2A4())
                {
                    throw new IOException();
                }

                CopyAll(_directoryInfo, ((MonoGameFileHandle)dest)._directoryInfo);
            }
            else
            {
                if(type_28835A82() == FileType.INTERNAL_ )
                {
                    CopyInternalTo(dest);
                }
                else
                {
                    CopyRawFiles(dest);
                }
            }
        }

        private void CopyInternalTo(FileHandle dest)
        {
            using (var inputStream = ((MonoGameFiles)Mdx.files_)._contentManager.OpenStream(path_E605312C()))
            {
                if (dest.exists_FBE0B2A4())
                {
                    if (dest.isDirectory_FBE0B2A4())
                    {
                        using (var outputStream = File.Create(((MonoGameFileHandle)dest.child_1F3F44D2(name_E605312C())).originalPath()))
                        {
                            inputStream.CopyTo(outputStream);
                        }
                    }
                    else
                    {
                        using (var outputStream = ((MonoGameFileHandle)dest)._fileInfo.OpenWrite())
                        {
                            inputStream.CopyTo(outputStream);
                        }
                    }
                }
                else
                {
                    dest.parent_D17169DC().mkdirs_EFE09FC0();

                    using (var outputStream = ((MonoGameFileHandle)dest)._fileInfo.OpenWrite())
                    {
                        inputStream.CopyTo(outputStream);
                    }
                }
            }
        }

        private void CopyRawFiles(FileHandle dest)
        {
            if (dest.exists_FBE0B2A4())
            {
                if (dest.isDirectory_FBE0B2A4())
                {

                    _fileInfo.CopyTo(((MonoGameFileHandle)dest.child_1F3F44D2(name_E605312C())).originalPath(), true);
                }
                else
                {
                    _fileInfo.CopyTo(((MonoGameFileHandle)dest).originalPath(), true);
                }
            }
            else
            {
                dest.parent_D17169DC().mkdirs_EFE09FC0();

                _fileInfo.CopyTo(((MonoGameFileHandle)dest)._fileInfo.ToString(), true);
            }
        }

        public void moveTo_88D76E6E(FileHandle dest)
        {
            if (_fileType == FileType.INTERNAL_ || dest.type_28835A82() == FileType.INTERNAL_)
            {
                IOException exception = new IOException();
                exception._init_("You can't moveTo() an INTERNAL file");
                throw exception;
            }

            if (dest.exists_FBE0B2A4())
            {
                dest.deleteDirectory_FBE0B2A4();
            }

            if (!exists_FBE0B2A4())
            {
                IOException exception = new IOException();
                exception._init_(_originalPath + " does not exist");
                throw exception;
            }

            if (_isDirectory)
            {
                _directoryInfo.MoveTo(((MonoGameFileHandle) dest).originalPath());
            }
            else
            {
                _fileInfo.MoveTo(((MonoGameFileHandle)dest).originalPath());
            }
        }

        public long length_0BE0CBD4()
        {
            return _isDirectory ? 0 : _totalBytes;
        }

        public long lastModified_0BE0CBD4()
        {
            if (_fileType == FileType.INTERNAL_ || !exists_FBE0B2A4())
            {
                return 0;
            }
            if (Mdx.platform_.isConsole_FBE0B2A4())
            {
                return 0;
            }

            return _isDirectory ? DateTimeToTotalMs(_directoryInfo.LastWriteTimeUtc) : DateTimeToTotalMs(_fileInfo.LastWriteTimeUtc);
        }

        public FileHandle[] list_7A4B7BA9(FilenameFilter filter)
        {
            var matchingChilds = new List<FileHandle>();

            var childs = list_685A3945();

            //Not using foreach or LINQ query because they could be as much as 10x slower.
            
            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childs.Length; i++)
            {
                Java.Io.File file = new Java.Io.File();
                file._init_(originalPath());
                if (filter.accept_124B7F0F(file, childs[i].name_E605312C()))
                {
                    matchingChilds.Add(childs[i]);
                }
            }

            return matchingChilds.ToArray();
        }

        public FileHandle[] list_C0AF3A24(FileFilter filter)
        {
            var matchingChilds = new List<FileHandle>();

            var childs = list_685A3945();

            //Not using foreach or LINQ query because they could be as much as 10x slower.
            
            // ReSharper disable once ForCanBeConvertedToForeach
            // ReSharper disable once LoopCanBeConvertedToQuery
            for (var i = 0; i < childs.Length; i++)
            {
                Java.Io.File file = new Java.Io.File();
                file._init_(((MonoGameFileHandle)childs[i]).originalPath());
                if (filter.accept_0B8ED885(file))
                {
                    matchingChilds.Add(childs[i]);
                }
            }

            return matchingChilds.ToArray();
        }

        public void write_E87C2987(InputStream inputStream, bool append)
        {
            var outputStream = write_FF837EC6(append);
            var read = inputStream.read_0EE0D08D();
            while (read != -1)
            {
                outputStream.write_3518BA33(read);
                read = inputStream.read_0EE0D08D();
            }
        }

        private bool IsRoot()
        {
            CreateFileInfoReference();
            if (_isDirectory)
            {
                return _directoryInfo.Parent == null;
            }
            else
            {
                return _originalPath.Equals("/") || _originalPath.EndsWith(":/") || _originalPath.EndsWith(":\\");
            }
        }

        private static long DateTimeToTotalMs(DateTime dateTime)
        {
            return dateTime.Subtract(new DateTime(1970, 1, 1)).Ticks / TimeSpan.TicksPerMillisecond;
        }

        public override string ToString()
        {
            return _originalPath;
        }
    }
}