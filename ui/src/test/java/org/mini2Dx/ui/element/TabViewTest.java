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
package org.mini2Dx.ui.element;

import junit.framework.Assert;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.XmlSerializer;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.ui.render.UiContainerRenderTree;

/**
 * Unit and integration tests for {@link TabView}
 */
public class TabViewTest {
	private final Mockery mockery = new Mockery();

	private UiContainerRenderTree uiContainerRenderTree;

	@Before
	public void setUp() {
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.reflect = new JvmReflection();
		Mdx.locks = new JvmLocks();
		Mdx.xml = new XmlSerializer();

		mockery.setImposteriser(ClassImposteriser.INSTANCE);

		uiContainerRenderTree = mockery.mock(UiContainerRenderTree.class);
	}

	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}

	@Test
	public void testSerialization() {
		TabView tabView = new TabView("tabView-3");

		Tab tab1 = new Tab("tab-1");
		tab1.setTitle("Tab 1");
		tab1.add(new Label("label-1"));
		tabView.add(tab1);

		Tab tab2 = new Tab("tab-2");
		tab2.setTitle("Tab 2");
		tab2.setIconPath("textures/icon.png");
		tab2.add(new Label("label-2"));
		tab2.add(new TextButton("textButton-1"));
		tabView.add(tab2);

		try {
			String xml = Mdx.xml.toXml(tabView);
			System.out.println(xml);
			TabView result = Mdx.xml.fromXml(xml, TabView.class);
			Assert.assertEquals(tabView.getId(), result.getId());
			Assert.assertEquals(tabView.getTotalTabs(), result.getTotalTabs());

			for (int i = 0; i < tabView.getTotalTabs(); i++) {
				Tab expectedTab = tabView.getTab(i);
				Tab resultTab = tabView.getTab(i);
				Assert.assertEquals(expectedTab.getId(), resultTab.getId());
				Assert.assertEquals(expectedTab.getTitle(), resultTab.getTitle());
				Assert.assertEquals(expectedTab.getIconPath(), resultTab.getIconPath());
				Assert.assertEquals(expectedTab.children.size, resultTab.children.size);
				for(int j = 0; j < expectedTab.children.size; j++) {
					Assert.assertEquals(expectedTab.children.get(j).getId(), resultTab.children.get(j).getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
