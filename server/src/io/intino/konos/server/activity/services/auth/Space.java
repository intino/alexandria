package io.intino.konos.server.activity.services.auth;

import java.net.MalformedURLException;
import java.net.URL;

public class Space {
    private final URL authServiceUrl;
    private final String authCallbackPath = "/authenticate-callback";
    private String baseUrl;
    private String authId;

    public Space(URL authServiceUrl) {
        this.authServiceUrl = authServiceUrl;
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String name() {
        return "space";
    }

    public String title() {
        return "";
    }

    public String secret() {
        return "1234";
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
            return new URL(home().toString() + authCallbackPath + "?authId=" + authId);
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
