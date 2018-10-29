package io.intino.konos.datalake;

import io.intino.konos.datalake.EventDatalake.User;
import io.intino.konos.datalake.fs.FSEventDatalake;
import io.intino.konos.datalake.jms.JMSEventDatalake;
import io.intino.konos.datalake.session.EventSession;
import io.intino.konos.datalake.session.FileStage;
import io.intino.konos.datalake.session.SetSession;
import io.intino.ness.setstore.SetStore;
import io.intino.ness.setstore.file.FileSetStore;

import java.util.List;
import java.util.stream.Stream;

import static io.intino.konos.datalake.Helper.*;
import static io.intino.konos.datalake.NessAccessor.Stage.Type.set;

public class NessAccessor {
	private final EventDatalake eventDatalake;
	private final SetStore setDatalake;

	public static SetSession createSetSession(FileStage stage) {
		return new SetSession(stage.start(set));
	}

	public static EventSession createEventSession(FileStage stage) {
		return new EventSession(stage.start(Stage.Type.event));//TODO
	}

	public NessAccessor(String url, String user, String password, String clientID) {
		this.eventDatalake = (url.startsWith("file://")) ? new FSEventDatalake(eventDatalakeDirectory(url), scaleOf(url)) : new JMSEventDatalake(url, user, password, clientID);
		this.setDatalake = (url.startsWith("file://")) ? new FileSetStore(setDatalakeDirectory(url), setScaleOf(url)) : null;
	}

	public void connect(String... args) {
		eventDatalake.connect(args);
	}

	public void disconnect() {
		eventDatalake.disconnect();
	}

	public boolean isConnected() {
		return eventDatalake.isConnected();
	}

	public EventDatalake eventDatalake() {
		return eventDatalake;
	}

	public SetStore setDatalake() {
		return setDatalake;
	}

	public List<User> users() {
		return eventDatalake.users();
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