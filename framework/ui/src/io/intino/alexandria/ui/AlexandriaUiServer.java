package io.intino.alexandria.ui;

import io.intino.alexandria.http.AlexandriaHttpServer;
import io.intino.alexandria.ui.server.UIRouter;
import io.intino.alexandria.ui.services.AuthService;

public class AlexandriaUiServer extends AlexandriaHttpServer<UIRouter> {
	private AuthService authService;

	public AlexandriaUiServer(int port, AuthService authService) {
		this(port, WebDirectory, MaxResourceSize, authService);
	}

	public AlexandriaUiServer(int port, String webDirectory, long maxResourceSize, AuthService authService) {
		super(port, webDirectory, maxResourceSize);
		this.authService = authService;
	}

	public AuthService authService() {
		return authService;
	}

	public AlexandriaUiServer authService(AuthService authService) {
		this.authService = authService;
		return this;
	}

	public AlexandriaUiServer start() {
		return (AlexandriaUiServer) super.start();
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(super.createRouter(path), path, authService);
	}

}
