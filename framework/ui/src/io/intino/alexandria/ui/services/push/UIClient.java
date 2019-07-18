package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.rest.spark.SparkClient;
import io.intino.alexandria.ui.Soul;

public class UIClient<S extends Soul> extends SparkClient {
    private S soul;

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

}
