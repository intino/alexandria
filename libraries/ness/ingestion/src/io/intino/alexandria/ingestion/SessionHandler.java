package io.intino.alexandria.ingestion;

import io.intino.alexandria.Session;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.UUID.randomUUID;

public class SessionHandler {
	private final File root;
	private final List<PrivateSession> sessions = new ArrayList<>();

	public SessionHandler() {
		this.root = null;
	}

	public SessionHandler(File root) {
		this.root = root;
		this.root.mkdirs();
		this.sessions.addAll(loadFileSessions());
	}

	public SetSession createSetSession() {
		PrivateProvider provider = new PrivateProvider();
		return new SetSession(provider);
	}

	public SetSession createSetSession(int autoFlushSize) {
		PrivateProvider provider = new PrivateProvider();
		return new SetSession(provider, autoFlushSize);
	}

	public EventSession createEventSession() {
		return new EventSession(new PrivateProvider());
	}

	public TransactionSession createTransactionSession() {
		return new TransactionSession(new PrivateProvider());
	}

	public TransactionSession createTransactionSession(int transactionBufferSize) {
		return new TransactionSession(new PrivateProvider(), transactionBufferSize);
	}

	public void pushTo(URI uri) {
		//TODO
	}

	public void pushTo(File stageFolder) {
		File destination = new File(stageFolder, root.getName());
		destination.mkdirs();
		sessions().forEach(s -> push(s, destination));
	}

	public void clear() {
		sessions.stream().filter(s -> s.data() instanceof FileSessionData).forEach(s -> ((FileSessionData) s.data()).file().delete());
		sessions.clear();
	}

	public Stream<Session> sessions() {
		return sessions.stream()
				.map(s -> new Session() {
					@Override
					public String name() {
						return s.name();
					}

					@Override
					public Type type() {
						return s.type();
					}

					@Override
					public InputStream inputStream() {
						return s.inputStream();
					}
				});
	}

	private List<PrivateSession> loadFileSessions() {
		return sessionFiles()
				.map(f -> new PrivateSession(name(f), typeOf(f), new FileSessionData(f))).collect(Collectors.toList());
	}

	private Stream<File> sessionFiles() {
		return this.root == null ? Stream.empty() : FS.allFilesIn(root, path -> path.getName().endsWith(Session.SessionExtension));
	}

	private String name(File f) {
		return f.getName().substring(0, f.getName().indexOf(typeOf(f).name()) - 1);
	}

	private Session.Type typeOf(File f) {
		String[] split = f.getName().split("\\.");
		return Session.Type.valueOf(split[split.length - 2]);
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

	private interface SessionData {
		InputStream inputStream();

		OutputStream outputStream();

		File outputFile();
	}

	public interface Provider {
		OutputStream outputStream(Session.Type type);

		OutputStream outputStream(String name, Session.Type type);

		File file(String name, Session.Type type);
	}

	private static class FileSessionData implements SessionData {
		private final File file;

		FileSessionData(File file) {
			this.file = file;
		}

		@Override
		public InputStream inputStream() {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				Logger.error(e);
				return null;
			}
		}

		public File file() {
			return file;
		}

		@Override
		public OutputStream outputStream() {
			try {
				if (!file.exists()) file.createNewFile();
				return new BufferedOutputStream(new FileOutputStream(file));
			} catch (IOException e) {
				Logger.error(e);
				return null;
			}
		}

		@Override
		public File outputFile() {
			return file;
		}
	}

	private static class MemorySessionData implements SessionData {

		private final ByteArrayOutputStream outputStream;

		public MemorySessionData() {
			this.outputStream = new ByteArrayOutputStream();
		}

		@Override
		public InputStream inputStream() {
			return new ByteArrayInputStream(outputStream.toByteArray());
		}

		@Override
		public OutputStream outputStream() {
			return outputStream;
		}

		@Override
		public File outputFile() {
			return null;
		}
	}

	private static class PrivateSession {
		private final String name;
		private final Session.Type type;
		private final SessionData sessionData;


		PrivateSession(String name, Session.Type type, SessionData sessionData) {
			this.name = name;
			this.type = type;
			this.sessionData = sessionData;
		}

		public String name() {
			return name;
		}

		public Session.Type type() {
			return type;
		}

		SessionData data() {
			return sessionData;
		}

		InputStream inputStream() {
			return sessionData.inputStream();
		}

		OutputStream outputStream() {
			return sessionData.outputStream();
		}

		public File file() {
			return sessionData.outputFile();
		}
	}

	private class PrivateProvider implements Provider {

		public OutputStream outputStream(Session.Type type) {
			return outputStream("", type);
		}

		public OutputStream outputStream(String name, Session.Type type) {
			PrivateSession session = session(name + suffix(), type);
			sessions.add(session);
			return session.outputStream();
		}

		@Override
		public File file(String name, Session.Type type) {
			PrivateSession session = session(name + suffix(), type);
			sessions.add(session);
			return session.file();
		}

		private PrivateSession session(String name, Session.Type type) {
			return new PrivateSession(name, type, root == null ? new MemorySessionData() : new FileSessionData(fileOf(name, type)));
		}

		private File fileOf(String name, Session.Type type) {
			return new File(root, filename(name, type));
		}

		private String filename(String name, Session.Type type) {
			return name + extensionOf(type);
		}

		private String suffix() {
			return "#" + randomUUID().toString();
		}

		private String extensionOf(Session.Type type) {
			return "." + type.name() + Session.SessionExtension;
		}
	}
}
