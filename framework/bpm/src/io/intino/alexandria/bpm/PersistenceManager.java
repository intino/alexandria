package io.intino.alexandria.bpm;

import io.intino.alexandria.Timetag;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public interface PersistenceManager {

	void delete(String path);

	List<String> list(String path);

	List<Timetag> finishedTimetags();

	InputStream read(String path);

	OutputStream write(String path);

	boolean exists(String path);

	class FilePersistenceManager implements PersistenceManager {
		private final File directory;

		public FilePersistenceManager(File directory) {
			directory.mkdirs();
			this.directory = directory;
		}

		@Override
		public void delete(String path) {
			new File(directory, path).delete();
		}

		@Override
		public List<String> list(String path) {
			File file = new File(directory, path);
			return file.exists() ? asList(file.list((f, n) -> n.endsWith(".process"))) : Collections.emptyList();
		}

		@Override
		public List<Timetag> finishedTimetags() {
			return Arrays.stream(new File(directory, "finished").listFiles(f -> f.isDirectory()))
					.filter(f -> f.getName().length() == 6)
					.map(f -> Timetag.of(f.getName()))
					.collect(toList());
		}

		@Override
		public InputStream read(String path) {
			try {
				if (!new File(directory, path).exists()) return new ByteArrayInputStream(new byte[0]);
				return Files.newInputStream(new File(directory, path).toPath());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public OutputStream write(String path) {
			try {
				File file = new File(directory, path);
				file.getParentFile().mkdirs();
				return new FileOutputStream(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public boolean exists(String path) {
			return new File(directory, path).exists();
		}
	}

	class InMemoryPersistenceManager implements PersistenceManager {
		private final Map<String, ByteArrayOutputStream> content = new HashMap<>();

		@Override
		public void delete(String path) {
			content.remove(path);
		}

		@Override
		public List<String> list(String path) {
			return content.keySet().stream()
					.filter(k -> !k.equals(path) && k.startsWith(path))
					.map(p -> p.substring(path.length()))
					.collect(toList());
		}

		@Override
		public List<Timetag> finishedTimetags() {
			return new ArrayList<>(content.keySet().stream()
					.filter(k -> k.startsWith("finished"))
					.map(p -> Timetag.of(p.substring(p.indexOf("/"), p.lastIndexOf("/"))))
					.collect(toSet()));
		}

		@Override
		public InputStream read(String path) {
			return new ByteArrayInputStream(content.getOrDefault(path, new ByteArrayOutputStream()).toByteArray());
		}

		@Override
		public OutputStream write(String path) {
			content.put(path, new ByteArrayOutputStream());
			return content.get(path);
		}

		@Override
		public boolean exists(String path) {
			return content.containsKey(path);
		}
	}
}
