package io.intino.alexandria.event.resource;

import io.intino.alexandria.Resource;
import io.intino.alexandria.event.EventReader.IO;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.io.*;
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

	private final DisposableResource resource;
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
		this.resource = DisposableResource.whenDestroyed(this).thenClose(zip);
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
		return () -> new ZipFileInputStream(filename, entryName);
	}

	@Override
	public void close() throws Exception {
		zip.close();
		resource.close();
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

	private static class ZipFileInputStream extends InputStream {

		private final ZipFile zipFile;
		private final InputStream inputStream;

		public ZipFileInputStream(String filename, String entryName) throws IOException {
			zipFile = new ZipFile(filename);
			inputStream = zipFile.getInputStream(zipFile.getEntry(entryName));
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public int read(byte[] b) throws IOException {
			return inputStream.read(b);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return inputStream.read(b, off, len);
		}

		@Override
		public byte[] readAllBytes() throws IOException {
			return inputStream.readAllBytes();
		}

		@Override
		public byte[] readNBytes(int len) throws IOException {
			return inputStream.readNBytes(len);
		}

		@Override
		public int readNBytes(byte[] b, int off, int len) throws IOException {
			return inputStream.readNBytes(b, off, len);
		}

		@Override
		public long skip(long n) throws IOException {
			return inputStream.skip(n);
		}

		@Override
		public int available() throws IOException {
			return inputStream.available();
		}

		@Override
		public void close() throws IOException {
			inputStream.close();
			zipFile.close();
		}

		@Override
		public void mark(int readlimit) {
			inputStream.mark(readlimit);
		}

		@Override
		public void reset() throws IOException {
			inputStream.reset();
		}

		@Override
		public boolean markSupported() {
			return inputStream.markSupported();
		}

		@Override
		public long transferTo(OutputStream out) throws IOException {
			return inputStream.transferTo(out);
		}
	}
}
