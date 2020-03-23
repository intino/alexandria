package io.intino.alexandria.wss.pushservice;

import io.intino.alexandria.wss.Client;

import java.util.List;

public interface ClientProvider<C extends Client> {
	List<C> clients(String sessionId);
	C client(String id);
	C client();
}
