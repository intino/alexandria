package io.intino.test.schemas;

import io.intino.alexandria.Resource;

import java.time.Instant;

public class DocumentArray {

	private Instant ts;
	private Resource[] files;

	public DocumentArray(Instant ts, Resource... files) {
		this.ts = ts;
		this.files = files;
	}

	public DocumentArray() {
	}

	public Instant ts() {
		return ts;
	}

	public Resource[] files() {
		return files;
	}
}
