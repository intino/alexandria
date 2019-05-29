package io.intino.alexandria.ui.displays.components.slider;

public interface Ordinal {
	String label();
	int step();
	String formatter();

	interface Formatter {
		String format(int value);
	}
}
