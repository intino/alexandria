package io.intino.konos.alexandria.rest;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class AlexandriaSparkBuilder {
	private static AlexandriaSpark instance;
	private static int port;
	private static String webDirectory;
	private static boolean ui = false;

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

	private static AlexandriaSpark getInstance() {
		return ui ? loadUISpark() : new AlexandriaSpark(port, webDirectory);
	}

	private static AlexandriaSpark loadUISpark() {
		try {
			Class<?> aClass = Class.forName("io.intino.konos.alexandria.ui.UIAlexandriaSpark");
			Constructor<?> constructor = aClass.getConstructors()[0].getParameterCount() == 4 ? aClass.getConstructors()[0] : aClass.getConstructors()[1];
			return (AlexandriaSpark) constructor.newInstance(port, webDirectory, null, null);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
		return null;
	}
}
