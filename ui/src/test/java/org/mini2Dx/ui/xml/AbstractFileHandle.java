package org.mini2Dx.ui.xml;

import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.files.FileType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public abstract class AbstractFileHandle implements FileHandle {
    @Override
    public String path() {
        return null;
    }

    @Override
    public String normalize() {
        return null;
    }

    @Override
    public FileHandle normalizedHandle() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String extension() {
        return null;
    }

    @Override
    public String nameWithoutExtension() {
        return null;
    }

    @Override
    public String pathWithoutExtension() {
        return null;
    }

    @Override
    public FileType type() {
        return null;
    }

    @Override
    public BufferedInputStream read(int bufferSize) throws IOException {
        return new BufferedInputStream(read(), bufferSize);
    }

    @Override
    public Reader reader() throws IOException {
        return new InputStreamReader(read(1024));
    }

    @Override
    public Reader reader(String charset) throws IOException {
        return reader();
    }

    @Override
    public BufferedReader reader(int bufferSize) throws IOException {
        return new BufferedReader(reader(), bufferSize);
    }

    @Override
    public BufferedReader reader(int bufferSize, String charset) throws IOException {
        return reader(bufferSize);
    }

    @Override
    public String readString() throws IOException {
        return null;
    }

    @Override
    public String readString(String charset) throws IOException {
        return null;
    }

    @Override
    public String[] readAllLines() throws IOException {
        return new String[0];
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public int readBytes(byte[] bytes, int offset, int size) throws IOException {
        return 0;
    }

    @Override
    public OutputStream write(boolean append) throws IOException {
        return null;
    }

    @Override
    public OutputStream write(boolean append, int bufferSize) throws IOException {
        return null;
    }

    @Override
    public void write(InputStream input, boolean append) throws IOException {

    }

    @Override
    public Writer writer(boolean append) throws IOException {
        return null;
    }

    @Override
    public Writer writer(boolean append, String charset) throws IOException {
        return null;
    }

    @Override
    public void writeString(String string, boolean append) throws IOException {

    }

    @Override
    public void writeString(String string, boolean append, String charset) throws IOException {

    }

    @Override
    public void writeBytes(byte[] bytes, boolean append) throws IOException {

    }

    @Override
    public void writeBytes(byte[] bytes, int offset, int length, boolean append) throws IOException {

    }

    @Override
    public FileHandle[] list() throws IOException {
        return new FileHandle[0];
    }

    @Override
    public FileHandle[] list(FileFilter filter) throws IOException {
        return new FileHandle[0];
    }

    @Override
    public FileHandle[] list(FilenameFilter filter) throws IOException {
        return new FileHandle[0];
    }

    @Override
    public FileHandle[] list(String suffix) throws IOException {
        return new FileHandle[0];
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public FileHandle child(String name) {
        return null;
    }

    @Override
    public FileHandle sibling(String name) {
        return null;
    }

    @Override
    public FileHandle parent() {
        return null;
    }

    @Override
    public void mkdirs() throws IOException {

    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean delete() throws IOException {
        return false;
    }

    @Override
    public boolean deleteDirectory() throws IOException {
        return false;
    }

    @Override
    public void emptyDirectory() throws IOException {

    }

    @Override
    public void emptyDirectory(boolean preserveTree) throws IOException {

    }

    @Override
    public void copyTo(FileHandle dest) throws IOException {

    }

    @Override
    public void moveTo(FileHandle dest) throws IOException {

    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public long lastModified() {
        return 0;
    }
}
