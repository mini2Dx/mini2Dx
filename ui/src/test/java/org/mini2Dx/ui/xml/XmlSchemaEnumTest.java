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

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import static javax.xml.xpath.XPathConstants.NODESET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlSchemaEnumTest {
    private Document xmlDocument;
    private XPath xPath;

    @Before
    public void setUp() throws Exception {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("mini2dx-ui.xsd");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        xmlDocument = builder.parse(input);
        xPath = XPathFactory.newInstance().newXPath();
    }

    @Test
    public void visibility() {
        assertEnumValues("visibility", Visibility.values());
    }

    @Test
    public void horizontal_alignment() {
        assertEnumValues("horizontalAlignment", HorizontalAlignment.values());
    }

    @Test
    public void flex_direction() {
        assertEnumValues("flexDirection", FlexDirection.values());
    }

    private void assertEnumValues(String xsdName, Enum... values) {
        Collection<String> valuesDefinedInSchema = findEnumerationValuesFor(xsdName);
        assertFalse("No values found in schema", valuesDefinedInSchema.isEmpty());

        for (Enum enumValue : values) {
            assertTrue("Value (" + enumValue.name() + ") is NOT defined in the schema",
                    valuesDefinedInSchema.remove(enumValue.name()));
        }

        assertEquals("There are extra values defined in the schema: " + valuesDefinedInSchema,
                0, valuesDefinedInSchema.size());
    }

    private Collection<String> findEnumerationValuesFor(String name) {
        try {
            String query = "//*[@name='" + name + "']//*[@value]";

            NodeList list = (NodeList) xPath.compile(query).evaluate(xmlDocument, NODESET);

            HashSet<String> values = new HashSet<>();
            for (int i = 0; i < list.getLength(); i++) {
                Node item = list.item(i);
                values.add(item.getAttributes().getNamedItem("value").getTextContent());
            }
            return values;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
