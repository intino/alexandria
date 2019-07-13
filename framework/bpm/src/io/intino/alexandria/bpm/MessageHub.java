package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;

public interface MessageHub {

	void sendMessage(String channel, Message message);

	void registerListener(String channel, OnMessageReceived onMessageReceived);

	interface OnMessageReceived {
		void process(Message message);
	}
}
