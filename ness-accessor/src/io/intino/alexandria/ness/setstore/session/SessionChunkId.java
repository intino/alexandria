package io.intino.alexandria.ness.setstore.session;

public class SessionChunkId {

	private static final String SEPARATOR = "/";

	static String chunkId(String tank, String timetag, String set) {
		return tank + SEPARATOR + timetag + SEPARATOR + set;
	}

	static String tankOf(String id) {
		return id.split(SEPARATOR)[0];
	}

	static String timetagOf(String id) {
		return id.split(SEPARATOR)[1];
	}

	static String setOf(String id) {
		return id.split(SEPARATOR)[2];
	}
}
