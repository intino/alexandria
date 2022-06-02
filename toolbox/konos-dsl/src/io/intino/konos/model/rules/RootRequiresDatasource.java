package io.intino.konos.model.rules;

import io.intino.magritte.lang.model.Node;
import io.intino.magritte.lang.model.NodeRoot;
import io.intino.magritte.lang.model.rules.NodeRule;

public class RootRequiresDatasource implements NodeRule {


	public boolean accept(Node node) {
		return !(node.container() instanceof NodeRoot) || hasDatasource(node);
	}

	private boolean hasDatasource(Node node) {
		return node.components().stream().anyMatch(c -> c.type().contains("Datasource"));
	}

	@Override
	public String errorMessage() {
		return "Factor requires Datasource";
	}
}
