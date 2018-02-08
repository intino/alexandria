package schemas;

import io.intino.konos.alexandria.schema.Resource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class DocumentList {

	private Instant ts;
	private List<Resource> files;

	public DocumentList(Instant ts, Resource... files) {
		this.ts = ts;
		this.files = Arrays.asList(files);
	}

	public DocumentList() {
	}

	public Instant ts() {
		return ts;
	}

	public List<Resource> files() {
		return files;
	}
}
