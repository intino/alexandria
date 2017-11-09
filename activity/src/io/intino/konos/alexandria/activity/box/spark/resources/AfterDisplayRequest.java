package io.intino.konos.alexandria.activity.box.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.activity.box.spark.ActivitySparkManager;

public class AfterDisplayRequest implements io.intino.konos.alexandria.rest.Resource {
    private final ActivitySparkManager manager;

    public AfterDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        manager.unlinkFromThread();
    }
}
