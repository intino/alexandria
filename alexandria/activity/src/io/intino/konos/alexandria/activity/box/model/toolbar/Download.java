package io.intino.konos.alexandria.activity.box.model.toolbar;

import io.intino.konos.alexandria.activity.box.Resource;
import io.intino.konos.alexandria.activity.box.model.Element;

import java.util.ArrayList;
import java.util.List;

public class Download extends Operation {
	protected List<String> options = new ArrayList<>();
	private Execution execution;

	public Download() {
		this.sumusIcon("file-download");
	}

	public List<String> options() {
		return options;
	}

	public Download add(String option) {
		this.options.add(option);
		return this;
	}

	public Resource execute(Element element, String option, String username) {
		return this.execution != null ? this.execution.download(element, option, username) : null;
	}

	public Download execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource download(Element element, String option, String username);
	}
}
