package io.intino.alexandria.terminal.remotedatalake;

import io.intino.alexandria.terminal.JmsConnector;

import java.util.concurrent.atomic.AtomicReference;

public class DatalakeAccessor {

	private static final Object monitor = new Object();
	private final JmsConnector connector;

	public DatalakeAccessor(JmsConnector connector) {
		this.connector = connector;
	}

	String query(String query) {
		AtomicReference<String> response = new AtomicReference<>(null);
		synchronized (monitor) {
			connector.requestResponse("service.ness.datalake", query, s -> {
				synchronized (monitor) {
					monitor.notify();
				}
			});
			waitForResponse();
			return response.get();
		}
	}

	private static void waitForResponse() {
		try {
			monitor.wait(1000 * 30);
		} catch (InterruptedException e) {
			io.intino.alexandria.logger.Logger.error(e);
		}
	}

}
