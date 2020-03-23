package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.Resource;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.spark.UISparkManager;

public class BeforeDisplayRequest implements Resource {
    private final UISparkManager manager;

    public BeforeDisplayRequest(UISparkManager manager) {
        this.manager = manager;
    }

    public void execute(UIClient client) throws AlexandriaException {
        if (client == null) return;
        manager.linkToThread(client);
    }

    @Override
    public void execute() throws AlexandriaException {
        String clientId = manager.fromQuery("clientId", String.class);
        execute(manager.client(clientId));
    }

}
