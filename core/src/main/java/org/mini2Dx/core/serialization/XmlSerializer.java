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
package org.mini2Dx.core.serialization;

import org.mini2Dx.core.exception.SerializationException;

import java.io.Reader;
import java.io.Writer;

/**
 * Common interface to XML serialization implementations. All implementations
 * serialize data based on {@link org.mini2Dx.core.serialization.annotation.Field} annotations
 */
public interface XmlSerializer {
    /**
     * Reads a XML document and converts it into an object of the specified type
     *
     * @param xml The XML document
     * @param <T> The type of {@link Class} to return
     * @param clazz The {@link Class} to convert the document to
     * @return The object deserialized from XML
     * @throws SerializationException Thrown when the data is invalid
     */
    public <T> T fromXml(String xml, Class<T> clazz)
            throws SerializationException;

    /**
     * Reads an XML document from a {@link Reader} and converts it into an object of the specified type
     *
     * @param xmlReader The input stream reading the XML document
     * @param clazz The {@link Class} to convert the document to
     * @param <T> The type of {@link Class} to return
     * @return The object deserialized from XML
     * @throws SerializationException Thrown when the data is invalid
     */
    public <T> T fromXml(Reader xmlReader, Class<T> clazz)
            throws SerializationException;

    /**
     * Writes a XML document by searching the object for {@link org.mini2Dx.core.serialization.annotation.Field} annotations
     * @param object The object to convert to XML
     * @param <T> The type of {@link Class} to write
     * @return The object serialized as XML
     * @throws SerializationException Thrown when the object is invalid
     */
    public <T> String toXml(T object) throws SerializationException;

    /**
     * Writes a XML document to a {@link Writer} by searching the object for {@link org.mini2Dx.core.serialization.annotation.Field} annotations
     * @param object The object to convert to XML
     * @param <T> The type of {@link Class} to write
     * @param writer The stream to write the XML document to
     * @throws SerializationException Thrown when the object is invalid
     */
    public <T> void toXml(T object, Writer writer) throws SerializationException;
}