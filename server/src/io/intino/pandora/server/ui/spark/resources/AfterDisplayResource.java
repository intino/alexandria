package io.intino.pandora.server.ui.spark.resources;

import io.intino.pandora.exceptions.PandoraException;
import io.intino.pandora.server.Resource;
import io.intino.pandora.server.ui.spark.UISparkManager;

public class AfterDisplayResource implements Resource {
    private final UISparkManager manager;

    public AfterDisplayResource(UISparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws PandoraException {
        manager.unlinkFromThread();
    }
}
