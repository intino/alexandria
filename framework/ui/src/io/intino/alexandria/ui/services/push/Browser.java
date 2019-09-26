package io.intino.alexandria.ui.services.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Browser {
    private String baseUrl;
    private String basePath;
    private String homeUrl;
    private String userHomeUrl;
    private String requestUrl;
    private String language;
    private String metadataLanguage;
    private String metadataIpAddress;
    private int timezoneOffset = 0;
    private Map<String, Object> preferences = new HashMap<>();
    private Consumer<String> redirectManager = null;

    private static final String PushPath = "/push?id=%s&currentSession=%s&language=%s";

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
        String result = url.replace("https", "wss").replace("http", "ws");
        result += String.format(PushPath, clientId, sessionId, language);
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

    public int timezoneOffset() {
        return timezoneOffset;
    }

    public void timezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public List<Object> preferences() {
        return preferences.values().stream().collect(Collectors.toList());
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
}
