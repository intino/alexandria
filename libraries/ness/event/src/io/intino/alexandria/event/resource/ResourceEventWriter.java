package io.intino.alexandria.event.resource;

import io.intino.alexandria.event.EventWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.event.resource.ResourceHelper.serializeMetadata;

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
		ZipEntry entry = new ZipEntry(event.getREI().resourceId());
		entry.setExtra(serializeMetadata(event, file));
		zip.putNextEntry(entry);
		try(InputStream resourceData = event.resource().stream()) {
			resourceData.transferTo(zip);
		}
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
