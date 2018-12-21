package schemas;

import io.intino.alexandria.Resource;

import java.time.Instant;

public class Document {

	private Instant ts;
	private Resource attachment;

	public Document(Instant ts, Resource attachment) {
		this.ts = ts;
		this.attachment = attachment;
	}

	public Document() {
	}

	public Instant ts() {
		return ts;
	}

	public Resource attachment() {
		return attachment;
	}
}
