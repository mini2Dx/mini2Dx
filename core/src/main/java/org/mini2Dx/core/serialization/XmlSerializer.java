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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collections.concurrent.ConcurrentObjectMap;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.exception.RequiredFieldException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.aot.AotSerializedConstructorData;
import org.mini2Dx.core.serialization.collection.DeserializedCollection;
import org.mini2Dx.core.serialization.collection.SerializedCollection;
import org.mini2Dx.core.serialization.map.deserialize.DeserializedMap;
import org.mini2Dx.core.serialization.map.serialize.SerializedMap;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.gdx.xml.XmlWriter;

import java.io.*;
import java.util.Collection;
import java.util.Map;

/**
 * XML serialization based on {@link org.mini2Dx.core.serialization.annotation.Field} annotations.<br>
 *     Note: Only supports 8-bit character sets, CDATA and special tags (&amp;lt;). To workaround the parser not allowing tags containing only spaces, spaces can be represented by &amp;amp;nbsp;
 */
public class XmlSerializer {
    private static final String LOGGING_TAG = XmlSerializer.class.getSimpleName();

    private final ObjectMap<String, Method[]> methodCache = new ConcurrentObjectMap<>();
    private final ObjectMap<String, Field[]> fieldCache = new ConcurrentObjectMap<>();

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
            throws SerializationException {
        return fromXml(new StringReader(xml), clazz);
    }

