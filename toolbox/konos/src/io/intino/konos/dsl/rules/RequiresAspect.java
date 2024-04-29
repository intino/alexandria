package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;

public class RequiresAspect implements MogramRule {
	public boolean accept(Mogram node) {
		return !node.appliedFacets().isEmpty();
	}


	@Override
	public String errorMessage() {
		return "This concept must have declared with an aspect";
	}
}
