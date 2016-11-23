package io.intino.pandora.plugin.rules;

import tara.lang.model.Node;
import tara.lang.model.rules.NodeRule;

public class Named implements NodeRule {

	public boolean accept(Node node) {
		return !node.isAnonymous();
	}

	public String errorMessage() {
		return "This element must have name";
	}

}
