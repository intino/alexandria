package io.intino.konos.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JMXServer {

	private static final Logger logger = Logger.getGlobal();
	private final Map<String, Object[]> mbClasses;
	private final List<ObjectName> registeredBeans = new ArrayList<>();
	private MBeanServer server;

	public JMXServer(Map<String, Object[]> classWithParametersMap) {
		this.mbClasses = classWithParametersMap;
	}

	public void init() {
		server = allocateServer();
		for (String mbClass : mbClasses.keySet())
			registerMBean(server, mbClass, mbClasses.get(mbClass));
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

	private void registerMBean(MBeanServer server, String mbeanClassName, Object[] parameters) {
		try {
			final Class<?> mbeanClass = Class.forName(mbeanClassName);
			String objectNameName = "Konos" + ":type=" + mbeanClass.getInterfaces()[0].getName() + ",name=" + mbeanClass.getSimpleName();
			registeredBeans.add(createSimpleMBean(server, mbeanClassName, objectNameName, parameters));
		} catch (ClassNotFoundException e) {
			logger.severe("Error registering mBean: " + e.getMessage());
		}
	}

	private static ObjectName createSimpleMBean(MBeanServer server, String mbeanClassName, String objectNameName, Object[] parameters) {
		try {
			ObjectName mbeanObjectName = ObjectName.getInstance(objectNameName);
			if (server.isRegistered(mbeanObjectName)) return mbeanObjectName;
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
