/**
 * Copyright (c) 2014, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.desktop.data;

import java.nio.file.Paths;

import org.mini2Dx.core.M2Dx;
import org.mini2Dx.core.data.Data;
import org.mini2Dx.desktop.OsDetector;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Desktop implementation of {@link Data}
 * 
 * @author Thomas Cashman
 */
public class DesktopData implements Data {
    private final String saveDirectory;
    private final Serializer serializer;
    private final Gson gson;

    public DesktopData(String gameIdentifier) {
        saveDirectory = getSaveDirectoryForGame(gameIdentifier);

        serializer = new Persister();
        gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).serializeNulls().create();
    }

    private String getSaveDirectoryForGame(String gameIdentifier) {
        switch (OsDetector.getOs()) {
        case WINDOWS:
            return Paths.get(M2Dx.files.getExternalStoragePath(), "AppData", "Roaming", gameIdentifier).toAbsolutePath().toString();
        case MAC:
            return Paths.get(M2Dx.files.getExternalStoragePath(), "Library", "Application Support", gameIdentifier).toAbsolutePath()
                    .toString();
        case UNIX:
            if (gameIdentifier.contains(".")) {
                gameIdentifier = gameIdentifier.substring(gameIdentifier.indexOf('.') + 1);
            }
            return Paths.get(M2Dx.files.getExternalStoragePath(), "." + gameIdentifier).toAbsolutePath().toString();
        default:
            return Paths.get(M2Dx.files.getLocalStoragePath()).toAbsolutePath().toString();
        }
    }

    @Override
    public <T> T readXml(Class<T> clazz, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        return serializer.read(clazz, resolve(filepath).read());
    }

    @Override
    public <T> void writeXml(T object, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        ensureDirectoryExistsForFile(filepath);
        serializer.write(object, resolve(filepath).write(false));
    }

    @Override
    public <T> T readJson(Class<T> clazz, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        return gson.fromJson(resolve(filepath).readString(), clazz);
    }

    @Override
    public <T> void writeJson(T object, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        resolve(filepath).writeString(gson.toJson(object), false);
    }

    @Override
    public boolean hasFile(String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        FileHandle file = resolve(filepath);
        if (file.exists()) {
            return !file.isDirectory();
        }
        return false;
    }

    @Override
    public boolean hasDirectory(String... path) throws Exception {
        if (path.length == 0) {
            throw new Exception("No path specified");
        }
        FileHandle directoryHandle = resolve(path);
        if (directoryHandle.exists()) {
            return directoryHandle.isDirectory();
        }
        return false;
    }
    
    @Override
    public void createDirectory(String... path) throws Exception {
        if (path.length == 0) {
            throw new Exception("No path specified");
        }
        FileHandle directory = resolve(path);
        if (directory.exists()) {
            return;
        }
        ensureDataDirectoryExists();
        directory.mkdirs();
    }
    
    @Override
    public void wipe() throws Exception {
        FileHandle directory = M2Dx.files.external(saveDirectory);
        if(!directory.exists()) {
            return;
        }
        directory.emptyDirectory();
        directory.deleteDirectory();
    }
    
    private void ensureDataDirectoryExists() {
        FileHandle directory = M2Dx.files.external(saveDirectory);
        if(directory.exists()) {
            return;
        }
        directory.mkdirs();
    }
    
    private void ensureDirectoryExistsForFile(String... filepath) {
        ensureDataDirectoryExists();
        
        FileHandle file = resolve(filepath);
        if(file.exists()) {
            return;
        }
        FileHandle parent = file.parent();
        if(parent.exists()) {
            return;
        }
        parent.mkdirs();
    }

    private FileHandle resolve(String[] filepath) {
        return M2Dx.files.external(Paths.get(saveDirectory, filepath).toString());
    }

    public String getSaveDirectory() {
        return saveDirectory;
    }
}