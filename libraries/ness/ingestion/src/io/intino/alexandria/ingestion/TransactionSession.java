package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.LedWriter;

public class TransactionSession {
	private final SessionHandler.Provider provider;

	public TransactionSession(SessionHandler.Provider provider) {
		this.provider = provider;
	}

	public void put(String tank, Timetag timetag, Led<?> led) {
		new LedWriter(provider.outputStream(Fingerprint.of(tank, timetag).name(), Session.Type.led)).write(led);
	}

	public void put(String tank, Timetag timetag, LedStream<?> stream) {
		new LedWriter(provider.outputStream(Fingerprint.of(tank, timetag).name(), Session.Type.led)).write(stream);
	}
}
