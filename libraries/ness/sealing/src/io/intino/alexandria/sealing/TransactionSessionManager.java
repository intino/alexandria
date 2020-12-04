package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.datalake.file.FileTransactionStore;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.LedWriter;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.LedUtils;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class TransactionSessionManager {
	public static void seal(File stageFolder, File transactionsStore, File tempFolder) {
		try {
			AtomicInteger processed = new AtomicInteger();
			ExecutorService pool = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors() / 2));
			List<File> files = transactionSessions(stageFolder).sorted(comparing(File::getName)).collect(Collectors.toList());
			files.stream().<Runnable>map(e -> () -> {
				sealSession(transactionsStore, e, tempFolder);
				notifyProcess(processed, files.size());
			}).forEach(pool::execute);
			pool.shutdown();
			pool.awaitTermination(1, TimeUnit.DAYS);
			deleteDirectory(tempFolder);
		} catch (InterruptedException e) {
			Logger.error(e);
		}
	}

	private static void notifyProcess(AtomicInteger processed, int total) {
		int processedPerc = Math.round(((float) processed.incrementAndGet() / total) * 100);
		if (processedPerc / 10 > processed.get() / 10) Logger.info("Processed " + processedPerc + "%");
	}

	private static void sealSession(File transactionStore, File session, File tempFolder) {
		try {
			File sorted = sort(session, tempFolder);
			File destination = datalakeFile(transactionStore, fingerprintOf(session));
			if (destination.exists()) {
				merge(destination, sorted, tempFolder);
				sorted.delete();
			} else Files.move(sorted.toPath(), destination.toPath());
			session.renameTo(new File(session.getAbsolutePath() + ".treated"));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private static void merge(File destination, File session, File tempFolder) {
		try {
			File temp = Files.createTempFile(tempFolder.toPath(),"seal", ".led").toFile();
			LedStream.merged(Stream.of(new LedReader(destination).read(SealTransaction::new), new LedReader(session).read(SealTransaction::new))).serialize(temp);
			FS.copyInto(temp, new FileInputStream(session));
			temp.delete();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private static Stream<File> transactionSessions(File stage) {
		return FS.allFilesIn(stage, f -> f.getName().endsWith(Session.LedSessionExtension) && f.length() > 0f);
	}

	private static File datalakeFile(File eventStoreFolder, Fingerprint fingerprint) {
		File zimFile = new File(eventStoreFolder, fingerprint.toString() + FileTransactionStore.TransactionExtension);
		zimFile.getParentFile().mkdirs();
		return zimFile;
	}

	private static Fingerprint fingerprintOf(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	private static File sort(File transactionSession, File tempFolder) {
		File file = new File(transactionSession.getParentFile(), transactionSession.getName() + ".sort");
		LedUtils.sort(new File(tempFolder, "Chunks_" + transactionSession.getName() + "_" + Thread.currentThread().getName()), transactionSession, file, 1_000_000);
		return file;
	}

	private static String cleanedNameOf(File file) {
		return file.getName().substring(0, file.getName().indexOf("#")).replace("-", "/").replace(Session.LedSessionExtension, "");
	}

	private static class SealTransaction extends Transaction {
		public SealTransaction(ByteStore store) {
			super(store);
		}

		@Override
		protected long id() {
			return idOf(this);
		}

		@Override
		public int size() {
			return (int) bitBuffer.byteSize();
		}
	}

	private static void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) for (File file : allContents) deleteDirectory(file);
		directoryToBeDeleted.delete();
	}

}
