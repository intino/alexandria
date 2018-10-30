package io.intino.alexandria.nessaccesor;

import io.intino.alexandria.jms.TopicConsumer;
import io.intino.alexandria.ness.Scale;
import io.intino.alexandria.ness.setstore.file.FSSetStore;
import io.intino.alexandria.nessaccesor.fs.FSEventStore;
import io.intino.alexandria.nessaccesor.fs.FSStage;
import io.intino.alexandria.nessaccesor.jms.JMSEventStore;
import io.intino.ness.inl.Loader;
import io.intino.ness.inl.Message;
import io.intino.ness.inl.MessageInputStream;
import io.intino.sezzet.operators.SetStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.intino.alexandria.nessaccesor.Helper.*;
import static io.intino.alexandria.nessaccesor.NessAccessor.Stage.Type.event;
import static io.intino.alexandria.nessaccesor.NessAccessor.Stage.Type.set;
import static java.util.stream.Collectors.toList;

public class NessAccessor {
	private final EventStore eventStore;
	private final SetStore setStore;

	public NessAccessor(String url, String user, String password, String clientID) {
		this.eventStore = (url.startsWith("file://")) ? new FSEventStore(eventDatalakeDirectory(url), scaleOf(url)) : new JMSEventStore(url, user, password, clientID);
		this.setStore = (url.startsWith("file://")) ? new FSSetStore(setDatalakeDirectory(url), setScaleOf(url)) : null;
	}

	public static SetSession createSetSession(FSStage stage) {
		return new SetSession(stage);
	}

	public static EventSession createEventSession(FSStage stage) {
		return new EventSession(stage);
	}

	public void commit(Stage stage) {
		commitSetSessions(stage.sessions().filter(s -> s.type().equals(set)).collect(toList()));
		commitEventSessions(stage.sessions().filter(s -> s.type().equals(event)).collect(toList()));
		stage.sessions().forEach(s -> s.remove());
	}

	private void commitSetSessions(List<Stage.Session> setSessions) {
		setSessions.forEach(s -> setStore.storeSession(s.inputStream()));
		setStore.seal();
	}

	private void commitEventSessions(List<Stage.Session> eventSessions) {
		eventSessions.forEach(e -> process(e));
	}

	private void process(Stage.Session session) {
		try {
			MessageInputStream stream = Loader.Inl.of(session.inputStream());
			while(stream.hasNext()) eventStore.append(stream.next());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String... args) {
		eventStore.connect(args);
	}

	public void disconnect() {
		eventStore.disconnect();
	}

	public boolean isConnected() {
		return eventStore.isConnected();
	}

	public EventStore eventStore() {
		return eventStore;
	}

	public SetStore setStore() {
		return setStore;
	}

	public List<EventStore.User> users() {
		return eventStore.users();
	}

	public interface Stage {

		OutputStream start(Type type);

		Stream<Session> sessions();

		enum Type {event, set}

		interface Session {
			Type type();
			InputStream inputStream();
			void remove();
		}
	}

	public interface SetStore {
		Scale scale();

		List<Tank> tanks();

		Tank tank(String name);

		void storeSession(InputStream inputStream);

		void seal();

		interface Tank {
			String name();

			List<Tub> tubs();

			Tub first();

			Tub last();

			Tub on(Timetag tag);

			Stream<Tub> tubs(int count);

			List<Tub> tubs(Timetag from, Timetag to);

			List<Tub.Set> setsOf(Timetag from, Timetag to);

			List<Tub.Set> setsOf(Timetag from, Timetag to, SetFilter filter);

			interface Tub {
				String name();

				Tank tank();

				Timetag timetag();

				Set set(String set);

				List<Set> sets();

				List<Set> sets(SetFilter filter);

				interface Set {
					String name();

					int size();

					Tub tub();

					SetStream content();

					List<Variable> variables();

					Variable variable(String name);

					void put(long... ids);

					void put(List<Long> stream);

					void define(Variable variable);
				}
			}
		}

		interface SetFilter extends Predicate<Tank.Tub.Set> {
		}

		class Variable {
			public String name;
			public String value;

			public Variable(String name, String value) {
				this.name = name;
				this.value = value;
			}
		}

		class Timetag {
			private final Instant instant;
			private final Scale scale;
			private final String tag;

			public Timetag(Instant instant, Scale scale) {
				this.instant = instant;
				this.scale = scale;
				this.tag = scale.tag(instant);
			}

			public Instant instant() {
				return instant;
			}

			public Scale scale() {
				return scale;
			}

			public String value() {
				return tag;
			}

			public Timetag next() {
				return new Timetag(scale.plus(instant), scale);
			}

			public Timetag before() {
				return new Timetag(scale.minus(instant), scale);
			}

			public Instant toInstant() {
				return instant;
			}

			@Override
			public String toString() {
				return tag;
			}
		}
	}

	public static interface EventStore {
		String REFLOW_PATH = "service.ness.reflow";
		String FLOW_PATH = "flow.ness.reflow";
		String REGISTER_ONLY = "registerOnly";

		ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher);

		Tank add(String tank);

		void connect(String... args);

		void disconnect();

		boolean isConnected();

		List<User> users();

		void append(Message message);

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

			boolean feed(Message message);

			boolean put(Message message);

			TopicConsumer flow(String flowID);

			Tank batchSession(int blockSize);

			Tank endBatch();

			void unregister();
		}

		interface ReflowSession {
			void next();

			void finish();

			void play();

			void pause();
		}

		interface EventSession {
			void append(String tank, List<Message> messages);

			default void append(Tank tank, List<Message> messages) {
				append(tank.name(), messages);
			}

			void append(String tank, Message... messages);

			default void append(Tank tank, Message... messages) {
				append(tank.name(), messages);
			}

			void close();
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
}