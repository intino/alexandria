package io.intino.alexandria.ui;

import io.intino.alexandria.rest.AlexandriaSpark;
import io.intino.alexandria.ui.spark.UIRouter;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.EditorService;

public class UISpark extends AlexandriaSpark<UIRouter> {
	private AuthService authService;
	private EditorService editorService;

	public UISpark(int port, AuthService authService, EditorService editorService) {
		this(port, WebDirectory, authService, editorService);
	}

	public UISpark(int port, String webDirectory, AuthService authService, EditorService editorService) {
		super(port, webDirectory);
		this.authService = authService;
		this.editorService = editorService;
	}

	public AuthService authService() {
		return authService;
	}

	public UISpark editorService(EditorService editorService) {
		this.editorService = editorService;
		return this;
	}

	public UISpark authService(AuthService authService) {
		this.authService = authService;
		return this;
	}

	public EditorService editorService() {
		return editorService;
	}

	public UISpark start() {
		return (UISpark) super.start();
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(service, path, authService, editorService);
	}

}
