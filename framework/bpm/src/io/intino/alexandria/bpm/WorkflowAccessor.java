package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;

import java.util.function.Consumer;

public interface WorkflowAccessor {

	public <T> Class<T> outputOf(String task);

	public Class inputOf(String task);

	void trigger(String name, String callback, Object data);

	void subscribe(Consumer<Message> consumer);
}
