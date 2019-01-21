package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.fs.FS;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Predicate;

import static io.intino.ness.core.fs.FSEventStore.EventExtension;

public class TCPEventTank implements Datalake.EventStore.Tank {

	private final String name;
	private final ReflowService service;

	TCPEventTank(String name, ReflowService service) {
		this.name = name;
		this.service = service;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ZimStream content() {
		return ZimStream.Sequence.of(zimStreams(t -> true));
	}

	@Override
	public ZimStream content(Predicate<Timetag> filter) {
		return ZimStream.Sequence.of(zimStreams(filter));
	}

	private ZimStream[] zimStreams(Predicate<Timetag> filter) {
		String root = service.request("quickReflow");
		File directory = directory(root);
		if (!directory.exists()) return new ZimStream[0];
		return FS.allFilesIn(directory, f -> f.getName().endsWith(EventExtension)).sorted()
				.filter(f -> filter.test(timetagOf(f)))
				.map(ZimReader::new)
				.toArray(ZimStream[]::new);
	}

	private File directory(String value) {
		try {
			return new File(new File(new URI(value)), this.name);
		} catch (URISyntaxException e) {
			return new File(value);
		}
	}

	private Timetag timetagOf(File file) {
		return new Timetag(file.getName().replace(EventExtension, ""));
	}
}
