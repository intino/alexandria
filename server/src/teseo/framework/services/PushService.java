package teseo.framework.services;

import teseo.framework.core.Client;
import teseo.framework.core.Message;
import teseo.framework.core.Session;

import java.util.function.Consumer;

public interface PushService extends Service {

	String queryString(String sessionId, String clientId, String language);

	void onOpen(Consumer<Client> consumer);

	Connection onMessage(String clientId, Consumer<Message> listener);

	ClosedConnection onClose(String clientId);

	void pushBroadcast(Message message);

	void pushToSession(Session session, Message message);

	void pushToClient(Client client, Message message);

	interface OpenConnectionListener {
		void onOpen(Client client);
	}

	interface MessageListener {
		void onMessage(Message message);
	}

	interface ClosedConnection {
		void execute(Consumer<Client> client);
	}

	interface Connection {
		void unRegister();
	}

}
