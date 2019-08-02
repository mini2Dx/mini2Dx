/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.android.di;

import android.content.Context;
import android.util.Log;
import org.mini2Dx.gdx.utils.Array;
import dalvik.system.DexFile;
import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.DependencyInjection;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Enumeration;

/**
 * Android implementation of {@link ComponentScanner}
 */
public class AndroidComponentScanner implements ComponentScanner {
	private static final String TAG = AndroidComponentScanner.class
			.getSimpleName();

	private Context applicationContext;
	private Array<Class<?>> singletonClasses;
	private Array<Class<?>> prototypeClasses;

	public AndroidComponentScanner(Context applicationContext) {
		this.applicationContext = applicationContext;
		
		singletonClasses = new Array<Class<?>>();
		prototypeClasses = new Array<Class<?>>();
	}

	@Override
	public void scan(String[] packageNames) throws IOException {
		if (applicationContext == null) {
			throw new NullPointerException(
					AndroidComponentScanner.class.getSimpleName()
							+ ".APPLICATION_CONTEXT needs to be set before initialising "
							+ DependencyInjection.class.getSimpleName());
		}

		DexFile dex = new DexFile(
				applicationContext.getApplicationInfo().sourceDir);
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		Enumeration<String> entries = dex.entries();
		while (entries.hasMoreElements()) {
			String className = entries.nextElement();
			if (classNameContainsPackage(packageNames, className)) {
				try {
					checkClassForAnnotations(classLoader.loadClass(className));
				} catch (Exception e) {
					Log.wtf(TAG, e);
				}
			}
		}
	}

	private void checkClassForAnnotations(Class<?> clazz) {
		try {
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

	private boolean classNameContainsPackage(String[] packageNames,
			String className) {
		for (String packageName : packageNames) {
			if (className.toLowerCase().startsWith(packageName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Array<Class<?>> getSingletonClasses() {
		return singletonClasses;
	}

	@Override
	public Array<Class<?>> getPrototypeClasses() {
		return prototypeClasses;
	}

}
