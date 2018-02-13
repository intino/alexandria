package io.intino.konos.alexandria.activity.services.push;

import io.intino.konos.alexandria.activity.displays.Soul;
import io.intino.konos.alexandria.rest.spark.SparkClient;

public class ActivityClient<S extends Soul> extends SparkClient {
    private S soul;

    public ActivityClient(org.eclipse.jetty.websocket.api.Session session) {
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
