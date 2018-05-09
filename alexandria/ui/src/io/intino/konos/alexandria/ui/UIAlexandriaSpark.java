package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.rest.AlexandriaSpark;
import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.EditorService;
import io.intino.konos.alexandria.ui.spark.UIRouter;

public class UIAlexandriaSpark extends AlexandriaSpark<UIRouter> {
	private final AuthService authService;
	private final EditorService editorService;

	private static Setup setup;
	private static UIAlexandriaSpark instance;

	public UIAlexandriaSpark(int port, AuthService authService, EditorService editorService) {
		this(port, WebDirectory, authService, editorService);
	}

	public UIAlexandriaSpark(int port, String webDirectory, AuthService authService, EditorService editorService) {
		super(port, webDirectory);
		this.authService = authService;
		this.editorService = editorService;
	}

	public static void setup(int port, String webDirectory, AuthService authService, EditorService editorService) {
		setup = new Setup(port, webDirectory, authService, editorService);
	}

	public static UIAlexandriaSpark instance() {
		if (instance == null)
			instance = new UIAlexandriaSpark(setup.port, setup.webDirectory, setup.authService, setup.editorService);
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
		return new UIRouter(service, path, authService, editorService);
	}

	private static class Setup {
		int port;
		String webDirectory;
		AuthService authService;
		EditorService editorService;

		public Setup(int port, String webDirectory, AuthService authService) {
			this(port, webDirectory, authService, null);
		}

		public Setup(int port, String webDirectory, AuthService authService, EditorService editorService) {
			this.port = port;
			this.webDirectory = webDirectory;
			this.authService = authService;
			this.editorService = editorService;
		}
	}

}
