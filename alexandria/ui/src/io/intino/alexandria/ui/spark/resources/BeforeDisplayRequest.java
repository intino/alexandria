package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.rest.Resource;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.ui.services.push.UIClient;

public class BeforeDisplayRequest implements Resource {
    private final UISparkManager manager;

    public BeforeDisplayRequest(UISparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        String clientId = manager.fromQuery("clientId", String.class);
        UIClient client = manager.client(clientId);
        if (client != null)
            manager.linkToThread(client);
    }

}
