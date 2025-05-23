package io.intino.alexandria.ui;

import org.eclipse.jetty.websocket.api.Session;

public interface AlexandriaWebSocket {
	void onWebSocketConnect(Session session);
	void onWebSocketClose(Session session, int status, String reason);
	void onWebSocketText(Session session, String message);
	void onWebSocketBinary(Session session, byte[] data, int offset, int length);
	void onWebSocketError(Session session, Throwable error);
}
