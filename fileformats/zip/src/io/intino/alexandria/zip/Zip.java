package io.intino.alexandria.zip;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.util.Collections.singletonMap;

public class Zip {
	private static final int BUFFER_SIZE = 4096;
	private final File file;
	private final Map<Path, FileSystem> filesystems = new HashMap<>();

	public Zip(File file) {
		this.file = file;
	}

	public Zip create() throws IOException {
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(this.file));
		zipOutputStream.close();
		return this;
	}

	public boolean exists(String filePath) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			if (filePath.equals(ze.getName())) {
				zipFile.close();
				return true;
			}
		}
		zipFile.close();
		return false;
	}

	public String read(String filePath) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (filePath.equals(entry.getName())) {
				String text = new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
				zipFile.close();
				return text;
			}
		}
		zipFile.close();
		return null;
	}

	public void write(String filePath, String value, StandardOpenOption... options) throws IOException {
		if (!file.exists()) create();
		Path path = Paths.get(file.getAbsolutePath()).toRealPath();
		if (!filesystems.containsKey(path) || !filesystems.get(path).isOpen())
			filesystems.put(path, newFileSystem(path));
		try (FileSystem fs = filesystems.get(path)) {
			try (Writer writer = Files.newBufferedWriter(fs.getPath(filePath), options)) {
				writer.write(value);
			}
		}
	}

	public void write(String filePath, InputStream stream, StandardOpenOption... options) throws IOException {
		if (!file.exists()) create();
		Path path = Paths.get(file.getAbsolutePath()).toRealPath();
		if (!filesystems.containsKey(path) || !filesystems.get(path).isOpen())
			filesystems.put(path, newFileSystem(path));
		try (FileSystem fs = filesystems.get(path)) {
			Files.write(fs.getPath(filePath), stream.readAllBytes(), options);
		}
		stream.close();
	}

	public void unzip(String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) destDir.mkdir();
		ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) extractFile(zin, filePath);
			else {
				File dir = new File(filePath);
				dir.mkdir();
			}
			zin.closeEntry();
			entry = zin.getNextEntry();
		}
		zin.close();
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static InputStream read(ZipFile zipFile, String filePath) throws IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (filePath.equals(entry.getName())) return zipFile.getInputStream(entry);
		}
		return null;
	}

	private static FileSystem newFileSystem(Path path) throws IOException {
		return FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), singletonMap("create", "true"), ClassLoader.getSystemClassLoader());
	}
}
