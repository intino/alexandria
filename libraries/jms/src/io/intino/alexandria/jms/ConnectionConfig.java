package io.intino.alexandria.jms;

import java.io.File;

public class ConnectionConfig {
	private final String url;
	private final String user;
	private final String password;
	private final String clientId;
	private final File keyStore;
	private final File trustStore;
	private final char[] keyStorePassword;
	private final char[] trustStorePassword;

	public ConnectionConfig(String url, String user, String password, String clientId) {
		this(url, user, password, clientId, null, null, null, null);
	}

	public ConnectionConfig(String url, String user, String password, String clientId, File keyStore, File trustStore, char[] keyStorePassword, char[] trustStorePassword) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.clientId = clientId;
		this.keyStore = keyStore;
		this.trustStore = trustStore;
		this.keyStorePassword = keyStorePassword;
		this.trustStorePassword = trustStorePassword;
	}

	public boolean hasSSlCredentials() {
		return keyStore != null;
	}

	public String url() {
		return url;
	}

	public String user() {
		return user;
	}

	public String password() {
		return password;
	}

	public String clientId() {
		return clientId;
	}

	public File keyStore() {
		return keyStore;
	}

	public File trustStore() {
		return trustStore;
	}

	public char[] keyStorePassword() {
		return keyStorePassword;
	}

	public char[] trustStorePassword() {
		return trustStorePassword;
	}
}
