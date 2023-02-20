package io.intino.alexandria;


import io.intino.alexandria.Session.Type;

public class Fingerprint {

	private static final String SEPARATOR = "/";
	private final String fingerprint;

	public Fingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public static Fingerprint of(String tank, String source, Type type, Timetag timetag) {
		return new Fingerprint(tank + SEPARATOR + source + SEPARATOR + timetag + SEPARATOR + type);
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

	public Type type() {
		try {
			return Type.valueOf(fingerprint.split(SEPARATOR)[3]);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public int size() {
		return fingerprint.length();
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
}
