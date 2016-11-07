package io.intino.pandora.server.ui.spark.resources;

import io.intino.pandora.exceptions.PandoraException;
import io.intino.pandora.server.Resource;
import io.intino.pandora.server.ui.pushservice.UIClient;
import io.intino.pandora.server.ui.spark.UISparkManager;

public class BeforeDisplayResource implements Resource {
    private final UISparkManager manager;

    public BeforeDisplayResource(UISparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws PandoraException {
        String clientId = manager.fromQuery("clientId", String.class);
        UIClient client = manager.client(clientId);
        if (client != null)
            manager.linkToThread(client);
    }

}
