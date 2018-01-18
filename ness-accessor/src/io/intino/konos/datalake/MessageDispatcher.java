package io.intino.konos.datalake;

import io.intino.ness.inl.Message;

public interface MessageDispatcher {

	void dispatch(Message message);
}
