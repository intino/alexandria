package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.TransactionStore;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.file.FileTransactionStore.TransactionExtension;

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
		return transactions().findFirst().orElse(null);
	}

	@Override
	public TransactionStore.Transaction last() {
		List<File> files = transactionFiles().collect(Collectors.toList());
		return files.isEmpty() ? null : new FileTransaction(files.get(files.size() - 1));
	}

	@Override
	public Stream<TransactionStore.Transaction> transactions() {
		return transactionFiles().map(FileTransaction::new);
	}

	@Override
	public Stream<TransactionStore.Transaction> transactions(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on).filter(Objects::nonNull);
	}

	public TransactionStore.Transaction on(Timetag tag) {
		File file = new File(root, tag.value() + TransactionExtension);
		return file.exists() ? new FileTransaction(file) : null;
	}

	public File root() {
		return root;
	}

	private Stream<File> transactionFiles() {
		return FS.filesIn(root, path -> path.getName().endsWith(TransactionExtension));
	}
}
