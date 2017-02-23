package io.intino.konos.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JMXServer {

	private static final Logger logger = Logger.getGlobal();

	private final Map<String, Object[]> mbClasses;
	private final List<ObjectName> registeredBeans = new ArrayList<>();
	private JMXConnectorServer connector;
	private MBeanServer server;

	public JMXServer(Map<String, Object[]> classWithParametersMap) {
		this.mbClasses = classWithParametersMap;
	}

	public void init(String localhostIP, int port) {
		System.setProperty("java.rmi.server.randomIDs", "false");
		System.setProperty("com.sun.management.jmxremote.rmi.port", port + "");
		System.setProperty("java.rmi.server.hostname", localhostIP);
		server = allocateServer();
		for (String mbClass : mbClasses.keySet())
			registerMBean(server, mbClass, mbClasses.get(mbClass));
		createService(server, port);
	}

	private MBeanServer allocateServer() {
		final ArrayList<MBeanServer> declaredServers = MBeanServerFactory.findMBeanServer(null);
		return declaredServers.isEmpty() ? MBeanServerFactory.createMBeanServer() : declaredServers.get(0);
	}

	public List<ObjectName> registeredBeans() {
		return registeredBeans;
	}

	public MBeanServer getServer() {
		return server;
	}

	public void stop() {
		try {
			if (connector != null && connector.isActive()) connector.stop();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	private void createService(MBeanServer server, int port) {
		try {
			LocateRegistry.createRegistry(port);
			JMXServiceURL url =
					new JMXServiceURL("service:jmx:rmi://" + System.getProperty("java.rmi.server.hostname") +
							":" + port + "/jndi/rmi://" + System.getProperty("java.rmi.server.hostname") + ":" + port + "/jmxrmi");
			connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
			connector.start();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	private void registerMBean(MBeanServer server, String mbeanClassName, Object[] parameters) {
		try {
			final Class<?> mbeanClass = Class.forName(mbeanClassName);
			String objectNameName = "Konos" + ":type=" + mbeanClass.getInterfaces()[0].getName() + ",name=" + mbeanClass.getSimpleName();
			registeredBeans.add(createSimpleMBean(server, mbeanClassName, objectNameName, parameters));
		} catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
		}
	}

	private static ObjectName createSimpleMBean(MBeanServer server, String mbeanClassName, String objectNameName, Object[] parameters) {
		try {
			ObjectName mbeanObjectName = ObjectName.getInstance(objectNameName);
			server.registerMBean(newInstance(mbeanClassName, parameters), mbeanObjectName);
			return mbeanObjectName;
		} catch (Exception e) {
			logger.severe("Could not init the " + mbeanClassName + " MBean: " + e.getMessage());
			return null;
		}
	}

	private static Object newInstance(String mbeanClassName, Object[] parameters) {
		try {
			final Class<?> aClass = Class.forName(mbeanClassName);
			return aClass.getDeclaredConstructors()[0].newInstance(parameters);
		} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			logger.severe(e.getMessage());
			return null;
		}
	}

}
