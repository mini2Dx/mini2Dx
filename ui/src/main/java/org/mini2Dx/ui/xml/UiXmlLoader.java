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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.Tab;
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.xml.spi.*;

import java.io.Reader;

public class UiXmlLoader {
    private final FileHandleResolver fileHandleResolver;
    private ObjectMap<String, UiElementHandler> tagNameToHandler = new ObjectMap<>();

    public UiXmlLoader(FileHandleResolver fileHandleResolver) {
        this.fileHandleResolver = fileHandleResolver;

        CorePopulator corePopulator = new CorePopulator();
        ParentUiElementPopulator parentUiElementPopulator = new ParentUiElementPopulator();

        addTagHandler("animated-image", new AnimatedImageFactory(), corePopulator, new AnimatedImagePopulator());
        addTagHandler("button", new ButtonFactory(), corePopulator, parentUiElementPopulator, new ButtonPopulator());
        addTagHandler("check-box", new CheckBoxFactory(), corePopulator, new CheckboxPopulator());
        addTagHandler("container", new ContainerFactory(), corePopulator, parentUiElementPopulator);
        addTagHandler("div", new DivFactory(), corePopulator, parentUiElementPopulator);
        addTagHandler("flex-row", new FlexRowFactory(), corePopulator, new FlexRowPopulator(), parentUiElementPopulator);
        addTagHandler("image", new ImageFactory(), corePopulator, new ImagePopulator());
        addTagHandler("image-button", new ImageButtonFactory(), corePopulator, new ImageButtonPopulator());
        addTagHandler("label", new LabelFactory(), corePopulator, new LabelPopulator());
        addTagHandler("progress-bar", new ProgressBarFactory(), corePopulator, new ProgressBarPopulator());
        addTagHandler("radio-button", new RadioButtonFactory(), corePopulator, new RadioButtonPopulator());
        addTagHandler("scroll-box", new ScrollBoxFactory(), corePopulator, parentUiElementPopulator, new ScrollBoxPopulator());
        addTagHandler("select", new SelectFactory(), corePopulator, new SelectPopulator());
        addTagHandler("slider", new SliderFactory(), corePopulator, new SliderPopulator());
        addTagHandler("tab", new TabFactory(), new TabPopulator());
        addTagHandler("tab-view", new TabViewFactory(), corePopulator, new TabViewPopulator());
        addTagHandler("text-button", new TextButtonFactory(), corePopulator, new TextButtonPopulator());
        addTagHandler("text-box", new TextBoxFactory(), corePopulator, new TextBoxPopulator());
    }

    /**
     * Add a new tag handler for a XML tag
     *
     * @param tagName    - the XML tag name
     * @param factory    - the factory the creates a new instance of the UI element
     * @param populators - a collection of populators to populate the properties on the UI element
     */
    public void addTagHandler(String tagName, UiElementFactory factory, UiElementPopulator... populators) {
        tagNameToHandler.put(tagName, new UiElementHandler(factory, populators));
    }

    public <T extends UiElement> T load(String filename) {
        try {
            Reader reader = fileHandleResolver.resolve(filename).reader();
            XmlReader xmlReader = new XmlReader();
            XmlReader.Element root = xmlReader.parse(reader);

            return (T) processXmlTag(root);
        } catch (Exception e) {
            throw new MdxException("Failed to load UI file: " + filename, e);
        }
    }

    public <T> T load(String filename, Class<T> model) {
        UiElement container = load(filename);
        if (container != null && model != null) {
            return populateModel(container, model);
        } else {
            throw new MdxException("Failed to populate Ui file " + filename, new Exception("container or model is null"));
        }
    }

    public <T> T load(String filename, T model) {
        UiElement container = load(filename);
        if (container != null && model != null) {
            return populateModel(container, model);
        } else {
            throw new MdxException("Failed to populate Ui file " + filename, new Exception("container or model is null"));
        }
    }

    protected <T extends UiElement, M> M populateModel(T container, Class<M> model) {
        M newInstance = (M) Mdx.reflect.newInstance(model);
        for (Field field : Mdx.reflect.getDeclaredFields(model)) {
            if (field.isAnnotationPresent(org.mini2Dx.ui.annotation.UiElement.class)) {
                String fieldName = field.getName();
                String annotationId = field
                        .getDeclaredAnnotation(org.mini2Dx.ui.annotation.UiElement.class)
                        .getAnnotation(org.mini2Dx.ui.annotation.UiElement.class)
                        .id();
                String id = annotationId.length() > 0 ? annotationId : fieldName;
                UiElement element = container.getElementById(id);
                field.set(newInstance, field.getType().cast(element));
            }
        }
        return newInstance;
    }

    protected <T extends UiElement, M> M populateModel(T container, M model) {
        M newInstance = model;
        for (Field field : Mdx.reflect.getDeclaredFields(model.getClass())) {
            if (field.isAnnotationPresent(org.mini2Dx.ui.annotation.UiElement.class)) {
                String fieldName = field.getName();
                String annotationId = field
                        .getDeclaredAnnotation(org.mini2Dx.ui.annotation.UiElement.class)
                        .getAnnotation(org.mini2Dx.ui.annotation.UiElement.class)
                        .id();
                String id = annotationId.length() > 0 ? annotationId : fieldName;
                UiElement element = container.getElementById(id);
                field.set(newInstance, field.getType().cast(element));
            }
        }
        return newInstance;
    }

    private UiElement processXmlTag(XmlReader.Element root) {
        String tagName = XmlTagUtil.getTagNameWithoutPrefix(root);
        UiElementHandler handler = tagNameToHandler.get(tagName);
        if (handler == null) {
            throw new UnknownUiTagException(tagName);
        }

        boolean childTagsAlreadyProcessed = false;
        UiElement uiElement = handler.factory.build(root);
        for (UiElementPopulator populator : handler.populators) {
            if (populator.populate(root, uiElement)) {
                childTagsAlreadyProcessed = true;
            }
        }

        if (!childTagsAlreadyProcessed && uiElement instanceof ParentUiElement) {
            ParentUiElement parentUiElement = (ParentUiElement) uiElement;
            for (int i = 0; i < root.getChildCount(); i++) {
                UiElement childElement = processXmlTag(root.getChild(i));
                if (parentUiElement instanceof TabView) {
                    TabView tabView = (TabView) parentUiElement;
                    tabView.add((Tab) childElement);
                } else {
                    parentUiElement.add(childElement);
                }
            }
        }

        return uiElement;
    }

    private static class UiElementHandler {
        final UiElementFactory factory;
        final UiElementPopulator[] populators;

        public UiElementHandler(UiElementFactory factory, UiElementPopulator... populators) {
            this.factory = factory;
            this.populators = populators;
        }
    }
}
