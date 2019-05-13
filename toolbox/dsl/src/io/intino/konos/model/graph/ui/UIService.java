package io.intino.konos.model.graph.ui;

import io.intino.konos.model.graph.Template;

import java.util.List;
import java.util.stream.Collectors;

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

	public List<Template> templates() {
		return resourceList.stream().map(this::template).collect(Collectors.toList());
	}

	public Template template(UIService.Resource resource) {
		return resource.asPage().template();
	}

	private List<Resource> homeList() {
		return resourceList.stream().filter(Resource::isMain).collect(toList());
	}

}