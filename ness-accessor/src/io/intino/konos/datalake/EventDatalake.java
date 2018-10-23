package io.intino.konos.datalake;

import io.intino.konos.jms.TopicConsumer;
import io.intino.ness.inl.Message;

import java.util.List;

public interface EventDatalake {
	String REFLOW_PATH = "service.ness.reflow";
	String FLOW_PATH = "flow.ness.reflow";
	String REGISTER_ONLY = "registerOnly";

	ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher);

	BulkSession bulk();

	Tank add(String tank);

	void connect(String... args);

	void disconnect();

	boolean isConnected();

	List<User> users();

	interface Tank {
		Tank handler(MessageHandler handler);

		void handle(Message message);

		String name();

		default String flowChannel() {
			return "flow." + name();
		}

		default String putChannel() {
			return "put." + name();
		}

		default String feedChannel() {
			return "feed." + name();
		}

		boolean feed(io.intino.ness.inl.Message message);

		boolean put(io.intino.ness.inl.Message message);

		TopicConsumer flow(String flowID);

		Tank batchSession(int blockSize);

		Tank endBatch();

		void unregister();
	}

	interface BulkSession {
		void append(String tank, List<Message> messages);

		default void append(Tank tank, List<Message> messages) {
			append(tank.name(), messages);
		}

		void append(String tank, Message... messages);

		default void append(Tank tank, Message... messages) {
			append(tank.name(), messages);
		}

		void finish();
	}

	interface ReflowSession {
		void next();

		void finish();

		void play();

		void pause();
	}

	class User {
		String name;
		String password;

		public User(String name, String password) {
			this.name = name;
			this.password = password;
		}

		public String name() {
			return name;
		}

		public String password() {
			return password;
		}
	}
}
