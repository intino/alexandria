package io.intino.alexandria.jms;


import javax.jms.Message;

public interface Consumer extends java.util.function.Consumer<Message> {

	void accept(Message message);



	default String typeOf(String text) {
		return text.contains("\n") ? text.substring(0, text.indexOf("\n")).replace("[", "").replace("]", "") : text;
	}
}
