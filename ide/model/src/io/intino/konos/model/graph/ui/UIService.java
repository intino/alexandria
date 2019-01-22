package io.intino.konos.model.graph.ui;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class UIService extends AbstractUIService {

	public UIService(io.intino.tara.magritte.Node node) {
		super(node);
	}

	public Resource home() {
		return homeList().stream().findFirst().orElse(null);
	}

	public Resource userHome() {
		return homeList().stream().filter(Resource::isConfidential).findFirst().orElse(null);
	}

	private List<Resource> homeList() {
		return resourceList.stream().filter(Resource::isMain).collect(toList());
	}
}