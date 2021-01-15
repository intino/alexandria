package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.UnsortedLedStreamBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TransactionSession {
	private final SessionHandler.Provider provider;
	private final Map<Fingerprint, UnsortedLedStreamBuilder<? extends Schema>> builders;
	private final int transactionsBufferSize;

	public TransactionSession(SessionHandler.Provider provider) {
		this(provider, 1_000_000);
	}

	public TransactionSession(SessionHandler.Provider provider, int transactionsBufferSize) {
		this.provider = provider;
		this.transactionsBufferSize = transactionsBufferSize;
		this.builders = new HashMap<>();
	}

	public <T extends Schema> void put(String tank, Timetag timetag, Class<T> transactionClass, Stream<Consumer<T>> stream) {
		LedStream.Builder<T> builder = builder(Fingerprint.of(tank, timetag), transactionClass);
		stream.forEach(builder::append);
	}

	public <T extends Schema> void put(String tank, Timetag timetag, Class<T> transactionClass, Consumer<T> transaction) {
		LedStream.Builder<T> builder = builder(Fingerprint.of(tank, timetag), transactionClass);
		builder.append(transaction);
	}

	@SuppressWarnings("unchecked")
	private <T extends Schema> LedStream.Builder<T> builder(Fingerprint fingerprint, Class<T> transactionClass) {
		if (!builders.containsKey(fingerprint))
			builders.put(fingerprint, new UnsortedLedStreamBuilder<>(transactionClass, Schema.factoryOf(transactionClass), transactionsBufferSize, provider.file(fingerprint.name(), Session.Type.led)));
		return (LedStream.Builder<T>) builders.get(fingerprint);
	}

	public void close() {
		builders.forEach((f, b) -> b.close());
	}
}