package org.siani.teseo.jmx;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JMXClient {


	private JMXServiceURL url = null;


	public JMXClient(String serverDirection, int port) {
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + serverDirection + ":" + port + "/server");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public JMXConnection connect(Map<String, Object[]> mbClasses) {
		if (url == null) return null;
		try {
			JMXConnector connector = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection connection = connector.getMBeanServerConnection();
			return new JMXConnection(connection, connector, registerBeans(connection, mbClasses));
		} catch (IOException | MalformedObjectNameException | ReflectionException | InstanceAlreadyExistsException | NotCompliantMBeanException | MBeanException e) {
			e.printStackTrace();
			return null;
		}
	}


	private List<ObjectName> registerBeans(MBeanServerConnection connection, Map<String, Object[]> mbClasses) throws MalformedObjectNameException, IOException, NotCompliantMBeanException, ReflectionException, MBeanException, InstanceAlreadyExistsException {
		List<ObjectName> objectNames = new ArrayList<>();
		String domain = connection.getDefaultDomain();

		for (String mbClass : mbClasses.keySet()) {
			ObjectName stdMBeanName = new ObjectName(domain + ":type=" + mbClass + ",name=2");
			connection.createMBean(mbClass, stdMBeanName, mbClasses.get(mbClass), null);
			objectNames.add(stdMBeanName);
		}
		return objectNames;
	}

	public class JMXConnection {
		private final MBeanServerConnection connection;
		private final JMXConnector connector;
		private final List<ObjectName> beans;

		JMXConnection(MBeanServerConnection connection, JMXConnector connector, List<ObjectName> beans) {
			this.connection = connection;
			this.connector = connector;
			this.beans = beans;
		}

		public <T> T mBean(Class<T> mbClass) {
			ObjectName objectName = findObjectName(mbClass.getName().replace("MBean", ""));
			if (objectName == null) {
				System.err.println("MBClass not registered");
				return null;
			}
			return JMX.isMXBeanInterface(mbClass) ? JMX.newMBeanProxy(connection, objectName, mbClass, true) : null;
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
