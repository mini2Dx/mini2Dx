package org.mini2Dx.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.context.GameContext;
import org.mini2Dx.injection.dummy.TestBean;
import org.mini2Dx.injection.dummy.TestInterfaceImpl;

public class GameContextTest {
	@Test
	public void testGameContext() throws Exception {
		GameContext.initialise(new String[] { "org.mini2Dx.injection.dummy" });

		TestBean testBean1 = GameContext.getBean(TestBean.class);
		TestBean testBean2 = GameContext.getBean(TestBean.class);

		Assert.assertEquals(false, testBean1.equals(testBean2));
		Assert.assertEquals(true,
				testBean1.getDependency().equals(testBean2.getDependency()));

		Assert.assertEquals(true,
				testBean1.getInterfaceField() instanceof TestInterfaceImpl);
//		Assert.assertEquals(
//				false,
//				testBean1.getInterfaceField().equals(
//						testBean2.getInterfaceField()));
		
		TestInterfaceImpl testInterfaceImpl1 = GameContext.getBean(TestInterfaceImpl.class);
		TestInterfaceImpl testInterfaceImpl2 = GameContext.getBean(TestInterfaceImpl.class);
		
		Assert.assertEquals(false, testInterfaceImpl1.getValue() == testInterfaceImpl2.getValue());
	}
}
