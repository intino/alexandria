package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.EmptyNode;
import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.Parameter;
import io.intino.tara.lang.model.rules.NodeRule;

public class NamedIfMultipleSchema implements NodeRule {


	public boolean accept(Node node) {
		return !node.isAnonymous() || hasMultipleSchema(node);
	}

	private boolean hasMultipleSchema(Node node) {
		Parameter parameter = schemaParameter(node);
		return parameter != null && parameter.values().size() > 1 && !(parameter.values().get(0) instanceof EmptyNode);
	}

	private Parameter schemaParameter(Node node) {
		final Parameter parameter = node.parameters().stream().filter(p -> p.name().equalsIgnoreCase("eventTypes")).findFirst().orElse(null);
		if (parameter != null) return parameter;
		if (!node.parameters().isEmpty()) return node.parameters().get(0);
		return null;
	}

	@Override
	public String errorMessage() {
		return "Tank with no schema must have name";
	}
}
