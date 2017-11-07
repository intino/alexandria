package io.intino.konos.alexandria.framework.box.model;

import io.intino.konos.alexandria.framework.box.displays.AlexandriaLayoutDisplay;

public class Desktop extends Element {
    private LayoutDisplayProvider layoutDisplayProvider;

    public AlexandriaLayoutDisplay layoutDisplay() {
        return layoutDisplayProvider != null ? layoutDisplayProvider.display() : null;
    }

    public Desktop layoutDisplayProvider(LayoutDisplayProvider provider) {
        this.layoutDisplayProvider = provider;
        return this;
    }

    public interface LayoutDisplayProvider {
        AlexandriaLayoutDisplay display();
    }
}
