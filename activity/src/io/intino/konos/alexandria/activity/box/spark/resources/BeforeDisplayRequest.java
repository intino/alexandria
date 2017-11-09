package io.intino.konos.alexandria.activity.box.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.box.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.box.spark.ActivitySparkManager;

public class BeforeDisplayRequest implements io.intino.konos.alexandria.rest.Resource {
    private final ActivitySparkManager manager;

    public BeforeDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        String clientId = manager.fromQuery("clientId", String.class);
        ActivityClient client = manager.client(clientId);
        if (client != null)
            manager.linkToThread(client);
    }

}
