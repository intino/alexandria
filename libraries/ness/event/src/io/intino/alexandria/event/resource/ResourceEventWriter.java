package io.intino.alexandria.event.resource;

import io.intino.alexandria.event.EventWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.event.resource.ResourceHelper.serializeMetadata;
import static io.intino.alexandria.event.resource.ZipResourceReader.METADATA_EXTENSION;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceEventWriter implements EventWriter<ResourceEvent> {

	private static final String ENTRY_NAME_SEP = "$";

	private final ZipOutputStream zip;
	private final File file;

	public ResourceEventWriter(File file) throws IOException {
		this(file, true);
	}

	public ResourceEventWriter(File file, boolean append) throws IOException {
		this(IO.open(file, append), file);
	}

	public ResourceEventWriter(OutputStream outputStream) {
		this(outputStream, null);
	}

	public ResourceEventWriter(OutputStream outputStream, File file) {
		this.zip = new ZipOutputStream(outputStream);
		this.file = file;
	}

	@Override
	public void write(ResourceEvent event) throws IOException {
		writeResourceEntry(event);
		writeMetadataEntry(event);
	}

	private void writeResourceEntry(ResourceEvent event) throws IOException {
		ZipEntry entry = new ZipEntry(event.getREI().resourceId());
		zip.putNextEntry(entry);
		try(InputStream resourceData = event.resource().stream()) {
			resourceData.transferTo(zip);
		}
	}

	private void writeMetadataEntry(ResourceEvent event) throws IOException {
		ZipEntry entry = new ZipEntry(event.getREI().resourceId() + METADATA_EXTENSION);
		zip.putNextEntry(entry);
		zip.write(serializeMetadata(event, file).getBytes(UTF_8));
	}

	@Override
	public void flush() throws IOException {
		zip.flush();
	}

	@Override
	public void close() throws IOException {
		zip.close();
	}
}
