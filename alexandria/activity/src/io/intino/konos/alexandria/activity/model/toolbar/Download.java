package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.ArrayList;
import java.util.List;

public class Download extends Operation {
	protected List<String> options = new ArrayList<>();
	private Execution execution;

	public Download() {
		this.alexandriaIcon("file-download");
	}

	public List<String> options() {
		return options;
	}

	public Download add(String option) {
		this.options.add(option);
		return this;
	}

	public Resource execute(Element element, String option, ActivitySession session) {
		return this.execution != null ? this.execution.download(element, option, session) : null;
	}

	public Download execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource download(Element element, String option, ActivitySession session);
	}
}
