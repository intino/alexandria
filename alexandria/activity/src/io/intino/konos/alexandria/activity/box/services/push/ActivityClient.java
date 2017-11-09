package io.intino.konos.alexandria.activity.box.services.push;

import io.intino.konos.alexandria.activity.box.displays.Soul;
import io.intino.konos.alexandria.rest.spark.SparkClient;

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
