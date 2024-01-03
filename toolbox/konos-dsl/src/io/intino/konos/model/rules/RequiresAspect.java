package io.intino.konos.model.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.NodeRule;

public class RequiresAspect implements NodeRule {
	public boolean accept(Mogram node) {
		return !node.appliedFacets().isEmpty();
	}


	@Override
	public String errorMessage() {
		return "This concept must have declared with an aspect";
	}
}
