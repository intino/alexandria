package io.intino.alexandria.event.resource;

import io.intino.alexandria.Resource;
import io.intino.alexandria.event.EventReader.IO;
import io.intino.alexandria.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static io.intino.alexandria.event.resource.ResourceEvent.REI.ID_SEP;
import static io.intino.alexandria.event.resource.ResourceHelper.METADATA_FILE;
import static io.intino.alexandria.event.resource.ResourceHelper.deserializeMetadata;
import static java.util.Objects.requireNonNull;

public class ZipResourceReader implements Iterator<Resource>, AutoCloseable {

	private final ZipInputStream zip;
	private final File file;
	private ZipEntry nextEntry;

	public ZipResourceReader(File file) throws IOException {
		this(IO.open(file), file);
	}

	/** Using this constructor forces the ResourceEvent elements to load ALL its content into memory on creation.*/
	public ZipResourceReader(InputStream inputStream) throws IOException {
		this(zip(inputStream));
	}

	public ZipResourceReader(InputStream inputStream, File file) throws IOException {
		this(zip(inputStream), file);
	}

	/** Using this constructor forces the ResourceEvent elements to load ALL its content into memory on creation.*/
	public ZipResourceReader(ZipInputStream zip) throws IOException {
		this(zip, null);
	}

	/** If file is null, then it forces the ResourceEvent elements to load ALL its content into memory on creation.*/
	public ZipResourceReader(ZipInputStream zip, File file) throws IOException {
		this.zip = requireNonNull(zip);
		this.nextEntry = zip.getNextEntry();
		this.file = file;
	}

	@Override
	public boolean hasNext() {
		return nextEntry != null;
	}

	@Override
	public Resource next() {
		ZipEntry next = nextEntry;
		tryAdvance();
		return toResource(next);
	}

	private Resource toResource(ZipEntry entry) {
		Map<String, String> metadata = deserializeMetadata(entry.getExtra());
		if(file != null) metadata.put(METADATA_FILE, file.getAbsolutePath());
		String name = entry.getName().substring(entry.getName().indexOf(ID_SEP) + 1);
		Resource resource = new Resource(name, inputStreamProviderOf(metadata, entry.getName()));
		resource.metadata().putAll(metadata);
		return resource;
	}

	private Resource.InputStreamProvider inputStreamProviderOf(Map<String, String> metadata, String entryName) {
		try {
			return metadata.containsKey(METADATA_FILE) ? openZipFileEntry(metadata.get(METADATA_FILE), entryName) : readFromMemory();
		} catch (Exception e) {
			Logger.error(e);
			return () -> new ByteArrayInputStream(new byte[0]);
		}
	}

	private Resource.InputStreamProvider readFromMemory() throws IOException {
		byte[] bytes = zip.readAllBytes();
		return () -> new ByteArrayInputStream(bytes);
	}

	@SuppressWarnings("all")
	private Resource.InputStreamProvider openZipFileEntry(String filename, String entryName) {
		return () -> {
			ZipFile zipFile = new ZipFile(filename);
			return zipFile.getInputStream(zipFile.getEntry(entryName));
		};
	}

	@Override
	public void close() throws Exception {
		zip.close();
	}

	private void tryAdvance() {
		this.nextEntry = null;
		try {
			this.nextEntry = zip.getNextEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static ZipInputStream zip(InputStream inputStream) {
		return inputStream instanceof ZipInputStream ? (ZipInputStream) inputStream : new ZipInputStream(inputStream);
	}
}
