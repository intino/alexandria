package io.intino.alexandria.ingestion;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.LedWriter;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.UnsortedLedStreamBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TransactionSession {
	private final SessionHandler.Provider provider;
	private final File temp;
	private final Map<Fingerprint, LedStream.Builder<? extends Transaction>> builders;
	private final int transactionsBufferSize;

	public TransactionSession(SessionHandler.Provider provider, File temp) {
		this(provider, temp, 1_000_000);
	}

	public TransactionSession(SessionHandler.Provider provider, File temp, int transactionsBufferSize) {
		this.provider = provider;
		this.temp = temp;
		this.transactionsBufferSize = transactionsBufferSize;
		this.builders = new HashMap<>();
	}

	public <T extends Transaction> void put(String tank, Timetag timetag, Class<T> transactionClass, Stream<Consumer<T>> stream) {
		LedStream.Builder<T> builder = builder(tank, timetag, transactionClass);
		stream.forEach(builder::append);
	}

	public <T extends Transaction> void put(String tank, Timetag timetag, Class<T> transactionClass, Consumer<T> transaction) {
		LedStream.Builder<T> builder = builder(tank, timetag, transactionClass);
		builder.append(transaction);
	}

	@SuppressWarnings("unchecked")
	private <T extends Transaction> LedStream.Builder<T> builder(String tank, Timetag timetag, Class<T> transactionClass) {
		if (!builders.containsKey(Fingerprint.of(tank, timetag)))
			builders.put(Fingerprint.of(tank, timetag), new UnsortedLedStreamBuilder<>(transactionClass, Transaction.factoryOf(transactionClass), transactionsBufferSize, temp));
		return (LedStream.Builder<T>) builders.get(Fingerprint.of(tank, timetag));
	}

	public void close() {
		builders.forEach((f, b) -> new LedWriter(provider.file(f.name(), Session.Type.led)).write(b.build()));
	}
}