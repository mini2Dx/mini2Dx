/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.mini2Dx.context.ComponentScanner;
import org.mini2Dx.injection.annotation.Prototype;
import org.mini2Dx.injection.annotation.Singleton;

/**
 * Standard JVM implementation of {@link ComponentScanner}
 * 
 * @author Thomas Cashman
 * @author Jose Noheda
 */
public class DesktopComponentScanner implements ComponentScanner {
	private List<Class<?>> singletonClasses;
	private List<Class<?>> prototypeClasses;
	private ClassLoader classLoader;

	/**
	 * Constructor
	 */
	public DesktopComponentScanner() {
		classLoader = Thread.currentThread().getContextClassLoader();
		singletonClasses = new ArrayList<Class<?>>();
		prototypeClasses = new ArrayList<Class<?>>();
	}

	/**
	 * Scans multiple packages recursively for {@link Singleton} and {@link Prototype}
	 * annotated classes
	 * 
	 * @param packageNames  The package name to scan through, e.g. org.mini2Dx.component
	 * @throws IOException
	 */
	public void scan(String[] packageNames) throws IOException {
		for (String packageName : packageNames) {
			scan(packageName);
		}
	}

	/**
	 * Scans a package recursively for {@link Singleton} and {@link Prototype}
	 * annotated classes
	 * 
	 * @param packageName The package name to scan through, e.g. org.mini2Dx.component
	 * @throws IOException
	 */
	private void scan(String packageName) throws IOException {
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		if (resources == null)
			return;

		while (resources.hasMoreElements()) {
			String filePath = resources.nextElement().getFile();

			/* Workaround for Windows-based OS */
			if (filePath.indexOf("%20") > 0)
				filePath = filePath.replaceAll("%20", " ");

			if (filePath == null)
				continue;

			if (isFilePathForJar(filePath)) {
				filePath = convertFilePathToJarPath(filePath);
				addClassesFromJar(filePath, packageName);
			} else {
				addClassesFromDirectory(new File(filePath), packageName);
			}
		}
	}

	private boolean isFilePathForJar(String filePath) {
		return ((filePath.indexOf("!") > 0) & (filePath.indexOf(".jar") > 0));
	}

	private String convertFilePathToJarPath(String filePath) {
		String jarPath = filePath.substring(0, filePath.indexOf("!"))
				.substring(filePath.indexOf(":") + 1);
		if (jarPath.indexOf(":") >= 0)
			jarPath = jarPath.substring(1);
		return jarPath;
	}

	private void addClassesFromJar(String jarPath, String packageName)
			throws IOException {
		JarInputStream jarFile = new JarInputStream(
				new FileInputStream(jarPath));
		JarEntry jarEntry = jarFile.getNextJarEntry();

		while (jarEntry != null) {
			String className = jarEntry.getName();
			if (className.endsWith(".class")) {
				className = stripFilenameExtension(className);
				if (className.startsWith(packageName)) {
					checkClassForAnnotations(className);
				}
			}
			jarEntry = jarFile.getNextJarEntry();
		}
		jarFile.close();
	}

	private void addClassesFromDirectory(File directory, String packageName) {
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isFile() && file.getPath().endsWith(".class")) {
					String name = packageName + '.'
							+ stripFilenameExtension(file.getName());
					checkClassForAnnotations(name);
				} else if (file.isDirectory()) {
					addClassesFromDirectory(file,
							packageName + "." + file.getName());
				}
			}
		}
	}

	private void checkClassForAnnotations(String className) {
		try {
			className = className.replace('/', '.');
			Class<?> clazz = Class.forName(className);
			Annotation annotation = clazz.getAnnotation(Singleton.class);
			if (annotation != null) {
				singletonClasses.add(clazz);
				return;
			}

			annotation = clazz.getAnnotation(Prototype.class);
			if (annotation != null) {
				prototypeClasses.add(clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String stripFilenameExtension(String className) {
		return className.substring(0, className.indexOf('.'));
	}

	public List<Class<?>> getSingletonClasses() {
		return singletonClasses;
	}

	public List<Class<?>> getPrototypeClasses() {
		return prototypeClasses;
	}
}
