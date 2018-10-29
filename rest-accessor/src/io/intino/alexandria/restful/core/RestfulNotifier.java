package io.intino.alexandria.restful.core;

import javax.websocket.*;
import java.net.URI;
import java.util.function.Consumer;

@ClientEndpoint
public class RestfulNotifier implements io.intino.alexandria.restful.RestfulNotifier {
	private String webSocketUri;
	private Session session;
	private Consumer<String> listener;

	@Override
	public void listen(Consumer<String> listener, String webSocketUri) {
		this.webSocketUri = webSocketUri;
		this.listener = listener;
		this.connect();
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
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(webSocketUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
