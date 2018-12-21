package io.intino.alexandria.ui.services.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Browser {
    private String baseUrl;
    private String basePath;
    private String homeUrl;
    private String userHomeUrl;
    private String language;
    private String metadataLanguage;
    private String metadataIpAddress;
    private int timezoneOffset = 0;
    private Map<String, String> preferences = new HashMap<>();

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

    public List<String> preferences() {
        return preferences.values().stream().collect(Collectors.toList());
    }

    public String preference(String name) {
        return preferences.get(name);
    }

    public Browser add(String preference, String value) {
        preferences.put(preference, value);
        return this;
    }
}
