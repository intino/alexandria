package io.intino.alexandria.terminal;

import io.intino.alexandria.jms.ConnectionConfig;

import java.io.File;

public class ConnectorFactory {

	public static Connector createConnector(ConnectionConfig config, File outboxDirectory) {
		if (config.url().equals("test")) return new StubConnector(outboxDirectory);
		return new JmsConnector(config, outboxDirectory);
	}
}
