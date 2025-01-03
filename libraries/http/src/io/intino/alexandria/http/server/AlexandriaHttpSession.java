package io.intino.alexandria.http.server;

import io.intino.alexandria.http.pushservice.Client;
import io.intino.alexandria.http.pushservice.ClientProvider;
import io.intino.alexandria.http.pushservice.Session;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaHttpSession<C extends Client> implements Session<C> {
	private final String id;
	private ClientProvider<C> clientProvider;
	private Function<String, String> loginListener;
	private Consumer<Boolean> logoutListener;

	public AlexandriaHttpSession(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	public List<C> clients() {
		return clientProvider.clients(id);
	}

	public int timezoneOffset() {
		List<C> clients = clients();
		return !clients.isEmpty() ? clients.get(0).timezoneOffset() : 0;
	}

	@Override
	public C client(String id) {
		return clientProvider.client(id);
	}

	@Override
	public C client() {
		return clientProvider.client();
	}

	@Override
	public void clientProvider(ClientProvider<C> clientProvider) {
		this.clientProvider = clientProvider;
	}

	public void send(String message) {
		clients().forEach(client -> client.send(message));
	}

	public void whenLogin(Function<String, String> listener) {
		this.loginListener = listener;
	}

	public void whenLogout(Consumer<Boolean> listener) {
		this.logoutListener = listener;
	}

	@Override
	public String login(String baseUrl) {
		if (loginListener != null)
			return loginListener.apply(baseUrl);
		return null;
	}

	@Override
	public void logout() {
		if (logoutListener != null)
			logoutListener.accept(true);
	}

}
