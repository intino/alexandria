package io.intino.pandora.server.activity.spark.resources;

import io.intino.pandora.exceptions.PandoraException;
import io.intino.pandora.server.Resource;
import io.intino.pandora.server.activity.spark.ActivitySparkManager;

public class AfterDisplayRequest implements Resource {
    private final ActivitySparkManager manager;

    public AfterDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws PandoraException {
        manager.unlinkFromThread();
    }
}
