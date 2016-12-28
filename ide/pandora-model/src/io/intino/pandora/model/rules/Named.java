package io.intino.pandora.model.rules;

import io.intino.tara.lang.model.Node;
import io.intino.tara.lang.model.rules.NodeRule;

public class Named implements NodeRule {

	public boolean accept(Node node) {
		return !node.isAnonymous();
	}

	public String errorMessage() {
		return "This element must have name";
	}

}
