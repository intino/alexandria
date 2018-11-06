package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.Timetag;
import io.intino.ness.core.fs.FS;

import java.io.File;
import java.util.function.Predicate;

import static io.intino.ness.core.fs.FSEventStore.EventExtension;

public class TCPEventTank implements Datalake.EventStore.Tank {

	private final String name;
	private final AdminService service;

	TCPEventTank(String name, AdminService service) {
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
		String value = service.request("quickReflow");//TODO change message
		return FS.filesIn(new File(value), f -> f.getName().endsWith(EventExtension))
				.filter(f -> filter.test(timetagOf(f)))
				.map(ZimReader::new)
				.toArray(ZimStream[]::new);
	}

	private Timetag timetagOf(File file) {
		return new Timetag(file.getName().replace(EventExtension, ""));
	}

}
