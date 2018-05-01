package io.intino.konos.alexandria.ui.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.ui.spark.UISparkManager;

public class AfterDisplayRequest implements io.intino.konos.alexandria.rest.Resource {
    private final UISparkManager manager;

    public AfterDisplayRequest(UISparkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        manager.unlinkFromThread();
    }
}
