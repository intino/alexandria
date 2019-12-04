package io.intino.alexandria.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Zip {
	private final File file;

	public Zip(File file) {
		this.file = file;
	}

	public boolean exists(String filePath) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			if (filePath.equals(ze.getName())) return true;
		}
		return false;
	}

	public InputStream read(String filePath) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (filePath.equals(entry.getName())) return zipFile.getInputStream(entry);
		}
		return null;
	}

	public void write(String filePath, String value, StandardOpenOption... options) throws IOException {
		Path path = Paths.get(file.getAbsolutePath());
		try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), Map.of("create", "true"), ClassLoader.getSystemClassLoader())) {
			try (Writer writer = Files.newBufferedWriter(fs.getPath(filePath), StandardCharsets.UTF_8, options)) {
				writer.write(value);
			}
		}
	}

	public void write(String filePath, InputStream stream, StandardOpenOption... options) throws IOException {
		Path path = Paths.get(file.getAbsolutePath());
		try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), Map.of("create", "true"), ClassLoader.getSystemClassLoader())) {
			Files.write(fs.getPath(filePath), stream.readAllBytes(), options);
		}
	}
}
