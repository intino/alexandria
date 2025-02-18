package io.intino.alexandria.ui.server.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.Resource;
import io.intino.alexandria.ui.server.AlexandriaUiManager;

public class AfterDisplayRequest implements Resource {
    private final AlexandriaUiManager manager;

    public AfterDisplayRequest(AlexandriaUiManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() throws AlexandriaException {
        manager.unlinkFromThread();
    }

}
