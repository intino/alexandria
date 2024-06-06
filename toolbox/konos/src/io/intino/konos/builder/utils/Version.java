package io.intino.konos.builder.utils;

import io.intino.konos.builder.context.KonosException;

import java.util.Comparator;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

public class Version implements Comparable<Version> {
	private static final Logger LOG = Logger.getGlobal();

	public enum Level {Minor, Medium, Mayor}

	private static final String SNAPSHOT = "-SNAPSHOT";
	private final String version;

	public Version(String version) throws KonosException {
		if (version == null)
			throw new KonosException("Version can not be null");
		if (!version.matches("[0-9]+(\\.[0-9]+)*" + "(-SNAPSHOT)?"))
			throw new KonosException("Invalid version format: " + version);
		this.version = version;
	}

	public final String get() {
		return this.version;
	}

	public Level distanceTo(Version that) {
		String[] thisParts = this.get().split("\\.");
		String[] thatParts = that.get().split("\\.");
		if (!thisParts[0].equals(thatParts[0])) return Level.Mayor;
		if (!thisParts[1].equals(thatParts[1])) return Level.Medium;
		return Level.Minor;
	}

	@Override
	public String toString() {
		return version;
	}

	public boolean isSnapshot() {
		return version.endsWith(SNAPSHOT);
	}

	public Version nextSnapshot() throws KonosException {
		String v = this.version.replace(SNAPSHOT, "");
		String[] split = v.split("\\.");
		split[split.length - 1] = String.valueOf(parseInt(split[split.length - 1]) + 1);
		return new Version(String.join(".", split) + SNAPSHOT);
	}

	public Version nextRelease(Level level) throws KonosException {
		String v = this.version.replace(SNAPSHOT, "");
		String[] split = v.split("\\.");
		split[split.length - 1 - level.ordinal()] = String.valueOf(parseInt(split[split.length - 1 - level.ordinal()]) + 1);
		return new Version(String.join(".", split));
	}

	public Version next() throws KonosException {
		String[] split = version.split("\\.");
		split[split.length - 1] = String.valueOf(parseInt(split[split.length - 1]) + 1);
		return new Version(String.join(".", split));
	}


	@Override
	public int compareTo(Version that) {
		try {
			if (that == null) return 1;
			String[] thisParts = this.get().substring(0, this.get().contains("-") ? this.get().indexOf("-") : this.get().length()).split("\\.");
			String[] thatParts = that.get().substring(0, that.get().contains("-") ? that.get().indexOf("-") : that.get().length()).split("\\.");
			int length = Math.max(thisParts.length, thatParts.length);
			for (int i = 0; i < length; i++) {
				int thisPart = i < thisParts.length ? parseInt(thisParts[i]) : 0;
				int thatPart = i < thatParts.length ?
						parseInt(thatParts[i]) : 0;
				if (thisPart < thatPart) return -1;
				if (thisPart > thatPart) return 1;
			}
			return 0;
		} catch (NumberFormatException e) {
			LOG.severe(e.getMessage());
			return 0;
		}
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (that == null)
			return false;
		if (this.getClass() != that.getClass())
			return false;
		return this.compareTo((Version) that) == 0;
	}

	public static Comparator<? super String> comparator() {
		return (o1, o2) -> {
			try {
				return new Version(o1).compareTo(new Version(o2));
			} catch (KonosException e) {
				return 0;
			}
		};
	}

}
