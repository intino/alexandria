package io.intino.alexandria.event.resource;

import io.intino.alexandria.event.EventWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.Map;

import static io.intino.alexandria.event.resource.ResourceHelper.serializeMetadata;

public class ResourceEventWriter implements EventWriter<ResourceEvent> {
	private final File file;

	public ResourceEventWriter(File file) {
		this.file = file;
	}

	@Override
	public void write(ResourceEvent event) throws IOException {
		URI uri = URI.create("jar:" + file.toPath().toUri());
		String entryName = event.getREI().resourceId().replace("/", "$");
		String entryMetadataName = event.getREI().resourceId().replace("/", "$") + ".metadata";
		String metadata = serializeMetadata(event, file);
		try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of("create", "true")); InputStream data = event.resource().stream()) {
			Path nf = fs.getPath(entryName);
			Files.write(nf, data.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			nf = fs.getPath(entryMetadataName);
			Files.writeString(nf, metadata, StandardOpenOption.CREATE);
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws IOException {
	}
}
