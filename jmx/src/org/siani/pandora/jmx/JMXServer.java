package org.siani.pandora.jmx;

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

public class JMXServer {


	private final Map<String, Object[]> mbClasses;
	private final List<ObjectName> registeredBeans = new ArrayList<>();
	private JMXConnectorServer connector;
	private MBeanServer server;

	public JMXServer(Map<String, Object[]> classWithParametersMap) {
		this.mbClasses = classWithParametersMap;
	}

	public void init(int port) {
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
			e.printStackTrace();
		}
	}

	private void createService(MBeanServer server, int port) {
		try {
			LocateRegistry.createRegistry(port);
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi");
			connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
			connector.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerMBean(MBeanServer server, String mbeanClassName, Object[] parameters) {
		try {
			String objectNameName = server.getDefaultDomain() + ":type=" + Class.forName(mbeanClassName).getInterfaces()[0].getName() + ",name=1";
			registeredBeans.add(createSimpleMBean(server, mbeanClassName, objectNameName, parameters));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static ObjectName createSimpleMBean(MBeanServer server, String mbeanClassName, String objectNameName, Object[] parameters) {
		try {
			ObjectName mbeanObjectName = ObjectName.getInstance(objectNameName);
			server.registerMBean(newInstance(mbeanClassName, parameters), mbeanObjectName);
			return mbeanObjectName;
		} catch (Exception e) {
			System.err.println("Could not init the " + mbeanClassName + " MBean: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static Object newInstance(String mbeanClassName, Object[] parameters) {
		try {
			final Class<?> aClass = Class.forName(mbeanClassName);
			return aClass.getDeclaredConstructors()[0].newInstance(parameters);
		} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}

}
