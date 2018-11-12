package io.intino.alexandria.triplestore;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public interface TripleStore {

	static String lineOf(String... triple) {
		return triple[0] + ";" + triple[1] + ";" + triple[2] + "\n";
	}

	void put(String subject, String predicate, Object value);

	Stream<String[]> all();

	Stream<String[]> matches(String... pattern);

	public static class Builder {
		private OutputStream os;

		public Builder(OutputStream os) {
			this.os = new BufferedOutputStream(os);
		}

		private static byte[] bytesOf(String subject, String predicate, Object value) {
			return lineOf(subject, predicate, value.toString()).getBytes();
		}

		public Builder put(String subject, String predicate, Object value) {
			try {
				os.write(bytesOf(subject, predicate, value));
			} catch (IOException e) {
				Logger.error(e);
			}
			return this;
		}


		public void close() {
			try {
				os.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}
}
