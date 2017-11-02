package io.intino.alexandria.foundation.activity.services.push;

import io.intino.alexandria.foundation.pushservice.Client;
import io.intino.alexandria.foundation.spark.SparkSession;

public class ActivitySession<C extends ActivityClient> extends SparkSession<C> {
    private User user;
    private Browser browser;

    public ActivitySession(String id) {
        super(id);
        this.browser = new Browser();
    }

    public User user() {
        return this.user;
    }

    public void user(User user) {
        this.user = user;
    }

    public Browser browser() {
        return this.browser;
    }

    public void browser(Browser browser) {
        this.browser = browser;
    }

    public String discoverLanguage() {
        String language = browser.language();

        if (language != null)
            return language;

        Client client = currentClient();
        if (client != null && client.language() != null)
            return client.language();

        return user() != null ? user().language() : browser.languageFromMetadata();
    }

}
