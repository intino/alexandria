package io.intino.alexandria;


import io.intino.alexandria.event.Event.Format;

public class Fingerprint {

	private static final String SEPARATOR = "/";
	private final String fingerprint;

	public Fingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public static Fingerprint of(String tank, String source, Timetag timetag, Format format) {
		return new Fingerprint(tank + SEPARATOR + source + SEPARATOR + timetag + SEPARATOR + format);
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

	public String name() {
		return fingerprint.replace("/", "-");
	}


	public static String firstUpperCase(String value) {
		return value.isEmpty() ? "" : value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
