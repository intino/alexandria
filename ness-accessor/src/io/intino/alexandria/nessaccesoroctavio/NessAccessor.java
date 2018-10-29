package io.intino.alexandria.nessaccesoroctavio;

import io.intino.alexandria.nessaccesoroctavio.EventStore.User;
import io.intino.alexandria.nessaccesoroctavio.fs.FSEventStore;
import io.intino.alexandria.nessaccesoroctavio.jms.JMSEventStore;
import io.intino.alexandria.nessaccesoroctavio.session.EventSession;
import io.intino.alexandria.nessaccesoroctavio.session.FileStage;
import io.intino.alexandria.nessaccesoroctavio.session.SetSession;
import io.intino.ness.setstore.SetStore;
import io.intino.ness.setstore.file.FileSetStore;

import java.util.List;
import java.util.stream.Stream;

import static io.intino.alexandria.nessaccesoroctavio.Helper.*;
import static io.intino.alexandria.nessaccesoroctavio.NessAccessor.Stage.Type.set;

public class NessAccessor {
	private final EventStore eventStore;
	private final SetStore setStore;

	public static SetSession createSetSession(FileStage stage) {
		return new SetSession(stage.start(set));
	}

	public static EventSession createEventSession(FileStage stage) {
		return new EventSession(stage.start(Stage.Type.event));//TODO
	}

	public NessAccessor(String url, String user, String password, String clientID) {
		this.eventStore = (url.startsWith("file://")) ? new FSEventStore(eventDatalakeDirectory(url), scaleOf(url)) : new JMSEventStore(url, user, password, clientID);
		this.setStore = (url.startsWith("file://")) ? new FileSetStore(setDatalakeDirectory(url), setScaleOf(url)) : null;
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

	public List<User> users() {
		return eventStore.users();
	}

	public interface Stage {
		enum Type {event, set}

		Stream<Session> sessions();

		interface Session {

			Type type();

			void commit();
			//TODO Lo que se necesite para hacer el commit

		}
	}
}