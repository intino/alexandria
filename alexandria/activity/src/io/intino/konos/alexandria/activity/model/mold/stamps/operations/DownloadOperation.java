package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;

import java.util.List;

public class DownloadOperation extends Operation<String> {
	private List<String> options = new java.util.ArrayList<>();
	private Execution execution;

	public List<String> options() {
		return this.options;
	}

	public DownloadOperation options(List<String> options) {
		this.options = options;
		return this;
	}

	public Resource execute(Item item, String option, String username) {
		return item != null && execution != null ? execution.download(item.object(), option, username) : null;
	}

	public DownloadOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource download(Object object, String option, String username);
	}
}
