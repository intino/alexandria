package io.intino.alexandria.ui.displays.components.slider;

public interface Ordinal {
	String name();
	String label();
	int step();
	Formatter formatter(String language);

	interface Formatter {
		String format(long value);
	}
}
