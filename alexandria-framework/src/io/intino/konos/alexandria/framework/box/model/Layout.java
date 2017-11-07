package io.intino.konos.alexandria.framework.box.model;

import io.intino.konos.alexandria.framework.box.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Layout extends Element {
    private Mode mode;
    private List<ElementOption> optionList = new ArrayList<>();
    private SettingsLoader settingsLoader;
    private ElementDisplayBuilder displayBuilder;

    public Mode mode() {
        return mode;
    }

    public Layout mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public List<ElementOption> options() {
        return optionList;
    }

    public Layout add(ElementOption option) {
        this.optionList.add(option);
        return this;
    }

    public Settings settings() {
        return settingsLoader != null ? settingsLoader.load() : null;
    }

    public Layout settings(SettingsLoader loader) {
        this.settingsLoader = loader;
        return this;
    }

    public AlexandriaElementDisplay displayFor(Element element, Item item) {
        return displayBuilder != null ? displayBuilder.displayFor(element, item != null ? item.object() : null) : null;
    }

    public Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Item item) {
        return displayBuilder != null ? displayBuilder.displayTypeFor(element, item != null ? item.object() : null) : null;
    }

    public Layout elementDisplayBuilder(ElementDisplayBuilder builder) {
        this.displayBuilder = builder;
        return this;
    }

    public enum Mode {
        Menu, Tab;
    }

    public interface SettingsLoader {
        Settings load();
    }

    public interface ElementDisplayBuilder {
        AlexandriaElementDisplay displayFor(Element element, Object object);
        Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Object object);
    }

    public interface Settings {
        String title();
        String subtitle();
        URL logo();
        URL authServiceUrl();
    }
}
