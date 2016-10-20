package io.intino.pandora.jms;

import javax.jms.Message;

public interface Consumer {
	void consume(Message message);

}
