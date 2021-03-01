package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.triplestore.MemoryTripleStore;
import io.intino.alexandria.triplestore.TripleStore;

import java.util.Arrays;
import java.util.stream.Stream;

public class SetSession {
	private MemoryTripleStore.Builder tripleStore;
	private final SessionHandler.Provider provider;
	private final int autoFlush;
	private SetSessionWriter writer;
	private int count = 0;

	public SetSession(SessionHandler.Provider provider) {
		this(provider, 1_000_000);
	}

	public SetSession(SessionHandler.Provider provider, int autoFlush) {
		this.provider = provider;
		this.autoFlush = autoFlush;
	}

	public void put(String tank, Timetag timetag, String set, Stream<Long> ids) {
		ids.forEach(i -> writer().add(tank, timetag, set, i));
		if (count++ >= autoFlush) doFlush();
	}

	public void put(String tank, Timetag timetag, String set, long... ids) {
		put(tank, timetag, set, Arrays.stream(ids).boxed());
	}

	public void define(String tank, Timetag timetag, String set, String variable, String value) {
		tripleStore().put(Fingerprint.of(tank, timetag, set).toString(), variable, value);
	}

	private SetSessionWriter writer() {
		return this.writer != null ? writer : (writer = new SetSessionFileWriter(provider.outputStream(Session.Type.set)));
	}

	private TripleStore.Builder tripleStore() {
		return this.tripleStore != null ? tripleStore : (this.tripleStore = new MemoryTripleStore.Builder(provider.outputStream(Session.Type.setMetadata)));
	}

	public void flush() {
		if (writer != null) writer.flush();
		count = 0;
	}

	public void close() {
		if (writer != null) writer.close();
		if (tripleStore != null) tripleStore.close();
	}

	private void doFlush() {
		flush();
	}

}
