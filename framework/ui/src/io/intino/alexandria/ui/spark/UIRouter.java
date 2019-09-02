package io.intino.alexandria.ui.spark;

import io.intino.alexandria.rest.spark.SparkRouter;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.push.PushService;
import spark.Request;
import spark.Response;
import spark.Service;

public class UIRouter extends SparkRouter<UISparkManager> {
	protected final AuthService authService;
	private static boolean hasUserHome = false;

	public UIRouter(Service service, String path, AuthService authService) {
		super(service, path);
		if (isUserHomePath(path)) hasUserHome = true;
		this.authService = authService;
	}

	private boolean isUserHomePath(String path) {
		return path.contains(UISparkManager.KonosUserHomePath);
	}

	@Override
	protected UISparkManager manager(Request rq, Response rs) {
		return new UISparkManager(rq, rs, (PushService) pushService, authService, hasUserHome);
	}

}
