package io.intino.konos.model.rules;

import io.intino.magritte.lang.model.Node;
import io.intino.magritte.lang.model.rules.NodeRule;

public class Named implements NodeRule {

	public boolean accept(Node node) {
		return !node.isAnonymous();
	}

	public String errorMessage() {
		return "This element must have name";
	}

}
