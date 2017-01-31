package io.intino.konos.server.activity.spark;

import io.intino.konos.server.activity.services.AuthService;
import io.intino.konos.server.activity.services.push.PushService;
import io.intino.konos.server.spark.SparkRouter;
import spark.Request;
import spark.Response;

public class ActivityRouter extends SparkRouter<ActivitySparkManager> {
    protected final AuthService authService;

    public ActivityRouter(String path, AuthService authService) {
        super(path);
        this.authService = authService;
    }

    @Override
    protected ActivitySparkManager manager(Request rq, Response rs) {
        return new ActivitySparkManager(rq, rs, (PushService)pushService, authService);
    }

}
