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

	private Class[] constructorParamTypes = null;

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

	public int getTotalArgs() {
		return constructorArgNames.size;
	}

	public String getConstructorArgName(int index) {
		return constructorArgNames.get(index);
	}

	public Class getConstructorArgType(int index) {
		try {
			return Class.forName(constructorArgTypes.get(index));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Class[] getConstructorArgTypes() {
		if(constructorParamTypes == null) {
			constructorParamTypes = new Class[constructorArgTypes.size];
			for(int i = 0; i < constructorParamTypes.length; i++) {
				try {
					constructorParamTypes[i] = Class.forName(constructorArgTypes.get(i));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return constructorParamTypes;
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
