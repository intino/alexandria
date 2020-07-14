package io.intino.alexandria.ui.services.push;

import io.intino.alexandria.Json;
import io.intino.alexandria.http.pushservice.Client;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.requesters.DisplayPushRequester;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.websocket.api.Session;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PushService extends io.intino.alexandria.http.spark.PushService<UISession, UIClient> {
	private Map<String, DisplayPushRequester> requesterMap = new HashMap<>();
	private static final Map<String, Consumer<Long>> linkedListeners = new HashMap<>();

	@Override
	public UISession createSession(String id) {
		return new UISession(id);
	}

	@Override
	public UIClient createClient(Session session) {
		return new UIClient(session);
	}

	@Override
	public void linkToThread(UIClient client) {
		super.linkToThread(client);
		if (linkedListeners.containsKey(client.id())) linkedListeners.get(client.id()).accept(Thread.currentThread().getId());
	}

	@Override
	public void onMessage(UIClient client, String content) {
		UIMessage message = Json.fromString(decode(content), UIMessage.class);
		String requester = message.sender();

		if (requester == null) {
			super.onMessage(client, content);
			return;
		}

		try {
			linkToThread(client);
			requesterMap.get(requester).execute(client, message);
			unlinkFromThread();
		}
		catch (Throwable e) {
			if (e instanceof EofException) Logger.debug(e.getMessage());
			else Logger.error(e);
		}
	}

	@Override
	public void onClose(UIClient client) {
		super.onClose(client);
		linkedListeners.remove(client.id());
	}

	public void onLinkedToThread(Client client, Consumer<Long> listener) {
		linkedListeners.put(client.id(), listener);
	}

	public PushService register(String type, DisplayPushRequester requester) {
		requesterMap.put(type, requester);
		return this;
	}

	private static String decode(String object) {
		try {
			return URLDecoder.decode(object, StandardCharsets.UTF_8);
		} catch (IllegalArgumentException ex) {
			return object;
		}
	}

}
