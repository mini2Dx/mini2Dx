/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.desktop.playerdata;

import java.io.StringWriter;
import java.nio.file.Paths;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.playerdata.PlayerData;
import org.mini2Dx.core.serialization.JsonSerializer;
import org.mini2Dx.core.util.OsDetector;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Desktop implementation of {@link PlayerData}
 */
public class DesktopPlayerData implements PlayerData {
    private final String saveDirectory;
    private final Serializer xmlSerializer;
    private final JsonSerializer jsonSerializer;

    public DesktopPlayerData(String gameIdentifier) {
        saveDirectory = getSaveDirectoryForGame(gameIdentifier);

        xmlSerializer = new Persister();
        jsonSerializer = Mdx.json;
    }

    private String getSaveDirectoryForGame(String gameIdentifier) {
        switch (OsDetector.getOs()) {
        case WINDOWS:
            return Paths.get(Gdx.files.getExternalStoragePath(), "AppData", "Roaming", gameIdentifier).toAbsolutePath().toString();
        case MAC:
            return Paths.get(Gdx.files.getExternalStoragePath(), "Library", "Application Support", gameIdentifier).toAbsolutePath()
                    .toString();
        case UNIX:
            if (gameIdentifier.contains(".")) {
                gameIdentifier = gameIdentifier.substring(gameIdentifier.indexOf('.') + 1);
            }
            return Paths.get(Gdx.files.getExternalStoragePath(), "." + gameIdentifier).toAbsolutePath().toString();
        default:
            return Paths.get(Gdx.files.getLocalStoragePath()).toAbsolutePath().toString();
        }
    }

    @Override
    public <T> T readXml(Class<T> clazz, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        return xmlSerializer.read(clazz, resolve(filepath).read());
    }

    @Override
    public <T> void writeXml(T object, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        ensureDirectoryExistsForFile(filepath);
        StringWriter writer = new StringWriter();
        xmlSerializer.write(object, writer);
        resolve(filepath).writeString(writer.toString(), false);
        writer.flush();
    }

    @Override
    public <T> T readJson(Class<T> clazz, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        return jsonSerializer.fromJson(resolve(filepath).readString(), clazz);
    }

    @Override
    public <T> void writeJson(T object, String... filepath) throws Exception {
        if (filepath.length == 0) {
            throw new Exception("No file path specified");
        }
        resolve(filepath).writeString(jsonSerializer.toJson(object), false);
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
        FileHandle directory = Gdx.files.absolute(saveDirectory);
        if(!directory.exists()) {
            return;
        }
        directory.emptyDirectory();
        directory.deleteDirectory();
    }
    
    private void ensureDataDirectoryExists() {
        FileHandle directory = Gdx.files.absolute(saveDirectory);
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
        return Gdx.files.absolute(Paths.get(saveDirectory, filepath).toString());
    }

    public String getSaveDirectory() {
        return saveDirectory;
    }
}