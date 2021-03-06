package io.intino.alexandria.http.pushservice;

import java.util.function.Consumer;
import java.util.function.Function;

public interface PushService<S extends Session<C>, C extends Client> extends SessionProvider<S, C> {

	void onOpen(Function<C, Boolean> client);

	Connection onMessage(String clientId, Consumer<String> message);

	ClosedConnection onClose(String clientId);

	ClosedConnection onCloseScheduled(String clientId);

	void pushBroadcast(String message);

	void pushToSession(S session, String message);

	void pushToClient(C client, String message);

	interface OpenConnectionListener<C extends Client> {
		void onOpen(C client);
	}

	interface MessageListener {
		void onMessage(String message);
	}

	interface ClosedConnection<C extends Client> {
		void execute(Consumer<C> client);
	}

	interface Connection {
		void unRegister();
	}

}
