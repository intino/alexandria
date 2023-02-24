package io.intino.alexandria;


import io.intino.alexandria.event.Event.Format;

import java.io.File;

import static io.intino.alexandria.Session.SessionExtension;

public class Fingerprint {

	private static final String SEPARATOR = "/";
	private final String fingerprint;

	public Fingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public static Fingerprint of(String tank, String source, Timetag timetag, Format format) {
		return new Fingerprint(tank + SEPARATOR + source + SEPARATOR + timetag + SEPARATOR + format);
	}

	public static Fingerprint of(File file) {
		return new Fingerprint(cleanedNameOf(file));
	}

	public String tank() {
		return fingerprint.split(SEPARATOR)[0];
	}

	public String source() {
		return fingerprint.split(SEPARATOR)[1];
	}

	public Timetag timetag() {
		return new Timetag(fingerprint.split(SEPARATOR)[2]);
	}

	public Format format() {
		try {
			return Format.valueOf(firstUpperCase(fingerprint.split(SEPARATOR)[3]));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public String name() {
		return fingerprint.replace("/", "-");
	}

	@Override
	public String toString() {
		return fingerprint;
	}

	@Override
	public boolean equals(Object o) {
		return fingerprint.equals(o.toString());
	}

	@Override
	public int hashCode() {
		return fingerprint.hashCode();
	}


	private static String cleanedNameOf(File file) {
		return file.getName()
				.substring(0, file.getName().indexOf("#"))
				.replace("-", "/")
				.replace(SessionExtension, "");
	}

	public static String firstUpperCase(String value) {
		return value.isEmpty() ? "" : value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
