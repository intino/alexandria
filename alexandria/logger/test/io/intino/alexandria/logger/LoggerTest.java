package io.intino.alexandria.logger;

import io.intino.alexandria.logger.Logger.Level;
import org.junit.Test;

public class LoggerTest {


	@Test
	public void should_sout_original_exception_line() {
		Example example = example();
		try {
			example.m();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void should_sout_exceptions_with_message() {
		Logger.error("Example message", new NullPointerException());
	}


	@Test
	public void should_sout_messages() {
		Logger.info("info message");
		Logger.warn("warn message");
		Logger.error("error message");
	}

	@Test
	public void should_exclude_warn_messages() {
		Logger.excludeLevel(Level.WARN);
		Logger.info("info message");
		Logger.warn("warn message");
		Logger.error("error message");
	}

	@Test
	public void should_exclude_packages() {
		Logger.excludeLevel(Level.WARN);
		Logger.info("info message");
		Logger.warn("warn message");
		Logger.error("error message");
	}

	@Test
	public void should_sout_trace() {
		Logger.trace("trace message not sout");
		Logger.includeLevel(Level.TRACE);
		Logger.trace("trace message");
	}

	private Example example() {
		return null;
	}

	public static class Example {
		private String m() {
			return "mmmm";
		}
	}
}
