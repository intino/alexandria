package io.intino.alexandria.datalake.file.resource;

import io.intino.alexandria.Json;
import io.intino.alexandria.Resource;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.FileTub;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.event.resource.ZipFileEntryInputStream;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static io.intino.alexandria.event.Event.Format.Resource;
import static io.intino.alexandria.event.resource.ResourceEvent.REI.ID_SEP;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceEventTub implements Datalake.Store.Tub<ResourceEvent>, FileTub {

	private final File zip;

	public ResourceEventTub(File zip) {
		this.zip = zip;
	}

	public String name() {
		return zip.getName().replace(Resource.extension(), "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<ResourceEvent> events() {
		try {
			return EventStream.of(zip);
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	public List<ResourceEvent> findAll(String resourceName) {
		String suffix = ID_SEP + resourceName;
		try(ZipFile zipFile = new ZipFile(zip)) {
			return zipFile.stream()
					.filter(entry -> entry.getName().endsWith(suffix))
					.map(entry -> toResourceEvent(resourceName, entry))
					.collect(Collectors.toList());
		} catch (IOException e) {
			Logger.error(e);
			return new ArrayList<>(0);
		}
	}

	public Optional<ResourceEvent> find(ResourceEvent.REI rei) {
		try(ZipFile zipFile = new ZipFile(zip)) {
			ZipEntry entry = zipFile.getEntry(rei.resourceId());
			return entry == null ? Optional.empty() : Optional.of(toResourceEvent(rei.resourceName(), entry));
		} catch (IOException e) {
			Logger.error(e);
			return Optional.empty();
		}
	}

	@SuppressWarnings("all")
	private ResourceEvent toResourceEvent(String resourceName, ZipEntry entry) {
		Map<String, String> metadata = Json.fromJson(new String(entry.getExtra(), UTF_8), Map.class);
		io.intino.alexandria.Resource resource = new Resource(resourceName, () -> new ZipFileEntryInputStream(new ZipFile(zip), entry));
		resource.metadata().putAll(metadata);
		return ResourceEvent.of(resource);
	}

	@Override
	public String fileExtension() {
		return Resource.extension();
	}

	public File file() {
		return zip;
	}
}