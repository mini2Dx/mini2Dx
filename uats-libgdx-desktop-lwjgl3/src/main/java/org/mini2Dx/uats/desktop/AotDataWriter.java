/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.uats.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import org.lwjgl.Sys;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.di.BasicComponentScanner;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.lockprovider.jvm.JvmLocks;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.style.UiTheme;
import org.reflections8.Reflections;
import org.reflections8.scanners.*;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class AotDataWriter {

	public static void main(String [] args) {
		if(args.length < 2) {
			System.err.println("Must provide scan package name and path to output directory");
			return;
		}

		Mdx.locks = new JvmLocks();
		Mdx.reflect = new JvmReflection();
		Gdx.files = new Lwjgl3Files();
		Mdx.files = new LibgdxFiles();

		final File outputDirectory = new File(args[1]);
		final BasicComponentScanner dependencyInjectionClasses = new BasicComponentScanner();

		AotSerializationData.registerClass(UiTheme.class);
		AotSerializationData.registerClass(AnimatedImage.class);
		AotSerializationData.registerClass(Button.class);
		AotSerializationData.registerClass(Checkbox.class);
		AotSerializationData.registerClass(Container.class);
		AotSerializationData.registerClass(CustomUiElement.class);
		AotSerializationData.registerClass(Div.class);
		AotSerializationData.registerClass(FlexRow.class);
		AotSerializationData.registerClass(Image.class);
		AotSerializationData.registerClass(ImageButton.class);
		AotSerializationData.registerClass(Label.class);
		AotSerializationData.registerClass(ParentUiElement.class);
		AotSerializationData.registerClass(ProgressBar.class);
		AotSerializationData.registerClass(RadioButton.class);
		AotSerializationData.registerClass(ScrollBox.class);
		AotSerializationData.registerClass(Select.class);
		AotSerializationData.registerClass(Slider.class);
		AotSerializationData.registerClass(Tab.class);
		AotSerializationData.registerClass(TabButton.class);
		AotSerializationData.registerClass(TabView.class);
		AotSerializationData.registerClass(TextBox.class);
		AotSerializationData.registerClass(TextButton.class);
		AotSerializationData.registerClass(UiElement.class);

		Reflections reflections = new Reflections(args[0],
				new FieldAnnotationsScanner(),
				new TypeAnnotationsScanner(),
				new MethodAnnotationsScanner(),
				new MethodParameterScanner(),
				new SubTypesScanner());

		for(Class clazz : reflections.getTypesAnnotatedWith(Singleton.class)) {
			dependencyInjectionClasses.getSingletonClasses().add(clazz);
		}
		for(Class clazz : reflections.getTypesAnnotatedWith(Prototype.class)) {
			dependencyInjectionClasses.getPrototypeClasses().add(clazz);
		}

		for(Field field : reflections.getFieldsAnnotatedWith(org.mini2Dx.core.serialization.annotation.Field.class)) {
			AotSerializationData.registerClass(field.getDeclaringClass());
		}
		for(Class clazz : reflections.getTypesAnnotatedWith(NonConcrete.class)) {
			AotSerializationData.registerClass(clazz);
		}
		for(Class clazz : reflections.getSubTypesOf(UiElement.class)) {
			AotSerializationData.registerClass(clazz);
		}
		for(Constructor constructor: reflections.getConstructorsWithAnyParamAnnotated(org.mini2Dx.core.serialization.annotation.ConstructorArg.class)) {
			AotSerializationData.registerClass(constructor.getDeclaringClass());
		}

		try {
			File file = new File(outputDirectory, "aot-di.txt");
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileWriter writer = new FileWriter(file, false);
			dependencyInjectionClasses.saveTo(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File file = new File(outputDirectory, "aot-ser.txt");
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileWriter writer = new FileWriter(file, false);
			AotSerializationData.saveTo(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
