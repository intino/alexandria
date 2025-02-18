package io.intino.alexandria.http;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class AlexandriaHttpServerBuilder {
	private static AlexandriaHttpServer<?> instance;
	private static int port;
	private static String webDirectory;
	private static long maxResourceSize;
	private static boolean ui = false;
	private static List<Object> objects = new ArrayList<>();

	public static void setup(int port, String webDirectory) {
		AlexandriaHttpServerBuilder.port = port;
		AlexandriaHttpServerBuilder.webDirectory = webDirectory;
		AlexandriaHttpServerBuilder.maxResourceSize = AlexandriaHttpServer.MaxResourceSize;
	}

	public static void setup(int port, String webDirectory, long maxFormContentSize) {
		AlexandriaHttpServerBuilder.port = port;
		AlexandriaHttpServerBuilder.webDirectory = webDirectory;
		AlexandriaHttpServerBuilder.maxResourceSize = maxFormContentSize;
	}

	public static boolean isUI() {
		return ui;
	}

	public static void setUI(boolean value) {
		ui = value;
	}

	public static AlexandriaHttpServer<?> instance() {
		return instance == null ? instance = getInstance() : instance;
	}

	public static void addParameters(Object... objs) {
		for (int i = 0; i < objs.length; i++)
			if (objs[i] != null) objects.add(objs[i]);
	}

	private static AlexandriaHttpServer<?> getInstance() {
		return ui ? loadUIServer() : new AlexandriaHttpServer<>(port, webDirectory, maxResourceSize);
	}

	private static AlexandriaHttpServer<?> loadUIServer() {
		try {
			Class<?> aClass = Class.forName("io.intino.alexandria.ui.AlexandriaUiServer");
			Constructor<?> constructor = aClass.getConstructors()[0].getParameterCount() == 4 ? aClass.getConstructors()[0] : aClass.getConstructors()[1];
			return (AlexandriaHttpServer<?>) constructor.newInstance(port, webDirectory, maxResourceSize, !objects.isEmpty() ? objects.get(0) : null);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
		return null;
	}
}
