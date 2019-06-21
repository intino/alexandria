package io.intino.alexandria.datahub.model;

import java.util.List;

public class Broker {

	private int port;
	private int mqtt_port;
	private String connectorId;
	private List<Broker.User> users;
	private List<Broker.Pipe> pipes;


	public Broker(int port, int mqtt_port, String connectorId, List<User> users, List<Pipe> pipes) {
		this.port = port;
		this.mqtt_port = mqtt_port;
		this.connectorId = connectorId;
		this.users = users;
		this.pipes = pipes;
	}

	public int port() {
		return port;
	}

	public int mqtt_port() {
		return mqtt_port;
	}

	public String connectorId() {
		return connectorId;
	}

	public List<User> users() {
		return users;
	}

	public List<Pipe> pipes() {
		return pipes;
	}

	public static class User {
		String name;
		String password;

		public User(String name, String password) {
			this.name = name;
			this.password = password;
		}

		public String name() {
			return name;
		}

		public String password() {
			return password;
		}
	}

	public static class JmsConnector {
		String name;
		private ExternalBus bus;
		private List<String> topics;

		public JmsConnector(String name, ExternalBus bus, List<String> topics) {
			this.name = name;
			this.bus = bus;
			this.topics = topics;
		}

		public String name() {
			return name;
		}

		public ExternalBus bus() {
			return bus;
		}

		public List<String> topics() {
			return topics;
		}
	}

	public static class ExternalBus {
		private String originURL;
		private String sessionId;
		private String user;
		private String password;

		public ExternalBus(String originURL, String sessionId, String user, String password) {
			this.originURL = originURL;
			this.sessionId = sessionId;
			this.user = user;
			this.password = password;
		}

		public String originURL() {
			return originURL;
		}

		public String sessionId() {
			return sessionId;
		}

		public String user() {
			return user;
		}

		public String password() {
			return password;
		}
	}

	public static class Pipe {
		private String origin;
		private String destination;

		public Pipe(String origin, String destination) {
			this.origin = origin;
			this.destination = destination;
		}

		public String origin() {
			return origin;
		}

		public String destination() {
			return destination;
		}
	}
}
