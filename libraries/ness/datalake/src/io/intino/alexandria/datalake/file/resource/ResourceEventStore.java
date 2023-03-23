package io.intino.alexandria.datalake.file.resource;

import io.intino.alexandria.FS;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public class ResourceEventStore implements Datalake.ResourceStore, FileStore {

	private final File root;

	public ResourceEventStore(File root) {
		this.root = root;
	}

	@Override
	public Optional<ResourceEvent> find(ResourceEvent.REI rei) {
		try {
			Scale scale = scale();
			if(scale == null) return Optional.empty();
			File tub = new File(root, rei.type() + "/" + rei.ss() + "/" + Timetag.of(rei.ts(), scale) + fileExtension());
			return tub.exists() ? new ResourceEventTub(tub).find(rei) : Optional.empty();
		} catch (Exception e) {
			Logger.error(e);
			return Optional.empty();
		}
	}

	@Override
	public Stream<Tank<ResourceEvent>> tanks() {
		return FS.directoriesIn(root).map(ResourceEventTank::new);
	}

	public File directory() {
		return root;
	}

	@Override
	public ResourceEventTank tank(String name) {
		return new ResourceEventTank(new File(root, name));
	}

	@Override
	public String fileExtension() {
		return Event.Format.Resource.extension();
	}
}
