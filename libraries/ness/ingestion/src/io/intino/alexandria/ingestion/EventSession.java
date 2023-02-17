package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageWriter;
import org.xerial.snappy.SnappyOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EventSession {
	private final Map<Fingerprint, MessageWriter> writers = new ConcurrentHashMap<>();
	private final SessionHandler.Provider provider;
	private final int autoFlush;
	private final AtomicInteger count = new AtomicInteger();


	public EventSession(SessionHandler.Provider provider) {
		this(provider, 1_000_000);
	}

	public EventSession(SessionHandler.Provider provider, int autoFlush) {
		this.provider = provider;
		this.autoFlush = autoFlush;
	}

	public void put(String tank, Timetag timetag, MessageEvent... events) {
		put(tank, timetag, Arrays.stream(events));
		if (count.addAndGet(events.length) >= autoFlush) flush();
	}

	public void put(String tank, Timetag timetag, Stream<MessageEvent> eventStream) {
		put(writerOf(tank, timetag), eventStream);
	}

	public void flush() {
		try {
			for (MessageWriter w : writers.values())
				synchronized (w) {
					w.flush();
				}
			count.set(0);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public void close() {
		try {
			for (MessageWriter w : writers.values())
				synchronized (w) {
					w.close();
				}
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void put(MessageWriter writer, Stream<MessageEvent> events) {
		synchronized (writer) {
			events.forEach(e -> write(writer, e));
		}
	}

	private void write(MessageWriter writer, MessageEvent event) {
		try {
			writer.write(event.toMessage());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private MessageWriter writerOf(String tank, Timetag timetag) {
		return writerOf(Fingerprint.of(tank, timetag));
	}

	private MessageWriter writerOf(Fingerprint fingerprint) {
		synchronized (writers) {
			if (!writers.containsKey(fingerprint)) writers.put(fingerprint, createWriter(fingerprint));
			return writers.get(fingerprint);
		}
	}

	private MessageWriter createWriter(Fingerprint fingerprint) {
		return new MessageWriter(snappyStream(provider.outputStream(fingerprint.name(), Session.Type.event)));
	}

	private SnappyOutputStream snappyStream(OutputStream outputStream) {
		return new SnappyOutputStream(outputStream, autoFlush * 100);
	}
}