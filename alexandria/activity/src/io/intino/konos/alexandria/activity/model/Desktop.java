package io.intino.konos.alexandria.activity.model;

import io.intino.konos.alexandria.activity.displays.AlexandriaLayout;

import java.net.URL;

public class Desktop extends Element {
    private LayoutDisplayProvider layoutDisplayProvider;
    private String title;
    private String subtitle;
    private String logo = "";
    private String favicon = "";
    private URL authServiceUrl;

    public AlexandriaLayout layoutDisplay() {
        return layoutDisplayProvider != null ? layoutDisplayProvider.display() : null;
    }

    public Desktop layoutDisplayProvider(LayoutDisplayProvider provider) {
        this.layoutDisplayProvider = provider;
        return this;
    }

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

    public interface LayoutDisplayProvider {
        AlexandriaLayout display();
    }
}
