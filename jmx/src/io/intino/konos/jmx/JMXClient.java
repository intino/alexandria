package io.intino.konos.jmx;

import com.sun.tools.attach.*;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class JMXClient {
	private static final Logger logger = Logger.getGlobal();
	private static final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";
	private JMXServiceURL url = null;


	public JMXClient(String serverDirection, int port) {
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + serverDirection + ":" + port + "/jmxrmi");
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public JMXClient(String url) {
		try {
			this.url = new JMXServiceURL(url);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
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
				logger.log(Level.SEVERE, e.getMessage(), e);
				this.beans = new ArrayList<>();
			}
		}

		public <T> T mBean(Class<T> mbClass) {
			return mBean(mbClass, findObjectName(mbClass.getName()));
		}

		public <T> T mBean(Class<T> mbClass, ObjectName objectName) {
			if (objectName == null) {
				logger.severe("ObjectName is null");
				return null;
			}
			return JMX.isMXBeanInterface(mbClass) ?
					JMX.newMXBeanProxy(connection, objectName, mbClass, true) :
					JMX.newMBeanProxy(connection, objectName, mbClass, true);
		}

		public ObjectName findObjectName(String mbClass) {
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

		public List<String> attributes(ObjectName objectName) {
			List<String> attributes = new ArrayList<>();
			try {
				for (MBeanAttributeInfo info : connection.getMBeanInfo(objectName).getAttributes()) {
					final Object attribute = connection.getAttribute(objectName, info.getName());
					attributes.add(info.getName() + ": " + resolveValue(attribute, (OpenType) info.getDescriptor().getFieldValue("openType")));
				}
			} catch (RuntimeException | MBeanException | AttributeNotFoundException | ReflectionException | IntrospectionException | IOException | InstanceNotFoundException ignored) {
			}
			return attributes;
		}

		public List<String> operations(ObjectName objectName) {
			List<String> operations = new ArrayList<>();
			try {
				for (MBeanOperationInfo info : connection.getMBeanInfo(objectName).getOperations())
					operations.add(info.getReturnType() + " " + info.getName() + "(" + String.join(", ", Arrays.stream(info.getSignature()).map(mb -> mb.getType() + " " + mb.getName()).collect(Collectors.toList())) + ")");
			} catch (RuntimeException | ReflectionException | IntrospectionException | IOException | InstanceNotFoundException ignored) {
			}
			return operations;
		}

		public List<MBeanOperationInfo> operationInfos(ObjectName objectName) {
			List<MBeanOperationInfo> operations = new ArrayList<>();
			try {
				return Arrays.asList(connection.getMBeanInfo(objectName).getOperations());
			} catch (RuntimeException | ReflectionException | IntrospectionException | IOException | InstanceNotFoundException ignored) {
			}
			return operations;
		}

		public Object invokeOperation(ObjectName objectName, MBeanOperationInfo info, Object[] parameters) {
			final String[] signature = Arrays.stream(info.getSignature()).map(MBeanParameterInfo::getType).toArray(String[]::new);
			try {
				return connection.invoke(objectName, info.getName(), parameters, signature);
			} catch (InstanceNotFoundException | MBeanException | IOException | ReflectionException e) {
				return null;
			}
		}

		private String resolveValue(Object attribute, OpenType openType) {
			if (attribute instanceof CompositeDataSupport)
				return resolveCompositeValue((CompositeDataSupport) attribute, ((CompositeType) openType).keySet());
			else if (attribute != null)
				return attribute.toString();
			return "null";
		}

		private String resolveCompositeValue(CompositeDataSupport data, Collection<String> fieldNames) {
			Map<String, Object> map = new LinkedHashMap<>();
			for (String fieldName : fieldNames) map.put(fieldName, data.get(fieldName));
			return contentString(map);
		}

		private String contentString(Map<String, Object> contents) {
			StringBuilder sb = new StringBuilder("{");
			String sep = "";
			for (Map.Entry<String, Object> entry : contents.entrySet()) {
				sb.append(sep).append(entry.getKey()).append("=");
				String s = Arrays.deepToString(new Object[]{entry.getValue()});
				sb.append(s.substring(1, s.length() - 1));
				sep = ", ";
			}
			sb.append("}");
			return sb.toString();
		}


	}
}
