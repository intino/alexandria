package io.intino.pandora.server.ui.pushservice;

import io.intino.pandora.server.spark.SparkClient;
import io.intino.pandora.server.ui.displays.Soul;

public class UIClient<S extends Soul> extends SparkClient {
    private S soul;

    public UIClient(org.eclipse.jetty.websocket.api.Session session) {
        super(session);
    }

    public S soul() {
        return (S) this.soul;
    }

    public void soul(S soul) {
        this.soul = soul;
        this.soul.personify();
    }

}
