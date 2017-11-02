package io.intino.alexandria.foundation.activity.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.foundation.activity.spark.ActivitySparkManager;

public class AfterDisplayRequest implements io.intino.alexandria.foundation.Resource {
    private final ActivitySparkManager manager;

    public AfterDisplayRequest(ActivitySparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        manager.unlinkFromThread();
    }
}
