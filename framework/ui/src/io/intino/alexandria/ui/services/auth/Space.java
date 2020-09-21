package io.intino.alexandria.ui.services.auth;

import java.net.MalformedURLException;
import java.net.URL;

public class Space {
    private final URL authServiceUrl;
    private String baseUrl;
    private String authId;
    private String name = "space";
    private String title = "";
    private String secret = "1234";

    public Space(URL authServiceUrl) {
        this.authServiceUrl = authServiceUrl;
    }

    public Space setBaseUrl(String url) {
        baseUrl = url;
        return this;
    }

    public Space setAuthId(String authId) {
        this.authId = authId;
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
        try {
            return new URL(this.baseUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public URL logo() {
        return null;
    }

    public URL authCallbackUrl() {
        try {
            return new URL(home().toString() + "/authenticate-callback?authId=" + authId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public URL authServiceUrl() {
        return authServiceUrl;
    }

    public URL home() {
        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
