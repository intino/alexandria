package io.intino.pandora.server.activity;

import io.intino.pandora.server.PandoraSpark;
import io.intino.pandora.server.activity.services.AuthService;
import io.intino.pandora.server.activity.spark.ActivityRouter;

public class ActivityPandoraSpark extends PandoraSpark<ActivityRouter> {
	private final AuthService authService;

	public ActivityPandoraSpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	public ActivityPandoraSpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	@Override
	protected ActivityRouter createRouter(String path) {
		return new ActivityRouter(path, authService);
	}

}
