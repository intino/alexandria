package io.intino.alexandria.http.pushservice;

import java.util.List;

public interface ClientProvider<C extends Client> {
	List<C> clients(String sessionId);
	C client(String id);
	C client();
}
