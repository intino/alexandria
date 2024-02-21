package io.intino.alexandria.event.resource;

import io.intino.alexandria.event.EventWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Map;

import static io.intino.alexandria.event.resource.ResourceEvent.ENTRY_NAME_SEP;
import static io.intino.alexandria.event.resource.ResourceEvent.METADATA;
import static io.intino.alexandria.event.resource.ResourceHelper.serializeMetadata;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class ResourceEventWriter implements EventWriter<ResourceEvent> {

	private final File file;

	public ResourceEventWriter(File file) {
		this.file = file;
	}

	@Override
	public void write(ResourceEvent event) throws IOException {
		URI uri = URI.create("jar:" + file.toPath().toUri());
		try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of("create", "true")); InputStream data = event.resource().stream()) {
			addMetadata(fs, event.getREI(), data);
			addData(fs, event.getREI(), serializeMetadata(event, file));
		}
	}

	private static void addData(FileSystem fs, ResourceEvent.REI rei, String metadata) throws IOException {
		String entryMetadataName = rei.resourceId().replace("/", ENTRY_NAME_SEP) + METADATA;
		Files.writeString(fs.getPath(entryMetadataName), metadata, CREATE, APPEND);
	}

	private static void addMetadata(FileSystem fs, ResourceEvent.REI rei, InputStream data) throws IOException {
		Files.write(fs.getPath(rei.resourceId().replace("/", ENTRY_NAME_SEP)), data.readAllBytes(), CREATE, APPEND);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws IOException {
	}
}
