package io.intino.alexandria.rest;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class AlexandriaSparkBuilder {
	private static AlexandriaSpark instance;
	private static int port;
	private static String webDirectory;
	private static boolean ui = false;
	private static List<Object> objects = new ArrayList<>();

	public static void setup(int port, String webDirectory) {
		AlexandriaSparkBuilder.port = port;
		AlexandriaSparkBuilder.webDirectory = webDirectory;
	}

	public static boolean isUI() {
		return ui;
	}

	public static void setUI(boolean value) {
		ui = value;
	}

	public static AlexandriaSpark instance() {
		return instance == null ? instance = getInstance() : instance;
	}


	public static void addParameters(Object... objs) {
		for (int i=0; i<objs.length; i++)
			if (objs[i] != null) objects.add(objs[i]);
	}

	private static AlexandriaSpark getInstance() {
		return ui ? loadUISpark() : new AlexandriaSpark(port, webDirectory);
	}

	private static AlexandriaSpark loadUISpark() {
		try {
			Class<?> aClass = Class.forName("io.intino.alexandria.ui.UISpark");
			Constructor<?> constructor = aClass.getConstructors()[0].getParameterCount() == 3 ? aClass.getConstructors()[0] : aClass.getConstructors()[1];
			return (AlexandriaSpark) constructor.newInstance(port, webDirectory, objects.size() > 0 ? objects.get(0) : null);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
		return null;
	}
}
