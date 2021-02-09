/*******************************************************************************
 * Copyright 2021 See AUTHORS file
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
package org.mini2Dx.core.di;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

/**
 * A simple {@link ComponentScanner} that can only preset singletons and prototypes
 */
public class BasicComponentScanner implements ComponentScanner {
	protected Array<Class<?>> singletonClasses;
	protected Array<Class<?>> prototypeClasses;

	/**
	 * Constructor
	 */
	public BasicComponentScanner() {
		singletonClasses = new Array<Class<?>>();
		prototypeClasses = new Array<Class<?>>();
	}

	@Override
	public void scan(String[] packageNames) throws MdxException, IOException {
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

	@Override
	public Array<Class<?>> getSingletonClasses() {
		return singletonClasses;
	}

	@Override
	public Array<Class<?>> getPrototypeClasses() {
		return prototypeClasses;
	}
}
