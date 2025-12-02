package io.intino.alexandria.ollama.util;

import java.time.Duration;

public class OllamaKeepAlive {

	public static String unloadImmediately() {
		return "0";
	}

	public static String forever() {
		return "-1";
	}

	public static String getDefault() {
		return "5m";
	}

	public static String of(Duration duration) {
		return ofMinutes(duration.toMinutes());
	}

	public static String ofSeconds(long seconds) {
		return seconds + "s";
	}

	public static String ofMinutes(long minutes) {
		return minutes + "m";
	}

	public static String ofHours(long hours) {
		return hours + "h";
	}
}
