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
package org.mini2Dx.uats.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.lwjgl.Sys;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.ui.element.*;
import org.mini2Dx.ui.style.UiTheme;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AotWriter {

	public static void main(String [] args) throws IOException {
		Mdx.reflect = new JvmReflection();
		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();

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

		File file = new File("aot-data.txt");
		FileWriter writer = new FileWriter(file);
		AotSerializationData.saveTo(writer);
		writer.close();

		System.out.println("Wrote to " + file.getAbsolutePath());
	}
}
