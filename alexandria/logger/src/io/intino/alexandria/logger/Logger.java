package io.intino.alexandria.logger;

import org.slf4j.LoggerFactory;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class Logger {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	private static org.slf4j.Logger loggerOf(String namespace) {
		return LoggerFactory.getLogger(namespace);
	}

	public static void trace(String message) {
		logger.trace(message);
	}

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warn(String message) {
		logger.warn(message);
	}

	public static void error(String message) {
		logger.error(message);
	}

	public static void error(Throwable e) {
		logger.error(e.getMessage(), e);
	}

	public static void error(String message, Throwable e) {
		logger.error(message, e);
	}

	public static void trace(String namespace, String message) {
		loggerOf(namespace).trace(message);
	}

	public static void debug(String namespace, String message) {
		loggerOf(namespace).debug(message);
	}

	public static void warn(String namespace, String message) {
		loggerOf(namespace).warn(message);
	}

	public static void error(String namespace, String message) {
		loggerOf(namespace).error(message);
	}

	public static void error(String namespace, String message, Object o) {
		loggerOf(namespace).error(message, o);
	}
}
