package io.intino.alexandria.allotropy.prototype;

public interface Sprout {
	String filename();
	Serializer serializer();

	interface Serializer {
		String serialize(Packet packet);

		Serializer Null = packet -> null;
	}
}
