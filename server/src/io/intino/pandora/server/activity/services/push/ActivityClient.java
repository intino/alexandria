package io.intino.pandora.server.activity.services.push;

import io.intino.pandora.server.spark.SparkClient;
import io.intino.pandora.server.activity.displays.Soul;

public class ActivityClient<S extends Soul> extends SparkClient {
    private S soul;

    public ActivityClient(org.eclipse.jetty.websocket.api.Session session) {
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
