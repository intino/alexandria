package io.intino.alexandria.office.components;

import java.util.Arrays;

public enum Alignment {

	Left, Center, Right;

	public static Alignment getDefault() {
		return Left;
	}

	public static Alignment byName(String name) {
		return Arrays.stream(values()).filter(a -> a.name().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public String xml() {
		return "<w:jc w:val=\"" + name().toLowerCase() + "\"/>";
	}
}
