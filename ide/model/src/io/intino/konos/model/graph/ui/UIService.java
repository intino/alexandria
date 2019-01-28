package io.intino.konos.model.graph.ui;

import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PageDisplay;

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

	public List<Display> displays() {
		return resourceList.stream().map(this::display).collect(Collectors.toList());
	}

	public PageDisplay display(UIService.Resource resource) {
		if (resource.isEditorPage()) return resource.asEditorPage().editor();
		else if (resource.isBlankPage()) return resource.asBlankPage().display();
		else if (resource.isDesktopPage()) return resource.asDesktopPage().desktop();
		return null;
	}

	private List<Resource> homeList() {
		return resourceList.stream().filter(Resource::isMain).collect(toList());
	}

}