import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.jms.ConnectionConfig;
import io.intino.alexandria.terminal.Broker;
import io.intino.alexandria.terminal.JmsConnector;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JmsTerminalTest {
	private static final String TOPIC = "terminal.jms.test.events";
	private static final String QUEUE = "terminal.jms.test.requests";
	private static final long TIMEOUT_SECONDS = 5;

	private final List<JmsConnector> connectors = new ArrayList<>();
	private BrokerService broker;
	private int brokerPort;
	private String brokerUrl;
	private Path testDirectory;

	@Before
	public void setUp() throws Exception {
		testDirectory = Files.createTempDirectory("terminal-jms-test");
		brokerPort = reservePort();
		brokerUrl = "tcp://127.0.0.1:" + brokerPort;
		startBroker();
	}

	@After
	public void tearDown() throws Exception {
		for (int i = connectors.size() - 1; i >= 0; i--) {
			connectors.get(i).stop();
		}
		stopBroker();
		deleteRecursively(testDirectory);
	}

	@Test
	public void shouldDetectEmbeddedBrokerAsRunning() {
		assertTrue(Broker.isRunning(brokerUrl));
	}

	@Test
	public void shouldSendEventsAfterRestartingSameConnectorInstance() throws Exception {
		JmsConnector sender = newConnector("sender");
		JmsConnector receiver = newConnector("receiver");
		CountDownLatch firstEvent = new CountDownLatch(1);
		CountDownLatch secondEvent = new CountDownLatch(1);
		receiver.attachListener(TOPIC, event -> {
			if (!(event instanceof MessageEvent messageEvent)) return;
			String payload = messageEvent.toMessage().get("payload").asString();
			if ("first".equals(payload)) firstEvent.countDown();
			if ("second".equals(payload)) secondEvent.countDown();
		});
		waitForConsumerRegistration();

		sender.sendEvent(TOPIC, event("first"));
		assertTrue("The first event should be delivered before restarting the connector", firstEvent.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));

		sender.stop();
		sender.start();
		waitForCondition(() -> sender.session() != null && sender.connection() != null, "Sender should reconnect after start()");
		waitForConsumerRegistration();

		sender.sendEvent(TOPIC, event("second"));
		assertTrue("The second event should be delivered after restarting the same connector instance", secondEvent.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
	}

	@Test
	public void shouldReconnectThroughHealthCheckWithoutStoppingDispatchers() throws Exception {
		JmsConnector sender = newConnector("sender");
		JmsConnector receiver = newConnector("receiver");
		CountDownLatch eventDelivered = new CountDownLatch(1);
		receiver.attachListener(TOPIC, event -> {
			if (!(event instanceof MessageEvent messageEvent)) return;
			if ("after-health-check".equals(messageEvent.toMessage().get("payload").asString()))
				eventDelivered.countDown();
		});
		waitForConsumerRegistration();

		invoke(sender, "closeJmsResources");
		invoke(sender, "checkConnection");
		waitForCondition(() -> sender.session() != null && sender.connection() != null, "Sender should reconnect after checkConnection()");

		sender.sendEvent(TOPIC, event("after-health-check"));
		assertTrue("The connector should keep dispatching messages after an internal reconnection", eventDelivered.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
	}

	@Test
	public void shouldRecoverEventOutboxWhenBrokerComesBack() throws Exception {
		stopBroker();
		Path senderDirectory = Files.createDirectories(testDirectory.resolve("sender-event-outbox"));
		JmsConnector sender = newConnector("sender-event-outbox", senderDirectory);

		sender.sendEvent(TOPIC, event("from-event-outbox"));
		waitForCondition(() -> hasFiles(senderDirectory.resolve("events")), "The event should be persisted in the event outbox while the broker is down");

		startBroker();
		JmsConnector receiver = newConnector("receiver-event-outbox");
		CountDownLatch eventDelivered = new CountDownLatch(1);
		receiver.attachListener(TOPIC, event -> {
			if (!(event instanceof MessageEvent messageEvent)) return;
			if ("from-event-outbox".equals(messageEvent.toMessage().get("payload").asString()))
				eventDelivered.countDown();
		});
		waitForConsumerRegistration();

		invoke(sender, "checkConnection");
		waitForCondition(() -> sender.session() != null && sender.connection() != null, "Sender should reconnect to flush the event outbox");
		assertTrue("The persisted event should be delivered after reconnection", eventDelivered.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
		waitForCondition(() -> isDirectoryEmpty(senderDirectory.resolve("events")), "The event outbox should be empty after successful recovery");
	}

	@Test
	public void shouldRecoverMessageOutboxWhenBrokerComesBack() throws Exception {
		stopBroker();
		Path senderDirectory = Files.createDirectories(testDirectory.resolve("sender-message-outbox"));
		JmsConnector sender = newConnector("sender-message-outbox", senderDirectory);

		sender.sendQueueMessage(QUEUE, "from-message-outbox");
		waitForCondition(() -> hasFiles(senderDirectory.resolve("requests")), "The message should be persisted in the message outbox while the broker is down");

		startBroker();
		JmsConnector receiver = newConnector("receiver-message-outbox");
		CountDownLatch messageDelivered = new CountDownLatch(1);
		receiver.attachListener(QUEUE, (message, callback) -> {
			if ("from-message-outbox".equals(message)) messageDelivered.countDown();
		});
		waitForConsumerRegistration();

		invoke(sender, "checkConnection");
		waitForCondition(() -> sender.session() != null && sender.connection() != null, "Sender should reconnect to flush the message outbox");
		assertTrue("The persisted queue message should be delivered after reconnection", messageDelivered.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
		waitForCondition(() -> isDirectoryEmpty(senderDirectory.resolve("requests")), "The message outbox should be empty after successful recovery");
	}

	private JmsConnector newConnector(String name) throws IOException {
		Path connectorDirectory = Files.createDirectories(testDirectory.resolve(name + "-" + UUID.randomUUID()));
		return newConnector(name, connectorDirectory);
	}

	private JmsConnector newConnector(String name, Path connectorDirectory) {
		JmsConnector connector = new JmsConnector(new ConnectionConfig(brokerUrl, "", "", name + "-" + UUID.randomUUID()), connectorDirectory.toFile());
		connectors.add(connector);
		connector.start();
		return connector;
	}

	private MessageEvent event(String payload) {
		MessageEvent event = new MessageEvent("TerminalJmsTest", "test");
		event.toMessage().set("payload", payload);
		return event;
	}

	private void waitForConsumerRegistration() throws InterruptedException {
		Thread.sleep(250);
	}

	private void waitForCondition(BooleanSupplier condition, String errorMessage) throws InterruptedException {
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
		while (System.nanoTime() < deadline) {
			if (condition.getAsBoolean()) return;
			Thread.sleep(50);
		}
		assertTrue(errorMessage, condition.getAsBoolean());
	}

	private void invoke(JmsConnector connector, String methodName) throws Exception {
		Method method = JmsConnector.class.getDeclaredMethod(methodName);
		method.setAccessible(true);
		method.invoke(connector);
	}

	private void startBroker() throws Exception {
		broker = new BrokerService();
		broker.setBrokerName("terminal-jms-" + UUID.randomUUID());
		broker.setPersistent(false);
		broker.setUseJmx(false);
		broker.setUseShutdownHook(false);
		broker.addConnector(brokerUrl);
		broker.start();
		broker.waitUntilStarted();
	}

	private void stopBroker() throws Exception {
		if (broker == null) return;
		broker.stop();
		broker.waitUntilStopped();
		broker = null;
	}

	private int reservePort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}

	private boolean hasFiles(Path directory) {
		if (directory == null || Files.notExists(directory) || !Files.isDirectory(directory)) return false;
		try (Stream<Path> files = Files.list(directory)) {
			return files.findAny().isPresent();
		} catch (IOException e) {
			return false;
		}
	}

	private boolean isDirectoryEmpty(Path directory) {
		return !hasFiles(directory);
	}

	private void deleteRecursively(Path path) throws IOException {
		if (path == null || Files.notExists(path)) return;
		try (Stream<Path> stream = Files.walk(path)) {
			stream.sorted(Comparator.reverseOrder()).forEach(p -> {
				try {
					Files.deleteIfExists(p);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		} catch (RuntimeException e) {
			if (e.getCause() instanceof IOException ioException) throw ioException;
			throw e;
		}
	}
}
