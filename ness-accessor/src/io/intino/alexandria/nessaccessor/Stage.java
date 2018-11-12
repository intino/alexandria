package io.intino.alexandria.nessaccessor;

import io.intino.alexandria.nessaccessor.sessions.EventSession;
import io.intino.alexandria.nessaccessor.sessions.SetSession;
import io.intino.ness.core.Blob;

import java.io.OutputStream;
import java.util.stream.Stream;

import static java.util.UUID.randomUUID;


public interface Stage {

	OutputStream start(Blob.Type type);

	OutputStream start(String prefix, Blob.Type type);

	Stream<Blob> blobs();

	default SetSession createSetSession() {
		return new SetSession(this);
	}

	default SetSession createSetSession(int autoFlushSize) {
		return new SetSession(this, autoFlushSize);
	}

	default EventSession createEventSession() {
		return new EventSession(this);
	}

	default String name(String prefix) {
		return prefix + "#" + randomUUID().toString();
	}

	void clear();

}
