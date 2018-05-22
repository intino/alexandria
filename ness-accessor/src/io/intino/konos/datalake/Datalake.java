package io.intino.konos.datalake;

import io.intino.konos.jms.TopicConsumer;
import io.intino.ness.inl.Message;

public interface Datalake {
	String REFLOW_PATH = "service.ness.reflow";
	String FLOW_PATH = "flow.ness.reflow";
	String REGISTER_ONLY = "registerOnly";

	ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher);

	void commit();

	void add(String tank);

	void disconnect();

	void connect(String... args);

	interface Tank {
		void handler(MessageHandler handler);

		void handle(Message message);

		String name();

		default String flowChannel() {
			return "flow." + name();
		}

		default String dropChannel() {
			return "drop." + name();
		}

		default String feedChannel() {
			return "feed." + name();
		}

		public void feed(io.intino.ness.inl.Message message);

		void drop(io.intino.ness.inl.Message message);

		TopicConsumer flow(String flowID);

		void unregister();
	}

	interface ReflowSession {

		void next();

		void finish();

		void play();

		void pause();
	}
}
