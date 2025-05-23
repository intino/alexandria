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

	public <T> void registerWs(String path, AlexandriaWebSocket socket) {
		init();
		service.ws(path, config -> {
			config.onConnect(e -> socket.onWebSocketConnect(e.session));
			config.onClose(e -> socket.onWebSocketClose(e.session, e.status(), e.reason()));
			config.onMessage(e -> socket.onWebSocketText(e.session, e.message()));
			config.onBinaryMessage(e -> socket.onWebSocketBinary(e.session, e.data(), e.offset(), e.length()));
			config.onError(e -> socket.onWebSocketError(e.session, e.error()));
		});
	}

	public AlexandriaUiServer start() {
		return (AlexandriaUiServer) super.start();
	}

	@Override
	protected UIRouter createRouter(String path) {
		return new UIRouter(super.createRouter(path), path, authService);
	}

}
