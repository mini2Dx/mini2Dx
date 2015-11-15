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
package org.mini2Dx.android.serialization;

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

import org.mini2Dx.core.serialization.RequiredFieldException;
import org.mini2Dx.core.serialization.SerializationException;
import org.mini2Dx.core.serialization.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * Android implementation of {@link XmlSerializer}
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AndroidXmlSerializer implements XmlSerializer {

	@Override
	public <T> T fromXml(String xml, Class<T> clazz)
			throws SerializationException {
		return fromXml(new StringReader(xml), clazz);
	}
	
	@Override
	public <T> T fromXml(Reader xmlReader, Class<T> clazz)
			throws SerializationException {
		XmlPullParser xmlParser = Xml.newPullParser();
		T result = null;
		try {
			xmlParser.setInput(xmlReader);
			result = deserializeObject(xmlParser, clazz);
		} catch (XmlPullParserException e) {
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
		org.xmlpull.v1.XmlSerializer xmlSerializer = Xml.newSerializer();
		try {
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8", true);
			writeObject(object, object.getClass().getSimpleName(), xmlSerializer);
			xmlSerializer.endDocument();
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	private <T> void writeObject(T object, String tagName,
			org.xmlpull.v1.XmlSerializer xmlSerializer)
			throws SerializationException {
		try {
			if(object == null) {
				writePrimitive(tagName, "", xmlSerializer);
				return;
			}

			Class<?> clazz = object.getClass();
			
			if (isPrimitive(clazz) || clazz.equals(String.class)) {
				writePrimitive(tagName, object, xmlSerializer);
				return;
			}
			if (clazz.isArray()) {
				writeArray(tagName, object, xmlSerializer);
				return;
			}
			if (Collection.class.isAssignableFrom(clazz)) {
				Collection collection = (Collection) object;
				writeArray(tagName, collection.toArray(), xmlSerializer);
				return;
			}
			if (Map.class.isAssignableFrom(clazz)) {
				writeMap(tagName, (Map) object, xmlSerializer);
				return;
			}

			if (tagName != null) {
				xmlSerializer.startTag("", tagName);
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
					writeObject(field.get(object), field.getName(), xmlSerializer);
				}
				currentClass = currentClass.getSuperclass();
			}
			
			if (tagName != null) {
				xmlSerializer.endTag("", tagName);
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writeMap(String tagName, Map map, org.xmlpull.v1.XmlSerializer xmlSerializer) throws SerializationException {
		try {
			if (tagName != null) {
				xmlSerializer.startTag("", tagName);
			}
			for (Object key : map.keySet()) {
				xmlSerializer.startTag("", "entry");
				writeObject(key, "key", xmlSerializer);
				writeObject(map.get(key), "value", xmlSerializer);
				xmlSerializer.endTag("", "entry");
			}
			if (tagName != null) {
				xmlSerializer.endTag("", tagName);
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writeArray(String tagName, T array, org.xmlpull.v1.XmlSerializer xmlSerializer) throws SerializationException {
		try {
			if (tagName != null) {
				xmlSerializer.startTag("", tagName);
			}

			int arrayLength = Array.getLength(array);
			for (int i = 0; i < arrayLength; i++) {
				writeObject(Array.get(array, i), "value", xmlSerializer);
			}
			
			if (tagName != null) {
				xmlSerializer.endTag("", tagName);
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}
	
	private <T> void writePrimitive(String tagName, T object, org.xmlpull.v1.XmlSerializer xmlSerializer) throws SerializationException {
		try {
			if (tagName != null) {
				xmlSerializer.startTag("", tagName);
			}
			xmlSerializer.text(String.valueOf(object));
			if (tagName != null) {
				xmlSerializer.endTag("", tagName);
			}
		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);
		} catch (IllegalStateException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	private <T> T deserializeObject(XmlPullParser xmlParser, Class<T> clazz)
			throws SerializationException {
		try {
			if (clazz.isPrimitive() || clazz.equals(String.class)) {
				return parsePrimitive(xmlParser.nextText(), clazz);
			}
			if (clazz.isEnum()) {
				return (T) Enum.valueOf((Class<? extends Enum>) clazz,
						xmlParser.nextText());
			}

			T result = ClassReflection.newInstance(clazz);
			int parserEventType = xmlParser.getEventType();

			while (parserEventType != XmlPullParser.END_DOCUMENT) {
				switch (parserEventType) {
				case XmlPullParser.START_TAG:
					Field currentField = findField(clazz, xmlParser.getName());
					currentField.setAccessible(true);
					
					Class<?> fieldClass = currentField.getType();
					if (fieldClass.isArray()) {
						xmlParser.next();
						setArrayField(xmlParser, currentField, fieldClass,
								result);
						break;
					}
					if (!fieldClass.isPrimitive()) {
						if (fieldClass.equals(String.class)) {
							setPrimitiveField(currentField, fieldClass, result,
									xmlParser.nextText());
						} else if (Map.class.isAssignableFrom(clazz)) {
							xmlParser.next();
							setMapField(xmlParser, currentField, fieldClass,
									result);
						} else if (Collection.class.isAssignableFrom(clazz)) {
							xmlParser.next();
							setCollectionField(xmlParser, currentField,
									fieldClass, result);
						} else {
							xmlParser.next();
							currentField.set(result,
									deserializeObject(xmlParser, clazz));
						}
						break;
					}
					setPrimitiveField(currentField, fieldClass, result,
							xmlParser.nextText());
					break;
				default:
					break;
				}
				parserEventType = xmlParser.next();
			}
			return result;
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
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

	private <T> void setCollectionField(XmlPullParser xmlParser, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Collection collection = (Collection) (fieldClass.isInterface() ? new ArrayList()
					: ClassReflection.newInstance(fieldClass));
			Class<?> valueClass = field.getElementType(0);

			String currentTag = xmlParser.getName();
			while (currentTag.equals("value")) {
				collection.add(deserializeObject(xmlParser, valueClass));
				xmlParser.next();
				currentTag = xmlParser.getName();
			}

			field.set(object, collection);
		} catch (XmlPullParserException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setMapField(XmlPullParser xmlParser, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Map map = (Map) (fieldClass.isInterface() ? new HashMap()
					: ClassReflection.newInstance(fieldClass));
			Class<?> keyClass = field.getElementType(0);
			Class<?> valueClass = field.getElementType(1);

			String currentTag = xmlParser.getName();
			while (currentTag.equals("entry")) {
				xmlParser.next();
				Object key = deserializeObject(xmlParser, keyClass);
				xmlParser.next();
				map.put(key, deserializeObject(xmlParser, valueClass));
				xmlParser.next();
				currentTag = xmlParser.getName();
			}
			field.set(object, map);
		} catch (XmlPullParserException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		} catch (ReflectionException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setArrayField(XmlPullParser xmlParser, Field field,
			Class<?> fieldClass, T object) throws SerializationException {
		try {
			Class<?> arrayType = fieldClass.getComponentType();
			List list = new ArrayList();

			String currentTag = xmlParser.getName();
			while (currentTag.equals("value")) {
				list.add(deserializeObject(xmlParser, arrayType));
				xmlParser.next();
				currentTag = xmlParser.getName();
			}
			field.set(object, list.toArray((Object[]) ArrayReflection
					.newInstance(arrayType, 0)));
		} catch (Exception e) {
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
