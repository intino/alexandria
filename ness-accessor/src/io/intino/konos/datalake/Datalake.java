package io.intino.konos.datalake;

import io.intino.konos.jms.TopicConsumer;
import io.intino.ness.inl.Message;

import java.util.List;

public interface Datalake {
	String REFLOW_PATH = "service.ness.reflow";
	String FLOW_PATH = "flow.ness.reflow";
	String REGISTER_ONLY = "registerOnly";

	ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher);

	void commit();

	void add(String tank);

	void disconnect();

	void connect(String... args);

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

		void feed(io.intino.ness.inl.Message... message);

		void put(io.intino.ness.inl.Message... message);

		Tank batchSession(int blockSize);

		Tank endBatch();

		TopicConsumer flow(String flowID);

		void unregister();
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
