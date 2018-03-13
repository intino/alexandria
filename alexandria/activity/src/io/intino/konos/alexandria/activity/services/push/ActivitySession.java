package io.intino.konos.alexandria.activity.services.push;

import io.intino.konos.alexandria.rest.pushservice.Client;
import io.intino.konos.alexandria.rest.spark.SparkSession;

public class ActivitySession extends SparkSession<ActivityClient> {
    private User user;
    private Browser browser;

    public ActivitySession(String id) {
        super(id);
        this.browser = new Browser();


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

    public String discoverLanguage() {
        String language = browser.language();

        if (language != null)
            return language;

        Client client = client();
        if (client != null && client.language() != null)
            return client.language();

        return user() != null ? user().language() : browser.languageFromMetadata();
    }

}
