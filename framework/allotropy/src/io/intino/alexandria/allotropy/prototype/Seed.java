package io.intino.alexandria.allotropy.prototype;

import io.intino.alexandria.allotropy.FormatError;

public interface Seed {
	String filename();
	Deserializer deserializer(String line) throws FormatError;

	interface Deserializer {
		Packet deserialize(String line) throws FormatError;

		Deserializer Null = line -> null;
	}
}
