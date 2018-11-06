package io.intino.alexandria.nessaccesor.tcp;

import io.intino.ness.core.Datalake;
import io.intino.ness.core.Stage;

public class TcpDatalake implements Datalake {

	private final Connection connection;

	public TcpDatalake(String uri, String username, String password, String clientId) {
		this.connection = new Connection(uri, username, password, clientId);
	}

	@Override
	public Datalake.Connection connection() {
		return null;//TODO
	}

	@Override
	public EventStore eventStore() {
		return null;
	}

	@Override
	public SetStore setStore() {
		return null;
	}

	@Override
	public void push(Stage stage) {

	}

	@Override
	public void seal() {

	}

	public static class Connection implements Datalake.Connection {
		private final String uri;
		private final String username;
		private final String password;
		private final String clientId;

		public Connection(String uri, String username, String password, String clientId) {
			this.uri = uri;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
		}

		@Override
		public void connect() {

		}

		@Override
		public void disconnect() {

		}
	}
}
