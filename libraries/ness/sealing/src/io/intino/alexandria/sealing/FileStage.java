package io.intino.alexandria.sealing;

import io.intino.alexandria.Session;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.Event.Format;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

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
		return session.name() + "." + session.format() + Session.SessionExtension;
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
		private final Format format;

		FileSession(File file) {
			this.file = file;
			this.format = formatOf(file.getName());
		}

		@Override
		public String name() {
			String name = file.getName();
			return name.substring(0, name.lastIndexOf("."));
		}

		private Format formatOf(String filename) {
			return Arrays.stream(Format.values())
					.filter(format -> filename.endsWith(extensionOf(format)))
					.findFirst()
					.orElse(null);
		}

		private String extensionOf(Format type) {
			return "." + type.name().toLowerCase() + SessionExtension;
		}

		@Override
		public Format format() {
			return format;
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

