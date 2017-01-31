package io.intino.konos.server.activity;

import io.intino.konos.server.KonosSpark;
import io.intino.konos.server.activity.services.AuthService;
import io.intino.konos.server.activity.spark.ActivityRouter;

public class ActivityKonosSpark extends KonosSpark<ActivityRouter> {
	private final AuthService authService;

	private static Setup setup;
	private static ActivityKonosSpark instance;

	private ActivityKonosSpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	private ActivityKonosSpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	public static void setup(int port, String webDirectory, AuthService authService) {
		setup = new Setup(port, webDirectory, authService);
	}

	public static ActivityKonosSpark instance() {
		if (instance == null)
			instance = new ActivityKonosSpark(setup.port, setup.webDirectory, setup.authService);
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
