package io.intino.konos.model.graph.rules;

import io.intino.magritte.lang.model.Node;
import io.intino.magritte.lang.model.rules.NodeRule;

public class WorkflowNeeded implements NodeRule {


	public boolean accept(Node node) {
		return node.container().components().stream().anyMatch(n -> n.types().contains("BusinessUnit"));
	}

	public String errorMessage() {
		return "This element needs a BusinessUnit declared to works properly";
	}

}
