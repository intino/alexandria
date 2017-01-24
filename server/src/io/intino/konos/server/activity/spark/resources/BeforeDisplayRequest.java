package io.intino.konos.server.activity.spark.resources;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.Resource;
import io.intino.konos.server.activity.services.push.ActivityClient;
import io.intino.konos.server.activity.spark.ActivitySparkManager;

public class BeforeDisplayRequest implements Resource {
    private final ActivitySparkManager manager;

    public BeforeDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws KonosException {
        String clientId = manager.fromQuery("clientId", String.class);
        ActivityClient client = manager.client(clientId);
        if (client != null)
            manager.linkToThread(client);
    }

}
