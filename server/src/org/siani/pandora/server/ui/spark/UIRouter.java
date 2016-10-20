package org.siani.pandora.server.ui.spark;

import org.siani.pandora.server.spark.SparkRouter;
import org.siani.pandora.server.ui.pushservice.PushService;
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
