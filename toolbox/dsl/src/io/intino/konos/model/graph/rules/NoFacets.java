package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Primitive;
import io.intino.tara.lang.model.rules.variable.VariableRule;

public class NoFacets implements VariableRule<Primitive.Reference> {

	@Override
	public boolean accept(Primitive.Reference value) {
		return value.reference().facets().isEmpty();
	}

	@Override
	public String errorMessage() {
		return "The reference cannot have facets";
	}
}