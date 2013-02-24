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
package org.mini2Dx.injection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.injection.dummy.TestBean;
import org.mini2Dx.injection.dummy.TestDependency;
import org.mini2Dx.injection.dummy.TestInterfaceImpl;
import org.mini2Dx.injection.exceptions.BeanException;

/**
 * Unit tests for {@link SingletonBeanRepository}
 *
 * @author Thomas Cashman
 */
public class SingletonBeanRepositoryTest {
	private TestBean bean;
	private TestDependency dependency;
	private TestInterfaceImpl interfaceImpl;
	private Integer intValue;
	
	@Before
	public void setup() throws BeanException {
		SingletonBeanRepository.open();
		SingletonBeanRepository.clear();
		SingletonBeanRepository.close();
		
		bean = new TestBean();
		dependency = new TestDependency();
		interfaceImpl = new TestInterfaceImpl();
		intValue = new Integer(23);
	}

	@Test
	public void testCloseWhenClosed() {
		try {
			SingletonBeanRepository.close();
			Assert.fail("Closed repository when it was already closed.");
		} catch (Exception e) {
			
		}
	}

	@Test
	public void testRegisterBeanClassOfTClosed() {
		try {
			SingletonBeanRepository.registerBean(TestDependency.class);
			Assert.fail("Added bean when repository was closed");
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testRegisterBeanClassOfTOpen() throws Exception {
		SingletonBeanRepository.open();
		SingletonBeanRepository.registerBean(new Integer(0));
		SingletonBeanRepository.registerBean(TestDependency.class);
		SingletonBeanRepository.registerBean(TestBean.class);
		SingletonBeanRepository.close();
		
		bean = SingletonBeanRepository.getBean(TestBean.class);
		dependency = SingletonBeanRepository.getBean(TestDependency.class);
		Assert.assertEquals(dependency.hashCode(), bean.getDependency().hashCode());
		Assert.assertEquals(dependency.getValue(), bean.getDependency().getValue());
	}

	@Test
	public void testRegisterBeanObjectClosed() {
		try {
			SingletonBeanRepository.registerBean(bean);
			Assert.fail("Added bean when repsoitory was closed");
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testRegisterBeanObjectOpen() throws Exception {
		SingletonBeanRepository.open();
		SingletonBeanRepository.registerBean(bean);
		SingletonBeanRepository.registerBean(dependency);
		SingletonBeanRepository.registerBean(intValue);
		SingletonBeanRepository.registerBean(interfaceImpl);
		SingletonBeanRepository.close();
		
		TestBean beanFromRepo = SingletonBeanRepository.getBean(TestBean.class);
		TestDependency dependencyFromRepo = SingletonBeanRepository.getBean(TestDependency.class);
		Integer intFromRepo = SingletonBeanRepository.getBean(Integer.class);
		
		Assert.assertEquals(bean, beanFromRepo);
		Assert.assertEquals(dependency, dependencyFromRepo);
		Assert.assertEquals(intValue, intFromRepo);
		
		Assert.assertEquals(intValue.intValue(), bean.getIntField().intValue());
		Assert.assertEquals(dependency.getValue(), bean.getDependency().getValue());
		Assert.assertEquals(interfaceImpl, bean.getInterfaceField());
		Assert.assertEquals(interfaceImpl.getValue(), bean.getInterfaceField().getValue());
	}
	
	@Test
	public void testNoMatchingBean() {
		try {
			SingletonBeanRepository.open();
			SingletonBeanRepository.registerBean(bean);
			SingletonBeanRepository.registerBean(intValue);
			SingletonBeanRepository.close();
			Assert.fail("Successfully closed repository without matching bean.");
		} catch (BeanException e) {
			
		}
	}
	
	@Test
	public void testNoSuitableConstructorError() {
		try {
			SingletonBeanRepository.open();
			SingletonBeanRepository.registerBean(Integer.class);
			Assert.fail("Successfully registered bean with no suitable constructor");
		} catch (BeanException e) {
			
		}
	}
}
