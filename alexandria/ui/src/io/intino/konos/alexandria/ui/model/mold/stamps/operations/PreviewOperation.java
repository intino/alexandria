package io.intino.konos.alexandria.ui.model.mold.stamps.operations;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.stamps.Operation;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.net.URL;

public class PreviewOperation extends Operation<URL> {
	private Execution execution;

	public PreviewOperation() {
		alexandriaIcon("icons:description");
	}

	public URL preview(Item item, UISession session) {
		return item != null && execution != null ? execution.preview(item.object(), session) : null;
	}

	public PreviewOperation preview(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		URL preview(Object object, UISession session);
	}
}
