package io.intino.alexandria.logger;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.LogRecord;

import static java.util.Collections.singletonList;

public class Logger {
	public enum Level {
		ERROR, WARN, INFO, DEBUG, TRACE
	}

	private static List<LogHandler> out = new ArrayList<>(singletonList(new PrintStreamLogHandler(System.out)));
	private static List<LogHandler> err = new ArrayList<>(singletonList(new PrintStreamLogHandler(System.err)));
	private static List<String> excludedPackages = new ArrayList<>();
	private static Set<Level> excludedLevels = new HashSet<>();
	private static String pattern = "[%level]\nts: %date\nsource: %C\nmessage: %m\n";

	static {
		excludedLevels.add(Level.TRACE);
	}

	public static void trace(String message) {
		if (isExcluded() || excludedLevels.contains(Level.TRACE)) return;
		out.forEach(o -> o.publish(format(Level.TRACE, message)));
	}

	public static void debug(String message) {
		if (isDebugging()) out.forEach(o -> o.publish(format(Level.DEBUG, message)));
	}

	public static void info(String message) {
		if (isExcluded() || excludedLevels.contains(Level.INFO)) return;
		out.forEach(o -> o.publish(format(Level.INFO, message)));
	}

	public static void warn(String message) {
		if (isExcluded() || excludedLevels.contains(Level.WARN)) return;
		out.forEach(o -> o.publish(format(Level.WARN, message)));
	}

	public static void error(String message) {
		if (isExcluded() || excludedLevels.contains(Level.ERROR)) return;
		err.forEach(o -> o.publish(format(Level.ERROR, message)));
	}

	public static void error(Throwable e) {
		if (isExcluded() || excludedLevels.contains(Level.ERROR)) return;
		err.forEach(o -> o.publish(format(e)));
	}

	public static void error(String message, Throwable e) {
		if (isExcluded() || excludedLevels.contains(Level.ERROR)) return;
		err.forEach(o -> o.publish(format(message, e)));
	}

	public static void excludePackage(String aPackage) {
		excludedPackages.add(aPackage);
	}

	public static void includePackage(String aPackage) {
		excludedPackages.remove(aPackage);
	}

	public static void excludeLevel(Level level) {
		excludedLevels.add(level);
	}

	public static void includeLevel(Level level) {
		excludedLevels.remove(level);
	}

	public static void addErrorHandler(LogHandler stream) {
		err.add(stream);
	}

	public static void addOutHandler(LogHandler stream) {
		out.add(stream);
	}

	static String format(LogRecord record) {
		return pattern.replace("%level", record.getLevel().getName()).replace("%date", Instant.now().toString()).replace("%C", record.getSourceClassName() + ":" + record.getSourceMethodName()).replace("%m", formatMessage(record.getMessage(), record.getThrown()));
	}

	private static String format(String message, Throwable e) {
		return pattern.replace("%level", Level.ERROR.name()).replace("%date", Instant.now().toString()).replace("%C", caller()).replace("%m", formatMessage(message, e));
	}

	private static String format(Throwable e) {
		return pattern.replace("%level", Level.ERROR.name()).replace("%date", Instant.now().toString()).replace("%C", caller()).replace("%m", formatMessage(e));
	}

	private static String format(Level level, String message) {
		return pattern.replace("%level", level.name()).replace("%date", Instant.now().toString()).replace("%C", caller()).replace("%m", formatMessage(message)) + "\n";
	}

	private static String formatMessage(String message, Throwable e) {
		if (message == null) return formatMessage(e);
		return "\n\t" + formatMessage(message) + (e != null ? ("\n\t\n\tCaused by:" + formatMessage(e)) : "");
	}

	private static String formatMessage(String message) {
		return !message.contains("\n") ? message : "\n\t" + message.replace("\n", "\n\t");
	}

	private static String formatMessage(Throwable e) {
		if (e == null) return "Null";
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return "\n\t" + writer.toString().replace("\n", "\n\t");
	}

	private static String caller() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(Logger.class.getName()) && !ste.getClassName().startsWith("java.util.ArrayList") && !ste.getClassName().startsWith("java.lang.Thread")) {
				return ste.getClassName() + ":" + ste.getMethodName() + ":" + ste.getLineNumber();
			}
		}
		return "Unknown";
	}

	private static boolean isDebugging() {
		return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	private static boolean isExcluded() {
		String caller = caller();
		return excludedPackages.stream().anyMatch(caller::startsWith);
	}

	public interface LogHandler {
		void publish(String message);
	}

	private static class PrintStreamLogHandler implements LogHandler {

		private final PrintStream stream;

		PrintStreamLogHandler(PrintStream stream) {
			this.stream = stream;
		}

		@Override
		public void publish(String message) {
			stream.print(message);
		}
	}
}
