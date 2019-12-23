package org.mini2Dx.ui.xml;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.xml.spi.CheckBoxFactory;
import org.mini2Dx.ui.xml.spi.CheckboxPopulator;
import org.mini2Dx.ui.xml.spi.ContainerFactory;
import org.mini2Dx.ui.xml.spi.CorePopulator;
import org.mini2Dx.ui.xml.spi.DivFactory;
import org.mini2Dx.ui.xml.spi.FlexDirectionPopulator;
import org.mini2Dx.ui.xml.spi.FlexRowFactory;
import org.mini2Dx.ui.xml.spi.LabelFactory;
import org.mini2Dx.ui.xml.spi.LabelPopulator;
import org.mini2Dx.ui.xml.spi.ParentUiElementPopulator;
import org.mini2Dx.ui.xml.spi.ProgressBarFactory;
import org.mini2Dx.ui.xml.spi.ProgressBarPopulator;
import org.mini2Dx.ui.xml.spi.TextBoxFactory;
import org.mini2Dx.ui.xml.spi.TextBoxPopulator;
import org.mini2Dx.ui.xml.spi.TextButtonFactory;
import org.mini2Dx.ui.xml.spi.TextButtonPopulator;
import org.mini2Dx.ui.xml.spi.UnknownUiTagException;

import java.io.Reader;

public class UiXmlLoader {
    private final FileHandleResolver fileHandleResolver;
    private ObjectMap<String, UiElementHandler> tagNameToHandler = new ObjectMap<>();

    public UiXmlLoader(FileHandleResolver fileHandleResolver) {
        this.fileHandleResolver = fileHandleResolver;

        CorePopulator corePopulator = new CorePopulator();
        ParentUiElementPopulator parentUiElementPopulator = new ParentUiElementPopulator();

        addTagHandler("container", new ContainerFactory(), corePopulator, parentUiElementPopulator);
        addTagHandler("div", new DivFactory(), corePopulator, parentUiElementPopulator);
        addTagHandler("flex-row", new FlexRowFactory(), corePopulator, new FlexDirectionPopulator(), parentUiElementPopulator);
        addTagHandler("text-button", new TextButtonFactory(), corePopulator, new TextButtonPopulator());
        addTagHandler("text-box", new TextBoxFactory(), corePopulator, new TextBoxPopulator());
        addTagHandler("check-box", new CheckBoxFactory(), corePopulator, new CheckboxPopulator());
        addTagHandler("label", new LabelFactory(), corePopulator, new LabelPopulator());
        addTagHandler("progress-bar", new ProgressBarFactory(), corePopulator, new ProgressBarPopulator());
    }

    /**
     * Add a new tag handler for a XML tag
     *
     * @param tagName    - the XML tag name
     * @param factory    - the factory the creates a new instance of the UI element
     * @param populators - a collection of populators to populate the properties on the UI element
     */
    public void addTagHandler(String tagName, UiXmlLoader.UiElementFactory factory, UiElementPopulator... populators) {
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

    private UiElement processXmlTag(XmlReader.Element root) {
        UiElementHandler handler = tagNameToHandler.get(root.getName());
        if (handler == null) {
            throw new UnknownUiTagException(root.getName());
        }

        UiElement uiElement = buildAndPopulateUiElement(root, handler);

        if (uiElement instanceof ParentUiElement) {
            ParentUiElement parentUiElement = (ParentUiElement) uiElement;
            for (int i = 0; i < root.getChildCount(); i++) {
                parentUiElement.add(processXmlTag(root.getChild(i)));
            }
        }

        return uiElement;
    }

    private UiElement buildAndPopulateUiElement(XmlReader.Element root, UiElementHandler handler) {
        UiElement uiElement = handler.factory.build(root);
        for (UiElementPopulator populator : handler.populators) {
            populator.populate(root, uiElement);
        }
        return uiElement;
    }

    public interface UiElementFactory<T extends UiElement> {
        /**
         * Simply instantiate an instance of the UiElement
         * <p>
         * This is intended to help handle scenarios where there is NO default constructor
         * for an UiElement
         *
         * @param xmlTag - the tag that triggered this element to be created
         * @return a new instance of the UiElement
         */
        T build(XmlReader.Element xmlTag);
    }

    public interface UiElementPopulator<T extends UiElement> {
        /**
         * Populate properties on the UiElement
         *
         * @param xmlTag    - the tag to pull values off of
         * @param uiElement - the UiElement to be populated
         */
        void populate(XmlReader.Element xmlTag, T uiElement);
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
