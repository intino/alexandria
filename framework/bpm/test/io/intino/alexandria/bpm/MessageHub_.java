package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;

import java.util.ArrayList;
import java.util.List;

class MessageHub_ implements MessageHub {
	private List<OnMessageReceived> listeners = new ArrayList<>();

	@Override
	public void sendMessage(String channel, Message message) {
		new Thread(() -> listeners.forEach(l -> l.process(message))).start();
	}

	@Override
	public void registerListener(String channel, OnMessageReceived onMessageReceived) {
		listeners.add(onMessageReceived);
	}
}
