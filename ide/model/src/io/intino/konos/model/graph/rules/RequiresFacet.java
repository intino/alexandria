package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.rules.NodeRule;

public class RequiresFacet implements NodeRule {
	public boolean accept(Node node) {
		return !node.facets().isEmpty();
	}


	@Override
	public String errorMessage() {
		return "This parameters should have a type as facet";
	}
}
