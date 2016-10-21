package io.intino.pandora.server.ui.spark;

import io.intino.pandora.server.spark.SparkRouter;
import io.intino.pandora.server.ui.pushservice.PushService;
import spark.Request;
import spark.Response;

public class UIRouter extends SparkRouter<UISparkManager> {

    public UIRouter(String path) {
        super(path);
    }

    @Override
    protected UISparkManager manager(Request rq, Response rs) {
        return new UISparkManager(rq, rs, (PushService) pushService);
    }

}
