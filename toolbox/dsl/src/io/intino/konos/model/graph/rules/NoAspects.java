package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Primitive;
import io.intino.tara.lang.model.rules.variable.VariableRule;

public class NoAspects implements VariableRule<Primitive.Reference> {

	@Override
	public boolean accept(Primitive.Reference value) {
		return value.reference().appliedAspects().isEmpty();
	}

	@Override
	public String errorMessage() {
		return "The reference cannot have facets";
	}
}