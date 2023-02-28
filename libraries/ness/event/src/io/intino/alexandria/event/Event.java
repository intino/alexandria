package io.intino.alexandria.event;

import java.time.Instant;
import java.util.Arrays;

public interface Event extends Comparable<Event> {

	Instant ts();
	String ss();
	Format format();

	@Override
	default int compareTo(Event o) {
		return ts().compareTo(o.ts());
	}

	enum Format {
		Unknown(""),
		Message(".zim"),
		Measurement(".zit");

		private final String extension;

		Format(String extension) {
			this.extension = extension;
		}

		public String extension() {
			return extension;
		}

		public static Format byExtension(String extension) {
			return Arrays.stream(values()).filter(f -> f.extension.equalsIgnoreCase(extension)).findFirst().orElse(Unknown);
		}
	}
}
