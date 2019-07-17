package io.intino.alexandria.datahub.model;

import io.intino.alexandria.Scale;
import io.intino.alexandria.message.MessageHub;

import java.util.List;

public class Configuration {
	private final DataSource dataSource;
	private final Tanks tanks;
	private String workingDirectory;
	private Broker broker;

	public Configuration(String workingDirectory, Broker broker, DataSource dataSource, Tanks tanks) {
		this.workingDirectory = workingDirectory;
		this.broker = broker;
		this.dataSource = dataSource;
		this.tanks = tanks;
	}

	public String workingDirectory() {
		return workingDirectory;
	}

	public Broker broker() {
		return broker;
	}

	public DataSource dataSource() {
		return dataSource;
	}

	public Tanks tanks() {
		return tanks;
	}

	public Configuration workingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
		return this;
	}

	public Configuration broker(Broker broker) {
		return this;
	}


	public static class Tanks {
		private List<Split> splits;
		private List<Tank> tanks;

		public Tanks(List<Split> splits, List<Tank> tanks) {
			this.splits = splits;
			this.tanks = tanks;
		}

		public List<Split> splits() {
			return splits;
		}

		public List<Tank> tanks() {
			return tanks;
		}

		public static class Tank {
			private String name;
			private Type type;
			private String split;

			public Tank(String name, Type type, String split) {
				this.name = name;
				this.type = type;
				this.split = split;
			}

			public String name() {
				return name;
			}

			public Type type() {
				return type;
			}

			public String split() {
				return split;
			}

			public enum Type {
				Event, Set, Singleton
			}
		}

		public static class Split {
			private String name;
			private List<String> values;

			public Split(String name, List<String> values) {
				this.name = name;
				this.values = values;
			}

			public String name() {
				return name;
			}

			public List<String> values() {
				return values;
			}
		}
	}

	public static class Local extends DataSource {
		private String path;

		public Local(String path) {
			this.path = path;
		}

		public String path() {
			return path;
		}
	}

	public static class Remote extends DataSource {
		private final MessageHub messageHub;
		private String path;

		public Remote(MessageHub messageHub) {
			this.messageHub = messageHub;
		}

		public MessageHub messageHub() {
			return messageHub;
		}
	}


	public static class StandAlone extends DataSource {
		private String path;
		private Scale scale;
		private String sealingPattern;

		public StandAlone(String path, Scale scale, String sealingPattern) {
			this.path = path;
			this.scale = scale;
			this.sealingPattern = sealingPattern;
		}

		public String path() {
			return path;
		}

		public Scale scale() {
			return scale;
		}

		public String sealingPattern() {
			return sealingPattern;
		}

	}

	public static class Mirror extends DataSource {
		private final String startingTimetag;
		private String originUrl;
		private String datalakeOriginPath;
		private String user;
		private String password;
		private String datalakeDestinationPath;
		private MessageHub messageHub;

		public Mirror(String originUrl, String datalakeOriginPath, String datalakeDestinationPath, String startingTimetag, String user, String password, MessageHub messageHub) {
			this.originUrl = originUrl;
			this.datalakeOriginPath = datalakeOriginPath;
			this.startingTimetag = startingTimetag;
			this.user = user;
			this.password = password;
			this.datalakeDestinationPath = datalakeDestinationPath;
			this.messageHub = messageHub;
		}

		public String originUrl() {
			return originUrl;
		}

		public String user() {
			return user;
		}

		public String password() {
			return password;
		}

		public String datalakeOriginPath() {
			return datalakeOriginPath;
		}

		public String datalakeDestinationPath() {
			return datalakeDestinationPath;
		}

		public String startingTimetag() {
			return startingTimetag;
		}

		public MessageHub messageHub() {
			return messageHub;
		}
	}


	public static abstract class DataSource {
	}
}
