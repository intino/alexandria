package io.intino.alexandria.ui.server.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.Resource;
import io.intino.alexandria.ui.server.AlexandriaUiManager;
import io.intino.alexandria.ui.services.push.UIClient;

public class BeforeDisplayRequest implements Resource {
    private final AlexandriaUiManager manager;

    public BeforeDisplayRequest(AlexandriaUiManager manager) {
        this.manager = manager;
    }

    public void execute(UIClient client) throws AlexandriaException {
        if (client == null) return;
        manager.linkToThread(client);
    }

    @Override
    public void execute() throws AlexandriaException {
        String clientId = manager.fromQuery("clientId");
        execute(manager.client(clientId));
    }

}
