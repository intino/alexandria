package teseo.framework.services;

import teseo.framework.core.Client;
import teseo.framework.core.Session;

import java.net.URL;

public interface BrowserService extends Service {
	URL baseUrl();
	URL baseResourceUrl();
	void url(URL url);

	void registerClient(Client client);
	void unRegisterClient(Client client);
	boolean existsClient(String id);
	<T extends Client> T getClient(String id);
	<T extends Client> T currentClient();
	void linkToThread(Client client);
	void unlinkFromThread();

	<T extends Session> T getSession(String id);
	<T extends Session> T currentSession();

}
