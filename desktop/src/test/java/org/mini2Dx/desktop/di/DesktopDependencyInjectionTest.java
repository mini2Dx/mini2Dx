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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.di.dummy.TestBeanA;
import org.mini2Dx.core.di.dummy.TestBeanB;
import org.mini2Dx.core.di.dummy.TestDependency;
import org.mini2Dx.core.di.dummy.TestInterfaceImpl;
import org.mini2Dx.core.di.dummy.TestManualBean;
import org.mini2Dx.core.di.dummy.TestPostInjectBean;
import org.mini2Dx.core.di.dummy.TestPrototypeBean;

/**
 * Integration test for {@link GameContext} and {@link DesktopComponentScanner}
 */
public class DesktopDependencyInjectionTest {

	@Before
	public void setUp() {
		Mdx.di = new DesktopDependencyInjection();
	}
	
	private void scanDependencies() throws Exception {
		Mdx.di.scan(new String[] { "org.mini2Dx.core.di.dummy" });
	}
	
	@Test
	public void testSingletonGeneration() throws Exception {
		scanDependencies();
		TestInterfaceImpl testInterfaceImpl1 = Mdx.di.getBean(TestInterfaceImpl.class);
		TestInterfaceImpl testInterfaceImpl2 = Mdx.di.getBean(TestInterfaceImpl.class);

		Assert.assertEquals(false, testInterfaceImpl1.getValue() == testInterfaceImpl2.getValue());
	}

	@Test
	public void testPrototypeGeneration() throws Exception {
		scanDependencies();
		TestPrototypeBean testBean1 = Mdx.di.getBean(TestPrototypeBean.class);
		TestPrototypeBean testBean2 = Mdx.di.getBean(TestPrototypeBean.class);

		Assert.assertEquals(false, testBean1.equals(testBean2));
		Assert.assertEquals(true, testBean1.getDependency().equals(testBean2.getDependency()));

		Assert.assertEquals(true, testBean1.getInterfaceField() instanceof TestInterfaceImpl);
	}
	
	@Test
	public void testPrototypeInjection() throws Exception {
		scanDependencies();
		TestBeanA testBeanA = Mdx.di.getBean(TestBeanA.class);
		TestBeanB testBeanB = Mdx.di.getBean(TestBeanB.class);
		
		testBeanA.getPrototypeBean().setIntField(10);
		testBeanB.getPrototypeBean().setIntField(11);
		
		Assert.assertEquals(true, testBeanA.getPrototypeBean().getIntField() != testBeanB.getPrototypeBean().getIntField());
	}
	
	@Test
	public void testPresetSingleton() throws Exception {
		Mdx.di.presetSingleton(TestManualBean.class);
		scanDependencies();
		TestManualBean result1 = Mdx.di.getBean(TestManualBean.class);
		result1.setValue(77);
		TestManualBean result2 = Mdx.di.getBean(TestManualBean.class);
		result2.setValue(79);
		Assert.assertEquals(79, result1.getValue());
		Assert.assertEquals(79, result2.getValue());
	}
	
	@Test
	public void testPresetPrototype() throws Exception {
		Mdx.di.presetPrototype(TestManualBean.class);
		scanDependencies();
		TestManualBean result1 = Mdx.di.getBean(TestManualBean.class);
		result1.setValue(77);
		TestManualBean result2 = Mdx.di.getBean(TestManualBean.class);
		result2.setValue(78);
		
		Assert.assertEquals(77, result1.getValue());
		Assert.assertEquals(78, result2.getValue());
	}
	
	@Test
	public void testPostInject() throws Exception {
		scanDependencies();
		
		TestPostInjectBean result = Mdx.di.getBean(TestPostInjectBean.class);
		Assert.assertEquals(101, result.getValue());
	}
	
	@Test
	public void testParentInjection() throws Exception {
		scanDependencies();
		
		TestDependency testDependency = Mdx.di.getBean(TestDependency.class);
		Assert.assertNotNull(testDependency.getParentDependency());
	}
}
