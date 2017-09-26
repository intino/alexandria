package io.intino.konos.server.activity.spark;

import io.intino.konos.server.activity.services.AuthService;
import io.intino.konos.server.activity.services.push.PushService;
import io.intino.konos.server.spark.SparkRouter;
import spark.Request;
import spark.Response;

public class ActivityRouter extends SparkRouter<ActivitySparkManager> {
    protected final AuthService authService;
    private static boolean hasUserHome = false;

    public ActivityRouter(String path, AuthService authService) {
        super(path);
        if (isUserHomePath(path)) hasUserHome = true;
        this.authService = authService;
    }

    private boolean isUserHomePath(String path) {
        return path.contains(ActivitySparkManager.KonosUserHomePath);
    }

    @Override
    protected ActivitySparkManager manager(Request rq, Response rs) {
        return new ActivitySparkManager(rq, rs, (PushService)pushService, authService, hasUserHome);
    }

}
