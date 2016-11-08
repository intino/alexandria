package io.intino.pandora.server.activity.pushservice;

public class Browser {
    private String baseUrl;
    private String language;
    private String metadataLanguage;

    private static final String PushPath = "/push?id=%s&currentSession=%s&language=%s";

    public String baseUrl() {
        return baseUrl;
    }

    public void baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String baseAssetUrl() {
        return this.baseUrl.toString() + "/asset";
    }

    public String pushUrl(String sessionId, String clientId, String language) {
        String result = baseUrl().replace("https", "wss").replace("http", "ws");
        result += String.format(PushPath, clientId, sessionId, language);
        return result;
    }

    public String language() {
        return language;
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

}
