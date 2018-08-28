package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.rest.AlexandriaSpark;
import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.EditorService;
import io.intino.konos.alexandria.ui.spark.UIRouter;

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

	public UIAlexandriaSpark editorService(io.intino.konos.alexandria.ui.services.EditorService editorService) {
		this.editorService = editorService;
		return this;
	}

	public UIAlexandriaSpark authService(io.intino.konos.alexandria.ui.services.AuthService authService) {
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