    /**
     * Reads an XML document from a {@link Reader} and converts it into an object of the specified type
     *
     * @param reader The input stream reading the XML document
     * @param clazz The {@link Class} to convert the document to
     * @param <T> The type of {@link Class} to return
     * @return The object deserialized from XML
     * @throws SerializationException Thrown when the data is invalid
     */
    public <T> T fromXml(Reader reader, Class<T> clazz)
            throws SerializationException {
        T result = null;
        try {
            XmlReader xmlReader = new XmlReader();
            XmlReader.Element root = xmlReader.parse(reader);
            result = deserializeObject(root, clazz);
        } catch (org.mini2Dx.gdx.xml.SerializationException e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Writes a XML document by searching the object for {@link org.mini2Dx.core.serialization.annotation.Field} annotations
     * @param object The object to convert to XML
     * @param <T> The type of {@link Class} to write
     * @return The object serialized as XML
     * @throws SerializationException Thrown when the object is invalid
     */
    public <T> String toXml(T object) throws SerializationException {
        StringWriter writer = new StringWriter();
        toXml(object, writer);
        return writer.toString();
    }

    /**
     * Writes a XML document to a {@link Writer} by searching the object for {@link org.mini2Dx.core.serialization.annotation.Field} annotations
     * @param object The object to convert to XML
     * @param <T> The type of {@link Class} to write
     * @param writer The stream to write the XML document to
     * @throws SerializationException Thrown when the object is invalid
     */
    public <T> void toXml(T object, Writer writer) throws SerializationException {
        try {
            XmlWriter xmlWriter = new XmlWriter(writer);
            writeObject(null, object, "data", xmlWriter);
            writer.flush();
            writer.close();
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void writeObject(Field fieldDefinition, T object, String tagName, XmlWriter xmlWriter) throws SerializationException {
        try {
            if (object == null) {
                writePrimitive(tagName, "", xmlWriter);
                return;
            }

            Class<?> clazz = object.getClass();

            if (isPrimitive(clazz) || clazz.equals(String.class)) {
                writePrimitive(tagName, object, xmlWriter);
                return;
            }
            if (Mdx.reflect.isArray(clazz)) {
                writeArray(fieldDefinition, object, xmlWriter);
                return;
            }
            if (Mdx.reflect.isEnum(clazz) || Mdx.reflect.isEnum(clazz.getSuperclass())) {
                writePrimitive(tagName, object, xmlWriter);
                return;
            }

            SerializedMap serializedMap = SerializedMap.getImplementation(clazz, object);
            if(serializedMap != null) {
                writeSerializedMap(fieldDefinition, serializedMap, xmlWriter);
                return;
            }

            SerializedCollection serializedCollection = SerializedCollection.getImplementation(clazz, object);
            if(serializedCollection != null) {
                writeSerializedCollection(fieldDefinition, serializedCollection, xmlWriter);
                return;
            }

            if (tagName != null) {
                xmlWriter.element(tagName);

                writeClassFieldIfRequired(fieldDefinition, object, tagName, xmlWriter);

                //Check for @ConstructorArg annotations in interface methods
                Class<?> [] interfaces = clazz.getInterfaces();
                for(int i = 0; i < interfaces.length; i++) {
                    final String className = interfaces[i].getName();
                    if(!methodCache.containsKey(className)) {
                        methodCache.put(className, Mdx.reflect.getDeclaredMethods(interfaces[i]));
                    }
                    final Method [] methods = methodCache.get(className);

                    for(Method method : methods) {
                        if(method.getParameterTypes().length > 0) {
                            continue;
                        }
                        Annotation annotation = method.getDeclaredAnnotation(ConstructorArg.class);
                        if(annotation == null) {
                            continue;
                        }
                        ConstructorArg constructorArg = annotation.getAnnotation(ConstructorArg.class);
                        xmlWriter.attribute(constructorArg.name(), String.valueOf(method.invoke(object)));
                    }
                }
            }

            Class<?> currentClass = clazz;
            while (currentClass != null && !currentClass.equals(Object.class)) {
                final String className = currentClass.getName();
                if(!methodCache.containsKey(className)) {
                    methodCache.put(className, Mdx.reflect.getDeclaredMethods(currentClass));
                }
                final Method [] methods = methodCache.get(className);

                for (Method method : methods) {
                    if (method.getParameterTypes().length > 0) {
                        continue;
                    }
                    Annotation annotation = method.getDeclaredAnnotation(ConstructorArg.class);
                    if (annotation == null) {
                        continue;
                    }
                    ConstructorArg constructorArg = annotation.getAnnotation(ConstructorArg.class);
                    xmlWriter.attribute(constructorArg.name(), String.valueOf(method.invoke(object)));
                }
                currentClass = currentClass.getSuperclass();
            }
            currentClass = clazz;
            while(currentClass != null && !currentClass.equals(Object.class)) {
                final String className = currentClass.getName();
                final AotSerializedClassData classData = AotSerializationData.getClassData(currentClass);

                if (!fieldCache.containsKey(className)) {
                    fieldCache.put(className, classData == null ? Mdx.reflect.getDeclaredFields(currentClass) : classData.getFieldDataAsFieldArray());
                }
                final Field [] fields = fieldCache.get(className);

                for (Field field : fields) {
                    Annotation annotation = field
                            .getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

                    if (annotation == null) {
                        continue;
                    }
                    org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = annotation
                            .getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

                    Object value = field.get(object);
                    if (!fieldAnnotation.optional() && value == null) {
                        throw new RequiredFieldException(currentClass, field.getName());
                    }
                    if(fieldAnnotation.optional() && value == null) {
                        continue;
                    }
                    writeObject(field, value, field.getName(), xmlWriter);
                }
                currentClass = currentClass.getSuperclass();
            }

            if (tagName != null) {
                xmlWriter.pop();
            }
        } catch (IllegalArgumentException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (ReflectionException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> T deserializeObject(XmlReader.Element element, Class<T> objClass)
            throws SerializationException {
        try {
            if (isPrimitive(objClass) || objClass.equals(String.class)) {
                return parsePrimitive(element.getText(), objClass);
            }
            if (Mdx.reflect.isEnum(objClass)) {
                return (T) Enum.valueOf((Class<? extends Enum>) objClass, element.getText());
            }

            Class<?> clazz = determineImplementation(element, objClass);
            T result = construct(element, clazz);

            for(int i = 0; i < element.getChildCount(); i++) {
                final XmlReader.Element child = element.getChild(i);
                final String currentFieldName = child.getName();
                Field currentField = findField(clazz, currentFieldName);

                Class<?> fieldClass = currentField.getType();
                if (Mdx.reflect.isArray(fieldClass)) {
                    int arraySize = child.getIntAttribute("length", 0);
                    setArrayField(child, currentField, fieldClass, result, arraySize);
                    continue;
                }
                if (Mdx.reflect.isEnum(fieldClass)) {
                    setEnumField(currentField, fieldClass, result, child.getText());
                } else if (!Mdx.reflect.isPrimitive(fieldClass)) {
                    if (fieldClass.equals(String.class)) {
                        setPrimitiveField(currentField, fieldClass, result, child.getText());
                    } else {
                        DeserializedMap deserializedMap = DeserializedMap.getImplementation(clazz, currentField, fieldClass, result);
                        if(deserializedMap != null) {
                            setSerializedMapField(child, deserializedMap);
                        } else {
                            DeserializedCollection deserializedCollection = DeserializedCollection.getImplementation(clazz, currentField, fieldClass, result);
                            if(deserializedCollection != null) {
                                setSerializedCollectionField(child, deserializedCollection);
                            } else if(currentField.isFinal()) {
                                throw new SerializationException("Cannot use @Field on final " + fieldClass.getName() + " fields.");
                            } else {
                                currentField.set(result, deserializeObject(child, fieldClass));
                            }
                        }
                    }
                } else {
                    setPrimitiveField(currentField, fieldClass, result, child.getText());
                }
            }
            callPostDeserializeMethods(result, clazz);
            return result;
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void callPostDeserializeMethods(T object, Class<?> clazz) throws SerializationException {
        Class<?> currentClass = clazz;
        while (currentClass != null && !currentClass.equals(Object.class)) {
            final String className = currentClass.getName();
            if(!methodCache.containsKey(className)) {
                methodCache.put(className, Mdx.reflect.getDeclaredMethods(currentClass));
            }
            final Method [] methods = methodCache.get(className);

            final AotSerializedClassData classData = AotSerializationData.getClassData(currentClass);
            if(classData != null) {
                if(classData.getPostDeserializeMethodName() != null) {
                    for(Method method : methods) {
                        if(method.getName().equals(classData.getPostDeserializeMethodName())) {
                            try {
                                method.invoke(object);
                            } catch (ReflectionException e) {
                                throw new SerializationException(e);
                            }
                        }
                    }
                }
            } else {
                for(Method method : methods) {
                    if(method.isAnnotationPresent(PostDeserialize.class)) {
                        try {
                            method.invoke(object);
                        } catch (ReflectionException e) {
                            throw new SerializationException(e);
                        }
                    }
                }
            }

            currentClass = currentClass.getSuperclass();
        }
    }

    private Class<?> determineImplementation(XmlReader.Element element, Class<?> clazz) throws ClassNotFoundException, SerializationException {
        if(Mdx.reflect.isInterface(clazz) || Mdx.reflect.isAbstract(clazz)) {
            final String classValue = element.getAttribute("class", null);
            if(classValue == null) {
                throw new SerializationException("No class field found for deserializing " + clazz.getName());
            }
            clazz = Mdx.reflect.forName(classValue);
        }
        return clazz;
    }

    private <T> T construct(final XmlReader.Element element, Class<?> clazz) throws SerializationException, IllegalArgumentException {
        Constructor[] constructors = Mdx.reflect.getConstructors(clazz);
        // Single constructor with no args
        if (constructors.length == 1 && constructors[0].getParameterAnnotations().length == 0) {
            return (T) constructors[0].newInstance();
        }
        final int attributesCountModifier = element.getAttributes() != null && element.getAttributes().containsKey("class") ? 1 : 0;

        final AotSerializedClassData classData = AotSerializationData.getClassData(clazz);
        if(classData != null) {
            AotSerializedConstructorData bestMatchedConstructor = null;
            for (int i = 0; i < classData.getTotalConstructors(); i++) {
                final AotSerializedConstructorData constructorData = classData.getConstructorData(i);

                boolean allMatched = true;
                for(int j = 0; j < constructorData.getTotalArgs(); j++) {
                    final String argName = constructorData.getConstructorArgName(j);

                    if (element.getAttributes() != null && element.getAttributes().containsKey(argName)) {
                        continue;
                    }
                    allMatched = false;
                    break;
                }

                if(!allMatched) {
                    continue;
                }

                if(bestMatchedConstructor == null) {
                    bestMatchedConstructor = constructorData;
                } else if(constructorData.getTotalArgs() > bestMatchedConstructor.getTotalArgs()) {
                    bestMatchedConstructor = constructorData;
                }
            }

            if(bestMatchedConstructor == null) {
                return (T) Mdx.reflect.newInstance(clazz);
            }

            final Object[] constructorParameters = new Object[bestMatchedConstructor.getTotalArgs()];
            for (int i = 0; i < bestMatchedConstructor.getTotalArgs(); i++) {
                constructorParameters[i] = parsePrimitive(element.getAttributes().get(bestMatchedConstructor.getConstructorArgName(i)), bestMatchedConstructor.getConstructorArgTypes()[i]);
            }

            try {
                return (T) clazz.getConstructor(bestMatchedConstructor.getConstructorArgTypes()).newInstance(constructorParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (T) Mdx.reflect.newInstance(clazz);
        } else {
            Constructor bestMatchedConstructor = null;
            Array<ConstructorArg> detectedAnnotations = new Array<ConstructorArg>(1);

            for (int i = 0; i < constructors.length; i++) {
                detectedAnnotations.clear();
                boolean allAnnotated = constructors[i].getParameterAnnotations().length > 0;

                for (int j = 0; j < constructors[i].getParameterAnnotations().length; j++) {
                    Annotation[] annotations = constructors[i].getParameterAnnotations()[j];
                    if (annotations.length == 0) {
                        allAnnotated = false;
                        break;
                    }

                    boolean hasConstructorArgAnnotation = false;
                    for (int k = 0; k < annotations.length; k++) {
                        if (!annotations[k].getAnnotationType().isAssignableFrom(ConstructorArg.class)) {
                            continue;
                        }
                        Annotation annotation = annotations[k];
                        if(annotation == null) {
                            continue;
                        }
                        ConstructorArg constructorArg = (ConstructorArg) annotation.getAnnotation(ConstructorArg.class);
                        if (element.getAttributes() == null || !element.getAttributes().containsKey(constructorArg.name())) {
                            continue;
                        }
                        detectedAnnotations.add(constructorArg);
                        hasConstructorArgAnnotation = true;
                        break;
                    }
                    if (!hasConstructorArgAnnotation) {
                        allAnnotated = false;
                    }
                }
                if (!allAnnotated) {
                    continue;
                }
                if (detectedAnnotations.size == element.getAttributes().size - attributesCountModifier) {
                    //Found exact match
                    bestMatchedConstructor = constructors[i];
                    break;
                }
                if (bestMatchedConstructor == null) {
                    bestMatchedConstructor = constructors[i];
                } else if (detectedAnnotations.size > bestMatchedConstructor.getParameterAnnotations().length) {
                    bestMatchedConstructor = constructors[i];
                }
            }
            if (bestMatchedConstructor == null || detectedAnnotations.size == 0) {
                if(detectedAnnotations.size > 0) {
                    Mdx.log.error(LOGGING_TAG, "Could not find suitable constructor for " + clazz.getSimpleName() + ". Falling back to default constructor.");
                }
                return (T) Mdx.reflect.newInstance(clazz);
            }

            final Object[] constructorParameters = new Object[detectedAnnotations.size];
            for (int i = 0; i < detectedAnnotations.size; i++) {
                ConstructorArg constructorArg = detectedAnnotations.get(i);
                constructorParameters[i] = parsePrimitive(element.getAttributes().get(constructorArg.name()), constructorArg.clazz());
            }
            return (T) bestMatchedConstructor.newInstance(constructorParameters);
        }
    }

    private Field findField(Class<?> clazz, String fieldName) throws ReflectionException {
        Class<?> currentClass = clazz;
        while (currentClass != null && !currentClass.equals(Object.class)) {
            try {
                Field result = Mdx.reflect.getDeclaredField(currentClass, fieldName);
                if (result == null) {
                    continue;
                }
                return result;
            } catch (ReflectionException e) {
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new ReflectionException("No field '" + fieldName + "' found in class " + clazz.getName());
    }

    private <T> void setSerializedMapField(XmlReader.Element element, DeserializedMap deserializedMap)
            throws SerializationException {
        Class<?> keyClass = deserializedMap.getKeyClass();
        Class<?> valueClass = deserializedMap.getValueClass();

        for(XmlReader.Element entry : element.getChildrenByName("entry")) {
            Object key = deserializeObject(entry.getChildByName("key"), keyClass);
            Object value = deserializeObject(entry.getChildByName("value"), valueClass);
            deserializedMap.put(key, value);
        }
    }

    private <T> void setSerializedCollectionField(XmlReader.Element element, DeserializedCollection deserializedCollection)
            throws SerializationException {
        Class<?> valueClass = deserializedCollection.getValueClass();

        for(XmlReader.Element value : element.getChildrenByName("value")) {
            deserializedCollection.add(deserializeObject(value, valueClass));
        }
    }

    private <T> void setArrayField(XmlReader.Element element, Field field, Class<?> fieldClass, T object, int size)
            throws SerializationException {
        try {
            Class<?> arrayType = Mdx.reflect.arrayComponentType(fieldClass);
            Array list = new Array();

            for(XmlReader.Element value : element.getChildrenByName("value")) {
                list.add(deserializeObject(value, arrayType));
            }

            Object targetArray = null;
            if(field.isFinal()) {
                targetArray = field.get(object);
            } else {
                targetArray = Mdx.reflect.newArray(arrayType, size);
            }
            for (int i = 0; i < list.size; i++) {
                Mdx.reflect.arraySet(targetArray, i, list.get(i));
            }
            if(!field.isFinal()) {
                field.set(object, targetArray);
            }
        } catch (SerializationException e) {
            throw e;
        } catch (ReflectionException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void setEnumField(Field field, Class<?> fieldClass, T object, String value)
            throws SerializationException {
        if(field.isFinal()) {
            throw new SerializationException("Cannot use @Field on final enum fields. Use the @ConstructorArg method instead.");
        }
        try {
            field.set(object, Enum.valueOf((Class<Enum>) fieldClass, value));
        } catch (ReflectionException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void setPrimitiveField(Field field, Class<?> fieldClass, T object, String value)
            throws SerializationException {
        if(field.isFinal()) {
            throw new SerializationException("Cannot use @Field on final " + fieldClass.getName() + " fields.");
        }
        try {
            if (fieldClass.equals(Boolean.TYPE) || fieldClass.equals(Boolean.class)) {
                field.set(object, Boolean.parseBoolean(value));
            } else if (fieldClass.equals(Byte.TYPE) || fieldClass.equals(Byte.class)) {
                field.set(object, Byte.parseByte(value));
            } else if (fieldClass.equals(Character.TYPE) || fieldClass.equals(Character.class)) {
                field.set(object, value.charAt(0));
            } else if (fieldClass.equals(Double.TYPE) || fieldClass.equals(Double.class)) {
                field.set(object, Double.parseDouble(value));
            } else if (fieldClass.equals(Float.TYPE) || fieldClass.equals(Float.class)) {
                field.set(object, Float.parseFloat(value));
            } else if (fieldClass.equals(Integer.TYPE) || fieldClass.equals(Integer.class)) {
                field.set(object, Integer.parseInt(value));
            } else if (fieldClass.equals(Long.TYPE) || fieldClass.equals(Long.class)) {
                field.set(object, Long.parseLong(value));
            } else if (fieldClass.equals(Short.TYPE) || fieldClass.equals(Short.class)) {
                field.set(object, Short.parseShort(value));
            } else {
                field.set(object, value);
            }
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void writeClassFieldIfRequired(Field fieldDefinition, T object, String fieldName, XmlWriter xmlWriter) throws SerializationException, IOException {
        if (fieldDefinition == null) {
            return;
        }
        Class<?> clazz = object.getClass();
        Class<?> fieldDefinitionClass = fieldDefinition.getType();

        if(Mdx.reflect.isArray(fieldDefinitionClass)) {
            Class<?> arrayComponentType = Mdx.reflect.arrayComponentType(fieldDefinitionClass);
            if(Mdx.reflect.isInterface(arrayComponentType) && Mdx.reflect.getAnnotation(arrayComponentType, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isAssignableFrom(Collection.class, fieldDefinitionClass)) {
            Class<?> valueClass = fieldDefinition.getElementType(0);
            if(Mdx.reflect.isInterface(valueClass) && Mdx.reflect.getAnnotation(valueClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isAssignableFrom(Array.class, fieldDefinitionClass)) {
            Class<?> valueClass = fieldDefinition.getElementType(0);
            if(Mdx.reflect.isInterface(valueClass) && Mdx.reflect.getAnnotation(valueClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isAssignableFrom(Map.class, fieldDefinitionClass)) {
            Class<?> valueClass = fieldDefinition.getElementType(1);
            if(Mdx.reflect.isInterface(valueClass) && Mdx.reflect.getAnnotation(valueClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isAssignableFrom(ObjectMap.class, fieldDefinitionClass)) {
            Class<?> valueClass = fieldDefinition.getElementType(1);
            if(Mdx.reflect.isInterface(valueClass) && Mdx.reflect.getAnnotation(valueClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isInterface(fieldDefinitionClass)) {
            if(Mdx.reflect.getAnnotation(fieldDefinitionClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
        if(Mdx.reflect.isAbstract(fieldDefinitionClass)) {
            if(Mdx.reflect.getAnnotation(fieldDefinitionClass, NonConcrete.class) == null) {
                throw new SerializationException(fieldName + ":: Cannot serialize abstract class unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
            }
            xmlWriter.attribute("class", clazz.getName());
            return;
        }
    }

    private <T> void writeSerializedMap(Field field, SerializedMap map, XmlWriter xmlWriter) throws SerializationException {
        try {
            if (field != null) {
                xmlWriter.element(field.getName());
            }
            for (Object key : map.keys()) {
                xmlWriter.element("entry");
                writeObject(null, key, "key", xmlWriter);
                writeObject(field, map.get(key), "value", xmlWriter);
                xmlWriter.pop();
            }
            if (field != null) {
                xmlWriter.pop();
            }
        } catch (IllegalArgumentException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void writeSerializedCollection(Field field, SerializedCollection serializedCollection, XmlWriter xmlWriter) throws SerializationException {
        try {
            if (field != null) {
                xmlWriter.element(field.getName());
                xmlWriter.attribute("length", String.valueOf(serializedCollection.getLength()));
            }

            for (int i = 0; i < serializedCollection.getLength(); i++) {
                Object object = serializedCollection.get(i);
                if(object == null) {
                    continue;
                }
                writeObject(field, object, "value", xmlWriter);
            }

            if (field != null) {
                xmlWriter.pop();
            }

            serializedCollection.dispose();
        } catch (IllegalArgumentException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void writeArray(Field field, T array, XmlWriter xmlWriter) throws SerializationException {
        try {
            int arrayLength = Mdx.reflect.arrayLength(array);

            if (field != null) {
                xmlWriter.element(field.getName());
                xmlWriter.attribute("length", String.valueOf(arrayLength));
            }

            for (int i = 0; i < arrayLength; i++) {
                Object object = Mdx.reflect.arrayGet(array, i);
                if(object == null) {
                    continue;
                }
                writeObject(field, object, "value", xmlWriter);
            }

            if (field != null) {
                xmlWriter.pop();
            }
        } catch (IllegalArgumentException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private <T> void writePrimitive(String tagName, T object, XmlWriter xmlWriter) throws IOException {
        if (tagName != null) {
            xmlWriter.element(tagName, String.valueOf(object));
        } else {
            xmlWriter.write(String.valueOf(object));
        }
    }

    private <T> T parsePrimitive(String value, Class<T> fieldClass) throws SerializationException {
        if(value != null && value.contains("&nbsp;")) {
            value = value.replace("&nbsp;", " ");
        }
        try {
            if (fieldClass.equals(Boolean.TYPE) || fieldClass.equals(Boolean.class)) {
                return (T) new Boolean(value);
            } else if (fieldClass.equals(Byte.TYPE) || fieldClass.equals(Byte.class)) {
                return (T) new Byte(value);
            } else if (fieldClass.equals(Character.TYPE) || fieldClass.equals(Character.class)) {
                return (T) ((Character) value.charAt(0));
            } else if (fieldClass.equals(Double.TYPE) || fieldClass.equals(Double.class)) {
                return (T) new Double(value);
            } else if (fieldClass.equals(Float.TYPE) || fieldClass.equals(Float.class)) {
                return (T) new Float(value);
            } else if (fieldClass.equals(Integer.TYPE) || fieldClass.equals(Integer.class)) {
                return (T) new Integer(value);
            } else if (fieldClass.equals(Long.TYPE) || fieldClass.equals(Long.class)) {
                return (T) new Long(value);
            } else if (fieldClass.equals(Short.TYPE) || fieldClass.equals(Short.class)) {
                return (T) new Short(value);
            } else {
                return (T) value;
            }
        } catch (Exception e) {
            if(Mdx.log != null) {
                Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
            } else {
                e.printStackTrace();
            }
            throw new SerializationException(e.getMessage(), e);
        }
    }

    private boolean isPrimitive(Class<?> clazz) {
        if (Mdx.reflect.isPrimitive(clazz)) {
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