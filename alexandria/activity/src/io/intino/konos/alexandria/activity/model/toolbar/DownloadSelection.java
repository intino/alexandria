package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DownloadSelection extends Operation {
	protected List<String> options = new ArrayList<>();
	private Execution execution;

	public DownloadSelection() {
		this.alexandriaIcon("file-download");
	}

	public List<String> options() {
		return options;
	}

	public DownloadSelection add(String option) {
		this.options.add(option);
		return this;
	}

	public Resource execute(Element element, String option, List<Item> selection, String username) {
		List<Object> selectionObjects = selection.stream().map(Item::object).collect(toList());
		return this.execution != null ? this.execution.download(element, option, selectionObjects, username) : null;
	}

	public DownloadSelection execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource download(Element element, String option, List<Object> selection, String username);
	}
}
