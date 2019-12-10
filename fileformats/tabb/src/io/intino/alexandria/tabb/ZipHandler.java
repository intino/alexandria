package io.intino.alexandria.tabb;

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
import java.util.zip.ZipOutputStream;

import static java.util.Collections.singletonMap;

class ZipHandler {

	static InputStream openEntry(File file, String entryName) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entryName.equals(entry.getName())) return zipFile.getInputStream(entry);
		}
		return null;
	}

	static void appendToEntry(File file, String entryName, String value) throws IOException {
		Path path = Paths.get(file.getAbsolutePath());
		try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), Map.of("create", "true"), ClassLoader.getSystemClassLoader())) {
			Path nf = fs.getPath(entryName);
			try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
				writer.write(value);
			} finally {
				fs.close();
			}
		}
	}

	static void writeEntry(File file, String entryName, String value) throws IOException {
		Path path = Paths.get(file.getAbsolutePath());
		try (FileSystem fs = FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), singletonMap("create", "true"), ClassLoader.getSystemClassLoader())) {
			Path nf = fs.getPath(entryName);
			try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				writer.write(value);
			} finally {
				fs.close();
			}
		}
	}

	static void writeEntry(ZipOutputStream zos, String name, InputStream is) throws IOException {
		zos.putNextEntry(new ZipEntry(name));
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) >= 0)
			zos.write(bytes, 0, length);
		zos.closeEntry();
	}

	static boolean hasEntry(File file, String entry) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			if (entry.equals(ze.getName())) return true;
		}
		return false;
	}
}
