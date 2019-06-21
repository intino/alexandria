package io.intino.alexandria.datahub.bus;

import io.intino.alexandria.datahub.model.Broker;
import io.intino.alexandria.logger.Logger;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.virtual.CompositeTopic;
import org.apache.activemq.broker.region.virtual.VirtualDestination;
import org.apache.activemq.broker.region.virtual.VirtualDestinationInterceptor;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.network.jms.InboundTopicBridge;
import org.apache.activemq.network.jms.JmsConnector;
import org.apache.activemq.network.jms.OutboundTopicBridge;
import org.apache.activemq.network.jms.SimpleJmsTopicConnector;
import org.apache.activemq.plugin.java.JavaRuntimeConfigurationBroker;
import org.apache.activemq.plugin.java.JavaRuntimeConfigurationPlugin;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class BusService {
	private static final String NESS = "ness";

	private final int brokerPort;
	private final int mqttPort;
	private final boolean persistent;
	private final File brokerStore;
	private final Map<String, String> users;
	private final List<Broker.JmsConnector> connectors;
	private final List<SimpleJmsTopicConnector> activeMQTopicConnectors;

	private JavaRuntimeConfigurationPlugin configurationPlugin;
	private SimpleAuthenticationPlugin authenticator;
	private BrokerService service;
	private Map<String, VirtualDestinationInterceptor> pipes = new HashMap<>();
	private JavaRuntimeConfigurationBroker confBroker;

	public BusService(int brokerPort, int mqttPort, boolean persistent, File brokerStore, Map<String, String> users, List<Broker.JmsConnector> connectors) {
		this.brokerPort = brokerPort;
		this.mqttPort = mqttPort;
		this.persistent = persistent;
		this.brokerStore = getCanonicalFile(brokerStore);
		this.users = users;
		this.connectors = connectors;
		this.activeMQTopicConnectors = new ArrayList<>();
	}

	public Map<String, List<String>> users() {
		Map<String, List<String>> users = new LinkedHashMap<>();
		Map<String, Set<Principal>> userGroups = authenticator.getUserGroups();
		for (String user : userGroups.keySet())
			if (!user.equals(NESS))
				users.put(user, userGroups.get(user).stream().map(Principal::getName).collect(toList()));
		return users;
	}

	public String newUser(String name) {
		if (authenticator.getUserPasswords().containsKey(name)) return null;
		String password = PasswordGenerator.nextPassword();
		authenticator.getUserPasswords().put(name, password);
		authenticator.getUserGroups().put(name, Collections.emptySet());
		return password;
	}

	public void removeUser(String name) {
		if (name.equals(NESS)) return;
		authenticator.getUserGroups().remove(name);
		authenticator.getUserPasswords().remove(name);
	}

	public void addJmsConnector(Broker.JmsConnector c) {
		try {
			SimpleJmsTopicConnector connector = new SimpleJmsTopicConnector();
			connector.setName(c.name());
			connector.setLocalTopicConnectionFactory(new ActiveMQConnectionFactory(NESS, NESS, "vm://ness?waitForStart=1000&create=false"));
			connector.setOutboundTopicConnectionFactory(new ActiveMQConnectionFactory(c.bus().user(), c.bus().password(), c.bus().originURL()));
			connector.setOutboundUsername(c.bus().user());
			connector.setOutboundPassword(c.bus().password());
			connector.setInboundTopicBridges(toInboundBridges(c.topics()));
//			else connector.setOutboundTopicBridges(toOutboundBridges(c.topics()));
			service.addJmsConnector(connector);
			this.activeMQTopicConnectors.add(connector);
			Logger.info("Connector with " + c.bus().originURL() + " started");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public List<JmsConnector> jmsConnectors() {
		return new ArrayList<>(this.activeMQTopicConnectors);
	}

	public List<String> topics() {
		try {
			return Arrays.stream(service.getBroker().getDestinations())
					.map(ActiveMQDestination::getPhysicalName).filter(n -> !n.contains("ActiveMQ.Advisory")).collect(Collectors.toList());
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public void start() {
		try {
			Logger.info("starting broker...");
			this.service = new BrokerService();
			this.authenticator = new SimpleAuthenticationPlugin(initUsers(users));
			this.configurationPlugin = new JavaRuntimeConfigurationPlugin();
			configure(persistent, connectors);
			this.service.start();
			this.service.waitUntilStarted();
			this.confBroker = (JavaRuntimeConfigurationBroker) service.getBroker().getAdaptor(JavaRuntimeConfigurationBroker.class);
			Logger.info("broker started!");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public boolean isPersistent() {
		return service.isPersistent();
	}

	public void stop() {
		try {
			if (service != null) {
				service.stop();
				service.waitUntilStopped();
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}


	public void pipe(String from, String to) {
		try {
			CompositeTopic compositeTopic = new CompositeTopic();
			compositeTopic.setName(from);
			compositeTopic.setForwardOnly(false);
			compositeTopic.setForwardTo(singletonList(new ActiveMQTopic(to)));
			VirtualDestinationInterceptor newInterceptor = new VirtualDestinationInterceptor();
			newInterceptor.setVirtualDestinations(new VirtualDestination[]{compositeTopic});
			pipes.put(from + "#" + to, newInterceptor);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	void stopPipe(String fromTopic, String toTopic) {
		if (!pipes.containsKey(fromTopic + "#" + toTopic)) return;
		pipes.remove(fromTopic + "#" + toTopic);
		updateInterceptors();
	}

	public Map<String, VirtualDestinationInterceptor> pipes() {
		return pipes;
	}

	ActiveMQDestination findTopic(String topic) {
		try {
			ActiveMQDestination[] destinations = service.getBroker().getDestinations();
			if (destinations == null) return null;
			return Arrays.stream(destinations).filter(d -> d.getPhysicalName().equals(topic)).findFirst().orElse(null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
	}

	void removeTopic(ActiveMQDestination destination) {
		try {
			if (destination != null) service.getBroker().removeDestination(service.getAdminConnectionContext(), destination, 0);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	org.apache.activemq.broker.Broker broker() {
		try {
			return service.getBroker();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
	}

	private OutboundTopicBridge[] toOutboundBridges(List<String> outboundTopics) {
		return outboundTopics.stream().map(topicName -> {
			OutboundTopicBridge bridge = new OutboundTopicBridge(topicName);
			bridge.setLocalTopicName(topicName);
			return bridge;
		}).toArray(OutboundTopicBridge[]::new);
	}

	private InboundTopicBridge[] toInboundBridges(List<String> inboundTopics) {
		return inboundTopics.stream().map(topicName -> {
			InboundTopicBridge bridge = new InboundTopicBridge(topicName);
			bridge.setLocalTopicName(topicName);
			return bridge;
		}).toArray(InboundTopicBridge[]::new);
	}

	private List<AuthenticationUser> initUsers(Map<String, String> modelUsers) {
		ArrayList<AuthenticationUser> users = new ArrayList<>();
		users.add(new AuthenticationUser(NESS, NESS, "admin"));
		for (Map.Entry<String, String> entry : modelUsers.entrySet())
			users.add(new AuthenticationUser(entry.getKey(), entry.getValue(), "users"));
		return users;
	}

	private void configure(boolean persistence, List<Broker.JmsConnector> connectors) {
		try {
			service.setBrokerName(NESS);
			persistent(persistence);
			service.setDataDirectory(new File(brokerStore, "activemq-data").getAbsolutePath());
			service.setRestartAllowed(true);
			service.setUseJmx(true);
			service.setUseShutdownHook(true);
			service.setAdvisorySupport(true);
			service.setPlugins(new BrokerPlugin[]{authenticator, configurationPlugin});
			addPolicies();
			addTCPConnector();
			addMQTTConnector();
			registerConnectors(connectors);
		} catch (Exception e) {
			Logger.error("Error configuring: " + e.getMessage(), e);
		}
	}

	void persistent(boolean persistence) {
		try {
			service.setPersistent(persistence);
			service.setPersistenceAdapter(persistence ? persistenceAdapter() : null);
		} catch (IOException ignored) {
		}
	}

	private void registerConnectors(List<Broker.JmsConnector> connectors) {
		connectors.forEach(this::addJmsConnector);
	}

	private void updateInterceptors() {
		try {
			final VirtualDestinationInterceptor[] interceptors = pipes.values().toArray(new VirtualDestinationInterceptor[0]);
			service.setDestinationInterceptors(interceptors);
			if (confBroker == null) return;
			List<VirtualDestination> destinations = new ArrayList<>();
			Arrays.stream(interceptors).forEach(i -> Collections.addAll(destinations, i.getVirtualDestinations()));
			confBroker.setVirtualDestinations(destinations.toArray(new VirtualDestination[0]), false);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	private void addPolicies() {
		final List<PolicyEntry> policyEntries = new ArrayList<>();
		final PolicyEntry entry = new PolicyEntry();
		entry.setAdvisoryForDiscardingMessages(true);
		entry.setTopicPrefetch(1);
		ConstantPendingMessageLimitStrategy pendingMessageLimitStrategy = new ConstantPendingMessageLimitStrategy();
		pendingMessageLimitStrategy.setLimit(1000000);
		entry.setPendingMessageLimitStrategy(pendingMessageLimitStrategy);
		final PolicyMap policyMap = new PolicyMap();
		policyMap.setPolicyEntries(policyEntries);
		service.setDestinationPolicy(policyMap);
	}

	private void addTCPConnector() throws Exception {
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://0.0.0.0:" + brokerPort + "?transport.useKeepAlive=true"));
		connector.setName("OWireConn");
		service.addConnector(connector);
	}

	private void addMQTTConnector() throws Exception {
		TransportConnector mqtt = new TransportConnector();
		mqtt.setUri(new URI("mqtt://0.0.0.0:" + mqttPort));
		mqtt.setName("MQTTConn");
		service.addConnector(mqtt);
	}

	private KahaDBPersistenceAdapter persistenceAdapter() {
		KahaDBPersistenceAdapter adapter = new KahaDBPersistenceAdapter();
		adapter.setDirectoryArchive(new File(brokerStore, "archive"));
		adapter.setIndexDirectory(new File(brokerStore, "entries"));
		adapter.setDirectory(brokerStore);
		adapter.setBrokerName(NESS);
		adapter.setBrokerService(service);
		return adapter;
	}

	private File getCanonicalFile(File brokerStore) {
		try {
			return brokerStore.getCanonicalFile();
		} catch (IOException e) {
			return brokerStore;
		}
	}

	static final class PasswordGenerator {
		static String nextPassword() {
			return new BigInteger(130, new SecureRandom()).toString(32).substring(0, 12);
		}
	}
}
