package io.intino.konos.datalake.session;


import io.intino.konos.datalake.NessAccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.UUID.randomUUID;
import static java.util.stream.Stream.empty;

public class FileStage implements NessAccessor.Stage {
	public static final String Extension = "Session";
	private final File root;

	public FileStage(File root) throws FileNotFoundException {
		if (!root.exists()) throw new FileNotFoundException();
		this.root = root;
	}

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

	private static String extensionOf(Type type) {
		return "." + type.name() + Extension;
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

		private boolean test(Type type) {
			return file.getName().endsWith(extensionOf(type));
		}

		@Override
		public void commit() {
			//TODO
		}
	}
}
