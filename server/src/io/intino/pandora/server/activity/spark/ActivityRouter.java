package io.intino.pandora.server.activity.spark;

import io.intino.pandora.server.spark.SparkRouter;
import io.intino.pandora.server.activity.pushservice.PushService;
import spark.Request;
import spark.Response;

public class ActivityRouter extends SparkRouter<ActivitySparkManager> {

    public ActivityRouter(String path) {
        super(path);
    }

    @Override
    protected ActivitySparkManager manager(Request rq, Response rs) {
        return new ActivitySparkManager(rq, rs, (PushService) pushService);
    }

}
