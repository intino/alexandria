package io.intino.alexandria.tabb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.util.Collections.singletonMap;

class ZipHandler {
	private static final Map<Path, FileSystem> filesystems = new HashMap<>();

	static InputStream openEntry(ZipFile zipFile, String entryName) throws IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entryName.equals(entry.getName())) return zipFile.getInputStream(entry);
		}
		return null;
	}

	static byte[] readEntry(File file, String entry) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		InputStream inputStream = openEntry(zipFile, entry);
		if (inputStream == null) {
			zipFile.close();
			return new byte[0];
		}
		byte[] bytes = inputStream.readAllBytes();
		inputStream.close();
		zipFile.close();
		return bytes;
	}


	static void writeEntry(File file, String entryName, String value) throws IOException {
		Path path = Paths.get(file.getAbsolutePath()).toRealPath();
		if (!filesystems.containsKey(path) || !filesystems.get(path).isOpen())
			filesystems.put(path, newFileSystem(path));
		try (FileSystem fs = filesystems.get(path)) {
			try (Writer writer = Files.newBufferedWriter(fs.getPath(entryName), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				writer.write(value);
			}
		}
	}

	static void appendToEntry(File file, String entryName, String value) throws IOException {
		Path path = Paths.get(file.getAbsolutePath()).toRealPath();
		if (!filesystems.containsKey(path) || !filesystems.get(path).isOpen())
			filesystems.put(path, newFileSystem(path));
		try (FileSystem fs = filesystems.get(path)) {
			try (Writer writer = Files.newBufferedWriter(fs.getPath(entryName), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
				writer.write(value);
			}
		}
	}

	static void writeEntry(ZipOutputStream zos, String name, InputStream is) throws IOException {
		zos.putNextEntry(new ZipEntry(name));
		zos.write(is.readAllBytes());
		zos.closeEntry();
	}

	private static FileSystem newFileSystem(Path path) throws IOException {
		return FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), singletonMap("create", "true"), ClassLoader.getSystemClassLoader());
	}

	static boolean hasEntry(File file, String entry) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			if (entry.equals(ze.getName())) {
				zipFile.close();
				return true;
			}
		}
		zipFile.close();
		return false;
	}
}
