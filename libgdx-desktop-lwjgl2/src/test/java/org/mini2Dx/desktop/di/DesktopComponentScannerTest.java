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
package org.mini2Dx.desktop.di;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.desktop.DesktopComponentScanner;
import org.mini2Dx.libgdx.game.GameWrapper;
import org.mini2Dx.core.di.dummy.*;

/**
 * Unit tests for {@link ComponentScanner}
 */
public class DesktopComponentScannerTest {
	private ComponentScanner componentScanner;

	@Before
	public void setup() {
		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
		Mdx.reflect = new JvmReflection();
		Mdx.platform = GameWrapper.getPlatform();
		componentScanner = new DesktopComponentScanner();
	}

	@Test
	public void testScanPackage() throws Exception {
		componentScanner.scan(new String[] { "org.mini2Dx.core.di.dummy" });

		Assert.assertEquals(true, componentScanner.getPrototypeClasses()
				.contains(TestPrototypeBean.class, false));
		Assert.assertEquals(true, componentScanner.getSingletonClasses()
				.contains(TestDependency.class, false));
	}

}
