package io.intino.konos.datalake;

import io.intino.ness.inl.Message;

public interface MessageHandler {
	void handle(Message message);
}