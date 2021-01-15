package io.intino.konos.model.graph.rules;
import io.intino.magritte.lang.model.Node;
import io.intino.magritte.lang.model.rules.NodeRule;

import java.util.List;

public class AxisAccomplishSource implements NodeRule {


	public boolean accept(Node node) {
		return true;
	}

	@Override
	public String errorMessage() {
		return "A factor axis must have Category source. A distribution axis must have Numeric source";
	}
}
