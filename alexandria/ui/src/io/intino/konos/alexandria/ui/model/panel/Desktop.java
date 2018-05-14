package io.intino.konos.alexandria.ui.model.panel;

import io.intino.konos.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Panel;

import java.net.URL;

public class Desktop extends Panel {
    private String title;
    private String subtitle;
    private String logo = "";
    private String favicon = "";
    private URL authServiceUrl;
    private ElementDisplayBuilder displayBuilder;

    public String title() {
        return title;
    }

    public Desktop title(String title) {
        this.title = title;
        return this;
    }

    public String subtitle() {
        return subtitle;
    }

    public Desktop subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String logo() {
        return logo;
    }

    public Desktop logo(String logo) {
        this.logo = logo;
        return this;
    }

    public String favicon() {
        return favicon;
    }

    public Desktop favicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

    public URL authServiceUrl() {
        return authServiceUrl;
    }

    public Desktop authServiceUrl(URL authServiceUrl) {
        this.authServiceUrl = authServiceUrl;
        return this;
    }

    public AlexandriaElementDisplay displayFor(Element element, Item item) {
        return displayBuilder != null ? displayBuilder.displayFor(element, item != null ? item.object() : null) : null;
    }

    public Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Item item) {
        return displayBuilder != null ? displayBuilder.displayTypeFor(element, item != null ? item.object() : null) : null;
    }

    public Desktop elementDisplayBuilder(ElementDisplayBuilder builder) {
        this.displayBuilder = builder;
        return this;
    }

    public interface ElementDisplayBuilder {
        AlexandriaElementDisplay displayFor(Element element, Object object);
        Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Object object);
    }

}