package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.EmptyNode;
import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

public class NamedIfNoSchema implements NodeRule {


	public boolean accept(Node node) {
		return !node.isAnonymous() || hasSchema(node);
	}

	private boolean hasSchema(Node node) {
		return !node.parameters().isEmpty() && schemaParameter(node) != null && !schemaParameter(node).values().isEmpty() && schemaParameter(node).values().get(0) != null && !(schemaParameter(node).values().get(0) instanceof EmptyNode);
	}

	private Parameter schemaParameter(Node node) {
		final Parameter parameter = node.parameters().stream().filter(p -> p.name().equalsIgnoreCase("schema")).findFirst().orElse(null);
		if (parameter != null) return parameter;
		if (!node.parameters().isEmpty()) return node.parameters().get(0);
		return null;
	}

	@Override
	public String errorMessage() {
		return "Tank with no schema must have name";
	}
}
