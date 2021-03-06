package io.intino.alexandria.sealing;

import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

class FileStage implements Stage {
	private final File stageFolder;

	FileStage(File stageFolder) {
		this.stageFolder = stageFolder;
		this.stageFolder.mkdirs();
	}

	@Override
	public void push(Stream<Session> sessions) {
		sessions.forEach(s -> push(s, stageFolder));
	}

	public void clear() {
		FS.allFilesIn(stageFolder, file -> file.isFile() && isNotTreated(file)).forEach(f -> f.renameTo(new File(f.getAbsolutePath() + ".treated")));
		FS.foldersIn(stageFolder).filter(f -> !f.getName().equals("temp") && !f.getName().startsWith("treated.")).forEach(f -> f.renameTo(new File(f.getParentFile().getAbsolutePath() + File.separator + "treated." + f.getName())));
	}

	private boolean isNotTreated(File file) {
		return !file.getName().endsWith(".treated");
	}

	private void push(Session session, File stageFolder) {
		FS.copyInto(fileFor(session, stageFolder), session.inputStream());
	}

	private File fileFor(Session session, File stageFolder) {
		return new File(stageFolder, filename(session));
	}

	private String filename(Session session) {
		return session.name() + "." + session.type() + Session.SessionExtension;
	}

	public Stream<Session> sessions() {
		return files().map(FileSession::new);
	}

	private Stream<File> files() {
		return FS.allFilesIn(stageFolder, this::sessions);
	}

	private boolean sessions(File file) {
		return file.isDirectory() || file.getName().endsWith(Session.SessionExtension);
	}

	private static class FileSession implements Session {

		private final File file;
		private final Type type;

		FileSession(File file) {
			this.file = file;
			this.type = typeOf(file.getName());
		}

		@Override
		public String name() {
			String name = file.getName();
			return name.substring(0, name.lastIndexOf("."));
		}

		private Type typeOf(String filename) {
			return stream(Type.values())
					.filter(type -> filename.endsWith(extensionOf(type)))
					.findFirst()
					.orElse(null);
		}

		private String extensionOf(Type type) {
			return "." + type.name() + SessionExtension;
		}

		@Override
		public Type type() {
			return type;
		}

		@Override
		public InputStream inputStream() {
			return new BufferedInputStream(inputStreamOfFile());
		}

		private InputStream inputStreamOfFile() {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				Logger.error(e);
				return new ByteArrayInputStream(new byte[0]);
			}
		}

	}
}

