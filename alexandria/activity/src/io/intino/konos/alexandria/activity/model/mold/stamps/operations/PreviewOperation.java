package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;

public class PreviewOperation extends Operation<URL> {
	private Execution execution;

	public URL preview(Item item, User user) {
		return item != null && execution != null ? execution.preview(item.object(), user) : null;
	}

	public PreviewOperation preview(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		URL preview(Object object, User user);
	}
}
