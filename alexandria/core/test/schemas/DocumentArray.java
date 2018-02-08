package schemas;

import io.intino.konos.alexandria.schema.Resource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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
