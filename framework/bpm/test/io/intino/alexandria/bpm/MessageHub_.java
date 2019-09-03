package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class MessageHub_ implements MessageHub {
	private List<Consumer<Message>> listeners = new ArrayList<>();

	@Override
	public void sendMessage(String channel, Message message) {
		new Thread(() -> listeners.forEach(l -> l.accept(message))).start();
	}

	@Override
	public void attachListener(String channel, Consumer<Message> onMessageReceived) {
		listeners.add(onMessageReceived);
	}

	@Override
	public void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived) {

	}

	@Override
	public void detachListeners(String channel) {

	}

	@Override
	public void attachRequestListener(String channel, RequestConsumer onMessageReceived) {

	}

	@Override
	public void requestResponse(String channel, String message, Consumer<String> onResponse) {

	}

}
