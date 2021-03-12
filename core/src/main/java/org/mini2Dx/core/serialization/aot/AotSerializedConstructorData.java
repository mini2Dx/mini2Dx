package org.mini2Dx.core.serialization.aot;

import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.gdx.utils.Array;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class AotSerializedConstructorData {
	private static final String ARG_SEPERATOR = ":";
	private static final String INFO_SEPERATOR = ",";

	private final Array<String> constructorArgNames = new Array<String>();
	private final Array<String> constructorArgTypes = new Array<String>();

	private Class[] constructorArgClasses = null;
	private Class[] constructorArgClassesWithPrimitives = null;

	public AotSerializedConstructorData(Class ownerClass, Constructor constructor, Array<ConstructorArg> constructorArgs) {
		for(int i = 0; i < constructorArgs.size; i++) {
			this.constructorArgNames.add(constructorArgs.get(i).name());

			Class<?> clazz = null;
			if(constructorArgs.get(i).clazz() != null) {
				clazz = constructorArgs.get(i).clazz();
			} else {
				clazz = constructor.getParameterTypes()[i];
			}

			if(!clazz.equals(ownerClass)) {
				AotSerializationData.registerClass(clazz);
			}

			this.constructorArgTypes.add(clazz.getName());
		}
	}

	public AotSerializedConstructorData(Scanner scanner) throws ClassNotFoundException {
		final String [] data = scanner.nextLine().split(ARG_SEPERATOR);
		final int totalArgs = Integer.parseInt(data[0]);

		for(int i = 0; i < totalArgs; i++) {
			final String [] argData = data[i + 1].split(INFO_SEPERATOR);
			constructorArgNames.add(argData[0]);
			constructorArgTypes.add(argData[1]);
		}
	}

	public void saveTo(PrintWriter writer) {
		final StringBuilder result = new StringBuilder();
		result.append(constructorArgTypes.size);
		result.append(ARG_SEPERATOR);

		for(int i = 0; i < constructorArgNames.size; i++) {
			result.append(constructorArgNames.get(i));
			result.append(INFO_SEPERATOR);
			result.append(constructorArgTypes.get(i));

			if(i < constructorArgNames.size - 1) {
				result.append(ARG_SEPERATOR);
			}
		}

		writer.println(result.toString());
	}

	public synchronized int getTotalArgs() {
		return constructorArgNames.size;
	}

	public synchronized String getConstructorArgName(int index) {
		return constructorArgNames.get(index);
	}

	public synchronized Class getConstructorArgType(int index) {
		return getConstructorArgTypes()[index];
	}

	public synchronized Class getConstructorArgPrimitiveType(int index) {
		switch (constructorArgTypes.get(index).toLowerCase()) {
		case "java.lang.boolean":
			return Boolean.TYPE;
		case "java.lang.byte":
			return Byte.TYPE;
		case "java.lang.char":
			return Character.TYPE;
		case "java.lang.double":
			return Double.TYPE;
		case "java.lang.float":
			return Float.TYPE;
		case "java.lang.integer":
			return Integer.TYPE;
		case "java.lang.long":
			return Long.TYPE;
		case "java.lang.short":
			return Short.TYPE;
		}
		return null;
	}

	public synchronized Class[] getConstructorArgTypes() {
		if(constructorArgClasses == null) {
			constructorArgClasses = new Class[constructorArgTypes.size];
			for(int i = 0; i < constructorArgClasses.length; i++) {
				try {
					constructorArgClasses[i] = Class.forName(constructorArgTypes.get(i));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return constructorArgClasses;
	}

	public synchronized Class[] getConstructorArgTypesWithPrimitives() {
		if(constructorArgClassesWithPrimitives == null) {
			constructorArgClassesWithPrimitives = new Class[constructorArgTypes.size];
			for(int i = 0; i < constructorArgClassesWithPrimitives.length; i++) {
				Class primitiveType = getConstructorArgPrimitiveType(i);
				if(primitiveType == null) {
					try {
						constructorArgClassesWithPrimitives[i] = Class.forName(constructorArgTypes.get(i));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					constructorArgClassesWithPrimitives[i] = primitiveType;
				}
			}
		}
		return constructorArgClassesWithPrimitives;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AotSerializedConstructorData)) return false;
		AotSerializedConstructorData that = (AotSerializedConstructorData) o;
		return Objects.equals(constructorArgNames, that.constructorArgNames) &&
				Objects.equals(constructorArgTypes, that.constructorArgTypes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(constructorArgNames, constructorArgTypes);
	}
}
