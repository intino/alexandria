package io.intino.alexandria.nessaccessor.sessions;

import io.intino.alexandria.TripleStore;
import io.intino.alexandria.nessaccessor.Stage;
import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.Timetag;
import io.intino.ness.core.sessions.Fingerprint;
import io.intino.ness.core.sessions.SetSessionFileWriter;

import java.util.Arrays;
import java.util.stream.Stream;

public class SetSession {
	private final SetSessionFileWriter writer;
	private final TripleStore.Builder tripleStore;
	private final int maxSize;
	private int count = 0;

	public SetSession(Stage stage) {
		this(stage, 1000000);
	}

	public SetSession(Stage stage, int autoFlushSize) {
		this.writer = new SetSessionFileWriter(stage.start(Blob.Type.set));
		this.tripleStore = new TripleStore.Builder(stage.start(Blob.Type.setMetadata));
		this.maxSize = autoFlushSize;
	}

	public void put(String tank, Timetag timetag, String set, Stream<Long> ids) {
		ids.forEach(i -> writer.add(tank, timetag, set, i));
		if (count++ >= maxSize) doFlush();
	}

	public void put(String tank, Timetag timetag, String set, long... ids) {
		put(tank, timetag, set, Arrays.stream(ids).boxed());
	}

	public void define(String tank, Timetag timetag, String set, Datalake.SetStore.Variable variable) {
		tripleStore.put(Fingerprint.of(tank, timetag, set).toString(), variable.name, variable.value);
	}

	private void doFlush() {
		flush();
	}

	public void flush() {
		writer.flush();
		count = 0;
	}

	public void close() {
		writer.close();
		tripleStore.close();
	}

}
