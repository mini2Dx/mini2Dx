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
package org.mini2Dx.ios.serialization;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.mini2Dx.core.serialization.RequiredFieldException;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.core.serialization.XmlSerializer;

import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * iOS implementation of {@link XmlSerializer}
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IOSXmlSerializer implements XmlSerializer {
	@Override
	public <T> T fromXml(String xml, Class<T> clazz)
			throws SerializationException {
		return fromXml(new StringReader(xml), clazz);
	}
	

	@Override
	public <T> T fromXml(Reader xmlReader, Class<T> clazz)
			throws SerializationException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		T result = null;
		try {
			XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(xmlReader);
			xmlStreamReader.next();
			xmlStreamReader.next();
			result = deserializeObject(xmlStreamReader, "data", clazz);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			try {
				xmlReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public <T> String toXml(T object) throws SerializationException {
		StringWriter writer = new StringWriter();
		toXml(object, writer);
		return writer.toString();
	}
	
	@Override
	public <T> void toXml(T object, Writer writer)
			throws SerializationException {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(writer);
			xmlWriter.writeStartDocument();
			writeObject(object, "data", xmlWriter);
			xmlWriter.writeEndDocument();
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	private <T> void writeObject(T object, String tagName,
			XMLStreamWriter xmlWriter)
			throws SerializationException {
		try {
			if(object == null) {
				writePrimitive(tagName, "", xmlWriter);
				return;
			}

			Class<?> clazz = object.getClass();
			
			if (isPrimitive(clazz) || clazz.equals(String.class)) {
				writePrimitive(tagName, object, xmlWriter);
				return;
			}
			if (clazz.isArray()) {
				writeArray(tagName, object, xmlWriter);
				return;
			}
			if (Collection.class.isAssignableFrom(clazz)) {
				Collection collection = (Collection) object;
				writeArray(tagName, collection.toArray(), xmlWriter);
				return;
			}
			if (Map.class.isAssignableFrom(clazz)) {
				writeMap(tagName, (Map) object, xmlWriter);
				return;
			}

			if (tagName != null) {
				xmlWriter.writeStartElement(tagName);
			}
			
			Class<?> currentClass = clazz;
			while(currentClass != null && !currentClass.equals(Object.class)) {
				for (Field field : ClassReflection.getDeclaredFields(currentClass)) {
					field.setAccessible(true);
					Annotation annotation = field
							.getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					if (annotation == null) {
						continue;
					}
					org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = annotation
							.getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					if (!fieldAnnotation.optional() && field.get(object) == null) {
						throw new RequiredFieldException(currentClass, field.getName());
					}
					writeObject(field.get(object), field.getName(), xmlWriter);
				}
				currentClass = currentClass.getSuperclass();
			}
			
			if (tagName != null) {
				xmlWriter.writeEndElement();
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writeMap(String tagName, Map map, XMLStreamWriter xmlWriter) throws SerializationException {
		try {
			if (tagName != null) {
				xmlWriter.writeStartElement(tagName);
			}
			for (Object key : map.keySet()) {
				xmlWriter.writeStartElement("entry");
				writeObject(key, "key", xmlWriter);
				writeObject(map.get(key), "value", xmlWriter);
				xmlWriter.writeEndElement();
			}
			if (tagName != null) {
				xmlWriter.writeEndElement();
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writeArray(String tagName, T array, XMLStreamWriter xmlWriter) throws SerializationException {
		try {
			if (tagName != null) {
				xmlWriter.writeStartElement(tagName);
			}

			int arrayLength = Array.getLength(array);
			for (int i = 0; i < arrayLength; i++) {
				writeObject(Array.get(array, i), "value", xmlWriter);
			}
			
			if (tagName != null) {
				xmlWriter.writeEndElement();
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writePrimitive(String tagName, T object, XMLStreamWriter xmlWriter) throws SerializationException {
		try {
			if (tagName != null) {
				xmlWriter.writeStartElement(tagName);
			}
			xmlWriter.writeCharacters(String.valueOf(object));
			if (tagName != null) {
				xmlWriter.writeEndElement();
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}

	private <T> T deserializeObject(XMLStreamReader xmlReader, String xmlTag, Class<T> clazz)
			throws SerializationException {
		try {
			if (isPrimitive(clazz) || clazz.equals(String.class)) {
				return parsePrimitive(xmlReader.getText(), clazz);
			}
			if (clazz.isEnum()) {
				return (T) Enum.valueOf((Class<? extends Enum>) clazz,
						xmlReader.getText());
			}
			
			T result = ClassReflection.newInstance(clazz);
			int parserEventType = xmlReader.getEventType();
			boolean finished = false;

			while (!finished) {
				switch (parserEventType) {
				case XMLStreamConstants.START_ELEMENT:
					String currentFieldName = xmlReader.getLocalName();
					Field currentField = findField(clazz, currentFieldName);
					currentField.setAccessible(true);
					
					Class<?> fieldClass = currentField.getType();
					xmlReader.next();
					if (fieldClass.isArray()) {
						setArrayField(xmlReader, currentField, fieldClass,
								result);
						break;
					}
					if (!fieldClass.isPrimitive()) {
						if (fieldClass.equals(String.class)) {
							setPrimitiveField(currentField, fieldClass, result,
									xmlReader.getText());
						} else if (Map.class.isAssignableFrom(fieldClass)) {
							setMapField(xmlReader, currentField, fieldClass,
									result);
						} else if (Collection.class.isAssignableFrom(fieldClass)) {
							setCollectionField(xmlReader, currentField,
									fieldClass, result);
						} else {
							currentField.set(result, deserializeObject(xmlReader, currentFieldName, fieldClass));
						}
						break;
					}
					
					setPrimitiveField(currentField, fieldClass, result,
							xmlReader.getText());
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(xmlReader.getLocalName().equals(xmlTag)) {
						finished = true;
					}
					break;
				case XMLStreamConstants.END_DOCUMENT:
					finished = true;
					break;
				default:
					break;
				}
				if(!finished) {
					parserEventType = xmlReader.next();
				}
			}
			return result;
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerializationException(e);
		}
	}
	
	private Field findField(Class<?> clazz, String fieldName) throws ReflectionException {
		Class<?> currentClass = clazz;
		while(currentClass != null && !currentClass.equals(Object.class)) {
			try {
				Field result = ClassReflection.getDeclaredField(
						currentClass, fieldName);
				if(result == null) {
					continue;
				}
				return result;
			} catch (ReflectionException e) {}
			currentClass = currentClass.getSuperclass();
		}
		throw new ReflectionException("No field '" + fieldName + "' found in class " + clazz.getName());
	}

	private <T> void setCollectionField(XMLStreamReader xmlReader, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Collection collection = (Collection) (fieldClass.isInterface() ? new ArrayList()
					: ClassReflection.newInstance(fieldClass));
			Class<?> valueClass = field.getElementType(0);
			
			boolean finished = false;
			while (!finished) {
				switch(xmlReader.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					xmlReader.next();
					collection.add(deserializeObject(xmlReader, "value", valueClass));
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(!xmlReader.getLocalName().equals("value")) {
						finished = true;
					} else {
						xmlReader.next();
					}
					break;
				default:
					xmlReader.next();
					break;
				}
			}

			field.set(object, collection);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setMapField(XMLStreamReader xmlReader, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Map map = (Map) (fieldClass.isInterface() ? new HashMap()
					: ClassReflection.newInstance(fieldClass));
			Class<?> keyClass = field.getElementType(0);
			Class<?> valueClass = field.getElementType(1);

			boolean finished = false;
			Object key = null;
			Object value = null;
			while (!finished) {
				switch(xmlReader.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
				{
					String currentTag = xmlReader.getLocalName();
					switch(currentTag) {
					case "entry":
						xmlReader.nextTag();
						break;
					case "key":
						xmlReader.next();
						key = deserializeObject(xmlReader, "key", keyClass);
						break;
					case "value":
						xmlReader.next();
						value = deserializeObject(xmlReader, "value", valueClass);
						break;
					default:
						finished = true;
						break;
					}
					break;
				}
				case XMLStreamConstants.END_ELEMENT:
				{
					String currentTag = xmlReader.getLocalName();
					switch(currentTag) {
					case "entry":
						xmlReader.nextTag();
						break;
					case "key":
						xmlReader.nextTag();
						break;
					case "value":
						map.put(key, value);
						xmlReader.nextTag();
						break;
					default:
						finished = true;
						break;
					}
					break;
				}
				default:
					//Handle whitespace in pretty XML
					xmlReader.next();
					break;
				}
			}
			field.set(object, map);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setArrayField(XMLStreamReader xmlReader, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Class<?> arrayType = fieldClass.getComponentType();
			List list = new ArrayList();

			boolean finished = false;
			while (!finished) {
				switch(xmlReader.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
				{
					String currentTag = xmlReader.getLocalName();
					if(!currentTag.equals("value")) {
						finished = true;
						break;
					}
					xmlReader.next();
					list.add(deserializeObject(xmlReader, "value", arrayType));
					xmlReader.next();
					break;
				}
				case XMLStreamConstants.END_ELEMENT:
				{
					String currentTag = xmlReader.getLocalName();
					if(!currentTag.equals("value")) {
						finished = true;
						break;
					} else {
						xmlReader.next();
					}
					break;
				}
				default:
					//Handle whitespace in pretty XML
					xmlReader.next();
					break;
				}
			}
			Object targetArray = ArrayReflection.newInstance(arrayType, list.size());
			for(int i = 0; i < list.size(); i++) {
				ArrayReflection.set(targetArray, i, list.get(i));
			}
			field.set(object, targetArray);
		} catch (SerializationException e) {
			throw e;
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setPrimitiveField(Field field, Class<?> fieldClass,
			T object, String value) throws SerializationException {
		try {
			if (fieldClass.equals(Boolean.TYPE)
					|| fieldClass.equals(Boolean.class)) {
				field.set(object, Boolean.parseBoolean(value));
			} else if (fieldClass.equals(Byte.TYPE)
					|| fieldClass.equals(Byte.class)) {
				field.set(object, Byte.parseByte(value));
			} else if (fieldClass.equals(Character.TYPE)
					|| fieldClass.equals(Character.class)) {
				field.set(object, value.charAt(0));
			} else if (fieldClass.equals(Double.TYPE)
					|| fieldClass.equals(Double.class)) {
				field.set(object, Double.parseDouble(value));
			} else if (fieldClass.equals(Float.TYPE)
					|| fieldClass.equals(Float.class)) {
				field.set(object, Float.parseFloat(value));
			} else if (fieldClass.equals(Integer.TYPE)
					|| fieldClass.equals(Integer.class)) {
				field.set(object, Integer.parseInt(value));
			} else if (fieldClass.equals(Long.TYPE)
					|| fieldClass.equals(Long.class)) {
				field.set(object, Long.parseLong(value));
			} else if (fieldClass.equals(Short.TYPE)
					|| fieldClass.equals(Short.class)) {
				field.set(object, Short.parseShort(value));
			} else {
				field.set(object, value);
			}
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> T parsePrimitive(String value, Class<T> fieldClass)
			throws SerializationException {
		try {
			if (fieldClass.equals(Boolean.TYPE)
					|| fieldClass.equals(Boolean.class)) {
				return (T) new Boolean(value);
			} else if (fieldClass.equals(Byte.TYPE)
					|| fieldClass.equals(Byte.class)) {
				return (T) new Byte(value);
			} else if (fieldClass.equals(Character.TYPE)
					|| fieldClass.equals(Character.class)) {
				return (T) ((Character) value.charAt(0));
			} else if (fieldClass.equals(Double.TYPE)
					|| fieldClass.equals(Double.class)) {
				return (T) new Double(value);
			} else if (fieldClass.equals(Float.TYPE)
					|| fieldClass.equals(Float.class)) {
				return (T) new Float(value);
			} else if (fieldClass.equals(Integer.TYPE)
					|| fieldClass.equals(Integer.class)) {
				return (T) new Integer(value);
			} else if (fieldClass.equals(Long.TYPE)
					|| fieldClass.equals(Long.class)) {
				return (T) new Long(value);
			} else if (fieldClass.equals(Short.TYPE)
					|| fieldClass.equals(Short.class)) {
				return (T) new Short(value);
			} else {
				return (T) value;
			}
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}
	
	private boolean isPrimitive(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		if (clazz.equals(Boolean.class)) {
			return true;
		}
		if (clazz.equals(Byte.class)) {
			return true;
		}
		if (clazz.equals(Character.class)) {
			return true;
		}
		if (clazz.equals(Double.class)) {
			return true;
		}
		if (clazz.equals(Float.class)) {
			return true;
		}
		if (clazz.equals(Integer.class)) {
			return true;
		}
		if (clazz.equals(Long.class)) {
			return true;
		}
		if (clazz.equals(Short.class)) {
			return true;
		}
		return false;
	}
}
