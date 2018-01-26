package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;

import java.net.URL;

public class PreviewOperation extends Operation<String> {
	private Execution execution;

	public URL preview(Item item, String username) {
		return item != null && execution != null ? execution.preview(item.object(), username) : null;
	}

	public PreviewOperation preview(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		URL preview(Object object, String username);
	}
}
