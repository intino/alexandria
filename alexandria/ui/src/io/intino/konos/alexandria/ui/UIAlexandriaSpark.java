package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.rest.AlexandriaSpark;
import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.spark.UIRouter;

public class UIAlexandriaSpark extends AlexandriaSpark<UIRouter> {
	private final AuthService authService;

	private static Setup setup;
	private static UIAlexandriaSpark instance;

	public UIAlexandriaSpark(int port, AuthService authService) {
		this(port, WebDirectory, authService);
	}

	public UIAlexandriaSpark(int port, String webDirectory, AuthService authService) {
		super(port, webDirectory);
		this.authService = authService;
	}

	public static void setup(int port, String webDirectory, AuthService authService) {
		setup = new Setup(port, webDirectory, authService);
	}

	public static UIAlexandriaSpark instance() {
		if (instance == null)
			instance = new UIAlexandriaSpark(setup.port, setup.webDirectory, setup.authService);
		return instance;
	}

	public UIAlexandriaSpark start() {
		return (UIAlexandriaSpark) super.start();
	}

	public void stop() {
		if (service != null) service.stop();
		service = null;
		instance = null;
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(service, path, authService);
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
