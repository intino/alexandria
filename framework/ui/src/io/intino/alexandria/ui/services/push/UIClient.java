package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.http.spark.SparkClient;
import io.intino.alexandria.ui.Soul;

import java.util.Map;

public class UIClient<S extends Soul> extends SparkClient {
    private S soul;
    private Map<String, String> cookies;

    public UIClient(org.eclipse.jetty.websocket.api.Session session) {
        super(session);
    }

    public S soul() {
        return this.soul;
    }

    public void soul(S soul) {
        this.soul = soul;
        this.soul.personify();
    }

    public void cookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String cookie(String name) {
        return cookies.getOrDefault(name, null);
    }
}
