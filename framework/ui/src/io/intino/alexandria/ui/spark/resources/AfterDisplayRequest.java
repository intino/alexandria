package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.Resource;
import io.intino.alexandria.ui.spark.UISparkManager;

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
