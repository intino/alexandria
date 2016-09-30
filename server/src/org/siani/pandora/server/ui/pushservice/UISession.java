package org.siani.pandora.server.ui.pushservice;

import org.siani.pandora.server.pushservice.Client;
import org.siani.pandora.server.spark.SparkSession;

public class UISession extends SparkSession<UIClient> {
    private User user;
    private Browser browser;

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
