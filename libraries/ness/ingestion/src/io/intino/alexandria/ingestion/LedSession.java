package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.io.LedWriter;

public class LedSession {
	private final SessionHandler.Provider provider;

	public LedSession(SessionHandler.Provider provider) {
		this.provider = provider;
	}

	public void put(String tank, Timetag timetag, Led<?> led) {
		new LedWriter(provider.outputStream(Fingerprint.of(tank, timetag).name(), Session.Type.led)).write(led);
	}
}
