package io.intino.pandora.server.activity;

import io.intino.pandora.server.PandoraSpark;
import io.intino.pandora.server.activity.services.AuthService;
import io.intino.pandora.server.activity.spark.ActivityRouter;

public class ActivityPandoraSpark extends PandoraSpark<ActivityRouter> {
	private final AuthService authService;

	private static Setup setup;
	private static ActivityPandoraSpark instance;

	private ActivityPandoraSpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	private ActivityPandoraSpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	public static void setup(int port, String webDirectory, AuthService authService) {
		setup = new Setup(port, webDirectory, authService);
	}

	public static ActivityPandoraSpark instance() {
		if (instance == null)
			instance = new ActivityPandoraSpark(setup.port, setup.webDirectory, setup.authService);
		return instance;
	}

	@Override
	protected ActivityRouter createRouter(String path) {
		return new ActivityRouter(path, authService);
	}

	private static class Setup {
		int port;
		String webDirectory;
		AuthService authService;

		public Setup(int port, String webDirectory, AuthService authService) {
			this.port = port;
			this.webDirectory = webDirectory;
			this.authService = authService;
		}
	}

}
