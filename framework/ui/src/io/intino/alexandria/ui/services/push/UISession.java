package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.http.pushservice.Client;
import io.intino.alexandria.http.pushservice.Session;
import io.intino.alexandria.http.spark.SparkSession;
import io.intino.alexandria.ui.services.auth.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UISession extends SparkSession<UIClient> {
    private User user;
    private Browser browser;
    private String device = null;
    private Token token = null;
    private Map<String, String> preferences = new HashMap<>();

    public UISession(String id) {
        super(id);
        this.browser = new Browser();
    }

    public User user() {
        return this.user;
    }

    public void user(User user) {
        this.user = user;
    }

    public boolean isLogged() {
        return user() != null;
    }

    public Browser browser() {
        return this.browser;
    }

    public void browser(Browser browser) {
        this.browser = browser;
    }

    public String device() {
        return device;
    }

    public UISession device(String device) {
        this.device = device;
        return this;
    }

    public boolean isMobile() {
        return device != null;
    }

    public String discoverLanguage() {
        User user = user();
        if (user != null && user.language() != null && !user.language().isEmpty()) return user.language();

        String language = browser.language();
        if (language != null) return language;

        Client client = client();
        if (client != null && client.language() != null)
            return client.language();

        return "en";
    }

    @Override
    public void logout() {
        super.logout();
        token(null);
        user(null);
    }

    public void token(Token token) {
        this.token = token;
    }

    public Token token() {
        return this.token;
    }

    public List<String> preferences() {
        return new ArrayList<>(preferences.values());
    }

    public String preference(String name) {
        return preferences.get(name);
    }

    public Session add(String preference, String value) {
        preferences.put(preference, value);
        return this;
    }

}
