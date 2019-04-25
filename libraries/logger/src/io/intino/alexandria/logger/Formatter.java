package io.intino.alexandria.logger;


public class Formatter extends java.util.logging.Formatter {

	public Formatter() {
	}

	@Deprecated
	public Formatter(String type) {
	}

	@Override
	public String format(java.util.logging.LogRecord record) {
		return Logger.format(record);
	}
}
