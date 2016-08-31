package org.siani.teseo.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JMXServer {


	private final Map<String, Object[]> mbClasses;
	private final List<ObjectName> registeredBeans = new ArrayList<>();
	private JMXConnectorServer connector;

	public JMXServer(Map<String, Object[]> classWithParametersMap) {
		this.mbClasses = classWithParametersMap;
	}

	public void init(int port) {
		MBeanServer server = MBeanServerFactory.createMBeanServer();
		for (String mbClass : mbClasses.keySet())
			registerMBean(server, mbClass, mbClasses.get(mbClass));
		createService(server, port);
	}

	public List<ObjectName> registeredBeans() {
		return registeredBeans;
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
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/server");
			connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
			connector.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerMBean(MBeanServer server, String mbeanClassName, Object[] parameters) {
		try {
			Class<?> aClass = Class.forName(mbeanClassName);
			String objectNameName = server.getDefaultDomain() + ":type=" + aClass.getInterfaces()[0].getName() + ",name=1";
			registeredBeans.add(createSimpleMBean(server, mbeanClassName, objectNameName, parameters));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static ObjectName createSimpleMBean(MBeanServer server, String mbeanClassName, String objectNameName, Object[] parameters) {
		try {
			ObjectName mbeanObjectName = ObjectName.getInstance(objectNameName);
			server.instantiate(mbeanClassName, mbeanObjectName, parameters, null);
			return mbeanObjectName;
		} catch (Exception e) {
			System.err.println("\t!!! Could not init the " + mbeanClassName + " MBean !!!");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

}
