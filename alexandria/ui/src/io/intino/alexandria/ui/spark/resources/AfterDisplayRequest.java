package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.rest.Resource;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.exceptions.AlexandriaException;

public class AfterDisplayRequest implements Resource {
    private final UISparkManager manager;

    public AfterDisplayRequest(UISparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        manager.unlinkFromThread();
    }
}
