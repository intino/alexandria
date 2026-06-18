package io.intino.alexandria.ui;

import io.intino.alexandria.http.AlexandriaHttpServer;
import io.intino.alexandria.ui.server.UIRouter;
import io.intino.alexandria.ui.services.AuthService;
import io.javalin.websocket.WsHandlerType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		service.unsafe.routes.addWsHandler(WsHandlerType.WEBSOCKET, adapt(path), config -> {
			config.onConnect(e -> socket.onWebSocketConnect(e.session));
			config.onClose(e -> socket.onWebSocketClose(e.session, e.status(), e.reason()));
			config.onMessage(e -> socket.onWebSocketText(e.session, e.message()));
			config.onBinaryMessage(e -> {
				byte[] bytes = new byte[e.data().remaining()];
				e.data().duplicate().get(bytes);
				socket.onWebSocketBinary(e.session, bytes, 0, bytes.length);
			});
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

	private static String adapt(String path) {
		String regex = ":(\\w+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(path);
		return matcher.replaceAll("{$1}");
	}

}
