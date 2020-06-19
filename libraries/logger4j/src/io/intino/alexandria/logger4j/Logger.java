package io.intino.alexandria.logger4j;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class Logger {
	private static final String pattern = "[%level]\nts: %date\nsource: %C\nmessage:%m\n";
	private static final ConsoleAppender appender = new ConsoleAppender();

	public static void init() {
		init(Level.INFO);
	}

	public static void init(Level level) {
		org.apache.log4j.Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		appender.setLayout(new Layout() {
			@Override
			public void activateOptions() {

			}

			@Override
			public String format(LoggingEvent record) {
				return pattern.replace("%level", record.getLevel().toString()).
						replace("%date", Instant.now().toString()).
						replace("%C", record.getLocationInformation().getClassName() + ":" + record.getLocationInformation().getMethodName()).
						replace("%m", formatMessage(record.getRenderedMessage(), record.getThrowableInformation()));
			}

			private String formatMessage(String message, ThrowableInformation throwable) {
				if (message == null) return formatMessage(throwable);
				String result = formatMessage(message) + (throwable != null ? ("\n\t\n\tCaused by:" + formatMessage(throwable)) : "");
				return result.contains("\n") ? "\n\t" + result : " " + result;
			}

			private String formatMessage(String message) {
				return !message.contains("\n") ? message : "\n\t" + message.replace("\n", "\n\t");
			}

			private String formatMessage(ThrowableInformation e) {
				StringWriter writer = new StringWriter();
				e.getThrowable().printStackTrace(new PrintWriter(writer));
				return "\n\t" + writer.toString().replace("\n", "\n\t");
			}

			@Override
			public boolean ignoresThrowable() {
				return false;
			}
		});
		setLevel(level);
		appender.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(appender);
	}

	public static void setLevel(Level level) {
		appender.setThreshold(level);
	}
}