package io.intino.konos.alexandria.ui.model.mold.stamps.operations;

import io.intino.konos.alexandria.ui.Resource;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.stamps.Operation;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.List;

public class DownloadOperation extends Operation<String> {
	private List<String> options = new java.util.ArrayList<>();
	private Execution execution;

	public DownloadOperation() {
		alexandriaIcon("icons:file-download");
	}

	public List<String> options() {
		return this.options;
	}

	public DownloadOperation options(List<String> options) {
		this.options = options;
		return this;
	}

	public Resource execute(Item item, String option, UISession session) {
		return item != null && execution != null ? execution.download(item.object(), option, session) : null;
	}

	public DownloadOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource download(Object object, String option, UISession session);
	}
}
