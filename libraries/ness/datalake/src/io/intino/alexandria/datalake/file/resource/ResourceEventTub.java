package io.intino.alexandria.datalake.file.resource;

import io.intino.alexandria.Resource;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.FileTub;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.event.resource.ResourceHelper;
import io.intino.alexandria.event.resource.ZipFileEntryInputStream;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static io.intino.alexandria.event.Event.Format.Resource;
import static io.intino.alexandria.event.resource.ResourceEvent.REI.ID_SEP;
import static io.intino.alexandria.event.resource.ResourceHelper.METADATA_FILE;
import static io.intino.alexandria.event.resource.ZipResourceReader.METADATA_EXTENSION;

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
					.map(entry -> toResourceEvent(resourceName, entry, zipFile.getEntry(entry.getName() + METADATA_EXTENSION), zipFile))
					.collect(Collectors.toList());
		} catch (IOException e) {
			Logger.error(e);
			return new ArrayList<>(0);
		}
	}

	public Optional<ResourceEvent> find(ResourceEvent.REI rei) {
		try(ZipFile zipFile = new ZipFile(zip)) {
			ZipEntry entry = zipFile.getEntry(rei.resourceId());
			if(entry == null) return Optional.empty();
			ZipEntry metadata = zipFile.getEntry(entry.getName() + METADATA_EXTENSION);
			return Optional.of(toResourceEvent(rei.resourceName(), entry, metadata, zipFile));
		} catch (IOException e) {
			Logger.error(e);
			return Optional.empty();
		}
	}

	@SuppressWarnings("all")
	private ResourceEvent toResourceEvent(String resourceName, ZipEntry entry, ZipEntry metadataEntry, ZipFile zipFile) {
		Map<String, String> metadata = deserialize(metadataEntry, zipFile);
		io.intino.alexandria.Resource resource = new Resource(resourceName, () -> new ZipFileEntryInputStream(new ZipFile(zip), entry));
		resource.metadata().putAll(metadata);
		return ResourceEvent.of(resource);
	}

	private Map<String, String> deserialize(ZipEntry metadataEntry, ZipFile zipFile) {
		try(InputStream inputStream = zipFile.getInputStream(metadataEntry)) {
			Map<String, String> metadata = ResourceHelper.deserializeMetadata(new String(inputStream.readAllBytes()));
			if(metadata == null) metadata = new HashMap<>(0);
			metadata.put(METADATA_FILE, zip.getAbsolutePath());
			return metadata;
		} catch (IOException e) {
			return new HashMap<>(0);
		}
	}

	@Override
	public String fileExtension() {
		return Resource.extension();
	}

	public File file() {
		return zip;
	}
}