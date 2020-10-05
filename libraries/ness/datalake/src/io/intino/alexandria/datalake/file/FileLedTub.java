package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Ledger.Led;

import java.io.File;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.file.FileLedger.LedExtension;

public class FileLedTub implements Datalake.Ledger.Tub {
	private final File root;

	public FileLedTub(File root) {
		this.root = root;
	}

	public String name() {
		return root.getName();
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Scale scale() {
		return timetag().scale();
	}


	@Override
	public Led led(String led) {
		return new FileLed(new File(this.root, led + LedExtension));
	}

	@Override
	public Stream<Led> leds() {
		return FS.filesIn(root, f -> f.getName().endsWith(LedExtension)).map(FileLed::new);
	}

	@Override
	public Stream<Led> leds(Predicate<Led> filter) {
		return leds().filter(filter);
	}
}
