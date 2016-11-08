package io.intino.pandora.server.activity.spark.resources;

import io.intino.pandora.exceptions.PandoraException;
import io.intino.pandora.server.Resource;
import io.intino.pandora.server.activity.pushservice.ActivityClient;
import io.intino.pandora.server.activity.spark.ActivitySparkManager;

public class BeforeDisplayRequest implements Resource {
    private final ActivitySparkManager manager;

    public BeforeDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws PandoraException {
        String clientId = manager.fromQuery("clientId", String.class);
        ActivityClient client = manager.client(clientId);
        if (client != null)
            manager.linkToThread(client);
    }

}
