package io.intino.alexandria.nessaccesor.fs;

import io.intino.alexandria.nessaccesor.NessAccessor;

import java.io.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.UUID.randomUUID;
import static java.util.stream.Stream.empty;

public class FSStage implements NessAccessor.Stage {
	public static final String Extension = "Session";
	private final File root;

	public FSStage(File root) {
		root.mkdirs();
		this.root = root;
	}

	private static String extensionOf(Type type) {
		return "." + type.name() + Extension;
	}

	@Override
	public OutputStream start(NessAccessor.Stage.Type type) {
		try {
			return new FileOutputStream(file(type));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private File file(Type type) {
		return new File(root, randomUUID().toString() + extensionOf(type));
	}

	public Stream<Session> sessions() {
		return files().map(FileSession::new);
	}

	private Stream<File> files() {
		File[] files = root.listFiles(this::sessions);
		return files == null ? empty() : stream(files);
	}

	private boolean sessions(File dir, String name) {
		return name.endsWith(Extension);
	}

	private static class FileSession implements Session {

		private File file;

		public FileSession(File file) {
			this.file = file;
		}

		@Override
		public Type type() {
			return stream(Type.values()).filter(this::test).findFirst().orElse(null);
		}

		@Override
		public InputStream inputStream() {
			try {
				return new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public void remove() {
			file.delete();
		}

		private boolean test(Type type) {
			return file.getName().endsWith(extensionOf(type));
		}

		//TODO
	}
}
