package io.intino.alexandria.restaccessor.core;

import io.intino.alexandria.logger.Logger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

@ClientEndpoint
public class RestAccessorNotifier implements io.intino.alexandria.restaccessor.RestAccessorNotifier {
	private String webSocketUri;
	private Session session;
	private Consumer<String> listener;

	@Override
	public void listen(Consumer<String> listener, String webSocketUri) {
		this.webSocketUri = webSocketUri;
		this.listener = listener;
		this.connect();
	}

	public void close() {
		try {
			session.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message) {
		listener.accept(message);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		this.session = null;
	}

	public void sendMessage(String message) {
		if (session == null) return;
		session.getAsyncRemote().sendText(message);
	}

	private void connect() {
		if (session != null) return;
		try {
			this.session = ContainerProvider.getWebSocketContainer().connectToServer(this, new URI(webSocketUri));
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}