package io.intino.konos.alexandria.framework.box.model;

import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;

import java.net.URL;
import java.util.List;

public class Layout extends Element {
    private Mode mode;
    private List<ElementOption> optionList;
    private SettingsLoader settingsLoader;

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

    public enum Mode {
        Menu, Tab;
    }

    public interface SettingsLoader {
        Settings load();
    }

    public interface Settings {
        String title();
        String subtitle();
        URL logo();
        URL authServiceUrl();
    }
}
