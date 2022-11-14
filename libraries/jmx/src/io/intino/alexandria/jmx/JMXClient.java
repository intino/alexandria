package io.intino.alexandria.jmx;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import io.intino.alexandria.logger.Logger;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;


public class JMXClient {
	private static final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";
	private JMXServiceURL url = null;


	public JMXClient(String serverDirection, int port) {
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + serverDirection + ":" + port + "/jmxrmi");
		} catch (MalformedURLException e) {
			Logger.error(e);
		}
	}

	public JMXClient(String url) {
		try {
			this.url = new JMXServiceURL(url);
		} catch (MalformedURLException e) {
			Logger.error(e);
		}
	}

	public static Map<String, String> allJMXLocalURLs() {
		Map<String, String> set = new LinkedHashMap<>();
		List<VirtualMachineDescriptor> vms = VirtualMachine.list();
		for (VirtualMachineDescriptor desc : vms) {
			try {
				VirtualMachine vm = VirtualMachine.attach(desc);
				vm.startLocalManagementAgent();
				Properties props = vm.getAgentProperties();
				String connectorAddress = props.getProperty(CONNECTOR_ADDRESS);
				if (connectorAddress == null) continue;
				set.put(desc.displayName(), connectorAddress);
			} catch (AttachNotSupportedException | IOException ignored) {
			}
		}
		return set;
	}

	public JMXConnection connect() throws IOException {
		if (url == null) throw new IOException("url cannot be null");
		JMXConnector connector = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection connection = connector.getMBeanServerConnection();
		return new JMXConnection(connection, connector);
	}


	public class JMXConnection implements Closeable {
		private final MBeanServerConnection connection;
		private final JMXConnector connector;
		private List<ObjectName> beans;

		JMXConnection(MBeanServerConnection connection, JMXConnector connector) {
			this.connection = connection;
			this.connector = connector;
			try {
				this.beans = new ArrayList<>(this.connection.queryNames(null, null));
			} catch (IOException e) {
				Logger.error(e);
				this.beans = new ArrayList<>();
			}
		}

		public <T> T mBean(Class<T> mbClass) {
			return mBean(mbClass, findObjectName(mbClass.getName()));
		}

		public <T> T mBean(Class<T> mbClass, ObjectName objectName) {
			if (objectName == null) {
				Logger.error("ObjectName is null");
				return null;
			}
			return JMX.isMXBeanInterface(mbClass) ?
					JMX.newMXBeanProxy(connection, objectName, mbClass, true) :
					JMX.newMBeanProxy(connection, objectName, mbClass, true);
		}

		public List<ObjectName> beans() {
			return beans;
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
				Logger.error(e);
			}
		}

		public void removeNotificatorFor(String mbClass, NotificationListener listener) throws IOException {
			try {
				connection.removeNotificationListener(findObjectName(mbClass), listener);
			} catch (InstanceNotFoundException | ListenerNotFoundException e) {
				Logger.error(e);
			}
		}

		public MBeanInfo mBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
			return connection.getMBeanInfo(objectName);
		}

		public void close() {
			try {
				connector.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		public List<String> attributes(ObjectName objectName) {
			List<String> attributes = new ArrayList<>();
			try {
				for (MBeanAttributeInfo info : mBeanInfo(objectName).getAttributes()) {
					final Object attribute = connection.getAttribute(objectName, info.getName());
					attributes.add(info.getName() + ": " + resolveValue(attribute, (OpenType) info.getDescriptor().getFieldValue("openType")));
				}
			} catch (RuntimeException | MBeanException | AttributeNotFoundException | ReflectionException |
					 IntrospectionException | IOException | InstanceNotFoundException ignored) {
			}
			return attributes;
		}

		public Map<String, String> operations(ObjectName objectName) {
			Map<String, String> map = new LinkedHashMap<>();
			try {

				for (MBeanOperationInfo info : mBeanInfo(objectName).getOperations()) {
					final Descriptor descriptor = info.getDescriptor();
					final String parameters = descriptor.getFieldValue(Parameters.class.getSimpleName()).toString();
					final String description = descriptor.getFieldValue(Description.class.getSimpleName()).toString();
					map.put(info.getReturnType() + " " + info.getName() + "(" + (parameters != null ? parameters : parameters(info)) + ")", description == null ? "" : description);
					return map;
				}
			} catch (RuntimeException | ReflectionException | IntrospectionException | IOException |
					 InstanceNotFoundException ignored) {
			}
			return Collections.emptyMap();
		}

		public Map<MBeanOperationInfo, String> operationInfos(ObjectName objectName) {
			try {
				final List<MBeanOperationInfo> operations = Arrays.asList(mBeanInfo(objectName).getOperations());
				Map<MBeanOperationInfo, String> map = new LinkedHashMap<>();
				for (MBeanOperationInfo operation : operations)
					map.put(operation, operation.getDescriptor().getFieldValue(Description.class.getSimpleName()).toString());
				return map;
			} catch (RuntimeException | ReflectionException | IntrospectionException | IOException |
					 InstanceNotFoundException ignored) {
			}
			return Collections.emptyMap();
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

	private String parameters(MBeanOperationInfo info) {
		return String.join(", ", Arrays.stream(info.getSignature()).map(mb -> mb.getType() + " " + mb.getName()).collect(Collectors.toList()));
	}
}
