package io.intino.alexandria.itz;

import java.io.InputStream;
import java.util.Iterator;

public class ItzReader {
	private final InputStream is;

	public ItzReader(InputStream is) {
		this.is = is;
	}

	public Iterator<Data> data() {
		return null;
	}

	public String sensor() {
		return null;
	}

	public String[] measurements() {
		return new String[0];
	}
}
