package io.intino.alexandria.message;

import java.util.function.Consumer;

public interface MessageHub {


	void sendMessage(String channel, Message message);

	void attachListener(String channel, Consumer<Message> onMessageReceived);

	void detachListener(String channel);

}