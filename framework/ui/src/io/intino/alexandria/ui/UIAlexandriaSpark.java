package io.intino.alexandria.ui;

import io.intino.alexandria.rest.AlexandriaSpark;
import io.intino.alexandria.ui.spark.UIRouter;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.EditorService;

public class UIAlexandriaSpark extends AlexandriaSpark<UIRouter> {
	private AuthService authService;
	private EditorService editorService;

	public UIAlexandriaSpark(int port, AuthService authService, EditorService editorService) {
		this(port, WebDirectory, authService, editorService);
	}

	public UIAlexandriaSpark(int port, String webDirectory, AuthService authService, EditorService editorService) {
		super(port, webDirectory);
		this.authService = authService;
		this.editorService = editorService;
	}

	public AuthService authService() {
		return authService;
	}

	public UIAlexandriaSpark editorService(EditorService editorService) {
		this.editorService = editorService;
		return this;
	}

	public UIAlexandriaSpark authService(AuthService authService) {
		this.authService = authService;
		return this;
	}

	public EditorService editorService() {
		return editorService;
	}

	public UIAlexandriaSpark start() {
		return (UIAlexandriaSpark) super.start();
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(service, path, authService, editorService);
	}

}
