package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.LedgerStore;

import java.io.File;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.file.FileLedgerStore.LedExtension;

public class FileLedTank implements LedgerStore.Tank {
	private final File root;

	public FileLedTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public LedgerStore.Led first() {
		return ledger().findFirst().orElse(currentLed());
	}

	@Override
	public LedgerStore.Led last() {
		return FS.foldersIn(root, FS.Sort.Reversed).map(FileLed::new).findFirst().orElse(currentLed());
	}

	@Override
	public Stream<LedgerStore.Led> ledger() {
		return FS.foldersIn(root).map(FileLed::new);
	}


	@Override
	public Stream<LedgerStore.Led> ledger(Timetag from, Timetag to) {
		return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
	}

	public LedgerStore.Led on(Timetag tag) {
		return new FileLed(new File(root, tag.value() + LedExtension));
	}

	public File root() {
		return root;
	}

	private FileLed currentLed() {
		return new FileLed(new File(root, new Timetag(LocalDateTime.now(), Scale.Month).toString() + LedExtension));
	}
}
