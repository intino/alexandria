package teseo.framework.web;

import teseo.framework.core.Client;
import teseo.framework.core.Message;
import teseo.framework.core.Session;
import teseo.framework.services.PushService;
import teseo.framework.displays.MessageCarrier;

import java.util.Map;

public class WebMessageCarrier implements MessageCarrier {
	private final PushService pushService;

	public WebMessageCarrier(PushService pushService) {
		this.pushService = pushService;
	}

	@Override
	public void notifyAll(String message, Map<String, Object> parameters) {
		pushService.pushBroadcast(new Message(message, parameters));
	}

	@Override
	public void notify(Client client, String message, Map<String, Object> parameters) {
		pushService.pushToClient(client, new Message(message, parameters));
	}

	@Override
	public void notify(Session session, String message, Map<String, Object> parameters) {
		pushService.pushToSession(session, new Message(message, parameters));
	}

}
