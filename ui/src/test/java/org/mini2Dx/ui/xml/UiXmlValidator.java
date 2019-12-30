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

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UiXmlValidator {
    private final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private final Schema schema;

    public UiXmlValidator() {
        try {
            schema = factory.newSchema(classLoader().getResource("mini2dx-ui.xsd"));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public Exception invalidXml(String xml) {
        try (InputStream input = new ByteArrayInputStream(xml.getBytes(UTF_8))) {
            schema.newValidator().validate(new StreamSource(input));
            throw new AssertionError("XML is valid...this was not expected");
        } catch (Exception e) {
            return e;
        }
    }

    @Deprecated
    public void validateXml(String filePath) {
        try (InputStream input = classLoader().getResourceAsStream(filePath)) {
            schema.newValidator().validate(new StreamSource(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validateXmlContents(String xml) {
        try (InputStream input = new ByteArrayInputStream(xml.getBytes(UTF_8))) {
            schema.newValidator().validate(new StreamSource(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
