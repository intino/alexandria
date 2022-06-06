package io.intino.konos.model.rules;

import io.intino.magritte.lang.model.Node;
import io.intino.magritte.lang.model.rules.NodeRule;

public class CheckVirtual implements NodeRule {


	public boolean accept(Node node) {
		if (node.appliedAspects().stream().noneMatch(a -> a.type().contains("Virtual"))) return true;
		return (node.components().stream().noneMatch(c -> c.type().contains("Split")));
	}

	@Override
	public String errorMessage() {
		return "Virtual cubers cannot be splitted";
	}
}
