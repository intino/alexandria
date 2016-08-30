package teseo.framework.web.services;

import teseo.framework.core.Client;
import teseo.framework.core.Session;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserService implements teseo.framework.services.BrowserService {
	private final Map<String, Session> sessionMap = new HashMap<>();
	private final Map<Long, Session> linkSessionMap = new HashMap<>();
	private final Map<String, Client> clientMap = new HashMap<>();
	private final Map<Long, Client> linkClientMap = new HashMap<>();
	private URL baseUrl;

	@Override
	public URL baseUrl() {
		return baseUrl;
	}

	@Override
	public URL baseResourceUrl() {
		try {
			return new URL(baseUrl.toString() + "/resource");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void url(URL url) {
		this.baseUrl = url;
	}

	@Override
	public void registerClient(Client client) {
		registerSession(client);
		clientMap.put(client.id(), client);
	}

	@Override
	public void unRegisterClient(Client client) {
		removeFromSession(client);
		clientMap.remove(client.id());
	}

	@Override
	public boolean existsClient(String id) {
		return clientMap.containsKey(id);
	}

	@Override
	public <T extends Client> T getClient(String id) {
		return (T) clientMap.get(id);
	}

	@Override
	public <T extends Client> T currentClient() {
		return (T) linkClientMap.get(Thread.currentThread().getId());
	}

	@Override
	public <T extends Session> T getSession(String id) {
		return (T) sessionMap.get(id);
	}

	@Override
	public <T extends Session> T currentSession() {
		return (T) linkSessionMap.get(Thread.currentThread().getId());
	}

	@Override
	public void linkToThread(Client client) {
		long id = Thread.currentThread().getId();
		linkClientMap.put(id, client);
		linkSessionMap.put(id, sessionOf(client));
	}

	@Override
	public void unlinkFromThread() {
		long id = Thread.currentThread().getId();
		linkClientMap.remove(id);
		linkSessionMap.remove(id);
	}

	protected void registerSession(Client client) {
		sessionMap.putIfAbsent(client.sessionId(), createSession(client));
		addToSession(client);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Session> T createSession(Client client) {
		return (T) new Session(client.sessionId());
	}

	private Session sessionOf(Client client) {
		return sessionMap.get(client.sessionId());
	}

	private void addToSession(Client client) {
		sessionOf(client).add(client);
	}

	private void removeFromSession(Client client) {
		sessionOf(client).remove(client);
	}


}
