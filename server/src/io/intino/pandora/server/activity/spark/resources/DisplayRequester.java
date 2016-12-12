package io.intino.pandora.server.activity.spark.resources;

import io.intino.pandora.server.activity.displays.Display;
import io.intino.pandora.server.activity.displays.DisplayNotifierProvider;
import io.intino.pandora.server.activity.services.push.ActivityClient;
import io.intino.pandora.server.activity.spark.ActivitySparkManager;

public abstract class DisplayRequester extends Resource {

    public DisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
        super(manager, notifierProvider);
    }

    public <D extends Display> D display() {
        String displayId = manager.fromPath("displayId", String.class);
        ActivityClient client = manager.currentClient();
        return client.soul().get(displayId);
    }

    public String operation() {
        return manager.fromQuery("operation", String.class);
    }

}
