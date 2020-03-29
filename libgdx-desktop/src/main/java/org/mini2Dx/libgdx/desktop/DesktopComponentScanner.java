/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
package org.mini2Dx.libgdx.desktop;

import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.gdx.utils.Array;
import org.reflections.Reflections;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;
import java.util.Set;

public class DesktopComponentScanner implements ComponentScanner {
	private Array<Class<?>> singletonClasses;
	private Array<Class<?>> prototypeClasses;

	/**
	 * Constructor
	 */
	public DesktopComponentScanner() {
		singletonClasses = new Array<Class<?>>();
		prototypeClasses = new Array<Class<?>>();
	}

	/**
	 * Scans multiple packages recursively for {@link Singleton} and
	 * {@link Prototype} annotated classes
	 *
	 * @param packageNames
	 *            The package name to scan through, e.g. org.mini2Dx.component
	 */
	public void scan(String[] packageNames) {
		for (String packageName : packageNames) {
			scan(packageName);
		}
	}

	@Override
	public void saveTo(Writer writer) {
		final PrintWriter printWriter = new PrintWriter(writer);

		printWriter.println("--- Singletons ---");
		for(int i = 0; i < singletonClasses.size; i++) {
			printWriter.println(singletonClasses.get(i).getName());
		}
		printWriter.println("--- Prototypes ---");
		for(int i = 0; i < prototypeClasses.size; i++) {
			printWriter.println(prototypeClasses.get(i).getName());
		}

		printWriter.flush();
		printWriter.close();
	}

	@Override
	public void restoreFrom(Reader reader) throws ClassNotFoundException {
		final Scanner scanner = new Scanner(reader);
		boolean singletons = true;

		scanner.nextLine();
		while (scanner.hasNext()) {
			final String line = scanner.nextLine();
			if(line.startsWith("---")) {
				singletons = false;
			} else if(singletons) {
				singletonClasses.add(Class.forName(line));
			} else {
				prototypeClasses.add(Class.forName(line));
			}
		}
		scanner.close();
	}

	/**
	 * Scans a package recursively for {@link Singleton} and {@link Prototype}
	 * annotated classes
	 *
	 * @param packageName
	 *            The package name to scan through, e.g. org.mini2Dx.component
	 */
	private void scan(String packageName) {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> singletons = reflections
				.getTypesAnnotatedWith(Singleton.class);
		for(Class<?> clazz : singletons) {
			singletonClasses.add(clazz);
		}

		Set<Class<?>> prototypes = reflections
				.getTypesAnnotatedWith(Prototype.class);
		for(Class<?> clazz : prototypes) {
			prototypeClasses.addAll(clazz);
		}
	}

	public Array<Class<?>> getSingletonClasses() {
		return singletonClasses;
	}

	public Array<Class<?>> getPrototypeClasses() {
		return prototypeClasses;
	}
}
