package io.intino.konos;


import io.intino.ness.inl.Inl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class LogFormatter extends java.util.logging.Formatter {
	private String path;

	public LogFormatter(String path) {
		this.path = path;
	}

	@Override
	public String format(java.util.logging.LogRecord record) {
		final LogRecord logRecord = new LogRecord().created(Instant.now()).level(record.getLevel().getName()).
				message(record.getMessage()).
				sourceClass(record.getSourceClassName()).
				sourceMethod(record.getSourceMethodName());
		if (record.getThrown() != null) logRecord.stackTrace(stackTrace(record.getThrown()));
		return Inl.serialize(logRecord) + "\n\n";
	}

	private String stackTrace(Throwable thrown) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		thrown.printStackTrace(pw);
		return sw.toString();
	}

	public static class LogRecord implements java.io.Serializable {

		private java.time.Instant created;
		private String level = "";
		private String message = "";
		private String sourceClass = "";
		private String sourceMethod = "";
		private String stackTrace = "";

		public java.time.Instant created() {
			return this.created;
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

		public LogRecord created(java.time.Instant created) {
			this.created = created;
			return this;
		}

		public LogRecord level(String level) {
			this.level = level;
			return this;
		}

		public LogRecord message(String message) {
			this.message = message;
			return this;
		}

		public LogRecord sourceClass(String sourceClass) {
			this.sourceClass = sourceClass;
			return this;
		}

		public LogRecord sourceMethod(String sourceMethod) {
			this.sourceMethod = sourceMethod;
			return this;
		}

		public LogRecord stackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
			return this;
		}
	}
}
