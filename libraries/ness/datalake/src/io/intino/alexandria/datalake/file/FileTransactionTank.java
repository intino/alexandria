package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.TransactionStore;

import java.io.File;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.file.FileTransactionStore.LedExtension;

public class FileTransactionTank implements TransactionStore.Tank {
	private final File root;

	public FileTransactionTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public TransactionStore.Transaction first() {
		return transactions().findFirst().orElse(currentLed());
	}

	@Override
	public TransactionStore.Transaction last() {
		return FS.foldersIn(root, FS.Sort.Reversed).map(FileTransaction::new).findFirst().orElse(currentLed());
	}

	@Override
	public Stream<TransactionStore.Transaction> transactions() {
		return FS.foldersIn(root).map(FileTransaction::new);
	}


	@Override
	public Stream<TransactionStore.Transaction> transactions(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
	}

	public TransactionStore.Transaction on(Timetag tag) {
		return new FileTransaction(new File(root, tag.value() + LedExtension));
	}

	public File root() {
		return root;
	}

	private FileTransaction currentLed() {
		return new FileTransaction(new File(root, new Timetag(LocalDateTime.now(), Scale.Month).toString() + LedExtension));
	}
}
