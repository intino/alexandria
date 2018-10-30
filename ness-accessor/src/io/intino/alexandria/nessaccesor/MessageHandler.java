package io.intino.alexandria.nessaccesor;

import io.intino.ness.inl.Message;

public interface MessageHandler {
	void handle(Message message);
}