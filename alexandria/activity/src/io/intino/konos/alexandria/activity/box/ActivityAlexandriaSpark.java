package io.intino.konos.alexandria.activity.box;

import io.intino.konos.alexandria.rest.AlexandriaSpark;
import io.intino.konos.alexandria.activity.box.services.AuthService;
import io.intino.konos.alexandria.activity.box.spark.ActivityRouter;

public class ActivityAlexandriaSpark extends AlexandriaSpark<ActivityRouter> {
	private final AuthService authService;

	private static Setup setup;
	private static ActivityAlexandriaSpark instance;

	public ActivityAlexandriaSpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	public ActivityAlexandriaSpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	public static void setup(int port, String webDirectory, AuthService authService) {
		setup = new Setup(port, webDirectory, authService);
	}

	public static ActivityAlexandriaSpark instance() {
		if (instance == null)
			instance = new ActivityAlexandriaSpark(setup.port, setup.webDirectory, setup.authService);
		return instance;
	}

	public ActivityAlexandriaSpark start() {
		return (ActivityAlexandriaSpark) super.start();
	}

	public void stop() {
		if (service != null) service.stop();
		service = null;
		instance = null;
	}

	@Override
	protected ActivityRouter createRouter(String path) {
		return new ActivityRouter(service, path, authService);
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
