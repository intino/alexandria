package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.rules.NodeRule;

public class JobOrPostCondition implements NodeRule {

	public boolean accept(Node node) {
		final boolean job = node.components().stream().anyMatch(c -> c.type().contains("Job"));
		final boolean postCondition = node.components().stream().anyMatch(c -> c.type().contains("PostCondition"));
		return !(job && postCondition);
	}
}
