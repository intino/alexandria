package io.intino.alexandria.nessaccesor.tcp;


import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;

import java.util.stream.Stream;

public class TcpDatalake implements Datalake {

	private final String uri;
	private final String username;
	private final String password;
	private final String clientId;

	public TcpDatalake(String uri, String username, String password, String clientId) {
		this.uri = uri;
		this.username = username;
		this.password = password;
		this.clientId = clientId;
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
	public void push(Stream<Blob> stream) {

	}

	@Override
	public void seal() {

	}
}
