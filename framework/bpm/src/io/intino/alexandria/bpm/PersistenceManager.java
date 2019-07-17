package io.intino.alexandria.bpm;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public interface PersistenceManager {

	void delete(String path);

	InputStream read(String path);

	OutputStream write(String path);

	class FilePersistenceManager implements PersistenceManager {
		private final File directory;

		public FilePersistenceManager(File directory) {
			this.directory = directory;
		}

		@Override
		public void delete(String path) {
			new File(directory, path).delete();
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
	}

	class InMemoryPersistenceManager implements PersistenceManager {
		private final Map<String, ByteArrayOutputStream> content = new HashMap<>();

		@Override
		public void delete(String path) {
			content.remove(path);
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
	}
}
