package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.rest.spark.RequestAdapter;
import io.intino.alexandria.ui.displays.requesters.DisplayPushRequester;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

public class PushService extends io.intino.alexandria.rest.spark.PushService<UISession, UIClient> {
	private Map<String, DisplayPushRequester> requesterMap = new HashMap<>();

	@Override
	public UISession createSession(String id) {
		return new UISession(id);
	}

	@Override
	public UIClient createClient(Session session) {
		return new UIClient(session);
	}

	@Override
	public void onMessage(UIClient client, String content) {
		UIMessage message = RequestAdapter.adaptFromJSON(content, UIMessage.class);
		String requester = message.sender();

		try {
			linkToThread(client);
			requesterMap.get(requester).execute(client, message);
			unlinkFromThread();
		}
		catch (Throwable e) {
			Logger.error(e);
		}
	}

	public PushService register(String type, DisplayPushRequester requester) {
		requesterMap.put(type, requester);
		return this;
	}

}
