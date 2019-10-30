package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.rules.NodeRule;

import java.util.List;

import static io.intino.konos.model.graph.rules.ViewRule.ERROR_TYPE.*;

public class ViewRule implements NodeRule {

	enum ERROR_TYPE {
		NAME, SIZE, COMPONENT_NOT_ALLOWED
	}

	private ERROR_TYPE error = NAME;

	public boolean accept(Node node) {
		final List<Node> components = node.components();
		if (components.size() != 1) {
			error(SIZE);
			return false;
		}
		return components.stream().noneMatch(component -> hasForbiddenTypes(component) || (node.isAnonymous() && (hasFilteredCatalog(component) || isRenderDisplay(component))));
	}

	private boolean hasForbiddenTypes(Node component) {
		return component.types().contains("RenderPanels") ||
				component.types().contains("RenderObjects") && error(COMPONENT_NOT_ALLOWED);
	}

	private boolean hasFilteredCatalog(Node component) {
		return component.types().contains("RenderCatalogs") &&
				component.parameters().stream().anyMatch(p -> p.name().equals("filtered") &&
						p.values().get(0).toString().equals("true")) && error(NAME);
	}

	private boolean isRenderDisplay(Node component) {
		return component.types().contains("RenderDisplay") && error(NAME);
	}

	@Override
	public String errorMessage() {
		switch (error) {
			case NAME:
				return "This View must have name";
			case SIZE:
				return "This View must have just one Render";
			case COMPONENT_NOT_ALLOWED:
				return "RenderPanels and RenderObjects are not allowed here";
		}
		return "";
	}

	private boolean error(ERROR_TYPE type) {
		error = type;
		return true;
	}
}