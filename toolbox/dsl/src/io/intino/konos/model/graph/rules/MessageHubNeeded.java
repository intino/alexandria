package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.rules.NodeRule;

public class MessageHubNeeded implements NodeRule {
	public boolean accept(Node node) {
		return node.container().components().stream().anyMatch(n -> n.types().contains("MessageHub"));
	}

	@Override
	public String errorMessage() {
		return "This element needs a MessageHub declared to works properly";
	}
}