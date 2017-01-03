package io.intino.pandora.jmx;

import com.sun.tools.attach.*;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;


public class JMXClient {

	private static final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";
	private JMXServiceURL url = null;


	public JMXClient(String serverDirection, int port) {
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + serverDirection + ":" + port + "/jmxrmi");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public JMXClient(String url) {
		try {
			this.url = new JMXServiceURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> allJMXLocalURLs() throws IOException, AgentLoadException, AgentInitializationException {
		Map<String, String> set = new LinkedHashMap<>();
		List<VirtualMachineDescriptor> vms = VirtualMachine.list();
		for (VirtualMachineDescriptor desc : vms) {
			VirtualMachine vm;
			try {
				vm = VirtualMachine.attach(desc);
			} catch (AttachNotSupportedException e) {
				continue;
			}
			Properties props = vm.getAgentProperties();
			String connectorAddress = props.getProperty(CONNECTOR_ADDRESS);
			if (connectorAddress == null) {
				String agent = vm.getSystemProperties().getProperty("java.home") +
						File.separator + "lib" + File.separator + "management-agent.jar";
				vm.loadAgent(agent);
				connectorAddress = vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
			}
			if (connectorAddress != null) set.put(desc.displayName(), connectorAddress);
		}
		return set;
	}

	public JMXConnection connect() throws IOException {
		if (url == null) throw new IOException("url cannot be null");
		JMXConnector connector = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection connection = connector.getMBeanServerConnection();
		return new JMXConnection(connection, connector);
	}


	public class JMXConnection {
		private final MBeanServerConnection connection;
		private final JMXConnector connector;
		private List<ObjectName> beans;

		JMXConnection(MBeanServerConnection connection, JMXConnector connector) {
			this.connection = connection;
			this.connector = connector;
			try {
				this.beans = new ArrayList<>(this.connection.queryNames(null, null));
			} catch (IOException e) {
				this.beans = new ArrayList<>();
			}
		}

		public <T> T mBean(Class<T> mbClass) {
			ObjectName objectName = findObjectName(mbClass.getName());
			if (objectName == null) {
				System.err.println("MBClass not registered");
				return null;
			}
			return JMX.newMBeanProxy(connection, objectName, mbClass, true);
		}

		private ObjectName findObjectName(String mbClass) {
			for (ObjectName bean : beans)
				if (bean.getCanonicalName().contains(mbClass)) return bean;
			return null;
		}

		public void registerNotificatorFor(String mbClass, NotificationListener listener) {
			try {
				connection.addNotificationListener(findObjectName(mbClass), listener, null, null);
			} catch (InstanceNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}

		public void removeNotificatorFor(String mbClass, NotificationListener listener) throws IOException {
			try {
				connection.removeNotificationListener(findObjectName(mbClass), listener);
			} catch (InstanceNotFoundException | ListenerNotFoundException e) {
				e.printStackTrace();
			}
		}

		public void close() {
			try {
				connector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}
}
