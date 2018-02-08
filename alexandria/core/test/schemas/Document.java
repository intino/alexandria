package schemas;

import io.intino.konos.alexandria.schema.Resource;

import java.time.Instant;

public class Document {

	private Instant ts;
	private Resource file;

	public Document(Instant ts, Resource file) {
		this.ts = ts;
		this.file = file;
	}

	public Document() {
	}

	public Instant ts() {
		return ts;
	}

	public Resource file() {
		return file;
	}
}
