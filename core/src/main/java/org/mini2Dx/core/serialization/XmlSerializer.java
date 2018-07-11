/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of mini2Dx nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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