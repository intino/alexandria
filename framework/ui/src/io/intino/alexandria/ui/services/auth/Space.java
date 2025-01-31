package io.intino.alexandria.ui.services.auth;

import io.intino.alexandria.logger.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Space {
    private final URL url;
    private String baseUrl;
    private String name = "space";
    private String title = "";
    private String secret = "1234";

    public Space(URL url) {
        this.url = url;
    }

    public Space setBaseUrl(String url) {
        baseUrl = url;
        return this;
    }

    public String title() {
        return title;
    }

    public Space title(String title) {
        this.title = title;
        return this;
    }

    public String name() {
        return name;
    }

    public Space name(String name) {
        this.name = name;
        return this;
    }

    public String secret() {
        return secret;
    }

    public Space secret(String secret) {
        this.secret = secret;
        return this;
    }

    public URL url() {
        return url != null ? url : urlOf(baseUrl);
    }

    public URL logo() {
        return null;
    }

    public URL authCallbackUrl() {
        try {
            return new URL(url().toString() + "/authenticate-callback");
        } catch (MalformedURLException e) {
            Logger.error(e);
            return null;
        }
    }

    private URL urlOf(String baseUrl) {
        try {
            return URI.create(baseUrl).toURL();
        } catch (MalformedURLException e) {
            Logger.error(e);
            return null;
        }
    }

}
