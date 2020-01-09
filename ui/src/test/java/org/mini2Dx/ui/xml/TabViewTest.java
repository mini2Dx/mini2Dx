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
package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.element.TabView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TabViewTest extends AbstractUiElementXmlTest<TabView> {
    @Test
    public void with_namespace_prefix() {
        String xml =
                "<ui:tab-view xmlns:ui=\"https://github.com/mini2Dx/mini2Dx\" >" +
                        " <ui:tab title=\"1\">" +
                        "   <ui:container/>" +
                        " </ui:tab>" +
                        " <ui:tab title=\"2\">" +
                        "   <ui:container/>" +
                        " </ui:tab>" +
                        "</ui:tab-view>";

        TabView element = loadFile(xml);

        assertEquals(2, element.getTotalTabs());
    }

    @Test
    public void with_layout() {
        String xml = "<tab-view xmlns=\"https://github.com/mini2Dx/mini2Dx\" " +
                "layout=\"blah\" />";

        TabView element = loadFile(xml);

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void with_overflow_clipped() {
        String xml = "<tab-view xmlns=\"https://github.com/mini2Dx/mini2Dx\" " +
                "overflow-clipped=\"true\" />";

        TabView element = loadFile(xml);

        assertTrue(element.isOverflowClipped());
    }

    @Test
    public void with_overflow_clipped_non_boolean() {
        String xml = "<tab-view xmlns=\"https://github.com/mini2Dx/mini2Dx\" " +
                "overflow-clipped=\"blah\" />";

        assertXmlIsInvalid(xml, "'blah' is not a valid value for 'boolean'");
    }

    @Test
    public void with_tab_layout() {
        String xml =
                "    <tab-view>" +
                        "     <tab layout=\"blah\"/>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        assertEquals("blah", element.getTab(0).getFlexLayout());
    }

    @Test
    public void with_tab_default_layout() {
        String xml =
                "    <tab-view>" +
                        "     <tab/>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        assertEquals("flex-column:xs-12c", element.getTab(0).getFlexLayout());
    }

    @Test
    public void with_multiple_tabs() {
        String xml =
                "    <tab-view>" +
                        "     <tab title=\"1\"/>" +
                        "     <tab title=\"2\"/>" +
                        "     <tab title=\"3\"/>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        assertEquals("1", element.getTab(0).getTitle());
        assertEquals("2", element.getTab(1).getTitle());
        assertEquals("3", element.getTab(2).getTitle());
    }

    @Test
    public void with_tab_with_icon_path() {
        String xml =
                "    <tab-view>" +
                        "     <tab icon-path=\"blah\">" +
                        "     </tab>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        Tab tab = element.getCurrentTab();
        assertEquals("blah", tab.getIconPath());
    }

    @Test
    public void with_a_tab_with_child_container() {
        String xml =
                "    <tab-view>" +
                        "     <tab>" +
                        "       <container id=\"blah\" />" +
                        "     </tab>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        Tab tab = element.getCurrentTab();
        assertEquals("blah", tab.getChild(0).getId());
    }

    @Test
    public void with_tab_with_title() {
        String xml =
                "    <tab-view>" +
                        "     <tab title=\"blah\">" +
                        "     </tab>" +
                        "    </tab-view>";

        TabView element = loadFile(xml);

        Tab tab = element.getCurrentTab();
        assertEquals("blah", tab.getTitle());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <tab-view" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            layout=\"blah\"" +
                "            tab-button-layout=\"blah\"" +
                "            overflow-clipped=\"true\"" +
                "    >" +
                "     <tab>" +
                "       <container/>" +
                "     </tab>" +
                "    </tab-view>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "tab-view";
    }

    @Override
    protected void assertDefaultValues(TabView element) {
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
        assertEquals(0, element.getTotalTabs());
    }

}