package io.intino.alexandria.logger;


import io.intino.alexandria.inl.Inl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class Formatter extends java.util.logging.Formatter {
	private String path;

	public Formatter(String path) {
		this.path = path;
	}

	@Override
	public String format(java.util.logging.LogRecord record) {
		final Log log = new Log().ts(Instant.now()).level(record.getLevel().getName()).
				message(record.getMessage()).
				sourceClass(record.getSourceClassName()).
				sourceMethod(record.getSourceMethodName());
		if (record.getThrown() != null) log.stackTrace(stackTrace(record.getThrown()));
		return format(log);
	}

	private String format(Log log) {
		try {
			return Inl.toMessage(log) + "\n\n";
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private String stackTrace(Throwable thrown) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		thrown.printStackTrace(pw);
		return sw.toString();
	}

	public static class Log implements java.io.Serializable {

		private Instant ts;
		private String level = "";
		private String message = "";
		private String sourceClass = "";
		private String sourceMethod = "";
		private String stackTrace = "";

		public Instant created() {
			return this.ts;
		}

		public String level() {
			return this.level;
		}

		public String message() {
			return this.message;
		}

		public String sourceClass() {
			return this.sourceClass;
		}

		public String sourceMethod() {
			return this.sourceMethod;
		}

		public String stackTrace() {
			return this.stackTrace;
		}

		public Log ts(Instant created) {
			this.ts = created;
			return this;
		}

		public Log level(String level) {
			this.level = level;
			return this;
		}

		public Log message(String message) {
			this.message = message;
			return this;
		}

		public Log sourceClass(String sourceClass) {
			this.sourceClass = sourceClass;
			return this;
		}

		public Log sourceMethod(String sourceMethod) {
			this.sourceMethod = sourceMethod;
			return this;
		}

		public Log stackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
			return this;
		}
	}
}
