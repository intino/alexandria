package io.intino.alexandria.rest.pushservice;

import java.util.function.Consumer;
import java.util.function.Function;

public interface PushService<S extends Session<C>, C extends Client> extends SessionProvider<S, C> {

	void onOpen(Function<C, Boolean> client);

	Connection onMessage(String clientId, Consumer<Message> message);

	ClosedConnection onClose(String clientId);

	ClosedConnection onCloseScheduled(String clientId);

	void pushBroadcast(Message message);

	void pushToSession(S session, Message message);

	void pushToClient(C client, Message message);

	interface OpenConnectionListener<C extends Client> {
		void onOpen(C client);
	}

	interface MessageListener {
		void onMessage(Message message);
	}

	interface ClosedConnection<C extends Client> {
		void execute(Consumer<C> client);
	}

	interface Connection {
		void unRegister();
	}

}
