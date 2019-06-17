package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.rest.pushservice.Client;
import io.intino.alexandria.rest.spark.SparkSession;
import io.intino.alexandria.ui.services.auth.Token;

public class UISession extends SparkSession<UIClient> {
    private User user;
    private Browser browser;
    private String device = null;
    private Token token = null;

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
        if (user != null) return user.language();

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
    }

    public void token(Token token) {
        this.token = token;
    }

    public Token token() {
        return this.token;
    }
}
