package io.intino.alexandria.foundation.activity.spark;

import io.intino.alexandria.foundation.activity.services.AuthService;
import io.intino.alexandria.foundation.activity.services.push.PushService;
import io.intino.alexandria.foundation.spark.SparkRouter;
import spark.Request;
import spark.Response;
import spark.Service;

public class ActivityRouter extends SparkRouter<ActivitySparkManager> {
	protected final AuthService authService;
	private static boolean hasUserHome = false;

	public ActivityRouter(Service service, String path, AuthService authService) {
		super(service, path);
		if (isUserHomePath(path)) hasUserHome = true;
		this.authService = authService;
	}

	private boolean isUserHomePath(String path) {
		return path.contains(ActivitySparkManager.KonosUserHomePath);
	}

	@Override
	protected ActivitySparkManager manager(Request rq, Response rs) {
		return new ActivitySparkManager(rq, rs, (PushService) pushService, authService, hasUserHome);
	}

}
