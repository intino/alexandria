package io.intino.alexandria.ingestion;

import com.github.luben.zstd.ZstdOutputStream;
import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MessageEventSession {
	private final Map<Fingerprint, MessageWriter> writers = new ConcurrentHashMap<>();
	private final MessageSessionHandler.Provider provider;
	private final int autoFlush;
	private final AtomicInteger count = new AtomicInteger();

	public MessageEventSession(MessageSessionHandler.Provider provider) {
		this(provider, 1_000_000);
	}

	public MessageEventSession(MessageSessionHandler.Provider provider, int autoFlush) {
		this.provider = provider;
		this.autoFlush = autoFlush;
	}

	public void put(String tank, String source, Timetag timetag, MessageEvent... events) throws IOException {
		put(tank, source, timetag, Arrays.stream(events));
		if (count.addAndGet(events.length) >= autoFlush) flush();
	}

	public void put(String tank, String source, Timetag timetag, Stream<MessageEvent> eventStream) throws IOException {
		put(writerOf(tank, source, timetag), eventStream);
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

	private MessageWriter writerOf(String tank, String source, Timetag timetag) throws IOException {
		return writerOf(Fingerprint.of(tank, source, Session.Type.message, timetag));
	}

	private MessageWriter writerOf(Fingerprint fingerprint) throws IOException {
		synchronized (writers) {
			if (!writers.containsKey(fingerprint)) writers.put(fingerprint, createWriter(fingerprint));
			return writers.get(fingerprint);
		}
	}

	private MessageWriter createWriter(Fingerprint fingerprint) throws IOException {
		return new MessageWriter(zStream(provider.outputStream(fingerprint.name(), Session.Type.message)));
	}

	private ZstdOutputStream zStream(OutputStream outputStream) throws IOException {
		return new ZstdOutputStream(outputStream, autoFlush * 100);
	}
}