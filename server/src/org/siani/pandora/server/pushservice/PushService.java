package org.siani.pandora.server.pushservice;

import java.util.function.Consumer;

public interface PushService<S extends Session, C extends Client> extends SessionProvider {

	void onOpen(Consumer<C> client);

	Connection onMessage(String clientId, Consumer<Message> message);

	ClosedConnection onClose(String clientId);

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
