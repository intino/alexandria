package io.intino.alexandria.ui.services.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Browser {
    private String baseUrl;
    private String basePath;
    private String homeUrl;
    private String userHomeUrl;
    private String requestUrl;
    private String language;
    private String metadataLanguage;
    private String metadataIpAddress;
    private final Map<String, Object> preferences = new HashMap<>();
    private Consumer<String> redirectManager = null;
    private Origin origin;

    public enum Origin { Mobile, Web }

    public static final String PushPath = "/_alexandria/push";

    public String baseUrl() {
        return baseUrl;
    }

    public void baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String basePath() {
        return basePath;
    }

    public void basePath(String basePath) {
        this.basePath = basePath;
    }

    public String homeUrl() {
        return homeUrl;
    }

    public void homeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String userHomeUrl() {
        return userHomeUrl;
    }

    public void userHomeUrl(String userHomeUrl) {
        this.userHomeUrl = userHomeUrl;
    }

    public void requestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String requestUrl() {
        return this.requestUrl;
    }

    public String baseAssetUrl() {
        return this.baseUrl.toString() + "/asset";
    }

    public String pushUrl(String sessionId, String clientId, String language) {
        return pushUrl(sessionId, clientId, language, baseUrl());
    }

    public String pushUrl(String sessionId, String clientId, String language, String url) {
        return pushUrl(sessionId, clientId, language, url, PushPath);
    }

    public String pushUrl(String sessionId, String clientId, String language, String url, String path) {
        String result = url.replace("https", "wss").replace("http", "ws");
        result += String.format(path + "?id=%s&currentSession=%s&language=%s", clientId, sessionId, language);
        return result;
    }

    public String language() {
        return language != null ? language : languageFromMetadata();
    }

    public void language(String language) {
        this.language = language;
    }

    public String languageFromMetadata() {
        return metadataLanguage;
    }

    public void metadataLanguage(String language) {
        this.metadataLanguage = language;
    }

    public String ipAddressFromMetadata() {
        return metadataIpAddress;
    }

    public void metadataIpAddress(String ipAddress) {
        this.metadataIpAddress = ipAddress;
    }

    public List<Object> preferences() {
        return new ArrayList<>(preferences.values());
    }

    public <T> T preference(String name) {
        return (T) preferences.get(name);
    }

    public void onRedirect(Consumer<String> redirectManager) {
        this.redirectManager = redirectManager;
    }

    public void redirect(String location) {
        if (redirectManager != null) redirectManager.accept(location);
    }

    public Browser add(String preference, Object value) {
        preferences.put(preference, value);
        return this;
    }

    public void origin(Origin origin) {
        this.origin = origin;
    }

    public boolean isMobile() {
        return origin == Origin.Mobile;
    }

    public boolean isWeb() {
        return origin == Origin.Web;
    }
}
