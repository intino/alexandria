package io.intino.konos.server.activity.spark.resources;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.Resource;
import io.intino.konos.server.activity.spark.ActivitySparkManager;

public class AfterDisplayRequest implements Resource {
    private final ActivitySparkManager manager;

    public AfterDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws KonosException {
        manager.unlinkFromThread();
    }
}
