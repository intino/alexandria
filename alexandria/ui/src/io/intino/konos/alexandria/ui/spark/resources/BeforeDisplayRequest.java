package io.intino.konos.alexandria.ui.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.ui.services.push.UIClient;
import io.intino.konos.alexandria.ui.spark.UISparkManager;

public class BeforeDisplayRequest implements io.intino.konos.alexandria.rest.Resource {
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
