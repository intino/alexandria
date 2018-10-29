package io.intino.alexandria;


import io.intino.alexandria.inl.MessageToObject;
import io.intino.alexandria.inl.ObjectToMessage;
import io.intino.ness.inl.Loader;
import io.intino.ness.inl.Message;
import io.intino.ness.inl.MessageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Inl {

	public static Message toMessage(Object object) {
		return ObjectToMessage.toMessage(object);
	}

	public static <T> T fromMessage(Message object, Class<T> t) {
		return MessageToObject.fromMessage(object, t);
	}

	public static List<Message> load(String text) {
		ArrayList<Message> list = new ArrayList<>();
		Message message;
		try {
			MessageInputStream inputStream = Loader.Inl.of(new ByteArrayInputStream(text.getBytes()));
			while ((message = inputStream.next()) != null) list.add(message);
		} catch (IOException ignored) {
		}
		return list;
	}
}
